package dk.brics.tajs.analysis.dom.style;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

public class ClientBoundingRect {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.CLIENTBOUNDINGRECT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.CLIENTBOUNDINGRECT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.CLIENTBOUNDINGRECT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "ClientBoundingRect", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype Object
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, INSTANCES, "top", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.UNKNOWN);
		createDOMProperty(s, INSTANCES, "right", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.UNKNOWN);
		createDOMProperty(s, INSTANCES, "bottom", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.UNKNOWN);
		createDOMProperty(s, INSTANCES, "left", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.UNKNOWN);
		createDOMProperty(s, INSTANCES, "width", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.UNKNOWN);
		createDOMProperty(s, INSTANCES, "height", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.UNKNOWN);

		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
