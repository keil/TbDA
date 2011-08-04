package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.options.Options;
import org.jdom.Document;
import org.jdom.Element;

import java.util.Collections;

public class DOMVisitor extends HTMLVisitorImpl {

	private final State s;

	public DOMVisitor(Document document, State s) {
		super(document);
		this.s = s;
	}

	@Override
	public void visit(Element element) {
		super.visit(element);

		// Ignore HTML content?
		if (Options.isIgnoreHTMLContent()) {
			return;
		}

		// Pick up special properties
		ObjectLabel label = DOMFunctions.getHTMLObjectLabel(element.getName());
		if (label != null) {
			// Special Property: id
			String id = element.getAttributeValue("id");
			if (id != null) {
				s.addElementById(id, Collections.singleton(label));

				// TODO An element with id FOO is available as FOO
				// DOMFunctions.createDOMProperty(DOMWindow.WINDOW, id,
				// Value.makeObject(label));
			}

			// Special Property: name
			String name = element.getAttributeValue("name");
			if (name != null) {
				s.addElementByName(name, Collections.singleton(label));
			}

			// Special Property: tagName
			String tagname = element.getName();
			if (tagname != null) {
				s.addElementByTagName(tagname, Collections.singleton(label));
			}
		}

	}

}
