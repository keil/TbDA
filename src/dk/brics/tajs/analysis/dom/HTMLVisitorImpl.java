package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.options.Options;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class HTMLVisitorImpl implements HTMLVisitor {

	private final Document document;

	protected HTMLVisitorImpl(Document document) {
		this.document = document;
	}

	@Override
	public void visitDocument() {
		visit(document.getDocumentElement());
	}

	@Override
	public void visit(Element element) {
		String tagName = element.getTagName();
		if ("a".equalsIgnoreCase(tagName)) {
			visitA(element);
		} else if ("applet".equalsIgnoreCase(tagName)) {
			visitApplet(element);
		} else if ("area".equalsIgnoreCase(tagName)) {
			visitArea(element);
		} else if ("base".equalsIgnoreCase(tagName)) {
			visitBase(element);
		} else if ("basefont".equalsIgnoreCase(tagName)) {
			visitBaseFont(element);
		} else if ("body".equalsIgnoreCase(tagName)) {
			visitBody(element);
		} else if ("br".equalsIgnoreCase(tagName)) {
			visitBR(element);
		} else if ("button".equalsIgnoreCase(tagName)) {
			visitButton(element);
		} else if ("caption".equalsIgnoreCase(tagName)) {
			visitCaption(element);
		} else if ("col".equalsIgnoreCase(tagName)) {
			visitCol(element);
		} else if ("dir".equalsIgnoreCase(tagName)) {
			visitDir(element);
		} else if ("div".equalsIgnoreCase(tagName)) {
			visitDiv(element);
		} else if ("dl".equalsIgnoreCase(tagName)) {
			visitDl(element);
		} else if ("fieldset".equalsIgnoreCase(tagName)) {
			visitFieldSet(element);
		} else if ("font".equalsIgnoreCase(tagName)) {
			visitFont(element);
		} else if ("form".equalsIgnoreCase(tagName)) {
			visitForm(element);
		} else if ("frame".equalsIgnoreCase(tagName)) {
			visitFrame(element);
		} else if ("frameset".equalsIgnoreCase(tagName)) {
			visitFrameSet(element);
		} else if ("h1".equalsIgnoreCase(tagName)) {
			visitH1(element);
		} else if ("h2".equalsIgnoreCase(tagName)) {
			visitH2(element);
		} else if ("h3".equalsIgnoreCase(tagName)) {
			visitH3(element);
		} else if ("h4".equalsIgnoreCase(tagName)) {
			visitH4(element);
		} else if ("h5".equalsIgnoreCase(tagName)) {
			visitH5(element);
		} else if ("h6".equalsIgnoreCase(tagName)) {
			visitH6(element);
		} else if ("head".equalsIgnoreCase(tagName)) {
			visitHead(element);
		} else if ("hr".equalsIgnoreCase(tagName)) {
			visitHR(element);
		} else if ("html".equalsIgnoreCase(tagName)) {
			visitHTML(element);
		} else if ("iframe".equalsIgnoreCase(tagName)) {
			visitIFrame(element);
		} else if ("img".equalsIgnoreCase(tagName)) {
			visitImg(element);
		} else if ("input".equalsIgnoreCase(tagName)) {
			visitInput(element);
		} else if ("label".equalsIgnoreCase(tagName)) {
			visitLabel(element);
		} else if ("legend".equalsIgnoreCase(tagName)) {
			visitLegend(element);
		} else if ("li".equalsIgnoreCase(tagName)) {
			visitLI(element);
		} else if ("link".equalsIgnoreCase(tagName)) {
			visitLink(element);
		} else if ("map".equalsIgnoreCase(tagName)) {
			visitMap(element);
		} else if ("menu".equalsIgnoreCase(tagName)) {
			visitMenu(element);
		} else if ("meta".equalsIgnoreCase(tagName)) {
			visitMeta(element);
		} else if ("object".equalsIgnoreCase(tagName)) {
			visitObject(element);
		} else if ("ol".equalsIgnoreCase(tagName)) {
			visitOL(element);
		} else if ("optgroup".equalsIgnoreCase(tagName)) {
			visitOptGroup(element);
		} else if ("option".equalsIgnoreCase(tagName)) {
			visitOption(element);
		} else if ("p".equalsIgnoreCase(tagName)) {
			visitP(element);
		} else if ("param".equalsIgnoreCase(tagName)) {
			visitParam(element);
		} else if ("pre".equalsIgnoreCase(tagName)) {
			visitPre(element);
		} else if ("script".equalsIgnoreCase(tagName)) {
			visitScript(element);
		} else if ("select".equalsIgnoreCase(tagName)) {
			visitSelect(element);
		} else if ("style".equalsIgnoreCase(tagName)) {
			visitStyle(element);
		} else if ("table".equalsIgnoreCase(tagName)) {
			visitTable(element);
		} else if ("tbody".equalsIgnoreCase(tagName)) {
			visitTBody(element);
		} else if ("td".equalsIgnoreCase(tagName)) {
			visitTD(element);
		} else if ("textarea".equalsIgnoreCase(tagName)) {
			visitTextArea(element);
		} else if ("tfoot".equalsIgnoreCase(tagName)) {
			visitTFoot(element);
		} else if ("th".equalsIgnoreCase(tagName)) {
			visitTH(element);
		} else if ("thead".equalsIgnoreCase(tagName)) {
			visitTHead(element);
		} else if ("title".equalsIgnoreCase(tagName)) {
			visitTitle(element);
		} else if ("tr".equalsIgnoreCase(tagName)) {
			visitTR(element);
		} else if ("ul".equalsIgnoreCase(tagName)) {
			visitUL(element);
		} else {
			if (Options.isDebugEnabled()) {
				System.out.println("Unknown Element Tag: " + element.getTagName());
			}
		}

		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			org.w3c.dom.Node n = childNodes.item(i);
			if (n instanceof Element)
				visit((Element) n);
		}
	}

	@Override
	public void visitA(Element element) {

	}

	@Override
	public void visitApplet(Element element) {

	}

	@Override
	public void visitArea(Element element) {

	}

	@Override
	public void visitBase(Element element) {

	}

	@Override
	public void visitBaseFont(Element element) {

	}

	@Override
	public void visitBR(Element element) {

	}

	@Override
	public void visitBody(Element element) {

	}

	@Override
	public void visitButton(Element element) {

	}

	@Override
	public void visitCaption(Element element) {

	}

	@Override
	public void visitCol(Element element) {

	}

	@Override
	public void visitDir(Element element) {

	}

	@Override
	public void visitDiv(Element element) {

	}

	@Override
	public void visitDl(Element element) {

	}

	@Override
	public void visitFieldSet(Element element) {

	}

	@Override
	public void visitFont(Element element) {

	}

	@Override
	public void visitForm(Element element) {

	}

	@Override
	public void visitFrame(Element element) {

	}

	@Override
	public void visitFrameSet(Element element) {

	}

	@Override
	public void visitH1(Element element) {

	}

	@Override
	public void visitH2(Element element) {

	}

	@Override
	public void visitH3(Element element) {

	}

	@Override
	public void visitH4(Element element) {

	}

	@Override
	public void visitH5(Element element) {

	}

	@Override
	public void visitH6(Element element) {

	}

	@Override
	public void visitHead(Element element) {

	}

	@Override
	public void visitHR(Element element) {

	}

	@Override
	public void visitHTML(Element element) {

	}

	@Override
	public void visitIFrame(Element element) {

	}

	@Override
	public void visitImg(Element element) {

	}

	@Override
	public void visitInput(Element element) {

	}

	@Override
	public void visitLabel(Element element) {

	}

	@Override
	public void visitLegend(Element element) {

	}

	@Override
	public void visitLI(Element element) {

	}

	@Override
	public void visitLink(Element element) {

	}

	@Override
	public void visitMap(Element element) {

	}

	@Override
	public void visitMenu(Element element) {

	}

	@Override
	public void visitMeta(Element element) {

	}

	@Override
	public void visitObject(Element element) {

	}

	@Override
	public void visitOL(Element element) {

	}

	@Override
	public void visitOptGroup(Element element) {

	}

	@Override
	public void visitOption(Element element) {

	}

	@Override
	public void visitP(Element element) {

	}

	@Override
	public void visitParam(Element element) {

	}

	@Override
	public void visitPre(Element element) {

	}

	@Override
	public void visitScript(Element element) {

	}

	@Override
	public void visitSelect(Element element) {

	}

	@Override
	public void visitStyle(Element element) {

	}

	@Override
	public void visitTable(Element element) {

	}

	@Override
	public void visitTBody(Element element) {

	}

	@Override
	public void visitTD(Element element) {

	}

	@Override
	public void visitTextArea(Element element) {

	}

	@Override
	public void visitTFoot(Element element) {

	}

	@Override
	public void visitTH(Element element) {

	}

	@Override
	public void visitTHead(Element element) {

	}

	@Override
	public void visitTitle(Element element) {

	}

	@Override
	public void visitTR(Element element) {

	}

	@Override
	public void visitUL(Element element) {

	}
}
