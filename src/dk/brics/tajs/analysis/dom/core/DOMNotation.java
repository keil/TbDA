package dk.brics.tajs.analysis.dom.core;

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
 * This interface represents a notation declared in the DTD.
 */
public class DOMNotation {

	public static ObjectLabel NOTATION = new ObjectLabel(DOMObjects.NOTATION, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel NOTATION_PROTOTYPE = new ObjectLabel(DOMObjects.NOTATION_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object
		s.newObject(NOTATION_PROTOTYPE);
		createDOMInternalPrototype(s, NOTATION_PROTOTYPE, Value.makeObject(DOMNode.NODE_PROTOTYPE, new Dependency()));

		// Multiplied object
		s.newObject(NOTATION);
		createDOMInternalPrototype(s, NOTATION, Value.makeObject(NOTATION_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "Notation", Value.makeObject(NOTATION, new Dependency()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, NOTATION, "publicId", Value.makeAnyStr(new Dependency()).joinNull().setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, NOTATION, "systemId", Value.makeAnyStr(new Dependency()).joinNull().setReadOnly(), DOMSpec.LEVEL_1);

		s.multiplyObject(NOTATION);
		NOTATION = NOTATION.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
