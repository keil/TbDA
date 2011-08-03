package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.State;

/**
 * Initial State Builder for DOM Events.
 * <p/>
 * More information at:
 * <p/>
 * http://www.w3.org/TR/2000/REC-DOM-Level-2-Events-20001113/
 * http://www.w3.org/TR/2003/NOTE-DOM-Level-3-Events-20031107/
 * <p/>
 * http://www.w3.org/TR/REC-html40/interact/scripts.html#h-18.2.3
 */
public class EventBuilder {

	public static void build(State s) {
		Event.build(s);
		EventTarget.build(s);
		EventListener.build(s);
		EventException.build(s);
		DocumentEvent.build(s);
		MutationEvent.build(s);
		UIEvent.build(s);
		KeyboardEvent.build(s);
		MouseEvent.build(s);
		WheelEvent.build(s);
	}

}
