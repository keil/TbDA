package dk.brics.tajs.analysis.dom;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;

/**
 * DOM Window
 */
public class DOMWindow {

	public static ObjectLabel WINDOW;

	public static ObjectLabel HISTORY;
	public static ObjectLabel LOCATION;
	public static ObjectLabel NAVIGATOR;
	public static ObjectLabel SCREEN;

	public static void build(State s) {
		HISTORY = new ObjectLabel(DOMObjects.WINDOW_HISTORY, Kind.OBJECT);
		LOCATION = new ObjectLabel(DOMObjects.WINDOW_LOCATION, Kind.OBJECT);
		NAVIGATOR = new ObjectLabel(DOMObjects.WINDOW_NAVIGATOR, Kind.OBJECT);
		SCREEN = new ObjectLabel(DOMObjects.WINDOW_SCREEN, Kind.OBJECT);

		// NB: The WINDOW object has already been instantiated.

		/*
		 * Properties.
		 */
		// DOM LEVEL 0
		createDOMProperty(s, WINDOW, "innerHeight", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "innerWidth", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "length", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "name", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "outerHeight", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "outerWidth", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "pageXOffset", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "pageYOffset", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "parent", Value.makeObject(WINDOW, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "self", Value.makeObject(WINDOW, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "scrollMaxX", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "scrollMaxY", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "scrollX", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "scrollY", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "screenX", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "screenY", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "status", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "top", Value.makeObject(WINDOW, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "window", Value.makeObject(WINDOW, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		/*
		 * Functions.
		 */
		// DOM LEVEL 0
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_ALERT, "alert", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_ATOB, "atob", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_BACK, "back", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_BLUR, "blur", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_BTOA, "btoa", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_CLEAR_INTERVAL, "clearInterval", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_CLEAR_TIMEOUT, "clearTimeout", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_CLOSE, "close", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_CONFIRM, "confirm", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_ESCAPE, "escape", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_FOCUS, "focus", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_FORWARD, "forward", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_HOME, "home", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_MAXIMIZE, "maximize", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_MINIMIZE, "minimize", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_MOVEBY, "moveBy", 2, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_MOVETO, "moveTo", 2, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_OPEN, "open", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_PRINT, "print", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_PROMPT, "prompt", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_RESIZEBY, "resizeBy", 2, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_RESIZETO, "resizeTo", 2, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SCROLL, "scroll", 2, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SCROLLBY, "scrollBy", 2, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SCROLLBYLINES, "scrollByLines", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SCROLLBYPAGES, "scrollByPages", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SCROLLTO, "scrollTo", 2, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SET_INTERVAL, "setInterval", 2, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SET_TIMEOUT, "setTimeout", 2, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_STOP, "stop", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_UNESCAPE, "unescape", 1, DOMSpec.LEVEL_0);

		/**
		 * WINDOW HISTORY object
		 */
		s.newObject(HISTORY);
		DOMFunctions.createDOMInternalPrototype(s, HISTORY,
				Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		// Properties.
		createDOMProperty(s, HISTORY, "length", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, WINDOW, "history", Value.makeObject(HISTORY, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		// Functions.
		createDOMFunction(s, HISTORY, DOMObjects.WINDOW_HISTORY_BACK, "back", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, HISTORY, DOMObjects.WINDOW_HISTORY_FORWARD, "forward", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, HISTORY, DOMObjects.WINDOW_HISTORY_GO, "go", 1, DOMSpec.LEVEL_0);

		/**
		 * WINDOW LOCATION object
		 */
		s.newObject(LOCATION);
		createDOMInternalPrototype(s, LOCATION, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, WINDOW, "location", Value.makeObject(LOCATION, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		// Properties.
		createDOMProperty(s, LOCATION, "hash", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, LOCATION, "host", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, LOCATION, "hostname", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, LOCATION, "href", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, LOCATION, "pathname", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, LOCATION, "port", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, LOCATION, "protocol", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, LOCATION, "search", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, LOCATION, "hash", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		// Functions.
		createDOMFunction(s, LOCATION, DOMObjects.WINDOW_LOCATION_ASSIGN, "assign", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, LOCATION, DOMObjects.WINDOW_LOCATION_RELOAD, "reload", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, LOCATION, DOMObjects.WINDOW_LOCATION_REPLACE, "replace", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, LOCATION, DOMObjects.WINDOW_LOCATION_TOSTRING, "toString", 0, DOMSpec.LEVEL_0);

		/**
		 * WINDOW NAVIGATOR object
		 */
		s.newObject(NAVIGATOR);
		createDOMInternalPrototype(s, NAVIGATOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, WINDOW, "navigator", Value.makeObject(NAVIGATOR, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		// Properties.
		createDOMProperty(s, NAVIGATOR, "product", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, NAVIGATOR, "appName", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, NAVIGATOR, "appVersion", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, NAVIGATOR, "userAgent", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		/**
		 * WINDOW SCREEN object
		 */
		s.newObject(SCREEN);
		createDOMInternalPrototype(s, SCREEN, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, WINDOW, "screen", Value.makeObject(SCREEN, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		// Properties.
		createDOMProperty(s, SCREEN, "availTop", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, SCREEN, "availLeft", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, SCREEN, "availHeight", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, SCREEN, "availWidth", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, SCREEN, "colorDepth", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, SCREEN, "height", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, SCREEN, "left", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, SCREEN, "pixelDepth", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, SCREEN, "top", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, SCREEN, "width", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
	}

	public static Value evaluate(DOMObjects nativeObject, final FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case WINDOW_ALERT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_ATOB: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			return Value.makeAnyStr(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_BACK: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_BLUR: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_BTOA: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			return Value.makeAnyStr(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_CLOSE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_CLEAR_INTERVAL: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Conversion.toInteger(NativeFunctions.readParameter(call, 0), c);
			// TODO: Fix Later: Event Handlers cannot be removed.
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_CLEAR_TIMEOUT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Conversion.toInteger(NativeFunctions.readParameter(call, 0), c);
			// TODO: Fix Later: Event Handlers cannot be removed.
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_CONFIRM: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			return Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_ESCAPE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			return Value.makeAnyStr(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_FOCUS: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_FORWARD: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_HISTORY_BACK: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_HISTORY_FORWARD: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_HISTORY_GO: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value v = */Conversion.toNumber(call.getArg(0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_HOME: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_LOCATION_ASSIGN: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value url = */Conversion.toString(call.getArg(0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_LOCATION_RELOAD: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value url = */Conversion.toBoolean(call.getArg(0));
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_LOCATION_REPLACE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value url = */Conversion.toString(call.getArg(0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_LOCATION_TOSTRING: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeAnyStr(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_MAXIMIZE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_MINIMIZE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_MOVEBY: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Conversion.toNumber(NativeFunctions.readParameter(call, 0), c);
			Conversion.toNumber(NativeFunctions.readParameter(call, 1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_MOVETO: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Conversion.toNumber(NativeFunctions.readParameter(call, 0), c);
			Conversion.toNumber(NativeFunctions.readParameter(call, 1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_OPEN: {
			if (Options.isDebugEnabled()) {
				System.out.println("WARNING: WINDOW_OPEN not supported");
			}
			return Value.makeObject(DOMWindow.WINDOW, new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_PRINT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_PROMPT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 2);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			Conversion.toString(NativeFunctions.readParameter(call, 1), c);
			return Value.makeAnyStr(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_RESIZEBY: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Conversion.toNumber(NativeFunctions.readParameter(call, 0), c);
			Conversion.toNumber(NativeFunctions.readParameter(call, 1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_RESIZETO: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Conversion.toNumber(NativeFunctions.readParameter(call, 0), c);
			Conversion.toNumber(NativeFunctions.readParameter(call, 1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_SCROLL: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Conversion.toNumber(NativeFunctions.readParameter(call, 0), c);
			Conversion.toNumber(NativeFunctions.readParameter(call, 1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_SCROLLBY: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Conversion.toNumber(NativeFunctions.readParameter(call, 0), c);
			Conversion.toNumber(NativeFunctions.readParameter(call, 1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_SCROLLBYLINES: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Conversion.toNumber(NativeFunctions.readParameter(call, 0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_SCROLLBYPAGES: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Conversion.toNumber(NativeFunctions.readParameter(call, 0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_SCROLLTO: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Conversion.toNumber(NativeFunctions.readParameter(call, 0), c);
			Conversion.toNumber(NativeFunctions.readParameter(call, 1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_STOP: {
			s.setToBottom();
			return Value.makeBottom(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_SET_INTERVAL:
		case WINDOW_SET_TIMEOUT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Value functionOrCode = call.getArg(0);

			// Handle string lattice before passing to
			// DOMFunctions.addTimeoutHandler
			if (functionOrCode.isMaybeSingleStr()) {
				// FIXME: only works if no parameters?!
				// Hack, to get somewhat better precision (to handle cases where
				// some writes 'foo()')
				String functionName = functionOrCode.getStr();
				if (functionName.endsWith("()")) {
					functionName = functionName.substring(0, functionName.length() - 2);
				} else if (functionName.endsWith("();")) {
					functionName = functionName.substring(0, functionName.length() - 3);
				}

				Function f = c.getFlowGraph().getFunction(functionName);
				if (f != null) {
					// We got lucky, found a matching function
					s.addTimeoutHandler(new ObjectLabel(f));
					return Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference());
				}
				throw new UnsupportedOperationException("setInterval/setTimeout expects an existing function with no arguments as argument.");
			} else if (functionOrCode.isMaybeAnyStr()) {
				throw new UnsupportedOperationException("Can't handle arbitrary strings in setInterval/setTimeout.");
			}

			DOMEvents.addTimeoutEventHandler(s, functionOrCode, c);
			return Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference());
		}
		case WINDOW_UNESCAPE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			return Value.makeAnyStr(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}
}
