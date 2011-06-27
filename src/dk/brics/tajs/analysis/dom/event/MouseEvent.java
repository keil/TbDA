package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * The MouseEvent interface provides specific contextual information associated
 * with Mouse events.
 */
public class MouseEvent {

	public static ObjectLabel MOUSE_EVENT = new ObjectLabel(DOMObjects.MOUSE_EVENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel MOUSE_EVENT_PROTOTYPE = new ObjectLabel(DOMObjects.MOUSE_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object
		s.newObject(MOUSE_EVENT_PROTOTYPE);
		createDOMInternalPrototype(s, MOUSE_EVENT_PROTOTYPE, Value.makeObject(UIEvent.UI_EVENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(MOUSE_EVENT);
		createDOMInternalPrototype(s, MOUSE_EVENT, Value.makeObject(MOUSE_EVENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, MOUSE_EVENT, "screenX", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MOUSE_EVENT, "screenY", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MOUSE_EVENT, "clientX", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MOUSE_EVENT, "clientY", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MOUSE_EVENT, "ctrlKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MOUSE_EVENT, "shiftKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MOUSE_EVENT, "altKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MOUSE_EVENT, "metaKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MOUSE_EVENT, "button", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, MOUSE_EVENT, "relatedTarget", DOMFunctions.makeAnyHTMLElement().setReadOnly(), DOMSpec.LEVEL_2);

		/*
		 * Functions.
		 */
		createDOMFunction(s, MOUSE_EVENT_PROTOTYPE, DOMObjects.MOUSE_EVENT_INIT_MOUSE_EVENT, "initMouseEvent", 15, DOMSpec.LEVEL_2);

		// Multiplied Object
		s.multiplyObject(MOUSE_EVENT);
		MOUSE_EVENT = MOUSE_EVENT.makeSingleton().makeSummary();
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case MOUSE_EVENT_INIT_MOUSE_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 15, 15);
			Value typeArg = Conversion.toString(call.getArg(0), c);
			Value canBubbleArg = Conversion.toBoolean(call.getArg(1));
			Value cancelableArg = Conversion.toBoolean(call.getArg(2));
			// View arg not checked
			Value detailArg = Conversion.toNumber(call.getArg(4), c);
			Value screenXArg = Conversion.toNumber(call.getArg(5), c);
			Value screenYArg = Conversion.toNumber(call.getArg(6), c);
			Value clientXArg = Conversion.toNumber(call.getArg(7), c);
			Value clientYArg = Conversion.toNumber(call.getArg(8), c);
			Value ctrlKeyArg = Conversion.toBoolean(call.getArg(9));
			Value altKeyArg = Conversion.toBoolean(call.getArg(10));
			Value shiftKeyArg = Conversion.toBoolean(call.getArg(11));
			Value metaKeyArg = Conversion.toBoolean(call.getArg(12));
			Value buttonArg = Conversion.toNumber(call.getArg(13), c);
			Value relatedTargetArg = DOMConversion.toEventTarget(call.getArg(14), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default:
			throw new UnsupportedOperationException("Unsupported Native Object: "
					+ nativeObject);
		}
	}
}
