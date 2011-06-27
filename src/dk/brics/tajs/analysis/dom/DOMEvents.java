package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.event.KeyboardEvent;
import dk.brics.tajs.analysis.dom.event.MouseEvent;
import dk.brics.tajs.analysis.dom.event.MutationEvent;
import dk.brics.tajs.analysis.dom.event.WheelEvent;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collections;

import java.util.Set;

public class DOMEvents {

	/**
	 * Create generic Keyboard Event.
	 */
	public static Value createAnyKeyboardEvent(State s) {
		Set<ObjectLabel> labels = Collections.newSet();
		labels.add(KeyboardEvent.KEYBOARD_EVENT);
		return Value.makeObject(labels, new Dependency(), new DependencyGraphReference());
	}

	/**
	 * Create generic Mouse Event.
	 */
	public static Value createAnyMouseEvent(State s) {
		Set<ObjectLabel> labels = Collections.newSet();
		labels.add(MouseEvent.MOUSE_EVENT);
		return Value.makeObject(labels, new Dependency(), new DependencyGraphReference());
	}

	/**
	 * Create a generic non-mouse, non-keyboard Event.
	 */
	public static Value createAnyEvent(State s) {
		Set<ObjectLabel> labels = Collections.newSet();
		labels.add(KeyboardEvent.KEYBOARD_EVENT);
		labels.add(MouseEvent.MOUSE_EVENT);
		labels.add(MutationEvent.MUTATION_EVENT);
		labels.add(WheelEvent.WHEEL_EVENT);
		return Value.makeObject(labels, new Dependency(), new DependencyGraphReference());
	}

	/**
	 * Returns true iff the specified attribute is an event attribute.
	 */
	public static boolean isEventAttribute(String attribute) {
		return isKeyboardEventAttribute(attribute)
		|| isMouseEventAttribute(attribute)
		|| isOtherEventAttribute(attribute);
	}

	/**
	 * Returns true iff the specified property is an event property.
	 */
	public static boolean isEventProperty(String property) {
		return isKeyboardEventProperty(property)
		|| isMouseEventProperty(property)
		|| isOtherEventProperty(property);
	}

	/**
	 * Returns true iff the specified attribute is an load event attribute.
	 */
	public static boolean isLoadEventAttribute(String attribute) {
		return attribute.equalsIgnoreCase("load") || attribute.equalsIgnoreCase("onload");
	}

	/**
	 * Returns true iff the specified attribute is an unload event attribute.
	 */
	public static boolean isUnloadEventAttribute(String attribute) {
		return attribute.equalsIgnoreCase("unload") || attribute.equalsIgnoreCase("onunload");
	}

	/**
	 * Returns true iff the specified attribute is a keyboard event attribute.
	 */
	public static boolean isKeyboardEventAttribute(String attribute) {
		return attribute.equalsIgnoreCase("onkeypress")
		|| attribute.equalsIgnoreCase("onkeydown")
		|| attribute.equalsIgnoreCase("onkeyup");
	}

	/**
	 * Returns true iff the specified property is a keyboard event property.
	 */
	public static boolean isKeyboardEventProperty(String property) {
		return property.equalsIgnoreCase("keypress")
		|| property.equalsIgnoreCase("keydown")
		|| property.equalsIgnoreCase("keyup");
	}

	/**
	 * Returns true iff the specified attribute is a mouse event attribute.
	 */
	public static boolean isMouseEventAttribute(String attribute) {
		return attribute.equalsIgnoreCase("onclick")
		|| attribute.equalsIgnoreCase("ondblclick")
		|| attribute.equalsIgnoreCase("onmousedown")
		|| attribute.equalsIgnoreCase("onmouseup")
		|| attribute.equalsIgnoreCase("onmouseover")
		|| attribute.equalsIgnoreCase("onmousemove")
		|| attribute.equalsIgnoreCase("onmouseout");
	}

	/**
	 * Returns true iff the specified property is a mouse event property.
	 */
	public static boolean isMouseEventProperty(String property) {
		return property.equalsIgnoreCase("click")
		|| property.equalsIgnoreCase("dblclick")
		|| property.equalsIgnoreCase("mousedown")
		|| property.equalsIgnoreCase("mouseup")
		|| property.equalsIgnoreCase("mouseover")
		|| property.equalsIgnoreCase("mousemove")
		|| property.equalsIgnoreCase("mouseout");
	}

	/**
	 * Returns true iff the specified attribute is an other event attribute.
	 */
	public static boolean isOtherEventAttribute(String attribute) {
		return attribute.equalsIgnoreCase("onfocus")
		|| attribute.equalsIgnoreCase("onblur")
		|| attribute.equalsIgnoreCase("onsubmit")
		|| attribute.equalsIgnoreCase("onreset")
		|| attribute.equalsIgnoreCase("onselect")
		|| attribute.equalsIgnoreCase("onchange")
		|| attribute.equalsIgnoreCase("onresize");
	}

	/**
	 * Returns true iff the specified property is an other event property.
	 */
	public static boolean isOtherEventProperty(String property) {
		return property.equalsIgnoreCase("focus")
		|| property.equalsIgnoreCase("blur")
		|| property.equalsIgnoreCase("submit")
		|| property.equalsIgnoreCase("reset")
		|| property.equalsIgnoreCase("select")
		|| property.equalsIgnoreCase("change")
		|| property.equalsIgnoreCase("resize");
	}

	/**
	 * Add Event Handler. (NOT Timeout Event Handlers.)
	 */
	public static void addEventHandler(State s, String property, Value v, Solver.SolverInterface c) {
		if (isKeyboardEventAttribute(property) || isKeyboardEventProperty(property)) {
			addKeyboardEventHandler(s, v, c);
		} else if (isMouseEventAttribute(property) || isMouseEventProperty(property)) {
			addMouseEventHandler(s, v, c);
		} else if (isLoadEventAttribute(property)) {
			addLoadEventHandler(s, v, c);
		} else if (isUnloadEventAttribute(property)) {
			addUnloadEventHandler(s, v, c);
		} else {
			addUnknownEventHandler(s, v, c);
		}
	}

	/**
	 * Add a Keyboard Event Handler.
	 */
	public static void addKeyboardEventHandler(State s, Value v, Solver.SolverInterface c) {
		s.addKeyboardEventHandler(DOMConversion.toEventHandler(s, v, c).getObjectLabels());
	}

	/**
	 * Add a Mouse Event Handler.
	 */
	public static void addMouseEventHandler(State s, Value v, Solver.SolverInterface c) {
		s.addMouseEventHandler(DOMConversion.toEventHandler(s, v, c).getObjectLabels());
	}

	/**
	 * Add an Unknown Event Handler.
	 */
	public static void addUnknownEventHandler(State s, Value v, Solver.SolverInterface c) {
		s.addUnknownEventHandler(DOMConversion.toEventHandler(s, v, c).getObjectLabels());
	}

	/**
	 * Add a Timeout Event Handler.
	 */
	public static void addTimeoutEventHandler(State s, Value v, Solver.SolverInterface c) {
		s.addTimeoutEventHandler(DOMConversion.toEventHandler(s, v, c).getObjectLabels());
	}

	/**
	 * Add a Load Event Handler.
	 */
	public static void addLoadEventHandler(State s, Value v, Solver.SolverInterface c) {
		s.addLoadEventHandler(DOMConversion.toEventHandler(s, v, c).getObjectLabels());
	}

	/**
	 * Removes a Load Event Handler
	 */
	public static void removeLoadEventHandler(State s, Value v, Solver.SolverInterface c) {
		s.removeLoadEventHandler(DOMConversion.toEventHandler(s, v, c).getObjectLabels());
	}

	/**
	 * Add a Unload Event Handler.
	 */
	public static void addUnloadEventHandler(State s, Value v, Solver.SolverInterface c) {
		s.addUnloadEventHandler(DOMConversion.toEventHandler(s, v, c).getObjectLabels());
	}

	/**
	 * Removes the given Event Handler.
	 */
	public static void removeEventHandler(State s, String property, Value v, Solver.SolverInterface c) {
		if (isLoadEventAttribute(property)) {
			removeLoadEventHandler(s, v, c);
		} else {
			if (Options.isDebugEnabled()) {
				System.out.println("WARN: EVENT_TARGET_REMOVE_EVENT_LISTENER not implemented.");
			}
		}
	}

}
