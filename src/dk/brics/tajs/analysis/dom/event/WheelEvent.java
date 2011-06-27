package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

public class WheelEvent {

	public static ObjectLabel WHEEL_EVENT = new ObjectLabel(DOMObjects.WHEEL_EVENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel WHEEL_EVENT_PROTOTYPE = new ObjectLabel(DOMObjects.WHEEL_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object
		s.newObject(WHEEL_EVENT_PROTOTYPE);
		createDOMInternalPrototype(s, WHEEL_EVENT_PROTOTYPE, Value.makeObject(MouseEvent.MOUSE_EVENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(WHEEL_EVENT);
		createDOMInternalPrototype(s, WHEEL_EVENT, Value.makeObject(WHEEL_EVENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Constants.
		 */
		createDOMProperty(s, WHEEL_EVENT_PROTOTYPE, "DOM_DELTA_PIXEL", Value.makeNum(0, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, WHEEL_EVENT_PROTOTYPE, "DOM_DELTA_LINE", Value.makeNum(1, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, WHEEL_EVENT_PROTOTYPE, "DOM_DELTA_PAGE", Value.makeNum(2, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);

		/*
		 * Properties.
		 */
		createDOMProperty(s, WHEEL_EVENT, "deltaX", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, WHEEL_EVENT, "deltaY", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, WHEEL_EVENT, "deltaZ", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, WHEEL_EVENT, "deltaMode", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);

		/*
		 * Functions.
		 */
		createDOMFunction(s, WHEEL_EVENT_PROTOTYPE, DOMObjects.WHEEL_EVENT_INIT_WHEEL_EVENT, "initWheelEvent", 16, DOMSpec.LEVEL_3);
		createDOMFunction(s, WHEEL_EVENT_PROTOTYPE, DOMObjects.WHEEL_EVENT_INIT_WHEEL_EVENT_NS, "initWheelEventNS", 17, DOMSpec.LEVEL_3);

		// Multiplied object
		s.multiplyObject(WHEEL_EVENT);
		WHEEL_EVENT = WHEEL_EVENT.makeSingleton().makeSummary();
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case WHEEL_EVENT_INIT_WHEEL_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 16, 16);
			Value typeArg = Conversion.toString(call.getArg(0), c);
			Value canBubbleArg = Conversion.toBoolean(call.getArg(1));
			Value cancelableArg = Conversion.toBoolean(call.getArg(2));
			// viewArg not checked
			Value detailArg = Conversion.toNumber(call.getArg(4), c);
			Value screenXArg = Conversion.toNumber(call.getArg(5), c);
			Value screenYArg = Conversion.toNumber(call.getArg(6), c);
			Value clientXArg = Conversion.toNumber(call.getArg(7), c);
			Value clientYArg = Conversion.toNumber(call.getArg(8), c);
			Value buttonArg = Conversion.toNumber(call.getArg(9), c);
			Value relatedTargetArg = DOMConversion.toEventTarget(call.getArg(10), c);
			Value modifiersListArg = Conversion.toString(call.getArg(11), c);
			Value deltaXArg = Conversion.toNumber(call.getArg(12), c);
			Value deltaYArg = Conversion.toNumber(call.getArg(13), c);
			Value deltaZArg = Conversion.toNumber(call.getArg(14), c);
			Value deltaMode = Conversion.toNumber(call.getArg(15), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
