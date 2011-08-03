package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.*;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

/**
 * The MutationEvent interface provides specific contextual information
 * associated with Mutation events.
 */
public class MutationEvent {

	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		PROTOTYPE = new ObjectLabel(DOMObjects.MUTATION_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.MUTATION_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Prototype object.
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(Event.PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "MutationEvent", Value.makeObject(INSTANCES, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, INSTANCES, "relatedNode", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "prevValue", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "newValue", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "attrName", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "attrChange", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);

		/*
		 * Constants (attrChangeType).
		 */
		createDOMProperty(s, PROTOTYPE, "MODIFICATION", Value.makeNum(1, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "ADDITION", Value.makeNum(2, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "REMOVAL", Value.makeNum(3, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);

		/*
		 * Functions.
		 */
		createDOMFunction(s, PROTOTYPE, DOMObjects.MUTATION_EVENT_INIT_MUTATION_EVENT, "initMutationEvent", 8, DOMSpec.LEVEL_2);

		// Multiply Object
		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		// DOM Registry
		DOMRegistry.registerMutationEventLabel(INSTANCES);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case MUTATION_EVENT_INIT_MUTATION_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 8, 8);
			/* Value typeArg = */Conversion.toString(call.getArg(0), c);
			/* Value canBubble = */Conversion.toBoolean(call.getArg(1));
			/* Value cancelable = */Conversion.toBoolean(call.getArg(2));
			/* Value relatedNode = */DOMConversion.toNode(call.getArg(3), c);
			/* Value prevValue = */Conversion.toString(call.getArg(4), c);
			/* Value newValue = */Conversion.toString(call.getArg(5), c);
			/* Value attrName = */Conversion.toString(call.getArg(6), c);
			/* Value attrChange = */Conversion.toNumber(call.getArg(7), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default:
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
	}
}
