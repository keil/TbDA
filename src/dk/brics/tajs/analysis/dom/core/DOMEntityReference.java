package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * EntityReference objects may be inserted into the structure model when an
 * entity reference is in the source document, or when the user wishes to insert
 * an entity reference.
 */
public class DOMEntityReference {

	public static ObjectLabel ENTITYREFERENCE = new ObjectLabel(DOMObjects.ENTITYREFERENCE, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel ENTITYREFERENCE_PROTOTYPE = new ObjectLabel(DOMObjects.ENTITYREFERENCE_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(ENTITYREFERENCE_PROTOTYPE);
		createDOMInternalPrototype(s, ENTITYREFERENCE_PROTOTYPE, Value.makeObject(DOMNode.NODE_PROTOTYPE, new Dependency()));

		// Multiplied object.
		s.newObject(ENTITYREFERENCE);
		createDOMInternalPrototype(s, ENTITYREFERENCE, Value.makeObject(ENTITYREFERENCE_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "EntityReference", Value.makeObject(ENTITYREFERENCE, new Dependency()));
		s.multiplyObject(ENTITYREFERENCE);
		ENTITYREFERENCE = ENTITYREFERENCE.makeSingleton().makeSummary();

		/*
		 * Properties.
		 */
		// No properties.

		/*
		 * Functions.
		 */
		// No functions.
	}
}
