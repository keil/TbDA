package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * The UIEvent interface provides specific contextual information associated
 * with User Interface events.
 */
public class UIEvent {

	public static ObjectLabel UI_EVENT = new ObjectLabel(DOMObjects.UI_EVENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel UI_EVENT_PROTOTYPE = new ObjectLabel(DOMObjects.UI_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(UI_EVENT_PROTOTYPE);
		createDOMInternalPrototype(s, UI_EVENT_PROTOTYPE, Value.makeObject(Event.EVENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(UI_EVENT);
		createDOMInternalPrototype(s, UI_EVENT, Value.makeObject(UI_EVENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, UI_EVENT, "detail", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, UI_EVENT, "view", Value.makeObject(DOMWindow.WINDOW, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);

		/*
		 * Functions.
		 */
		createDOMFunction(s, UI_EVENT_PROTOTYPE, DOMObjects.UI_EVENT_INIT_UI_EVENT, "initUIEvent", 5, DOMSpec.LEVEL_2);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case UI_EVENT_INIT_UI_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 5, 5);
			Value typeArg = Conversion.toString(call.getArg(0), c);
			Value canBubble = Conversion.toBoolean(call.getArg(1));
			Value cancelableArg = Conversion.toBoolean(call.getArg(2));
			// View arg not modelled...
			Value detailArg = Conversion.toNumber(call.getArg(4), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default:
			throw new UnsupportedOperationException("Unsupported Native Object: "
					+ nativeObject);
		}
	}
}
