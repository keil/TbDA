package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * This interface represents an entity, either parsed or unparsed, in an XML
 * document. Note that this models the entity itself not the entity declaration.
 * Entity declaration modeling has been left for a later Level of the DOM
 * specification.
 */
public class DOMEntity {

	public static ObjectLabel ENTITY = new ObjectLabel(DOMObjects.ENTITY, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel ENTITY_PROTOTYPE = new ObjectLabel(DOMObjects.ENTITY_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(ENTITY_PROTOTYPE);
		createDOMInternalPrototype(s, ENTITY_PROTOTYPE, Value.makeObject(DOMNode.NODE_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(ENTITY);
		createDOMInternalPrototype(s, ENTITY, Value.makeObject(ENTITY_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "Entity", Value.makeObject(ENTITY, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, ENTITY, "publicId", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).joinNull().setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, ENTITY, "systemId", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).joinNull().setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, ENTITY, "notationName", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).joinNull().setReadOnly(), DOMSpec.LEVEL_1);

		// DOM Level 3
		createDOMProperty(s, ENTITY, "inputEncoding", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).joinNull().setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, ENTITY, "xmlEncoding", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).joinNull().setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, ENTITY, "xmlVersion", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).joinNull().setReadOnly(), DOMSpec.LEVEL_3);

		s.multiplyObject(ENTITY);
		ENTITY = ENTITY.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
