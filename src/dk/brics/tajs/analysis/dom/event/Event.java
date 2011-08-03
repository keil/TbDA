package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;

/**
 * The Event interface is used to provide contextual information about an event
 * to the handler processing the event. An object which implements the Event
 * interface is generally passed as the first parameter to an event handler.
 * More specific context information is passed to event handlers by deriving
 * additional interfaces from Event which contain information directly relating
 * to the type of event they accompany. These derived interfaces are also
 * implemented by the object passed to the event listener.
 */
public class Event {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.EVENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "Event", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype object
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, PROTOTYPE, "type", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "target", DOMFunctions.makeAnyHTMLElement().setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "currentTarget", DOMFunctions.makeAnyHTMLElement().setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "eventPhase", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "bubbles", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "cancelable", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "timeStamp", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);

		// DOM LEVEL 0
		createDOMProperty(s, PROTOTYPE, "pageX", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, PROTOTYPE, "pageY", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		/*
		 * Constants (PhaseType).
		 */
		createDOMProperty(s, PROTOTYPE, "CAPTURING_PHASE", Value.makeNum(1, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "AT_TARGET", Value.makeNum(2, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "BUBBLING_PHASE", Value.makeNum(3, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);

		/*
		 * Functions.
		 */
		createDOMFunction(s, PROTOTYPE, DOMObjects.EVENT_STOP_PROPAGATION, "stopPropagation", 0, DOMSpec.LEVEL_2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.EVENT_PREVENT_DEFAULT, "preventDefault", 0, DOMSpec.LEVEL_2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.EVENT_INIT_EVENT, "initEvent", 3, DOMSpec.LEVEL_2);

	}

	/*
	 * Transfer function.
	 */

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case EVENT_INIT_EVENT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 3, 3);
			/* Value eventType = */Conversion.toString(call.getArg(0), c);
			/* Value canBubble = */Conversion.toBoolean(call.getArg(0));
			/* Value cancelable = */Conversion.toBoolean(call.getArg(0));
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case EVENT_PREVENT_DEFAULT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case EVENT_STOP_PROPAGATION: {
			throw new UnsupportedOperationException("EVENT_STOP_PROPAGATION not supported");
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}
}
