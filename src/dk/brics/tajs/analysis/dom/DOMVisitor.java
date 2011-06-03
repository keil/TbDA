package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;

import java.util.Collections;

import dk.brics.tajs.lattice.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;


public class DOMVisitor extends HTMLVisitorImpl {

	private final State s;

	public DOMVisitor(Document document, State s) {
		super(document);
		this.s = s;
	}

	@Override
	public void visit(Element element) {
		super.visit(element);

		// Pick up special properties
		ObjectLabel label = DOMFunctions.getHTMLObjectLabel(element.getTagName());
		if (label != null) {
			// Special Property: id
			String id = element.getAttribute("id");
			if (id.length() != 0) {
				s.addElementById(id, Collections.singleton(label));

				// TODO: What about clashes?
				// An element with id FOO is available as FOO
				createDOMProperty(s, DOMWindow.WINDOW, id, Value.makeObject(label, new Dependency()));
			}

			// Special Property: name
			String name = element.getAttribute("name");
			if (name.length() != 0) {
				s.addElementByName(name, Collections.singleton(label));
			}

			// Special Property: tagName
			String tagname = element.getTagName();
			if (tagname.length() != 0) {
				s.addElementByTagName(tagname, Collections.singleton(label));
			}
		}

	}

}
