package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.flowgraph.ObjectLabel;

public class DOMRegistry {

	private static ObjectLabel keyboardEvent;
	private static ObjectLabel mouseEvent;
	private static ObjectLabel ajaxEvent;
	private static ObjectLabel mutationEvent;
	private static ObjectLabel wheelEvent;

	public static void reset() {
		keyboardEvent = null;
		mouseEvent = null;
		ajaxEvent = null;
		mutationEvent = null;
		wheelEvent = null;
	}

	public static void registerKeyboardEventLabel(ObjectLabel l) {
		keyboardEvent = l;
	}

	public static void registerMouseEventLabel(ObjectLabel l) {
		mouseEvent = l;
	}

	public static void registerAjaxEventLabel(ObjectLabel l) {
		ajaxEvent = l;
	}

	public static void registerMutationEventLabel(ObjectLabel l) {
		mutationEvent = l;
	}

	public static void registerWheelEventLabel(ObjectLabel l) {
		wheelEvent = l;
	}

	public static ObjectLabel getKeyboardEventLabel() {
		if (keyboardEvent == null) {
			throw new IllegalStateException("No keyboard event object labels registered");
		}
		return keyboardEvent;
	}

	public static ObjectLabel getMouseEventLabel() {
		if (mouseEvent == null) {
			throw new IllegalStateException("No mouse event object labels registered");
		}
		return mouseEvent;
	}

	public static ObjectLabel getAjaxEventLabel() {
		if (ajaxEvent == null) {
			throw new IllegalStateException("No ajax event object labels registered");
		}
		return ajaxEvent;
	}

	public static ObjectLabel getMutationEventLabel() {
		if (mutationEvent == null) {
			throw new IllegalStateException("No mutation event object labels registered");
		}
		return mutationEvent;
	}

	public static ObjectLabel getWheelEventLabel() {
		if (wheelEvent == null) {
			throw new IllegalStateException("No wheel event object labels registered");
		}
		return wheelEvent;
	}

}
