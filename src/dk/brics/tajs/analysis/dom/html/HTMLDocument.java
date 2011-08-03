package dk.brics.tajs.analysis.dom.html;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;

import java.util.Set;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMUnknownArrayProperty;

/**
 * An HTMLDocument is the root of the HTML hierarchy and holds the entire
 * content. Besides providing access to the hierarchy, it also provides some
 * convenience methods for accessing certain sets of information from the
 * document.
 */
public class HTMLDocument {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLDOCUMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.HTMLDOCUMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.HTMLDOCUMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLDocument", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype Object
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(DOMDocument.INSTANCES, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, INSTANCES, "title", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "referrer", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "domain", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "URL", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "images", Value.makeObject(HTMLCollection.INSTANCES, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "applets", Value.makeObject(HTMLCollection.INSTANCES, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "links", Value.makeObject(HTMLCollection.INSTANCES, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "forms", Value.makeObject(HTMLCollection.INSTANCES, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "anchors", Value.makeObject(HTMLCollection.INSTANCES, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "cookie", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		// DOM LEVEL 0 / UNKNOWN
		createDOMProperty(s, INSTANCES, "width", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, INSTANCES, "height", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLDOCUMENT_OPEN, "open", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLDOCUMENT_CLOSE, "close", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLDOCUMENT_WRITE, "write", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLDOCUMENT_WRITELN, "writeln", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLDOCUMENT_GET_ELEMENTS_BY_NAME, "getElementsByName", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLDOCUMENT_GET_ELEMENTS_BY_CLASS_NAME, "getElementsByClassName", 1, DOMSpec.LEVEL_0);

		createDOMProperty(s, DOMWindow.WINDOW, "document", Value.makeObject(HTMLDocument.INSTANCES, new Dependency(), new DependencyGraphReference()),
				DOMSpec.LEVEL_0);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLDOCUMENT_OPEN: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case HTMLDOCUMENT_CLOSE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case HTMLDOCUMENT_WRITE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case HTMLDOCUMENT_WRITELN: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case HTMLDOCUMENT_GET_ELEMENTS_BY_NAME: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value name = Conversion.toString(call.getArg(0), c);
			if (name.isMaybeSingleStr()) {
				Set<ObjectLabel> labels = s.getElementsByName(name.getStr());
				Value v = Value.makeObject(labels, new Dependency(), new DependencyGraphReference());
				ObjectLabel nodeList = DOMFunctions.makeEmptyNodeList(s);
				if (labels.size() > 0) {
					createDOMUnknownArrayProperty(s, nodeList, v);
				}
				return Value.makeObject(nodeList, new Dependency(), new DependencyGraphReference());
			}
			return DOMFunctions.makeAnyHTMLNodeList(s);
		}
		case HTMLDOCUMENT_GET_ELEMENTS_BY_CLASS_NAME: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value className = */Conversion.toString(call.getArg(0), c);
			return DOMFunctions.makeAnyHTMLNodeList(s);
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
