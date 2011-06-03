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

/**
 * Document base URI. See the BASE element definition in HTML 4.0.
 */
public class HTMLBaseElement {

	public static ObjectLabel BASE = new ObjectLabel(DOMObjects.HTMLBASEELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel BASE_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLBASEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(BASE_PROTOTYPE);
		createDOMInternalPrototype(s, BASE_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(BASE);
		createDOMInternalPrototype(s, BASE, Value.makeObject(BASE_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLBaseElement", Value.makeObject(BASE, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, BASE, "href", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, BASE, "target", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(BASE);
		BASE = BASE.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
