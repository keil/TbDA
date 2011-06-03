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
 * Notice of modification to part of a document. See the INS and DEL element
 * definitions in HTML 4.01.
 */
public class HTMLModElement {

	public static ObjectLabel MOD = new ObjectLabel(DOMObjects.HTMLMODELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel MOD_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLMODELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(MOD_PROTOTYPE);
		createDOMInternalPrototype(s, MOD_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(MOD);
		createDOMInternalPrototype(s, MOD, Value.makeObject(MOD_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLModElement", Value.makeObject(MOD, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, MOD, "cite", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, MOD, "dateTime", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(MOD);
		MOD = MOD.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
