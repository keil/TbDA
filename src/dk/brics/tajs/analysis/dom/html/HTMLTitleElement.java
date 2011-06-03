package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

public class HTMLTitleElement {

	public static ObjectLabel TITLE = new ObjectLabel(DOMObjects.HTMLTITLEELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel TITLE_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLTITLEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(TITLE_PROTOTYPE);
		createDOMInternalPrototype(s, TITLE_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(TITLE);
		createDOMInternalPrototype(s, TITLE, Value.makeObject(TITLE_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLTitleElement", Value.makeObject(TITLE, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, TITLE, "text", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(TITLE);
		TITLE = TITLE.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
