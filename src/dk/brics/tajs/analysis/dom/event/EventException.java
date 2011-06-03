package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


public class EventException {

	public static ObjectLabel EVENT_EXCEPTION = new ObjectLabel(DOMObjects.EVENT_EXCEPTION, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel EVENT_EXCEPTION_PROTOTYPE = new ObjectLabel(DOMObjects.EVENT_EXCEPTION_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(EVENT_EXCEPTION_PROTOTYPE);
		createDOMInternalPrototype(s, EVENT_EXCEPTION_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency()));

		// Multiplied object.
		s.newObject(EVENT_EXCEPTION);
		createDOMInternalPrototype(s, EVENT_EXCEPTION, Value.makeObject(EVENT_EXCEPTION_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "EventException", Value.makeObject(EVENT_EXCEPTION, new Dependency()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, EVENT_EXCEPTION, "code", Value.makeAnyNumUInt(new Dependency()), DOMSpec.LEVEL_2);

		s.multiplyObject(EVENT_EXCEPTION);
		EVENT_EXCEPTION = EVENT_EXCEPTION.makeSingleton().makeSummary();

		/*
		 * Constants.
		 */
		createDOMProperty(s, EVENT_EXCEPTION_PROTOTYPE, "UNSPECIFIED_EVENT_TYPE_ERR", Value.makeNum(0, new Dependency()), DOMSpec.LEVEL_2);

		/*
		 * Functions.
		 */
	}
}
