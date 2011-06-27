package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * The EventListener interface is the primary method for handling events.
 * <p/>
 * EventTarget.addEventListener simply takes a function as an argument...
 */
public class EventListener {

	public static ObjectLabel EVENT_LISTENER = new ObjectLabel(DOMObjects.EVENT_LISTENER, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel EVENT_LISTENER_PROTOTYPE = new ObjectLabel(DOMObjects.EVENT_LISTENER_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(EVENT_LISTENER_PROTOTYPE);
		createDOMInternalPrototype(s, EVENT_LISTENER_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(EVENT_LISTENER);
		createDOMInternalPrototype(s, EVENT_LISTENER, Value.makeObject(EVENT_LISTENER_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		s.multiplyObject(EVENT_LISTENER);
		EVENT_LISTENER = EVENT_LISTENER.makeSingleton().makeSummary();

		/*
		 * Properties.
		 */
		// No properties.

		/*
		 * Functions.
		 */
		createDOMFunction(s, EVENT_LISTENER_PROTOTYPE, DOMObjects.EVENT_LISTENER_HANDLE_EVENT, "handleEvent", 1, DOMSpec.LEVEL_2);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
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
