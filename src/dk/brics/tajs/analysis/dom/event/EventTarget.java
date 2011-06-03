package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.*;
import dk.brics.tajs.analysis.dom.core.DOMNode;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * The EventTarget interface is implemented by all Nodes in an implementation
 * which supports the DOM Event Model. Therefore, this interface can be obtained
 * by using binding-specific casting methods on an instance of the Node
 * interface. The interface allows registration and removal of EventListeners on
 * an EventTarget and dispatch of events to that EventTarget.
 */
public class EventTarget {

	public static void build(State s) {
		// Event target has no native object... see class comment.

		/*
		 * Properties.
		 */
		// No properties.

		/*
		 * Functions.
		 */
		createDOMFunction(s, DOMNode.NODE_PROTOTYPE, DOMObjects.EVENT_TARGET_ADD_EVENT_LISTENER, "addEventListener", 3, DOMSpec.LEVEL_2);
		createDOMFunction(s, DOMNode.NODE_PROTOTYPE, DOMObjects.EVENT_TARGET_REMOVE_EVENT_LISTENER, "removeEventListener", 3, DOMSpec.LEVEL_2);
		createDOMFunction(s, DOMNode.NODE_PROTOTYPE, DOMObjects.EVENT_TARGET_DISPATCH_EVENT, "dispatchEvent", 1, DOMSpec.LEVEL_2);

		// DOM LEVEL 0
		createDOMFunction(s, DOMWindow.WINDOW, DOMObjects.WINDOW_ADD_EVENT_LISTENER, "addEventListener", 3, DOMSpec.LEVEL_0);
		createDOMFunction(s, DOMWindow.WINDOW, DOMObjects.WINDOW_REMOVE_EVENT_LISTENER, "removeEventListener", 3, DOMSpec.LEVEL_0);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		/*
		 * Events added with useCapture = true must be removed
		 * separately from events added with useCapture = false. Model this?
		 */
		case EVENT_TARGET_ADD_EVENT_LISTENER:
		case WINDOW_ADD_EVENT_LISTENER: {
			NativeFunctions.expectParameters(nativeObject, call, c, 3, 3);
			Value type = Conversion.toString(call.getArg(0), c);
			Value function = call.getArg(1);
			Value useCapture = Conversion.toBoolean(call.getArg(2));
			if (type.isMaybeSingleStr()) {
				DOMEvents.addEventHandler(s, type.getStr(), function, c);
			} else {
				DOMEvents.addUnknownEventHandler(s, function, c);
			}
			return Value.makeUndef(new Dependency());
		}
		case EVENT_TARGET_REMOVE_EVENT_LISTENER:
		case WINDOW_REMOVE_EVENT_LISTENER: {
			NativeFunctions.expectParameters(nativeObject, call, c, 3, 3);
			Value type = Conversion.toString(call.getArg(0), c);
			Value function = DOMConversion.toEventHandler(s, call.getArg(1), c);
			Value useCapture = Conversion.toBoolean(call.getArg(2));

			if (type.isMaybeSingleStr()) {
				DOMEvents.removeEventHandler(s, type.getStr(), function, c);
			} else {
				if (Options.isDebugEnabled()) {
					System.out.println("WARN: EVENT_TARGET_REMOVE_EVENT_LISTENER not implemented.");
				}
			}
			return Value.makeUndef(new Dependency());
		}
		case EVENT_TARGET_DISPATCH_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			if (Options.isDebugEnabled()) {
				System.out.println("WARN: EVENT_TARGET_DISPATCH_EVENT not implemented.");
			}
			return Value.makeUndef(new Dependency());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
