package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.*;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * The MutationEvent interface provides specific contextual information
 * associated with Mutation events.
 */
public class MutationEvent {

	public static ObjectLabel MUTATION_EVENT = new ObjectLabel(DOMObjects.MUTATION_EVENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel MUTATION_EVENT_PROTOTYPE = new ObjectLabel(DOMObjects.MUTATION_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(MUTATION_EVENT_PROTOTYPE);
		createDOMInternalPrototype(s, MUTATION_EVENT_PROTOTYPE, Value.makeObject(Event.EVENT_PROTOTYPE, new Dependency()));

		// Multiplied object.
		s.newObject(MUTATION_EVENT);
		createDOMInternalPrototype(s, MUTATION_EVENT, Value.makeObject(MUTATION_EVENT_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "MutationEvent", Value.makeObject(MUTATION_EVENT, new Dependency()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, MUTATION_EVENT, "relatedNode", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MUTATION_EVENT, "prevValue", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MUTATION_EVENT, "newValue", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MUTATION_EVENT, "attrName", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MUTATION_EVENT, "attrChange", Value.makeAnyNumUInt(new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);

		/*
		 * Constants (attrChangeType).
		 */
		createDOMProperty(s, MUTATION_EVENT_PROTOTYPE, "MODIFICATION", Value.makeNum(1, new Dependency()), DOMSpec.LEVEL_2);
		createDOMProperty(s, MUTATION_EVENT_PROTOTYPE, "ADDITION", Value.makeNum(2, new Dependency()), DOMSpec.LEVEL_2);
		createDOMProperty(s, MUTATION_EVENT_PROTOTYPE, "REMOVAL", Value.makeNum(3, new Dependency()), DOMSpec.LEVEL_2);

		/*
		 * Functions.
		 */
		createDOMFunction(s, MUTATION_EVENT_PROTOTYPE, DOMObjects.MUTATION_EVENT_INIT_MUTATION_EVENT, "initMutationEvent", 8, DOMSpec.LEVEL_2);

		// Multiply Object
		s.multiplyObject(MUTATION_EVENT);
		MUTATION_EVENT = MUTATION_EVENT.makeSingleton().makeSummary();
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case MUTATION_EVENT_INIT_MUTATION_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 8, 8);
			Value typeArg = Conversion.toString(call.getArg(0), c);
			Value canBubble = Conversion.toBoolean(call.getArg(1));
			Value cancelable = Conversion.toBoolean(call.getArg(2));
			Value relatedNode = DOMConversion.toNode(call.getArg(3), c);
			Value prevValue = Conversion.toString(call.getArg(4), c);
			Value newValue = Conversion.toString(call.getArg(5), c);
			Value attrName = Conversion.toString(call.getArg(6), c);
			Value attrChange = Conversion.toNumber(call.getArg(7), c);
			return Value.makeUndef(new Dependency());
		}
		default:
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
	}
}
