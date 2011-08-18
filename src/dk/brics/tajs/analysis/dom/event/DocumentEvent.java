package dk.brics.tajs.analysis.dom.event;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.lattice.Value;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.Node;

/**
 * The DocumentEvent interface provides a mechanism by which the user can create
 * an Event of a type supported by the implementation.
 * <p />
 * Simply augments the DOMDocument object.
 */
public class DocumentEvent {

	
	public static void build(State s) {
		/*
		 * Properties.
		 */
		// No properties.

		/*
		 * Functions.
		 */
		createDOMFunction(s, DOMDocument.PROTOTYPE, DOMObjects.DOCUMENT_EVENT_CREATE_EVENT, "createEvent", 1, DOMSpec.LEVEL_2);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case DOCUMENT_EVENT_CREATE_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value eventType = */Conversion.toString(call.getArg(0), c);
			return Value.makeObject(Event.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		default:
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
	}
}
