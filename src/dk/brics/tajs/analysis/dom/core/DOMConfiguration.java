package dk.brics.tajs.analysis.dom.core;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;

/**
 * The DOMConfiguration interface represents the configuration of a document and
 * maintains a table of recognized parameters.
 * <p/>
 * Introduced in DOM Level 3.
 */
public class DOMConfiguration {

	public static ObjectLabel INSTANCES;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel CONSTRUCTOR;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.CONFIGURATION_CONSTRUCTOR, Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.CONFIGURATION_PROTOTYPE, Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.CONFIGURATION_INSTANCES, Kind.FUNCTION);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "Configuration", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype object.
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */

		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 3
		createDOMFunction(s, PROTOTYPE, DOMObjects.CONFIGURATION_SET_PARAMETER, "setParameter", 2, DOMSpec.LEVEL_3);
		createDOMFunction(s, PROTOTYPE, DOMObjects.CONFIGURATION_GET_PARAMETER, "getParameter", 1, DOMSpec.LEVEL_3);
		createDOMFunction(s, PROTOTYPE, DOMObjects.CONFIGURATION_CAN_SET_PARAMETER, "canSetParameter", 2, DOMSpec.LEVEL_3);

	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case CONFIGURATION_CAN_SET_PARAMETER: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			/* Value name = */Conversion.toString(call.getArg(0), c);
			return Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		}
		case CONFIGURATION_GET_PARAMETER: {
			throw new UnsupportedOperationException("CONFIGURATION_GET_PARAMETER not supported");
		}
		case CONFIGURATION_SET_PARAMETER: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			/* Value name = */Conversion.toString(call.getArg(0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new UnsupportedOperationException("Unsupported Native Object " + nativeObject);
		}
		}
	}
}
