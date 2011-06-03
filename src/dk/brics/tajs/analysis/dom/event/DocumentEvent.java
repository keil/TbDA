package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * The DocumentEvent interface provides a mechanism by which the user can create
 * an Event of a type supported by the implementation.
 */
public class DocumentEvent {

	public static ObjectLabel DOCUMENT_EVENT = new ObjectLabel(DOMObjects.DOCUMENT_EVENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel DOCUMENT_EVENT_PROTOTYPE = new ObjectLabel(DOMObjects.DOCUMENT_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(DOCUMENT_EVENT_PROTOTYPE);
		createDOMInternalPrototype(s, DOCUMENT_EVENT_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency()));

		// Multiplied object.
		s.newObject(DOCUMENT_EVENT);
		createDOMInternalPrototype(s, DOCUMENT_EVENT, Value.makeObject(DOCUMENT_EVENT_PROTOTYPE, new Dependency()));
		s.multiplyObject(DOCUMENT_EVENT);
		DOCUMENT_EVENT = DOCUMENT_EVENT.makeSingleton().makeSummary();

		/*
		 * Properties.
		 */
		// No properties.

		/*
		 * Functions.
		 */
		createDOMFunction(s, DOCUMENT_EVENT_PROTOTYPE, DOMObjects.DOCUMENT_EVENT_CREATE_EVENT, "createEvent", 1, DOMSpec.LEVEL_2);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case DOCUMENT_EVENT_CREATE_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value eventType = Conversion.toString(call.getArg(0), c);
			return Value.makeObject(Event.EVENT, new Dependency());
		}
		default:
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
	}
}
