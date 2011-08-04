package dk.brics.tajs.analysis.dom;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import dk.brics.tajs.js2flowgraph.RhinoAST2Flowgraph;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collections;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.contrib.input.LineNumberElement;
import org.jdom.contrib.input.LineNumberSAXBuilder;
import org.jdom.input.SAXBuilder;
import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.TidyMessageListener;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class HTMLParserImpl implements HTMLParser {

	private final RhinoAST2Flowgraph flowGraphBuilder;

	public HTMLParserImpl(RhinoAST2Flowgraph flowGraphBuilder) {
		this.flowGraphBuilder = flowGraphBuilder;
	}

	@Override
	public Document build(String inputFile) throws IOException {

		String outputFile = inputFile;
		if (inputFile.endsWith(".htm")) {
			outputFile = inputFile.substring(0, inputFile.indexOf(".htm")) + ".tidy.htm";
		} else if (inputFile.endsWith(".html")) {
			outputFile = inputFile.substring(0, inputFile.indexOf(".html")) + ".tidy.html";
		} else {
			outputFile = outputFile + ".tidy";
		}

		{
			Tidy tidy = newTidy();
			org.w3c.dom.Document document = tidy.parseDOM(new FileInputStream(inputFile), null);
			XMLSerializer serializer = new XMLSerializer();
			serializer.setOutputByteStream(new FileOutputStream(outputFile));
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			tidy.pprint(document, outputStream);
			outputStream.close();
		}

		SAXBuilder builder = new LineNumberSAXBuilder();
		Document document = null;
		try {
			document = builder.build(outputFile);
		} catch (JDOMException e) {
			throw new IOException(e);
		}

		JavaScriptVisitor visitor = new JavaScriptVisitor(document, outputFile);
		visitor.visitDocument();
		List<JavaScriptSource> jsList = visitor.getJavaScript();

		if (!Options.isIgnoreHTMLContent()) {
			EventVisitor eventVisitor = new EventVisitor(document, flowGraphBuilder, outputFile);
			eventVisitor.visitDocument();
		}

		if (jsList.isEmpty()) {
			flowGraphBuilder.build("", outputFile);
		}
		for (JavaScriptSource javaScriptSource : jsList) {
			flowGraphBuilder.build(javaScriptSource.getJavaScript(), javaScriptSource.getFileName(), javaScriptSource.getLineNumber() - 1);
		}

		return document;
	}

	private static class EventVisitor extends HTMLVisitorImpl {

		private final RhinoAST2Flowgraph flowgraphBuilder;
		private final String filename;

		public EventVisitor(Document document, RhinoAST2Flowgraph flowgraphBuilder, String filename) {
			super(document);
			this.flowgraphBuilder = flowgraphBuilder;
			this.filename = filename;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void visit(Element element) {
			super.visit(element);

			// Pick up event handlers
			for (Attribute attribute : (List<Attribute>) element.getAttributes()) {
				String name = attribute.getName();
				String value = attribute.getValue();
				if (DOMEvents.isEventAttribute(name)) {
					flowgraphBuilder.addEventHandler(value, name, filename, getElementLinenumber(element));
				}
			}

		}

		private int getElementLinenumber(Element element) {
			return ((LineNumberElement) element).getStartLine() - 1;
		}

		@Override
		public void visitA(Element element) {
			// check if the href attribute is there
			String href = element.getAttributeValue("href");

			if (href == null) {
				return;
			}

			if (href.startsWith("javascript:")) {
				String jsCode = href.substring("javascript:".length());
				flowgraphBuilder.addEventHandler(jsCode, "onclick", filename, getElementLinenumber(element));
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void visitBody(Element element) {
			for (Attribute attribute : (List<Attribute>) element.getAttributes()) {
				String name = attribute.getName();
				String value = attribute.getValue();

				if (DOMEvents.isLoadEventAttribute(name) || DOMEvents.isUnloadEventAttribute(name)) {
					flowgraphBuilder.addEventHandler(value, name, filename, getElementLinenumber(element));
				}
			}
		}

	}

	/**
	 * Configures a new JTidy instance.
	 */
	private Tidy newTidy() {
		Tidy tidy = new Tidy();
		tidy.setMessageListener(new TidyMessageListener() {

			@Override
			public void messageReceived(TidyMessage msg) {
				if (Options.isDebugEnabled()) {
					System.out.println(String.format("HTML warning at %s:%s : %s", new Object[] { msg.getLine(), msg.getColumn(), msg.getMessage() }));
				}
			}
		});
		tidy.setDropEmptyParas(false);
		tidy.setDropFontTags(false);
		tidy.setDropProprietaryAttributes(false);
		tidy.setTrimEmptyElements(false);
		tidy.setXHTML(true);
		tidy.setIndentAttributes(false);
		tidy.setIndentCdata(false);
		tidy.setIndentContent(false);
		tidy.setQuiet(true);
		tidy.setShowWarnings(!Options.isQuietEnabled());
		tidy.setShowErrors(0);
		tidy.setEncloseBlockText(false);
		tidy.setEscapeCdata(false);
		tidy.setDocType("omit");
		Properties prop = new Properties();
		prop.put("new-blocklevel-tags", "canvas");
		tidy.getConfiguration().addProps(prop);
		return tidy;
	}

	private static class JavaScriptSource {
		private String fileName;
		private String javaScript;
		private int lineNumber;

		public String getFileName() {
			return fileName;
		}

		public String getJavaScript() {
			return javaScript;
		}

		public int getLineNumber() {
			return lineNumber;
		}

		private JavaScriptSource(String fileName, String javaScript, int lineNumber) {
			this.fileName = fileName;
			this.javaScript = javaScript;
			this.lineNumber = lineNumber;
		}
	}

	private static class JavaScriptVisitor extends HTMLVisitorImpl {

		private List<JavaScriptSource> fileToJS = Collections.newList();

		private final String htmlFileName;
		private final File file;

		private JavaScriptVisitor(Document document, String htmlFileName) {
			super(document);
			this.htmlFileName = htmlFileName;
			this.file = new File(htmlFileName);
		}

		public List<JavaScriptSource> getJavaScript() {
			return fileToJS;
		}

		@Override
		public void visitScript(Element element) {
			LineNumberElement elm = (LineNumberElement) element;
			String src = element.getAttributeValue("src");
			if (src == null) {
				// Embedded script
				fileToJS.add(new JavaScriptSource(htmlFileName, element.getText() + "\n", elm.getStartLine()));
			} else {
				// External script

				// Script in file?
				String pathname = file.getParent() + File.separator + src;
				File srcFile = new File(pathname);
				if (srcFile.exists()) {
					String s = readScriptFile(srcFile);
					if (s != null) {
						fileToJS.add(new JavaScriptSource(src, s, elm.getStartLine()));
					}
					return;
				}

				// Script in url?
				URL url = null;
				try {
					url = new URL(src);
					String s = readScriptURL(url);
					if (s != null) {
						fileToJS.add(new JavaScriptSource(src, s, elm.getStartLine()));
					}
				} catch (MalformedURLException e) {
					throw new RuntimeException(e);
				}

			}
		}

		private String readScriptFile(File file) {
			try {
				StringBuilder sb = new StringBuilder();
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				return sb.toString();
			} catch (IOException e1) {
				System.out.println("Unable to read src file: " + file.toString());
				return null;
			}
		}

		private String readScriptURL(URL url) {
			try {
				StringBuilder sb = new StringBuilder();
				BufferedReader bfr = new BufferedReader(new InputStreamReader(url.openStream()));
				String line;
				while ((line = bfr.readLine()) != null) {
					sb.append(line + "\n");
				}
				return sb.toString();
			} catch (MalformedURLException e1) {
				System.out.println("WARN: Malformed URL in src attribute: " + url);
				return null;
			} catch (IOException e2) {
				System.out.println("WARN: Could not fetch script from " + url);
				return null;
			}
		}

	}

}
