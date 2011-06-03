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
 * Force a line break. See the BR element definition in HTML 4.0.
 */
public class HTMLBRElement {

	public static ObjectLabel BR = new ObjectLabel(DOMObjects.HTMLBRELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel BR_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLBRELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(BR_PROTOTYPE);
		createDOMInternalPrototype(s, BR_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(BR);
		createDOMInternalPrototype(s, BR, Value.makeObject(BR_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLBRElement", Value.makeObject(BR, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, BR, "clear", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(BR);
		BR = BR.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
