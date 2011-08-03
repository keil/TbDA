package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

/**
 * The UIEvent interface provides specific contextual information associated
 * with User Interface events.
 */
public class UIEvent {

	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		PROTOTYPE = new ObjectLabel(DOMObjects.UI_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.UI_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Prototype object.
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(Event.PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, PROTOTYPE, "detail", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "view", Value.makeObject(DOMWindow.WINDOW, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_2);

		/*
		 * Functions.
		 */
		createDOMFunction(s, PROTOTYPE, DOMObjects.UI_EVENT_INIT_UI_EVENT, "initUIEvent", 5, DOMSpec.LEVEL_2);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case UI_EVENT_INIT_UI_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 5, 5);
			/* Value typeArg = */Conversion.toString(call.getArg(0), c);
			/* Value canBubble = */Conversion.toBoolean(call.getArg(1));
			/* Value cancelableArg = */Conversion.toBoolean(call.getArg(2));
			// View arg not modelled...
			/* Value detailArg = */Conversion.toNumber(call.getArg(4), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default:
			throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
		}
	}
}
