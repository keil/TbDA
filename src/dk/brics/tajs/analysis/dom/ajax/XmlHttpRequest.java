package dk.brics.tajs.analysis.dom.ajax;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

/**
 * The XMLHttpRequest object can be used by scripts to programmatically connect
 * to their originating server via HTTP. In the future W3C DOM might make use of
 * th EventTarget interface for this object.
 */
public class XmlHttpRequest {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.XML_HTTP_REQUEST_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.XML_HTTP_REQUEST_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.XML_HTTP_REQUEST_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "XMLHttpRequest", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype object
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Constants.
		 */
		createDOMProperty(s, PROTOTYPE, "UNSENT", Value.makeNum(0, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, PROTOTYPE, "OPENED", Value.makeNum(1, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, PROTOTYPE, "HEADERS_RECEIVED", Value.makeNum(2, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, PROTOTYPE, "LOADING", Value.makeNum(3, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, PROTOTYPE, "DONE", Value.makeNum(4, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		/*
		 * Properties.
		 */
		createDOMProperty(s, INSTANCES, "readyState", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_0);
		createDOMProperty(s, INSTANCES, "status", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_0);
		createDOMProperty(s, INSTANCES, "statusText", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_0);

		if (Options.isReturnJSON()) {
			createDOMProperty(s, INSTANCES, "responseText", Value.makeJSONStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		} else {
			createDOMProperty(s, INSTANCES, "responseText", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		}
		// TODO createDOMProperty(s, INSTANCES, "responseXML",
		// Value.makeObject(DOMDocument.DOCUMENT).setReadOnly(),
		// DOMSpec.LEVEL_0);

		/*
		 * Functions.
		 */
		createDOMFunction(s, PROTOTYPE, DOMObjects.XML_HTTP_REQUEST_OPEN, "open", 5, DOMSpec.LEVEL_0);
		createDOMFunction(s, PROTOTYPE, DOMObjects.XML_HTTP_REQUEST_SEND, "send", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, PROTOTYPE, DOMObjects.XML_HTTP_REQUEST_SET_REQUEST_HEADER, "setRequestHeader", 2, DOMSpec.LEVEL_0);
		createDOMFunction(s, PROTOTYPE, DOMObjects.XML_HTTP_REQUEST_ABORT, "abort", 0, DOMSpec.LEVEL_0);
		createDOMFunction(s, PROTOTYPE, DOMObjects.XML_HTTP_REQUEST_GET_RESPONSE_HEADER, "getResponseHeader", 1, DOMSpec.LEVEL_0);
		createDOMFunction(s, PROTOTYPE, DOMObjects.XML_HTTP_REQUEST_GET_ALL_RESPONSE_HEADERS, "getAllResponseHeaders", 0, DOMSpec.LEVEL_0);

		// Multiply object
		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();
	}

	/*
	 * Transfer functions
	 */

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case XML_HTTP_REQUEST_OPEN: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 5);
			/* Value method = */Conversion.toString(call.getArg(0), c);
			/* Value url = */Conversion.toString(call.getArg(1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}

		case XML_HTTP_REQUEST_SET_REQUEST_HEADER: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			/* Value header = */Conversion.toString(call.getArg(0), c);
			/* Value value = */Conversion.toString(call.getArg(1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}

		case XML_HTTP_REQUEST_SEND: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 1);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}

		case XML_HTTP_REQUEST_ABORT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}

		case XML_HTTP_REQUEST_GET_RESPONSE_HEADER: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value header = */Conversion.toString(call.getArg(0), c);
			return Value.makeAnyStr(new Dependency(), new DependencyGraphReference());
		}

		case XML_HTTP_REQUEST_GET_ALL_RESPONSE_HEADERS: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeAnyStr(new Dependency(), new DependencyGraphReference());
		}

		case XML_HTTP_REQUEST_CONSTRUCTOR: {
			return Value.makeObject(INSTANCES, new Dependency(), new DependencyGraphReference());
		}

		default: {
			throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
