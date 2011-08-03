package dk.brics.tajs.analysis.dom.html5;

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
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

/**
 * Each Storage object provides access to a list of key/value pairs, which are
 * sometimes called items. Keys and values are strings. Any string (including
 * the empty string) is a valid key.
 */
public class StorageElement {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.STORAGE_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.STORAGE_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.STORAGE_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "Storage", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype object
		s.newObject(PROTOTYPE);
		s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(INSTANCES);
		s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, INSTANCES, "length", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.HTML5);

		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		createDOMFunction(s, PROTOTYPE, DOMObjects.STORAGE_KEY, "key", 1, DOMSpec.HTML5);
		createDOMFunction(s, PROTOTYPE, DOMObjects.STORAGE_GET_ITEM, "getItem", 1, DOMSpec.HTML5);
		createDOMFunction(s, PROTOTYPE, DOMObjects.STORAGE_SET_ITEM, "setItem", 2, DOMSpec.HTML5);
		createDOMFunction(s, PROTOTYPE, DOMObjects.STORAGE_REMOVE_ITEM, "removeItem", 1, DOMSpec.HTML5);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case STORAGE_KEY: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value index = */Conversion.toNumber(call.getArg(0), c);
			return Value.makeAnyStr(new Dependency(), new DependencyGraphReference());
		}
		case STORAGE_GET_ITEM: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value key = */Conversion.toString(call.getArg(0), c);
			return Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).joinNull();
		}
		case STORAGE_SET_ITEM: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			/* Value key = */Conversion.toString(call.getArg(0), c);
			/* Value data = */Conversion.toString(call.getArg(1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case STORAGE_REMOVE_ITEM: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value key = */Conversion.toString(call.getArg(0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
