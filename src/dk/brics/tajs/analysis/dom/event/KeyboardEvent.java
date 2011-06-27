package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
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
 * The KeyboardEvent interface provides specific contextual information
 * associated with keyboard devices. Each keyboard event references a key using
 * an identifier. Keyboard events are commonly directed at the element that has
 * the focus.
 */
public class KeyboardEvent {

	public static ObjectLabel KEYBOARD_EVENT = new ObjectLabel(DOMObjects.KEYBOARD_EVENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel KEYBOARD_EVENT_PROTOTYPE = new ObjectLabel(DOMObjects.KEYBOARD_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object
		s.newObject(KEYBOARD_EVENT_PROTOTYPE);
		createDOMInternalPrototype(s, KEYBOARD_EVENT_PROTOTYPE, Value.makeObject(UIEvent.UI_EVENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(KEYBOARD_EVENT);
		createDOMInternalPrototype(s, KEYBOARD_EVENT, Value.makeObject(KEYBOARD_EVENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Constants (KeyLocation code)
		 */
		createDOMProperty(s, KEYBOARD_EVENT_PROTOTYPE, "DOM_KEY_LOCATION_STANDARD", Value.makeNum(0, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, KEYBOARD_EVENT_PROTOTYPE, "DOM_KEY_LOCATION_LEFT", Value.makeNum(1, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, KEYBOARD_EVENT_PROTOTYPE, "DOM_KEY_LOCATION_RIGHT", Value.makeNum(2, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, KEYBOARD_EVENT_PROTOTYPE, "DOM_KEY_LOCATION_NUMPAD", Value.makeNum(3, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, KEYBOARD_EVENT_PROTOTYPE, "DOM_KEY_LOCATION_MOBILE", Value.makeNum(4, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, KEYBOARD_EVENT_PROTOTYPE, "DOM_KEY_LOCATION_JOYSTICK", Value.makeNum(5, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);

		/*
		 * Properties
		 */
		createDOMProperty(s, KEYBOARD_EVENT, "keyIdentifier", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, KEYBOARD_EVENT, "keyLocation", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, KEYBOARD_EVENT, "ctrlKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, KEYBOARD_EVENT, "shiftKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, KEYBOARD_EVENT, "altKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, KEYBOARD_EVENT, "metaKey", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, KEYBOARD_EVENT, "repeat", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);

		// DOM LEVEL 0:
		createDOMProperty(s, KEYBOARD_EVENT_PROTOTYPE, "charCode", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, KEYBOARD_EVENT_PROTOTYPE, "key", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, KEYBOARD_EVENT_PROTOTYPE, "keyCode", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		/*
		 * Functions
		 */

		// DOM Level 3:
		createDOMFunction(s, KEYBOARD_EVENT_PROTOTYPE, DOMObjects.KEYBOARD_EVENT_GET_MODIFIER_STATE, "getModifierState", 1, DOMSpec.LEVEL_3);
		createDOMFunction(s, KEYBOARD_EVENT_PROTOTYPE, DOMObjects.KEYBOARD_EVENT_INIT_KEYBOARD_EVENT, "initKeyboardEvent", 7, DOMSpec.LEVEL_3);
		createDOMFunction(s, KEYBOARD_EVENT_PROTOTYPE, DOMObjects.KEYBOARD_EVENT_INIT_KEYBOARD_EVENT_NS, "initKeyboardEventNS", 8, DOMSpec.LEVEL_3);

		// Multiplied object
		s.multiplyObject(KEYBOARD_EVENT);
		KEYBOARD_EVENT = KEYBOARD_EVENT.makeSingleton().makeSummary();
	}

	/*
	 * Transfer functions
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case KEYBOARD_EVENT_GET_MODIFIER_STATE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value keyIdentifierArg = Conversion.toString(call.getArg(0), c);
			return Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		}
		case KEYBOARD_EVENT_INIT_KEYBOARD_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 7, 7);
			Value typeArg = Conversion.toString(call.getArg(0), c);
			Value canBubbleArg = Conversion.toBoolean(call.getArg(1));
			Value cancelableArg = Conversion.toBoolean(call.getArg(2));
			// viewArg not checked...
			Value keyIdentifierArg = Conversion.toString(call.getArg(4), c);
			Value keyLocationArg = Conversion.toNumber(call.getArg(5), c);
			Value modifiersListArg = Conversion.toString(call.getArg(6), c);
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
