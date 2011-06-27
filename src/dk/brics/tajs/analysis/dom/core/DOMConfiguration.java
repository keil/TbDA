package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * The DOMConfiguration interface represents the configuration of a document and
 * maintains a table of recognized parameters.
 * <p/>
 * Introduced in DOM Level 3.
 */
public class DOMConfiguration {

	public static ObjectLabel CONFIGURATION = new ObjectLabel(DOMObjects.CONFIGURATION, Kind.OBJECT);
	public static ObjectLabel CONFIGURATION_PROTOTYPE = new ObjectLabel(DOMObjects.CONFIGURATION_PROTOTYPE, Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(CONFIGURATION_PROTOTYPE);
		createDOMInternalPrototype(s, CONFIGURATION_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(CONFIGURATION);
		createDOMInternalPrototype(s, CONFIGURATION, Value.makeObject(CONFIGURATION_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "Configuration", Value.makeObject(CONFIGURATION, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 3
		createDOMProperty(s, CONFIGURATION, "parameterNames", Value.makeObject(DOMStringList.STRINGLIST_PROTOTYPE, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);

		s.multiplyObject(CONFIGURATION);
		CONFIGURATION = CONFIGURATION.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 3
		createDOMFunction(s, CONFIGURATION_PROTOTYPE, DOMObjects.CONFIGURATION_SET_PARAMETER, "setParameter", 2, DOMSpec.LEVEL_3);
		createDOMFunction(s, CONFIGURATION_PROTOTYPE, DOMObjects.CONFIGURATION_GET_PARAMETER, "getParameter", 1, DOMSpec.LEVEL_3);
		createDOMFunction(s, CONFIGURATION_PROTOTYPE, DOMObjects.CONFIGURATION_CAN_SET_PARAMETER, "canSetParameter", 2, DOMSpec.LEVEL_3);

	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case CONFIGURATION_CAN_SET_PARAMETER: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Value name = Conversion.toString(call.getArg(0), c);
			return Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		}
		case CONFIGURATION_GET_PARAMETER: {
			throw new UnsupportedOperationException("CONFIGURATION_GET_PARAMETER not supported");
		}
		case CONFIGURATION_SET_PARAMETER: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Value name = Conversion.toString(call.getArg(0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new UnsupportedOperationException("Unsupported Native Object " + nativeObject);
		}
		}
	}
}
