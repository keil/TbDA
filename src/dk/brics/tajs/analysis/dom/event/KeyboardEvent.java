package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
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

/**
 * The KeyboardEvent interface provides specific contextual information
 * associated with keyboard devices. Each keyboard event references a key using
 * an identifier. Keyboard events are commonly directed at the element that has
 * the focus.
 */
public class KeyboardEvent {

	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		PROTOTYPE = new ObjectLabel(DOMObjects.KEYBOARD_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.KEYBOARD_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Prototype object
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(UIEvent.PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Constants (KeyLocation code)
		 */
		createDOMProperty(s, PROTOTYPE, "DOM_KEY_LOCATION_STANDARD", Value.makeNum(0, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, PROTOTYPE, "DOM_KEY_LOCATION_LEFT", Value.makeNum(1, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, PROTOTYPE, "DOM_KEY_LOCATION_RIGHT", Value.makeNum(2, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, PROTOTYPE, "DOM_KEY_LOCATION_NUMPAD", Value.makeNum(3, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, PROTOTYPE, "DOM_KEY_LOCATION_MOBILE", Value.makeNum(4, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, PROTOTYPE, "DOM_KEY_LOCATION_JOYSTICK", Value.makeNum(5, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);

		/*
		 * Properties
		 */
		createDOMProperty(s, INSTANCES, "keyIdentifier", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "keyLocation", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "ctrlKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "shiftKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "altKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "metaKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "repeat", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);

		// DOM LEVEL 0:
		createDOMProperty(s, PROTOTYPE, "charCode", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, PROTOTYPE, "key", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, PROTOTYPE, "keyCode", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		/*
		 * Functions
		 */

		// DOM Level 3:
		createDOMFunction(s, PROTOTYPE, DOMObjects.KEYBOARD_EVENT_GET_MODIFIER_STATE, "getModifierState", 1, DOMSpec.LEVEL_3);
		createDOMFunction(s, PROTOTYPE, DOMObjects.KEYBOARD_EVENT_INIT_KEYBOARD_EVENT, "initKeyboardEvent", 7, DOMSpec.LEVEL_3);
		createDOMFunction(s, PROTOTYPE, DOMObjects.KEYBOARD_EVENT_INIT_KEYBOARD_EVENT_NS, "initKeyboardEventNS", 8, DOMSpec.LEVEL_3);

		// Multiplied object
		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		// DOM Registry
		DOMRegistry.registerKeyboardEventLabel(INSTANCES);
	}

	/*
	 * Transfer functions
	 */

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case KEYBOARD_EVENT_GET_MODIFIER_STATE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value keyIdentifierArg = */Conversion.toString(call.getArg(0), c);
			return Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		}
		case KEYBOARD_EVENT_INIT_KEYBOARD_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 7, 7);
			/* Value typeArg = */Conversion.toString(call.getArg(0), c);
			/* Value canBubbleArg = */Conversion.toBoolean(call.getArg(1));
			/* Value cancelableArg = */Conversion.toBoolean(call.getArg(2));
			// viewArg not checked...
			/* Value keyIdentifierArg = */Conversion.toString(call.getArg(4), c);
			/* Value keyLocationArg = */Conversion.toNumber(call.getArg(5), c);
			/* Value modifiersListArg = */Conversion.toString(call.getArg(6), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case KEYBOARD_EVENT_INIT_KEYBOARD_EVENT_NS: {
			throw new UnsupportedOperationException("KeyboardEvent.initKeyboardEventNS not supported!");
		}
		default: {
			throw new UnsupportedOperationException("Unsupported Native Object");
		}
		}
	}

}
