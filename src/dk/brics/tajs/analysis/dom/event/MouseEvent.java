package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.*;
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
 * The MouseEvent interface provides specific contextual information associated
 * with Mouse events.
 */
public class MouseEvent {

	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		PROTOTYPE = new ObjectLabel(DOMObjects.MOUSE_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.MOUSE_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Prototype object
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(UIEvent.PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, INSTANCES, "screenX", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "screenY", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "clientX", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "clientY", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "ctrlKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "shiftKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "altKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "metaKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "button", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "relatedTarget", DOMFunctions.makeAnyHTMLElement().setReadOnly(), DOMSpec.LEVEL_2);

		// DOM Level 0
		createDOMProperty(s, INSTANCES, "offsetX", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_0);
		createDOMProperty(s, INSTANCES, "offsetY", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_0);
		createDOMProperty(s, INSTANCES, "layerX", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_0);
		createDOMProperty(s, INSTANCES, "layerY", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_0);

		/*
		 * Functions.
		 */
		createDOMFunction(s, PROTOTYPE, DOMObjects.MOUSE_EVENT_INIT_MOUSE_EVENT, "initMouseEvent", 15, DOMSpec.LEVEL_2);

		// Multiplied Object
		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		// DOM Registry
		DOMRegistry.registerMouseEventLabel(INSTANCES);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case MOUSE_EVENT_INIT_MOUSE_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 15, 15);
			/* Value typeArg = */
			Conversion.toString(call.getArg(0), c);
			/* Value canBubbleArg = */
			Conversion.toBoolean(call.getArg(1));
			/* Value cancelableArg = */
			Conversion.toBoolean(call.getArg(2));
			// View arg not checked
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
			/* Value ctrlKeyArg = */
			Conversion.toBoolean(call.getArg(9));
			/* Value altKeyArg = */
			Conversion.toBoolean(call.getArg(10));
			/* Value shiftKeyArg = */
			Conversion.toBoolean(call.getArg(11));
			/* Value metaKeyArg = */
			Conversion.toBoolean(call.getArg(12));
			/* Value buttonArg = */
			Conversion.toNumber(call.getArg(13), c);
			/* Value relatedTargetArg = */
			DOMConversion.toEventTarget(call.getArg(14), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default:
			throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
		}
	}
}
