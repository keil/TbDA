package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

/**
 * The EventListener interface is the primary method for handling events.
 * <p/>
 * EventTarget.addEventListener simply takes a function as an argument...
 */
public class EventListener {

	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		PROTOTYPE = new ObjectLabel(DOMObjects.EVENT_LISTENER_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.EVENT_LISTENER_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Prototype object.
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		/*
		 * Properties.
		 */
		// No properties.

		/*
		 * Functions.
		 */
		createDOMFunction(s, PROTOTYPE, DOMObjects.EVENT_LISTENER_HANDLE_EVENT, "handleEvent", 1, DOMSpec.LEVEL_2);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case EVENT_LISTENER_HANDLE_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default:
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
	}
}
