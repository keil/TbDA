package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.js2flowgraph.RhinoAST2Flowgraph;
import dk.brics.tajs.options.Options;
import org.w3c.dom.*;
import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.TidyMessageListener;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Properties;

public class HTMLParserImpl implements HTMLParser {

	private final RhinoAST2Flowgraph flowGraphBuilder;

	public HTMLParserImpl(RhinoAST2Flowgraph flowGraphBuilder) {
		this.flowGraphBuilder = flowGraphBuilder;
	}

	@Override
	public Document build(String filename) throws IOException {
		Tidy tidy = newTidy();
		Document document = tidy.parseDOM(new FileInputStream(filename), null);

		JavaScriptVisitor visitor = new JavaScriptVisitor(document, new File(filename));
		visitor.visitDocument();
		String javaScript = visitor.getJavaScript();

		if (Options.isDebugEnabled()) {
			System.out.println("============  HTML Document ============");
			tidy.pprint(document, System.out);
			System.out.println("----------------------------------------");
			System.out.println();
			System.out.println("===========  JavaScript Code ===========");
			DecimalFormat decimalFormat = new DecimalFormat("0000");
			String[] lines = javaScript.split("\n");
			for (int i = 0; i < lines.length; i++) {
				System.out.println("L" + decimalFormat.format(i + 1) + ": " + lines[i]);
			}
			System.out.println("----------------------------------------");
			System.out.println();

			FileWriter writer = new FileWriter("dom.js");
			writer.write(javaScript);
			writer.close();
		}

		EventVisitor eventVisitor = new EventVisitor(document, flowGraphBuilder, filename);
		eventVisitor.visitDocument();

		flowGraphBuilder.build(javaScript, filename);

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

		@Override
		public void visit(Element element) {
			super.visit(element);

			// Pick up event handlers
			NamedNodeMap elementAttributes = element.getAttributes();
			for (int i = 0; i < elementAttributes.getLength(); i++) {
				Node attributeNode = elementAttributes.item(i);
				String attribute = attributeNode.getNodeName();
				if (DOMEvents.isEventAttribute(attribute)) {
					flowgraphBuilder.addEventHandler(attributeNode.getNodeValue(), attribute, filename);
				}
			}

		}

		@Override
		public void visitA(Element element) {
			//check if the href attribute is there
			Attr href = element.getAttributeNode("href");

			if (href == null) {
				return;
			}

			String attributeValue = href.getNodeValue();
			if (attributeValue.startsWith("javascript:")) {
				String jsCode = attributeValue.substring("javascript:".length());
				flowgraphBuilder.addEventHandler(jsCode, "onclick", filename);
			}

		}

		@Override
		public void visitBody(Element element) {
			NamedNodeMap elementAttributes = element.getAttributes();
			for (int i = 0; i < elementAttributes.getLength(); i++) {
				Node attributeNode = elementAttributes.item(i);
				String attribute = attributeNode.getNodeName();

				if (DOMEvents.isLoadEventAttribute(attribute) || DOMEvents.isUnloadEventAttribute(attribute)) {
					flowgraphBuilder.addEventHandler(attributeNode.getNodeValue(), attribute, filename);
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
					System.out.println(String.format("HTML warning at %s:%s : %s", new Object[]{
							msg.getLine(), msg.getColumn(), msg.getMessage()}));
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
		Properties prop = new Properties();
		prop.put("new-blocklevel-tags", "canvas");
		tidy.getConfiguration().addProps(prop);
		return tidy;
	}

	private static class JavaScriptVisitor extends HTMLVisitorImpl {

		private final StringBuilder javaScript = new StringBuilder();
		private final File file;

		private JavaScriptVisitor(Document document, File file) {
			super(document);
			this.file = file;
		}

		public String getJavaScript() {
			return javaScript.toString();
		}

		@Override
		public void visitScript(Element element) {
			String src = element.getAttribute("src");
			if (src.isEmpty()) {
				// Embedded script
				javaScript.append(element.getFirstChild().getNodeValue() + "\n");
			} else {
				// External script

				// Script in file?
				File srcFile = new File(file.getParent() + File.separator + src);
				if (srcFile.exists()) {
					String s = readScriptFile(srcFile);
					if (s != null) {
						javaScript.append(s);
					}
					return;
				}

				// Script in url?
						URL url = null;
				try {
					url = new URL(src);
					String s = readScriptURL(url);
					if (s != null) {
						javaScript.append(s);
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
