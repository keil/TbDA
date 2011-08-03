package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMRegistry;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

public class WheelEvent {

	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		PROTOTYPE = new ObjectLabel(DOMObjects.WHEEL_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.WHEEL_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Prototype object
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(MouseEvent.PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Constants.
		 */
		createDOMProperty(s, PROTOTYPE, "DOM_DELTA_PIXEL", Value.makeNum(0, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, PROTOTYPE, "DOM_DELTA_LINE", Value.makeNum(1, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, PROTOTYPE, "DOM_DELTA_PAGE", Value.makeNum(2, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);

		/*
		 * Properties.
		 */
		createDOMProperty(s, INSTANCES, "deltaX", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "deltaY", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "deltaZ", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "deltaMode", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);

		/*
		 * Functions.
		 */
		createDOMFunction(s, PROTOTYPE, DOMObjects.WHEEL_EVENT_INIT_WHEEL_EVENT, "initWheelEvent", 16, DOMSpec.LEVEL_3);
		createDOMFunction(s, PROTOTYPE, DOMObjects.WHEEL_EVENT_INIT_WHEEL_EVENT_NS, "initWheelEventNS", 17, DOMSpec.LEVEL_3);

		// Multiplied object
		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		// DOM Registry
		DOMRegistry.registerWheelEventLabel(INSTANCES);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case WHEEL_EVENT_INIT_WHEEL_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 16, 16);
			/* Value typeArg = */
			Conversion.toString(call.getArg(0), c);
			/* Value canBubbleArg = */
			Conversion.toBoolean(call.getArg(1));
			/* Value cancelableArg = */
			Conversion.toBoolean(call.getArg(2));
			// viewArg not checked
			/* Value detailArg = */
			Conversion.toNumber(call.getArg(4), c);
			/* Value screenXArg = */
			Conversion.toNumber(call.getArg(5), c);
			/* Value screenYArg = */
			Conversion.toNumber(call.getArg(6), c);
			/* Value clientXArg = */
			Conversion.toNumber(call.getArg(7), c);
			/* Value clientYArg = */
			Conversion.toNumber(call.getArg(8), c);
			/* Value buttonArg = */
			Conversion.toNumber(call.getArg(9), c);
			/* Value relatedTargetArg = */
			DOMConversion.toEventTarget(call.getArg(10), c);
			/* Value modifiersListArg = */
			Conversion.toString(call.getArg(11), c);
			/* Value deltaXArg = */
			Conversion.toNumber(call.getArg(12), c);
			/* Value deltaYArg = */
			Conversion.toNumber(call.getArg(13), c);
			/* Value deltaZArg = */
			Conversion.toNumber(call.getArg(14), c);
			/* Value deltaMode = */
			Conversion.toNumber(call.getArg(15), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
