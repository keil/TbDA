package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

/**
 * An HTMLDocument is the root of the HTML hierarchy and holds the entire
 * content. Besides providing access to the hierarchy, it also provides some
 * convenience methods for accessing certain sets of information from the
 * document.
 */
public class HTMLDocument {

	public static ObjectLabel DOCUMENT = new ObjectLabel(DOMObjects.HTMLDOCUMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel DOCUMENT_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLDOCUMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(DOCUMENT_PROTOTYPE);
		createDOMInternalPrototype(s, DOCUMENT_PROTOTYPE, Value.makeObject(DOMDocument.DOCUMENT, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(DOCUMENT);
		createDOMInternalPrototype(s, DOCUMENT, Value.makeObject(DOCUMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLDocument", Value.makeObject(DOCUMENT, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, DOCUMENT, "title", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOCUMENT, "referrer", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOCUMENT, "domain", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOCUMENT, "URL", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOCUMENT, "body", Value.makeObject(HTMLBodyElement.BODY, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOCUMENT, "images", Value.makeObject(HTMLCollection.COLLECTION, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOCUMENT, "applets", Value.makeObject(HTMLCollection.COLLECTION, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOCUMENT, "links", Value.makeObject(HTMLCollection.COLLECTION, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOCUMENT, "forms", Value.makeObject(HTMLCollection.COLLECTION, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOCUMENT, "anchors", Value.makeObject(HTMLCollection.COLLECTION, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOCUMENT, "cookie", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		// DOM LEVEL 0 / UNKNOWN
		createDOMProperty(s, DOCUMENT, "width", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);
		createDOMProperty(s, DOCUMENT, "height", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		s.multiplyObject(DOCUMENT);
		DOCUMENT = DOCUMENT.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.HTMLDOCUMENT_OPEN, "open", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.HTMLDOCUMENT_CLOSE, "close", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.HTMLDOCUMENT_WRITE, "write", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.HTMLDOCUMENT_WRITELN, "writeln", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.HTMLDOCUMENT_GET_ELEMENTS_BY_NAME, "getElementsByName", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.HTMLDOCUMENT_GET_ELEMENTS_BY_CLASS_NAME, "getElementsByClassName", 1, DOMSpec.LEVEL_0);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
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
					s.writeUnknownArrayProperty(nodeList, v);
				}
				return Value.makeObject(nodeList, new Dependency(), new DependencyGraphReference());
			} else if (name.isMaybeFuzzyStr()) {
				Set<ObjectLabel> labels = s.getAllElementsByName();
				Value v = Value.makeObject(labels, new Dependency(), new DependencyGraphReference());
				ObjectLabel nodeList = DOMFunctions.makeEmptyNodeList(s);
				if (labels.size() > 0) {
					s.writeUnknownArrayProperty(nodeList, v);
				}
				return Value.makeObject(nodeList, new Dependency(), new DependencyGraphReference());
			} else {
				Value v = Value.makeObject(DOMFunctions.makeAnyHTMLElement().getObjectLabels(), new Dependency(), new DependencyGraphReference());
				ObjectLabel nodeList = DOMFunctions.makeEmptyNodeList(s);
				s.writeUnknownArrayProperty(nodeList, v);
				return Value.makeObject(nodeList, new Dependency(), new DependencyGraphReference());
			}
		}
		case HTMLDOCUMENT_GET_ELEMENTS_BY_CLASS_NAME: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value className = Conversion.toString(call.getArg(0), c);
			return DOMFunctions.makeAnyHTMLNodeList(s);
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
