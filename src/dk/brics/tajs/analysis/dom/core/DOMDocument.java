package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.html.HTMLBuilder;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * The Document interface represents the entire HTML or XML document.
 * Conceptually, it is the root of the document tree, and provides the primary
 * access to the document's data.
 */
public class DOMDocument {

	public static ObjectLabel DOCUMENT = new ObjectLabel(DOMObjects.DOCUMENT, Kind.OBJECT);
	public static ObjectLabel DOCUMENT_PROTOTYPE = new ObjectLabel(DOMObjects.DOCUMENT_PROTOTYPE, Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(DOCUMENT_PROTOTYPE);
		createDOMInternalPrototype(s, DOCUMENT_PROTOTYPE, Value.makeObject(DOMNode.NODE_PROTOTYPE, new Dependency()));

		// Multiplied object.
		s.newObject(DOCUMENT);
		createDOMInternalPrototype(s, DOCUMENT, Value.makeObject(DOCUMENT_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "Document", Value.makeObject(DOCUMENT, new Dependency()));

		/**
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, DOCUMENT, "doctype", Value.makeObject(DOMDocumentType.TYPE, new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOCUMENT, "implementation", Value.makeObject(DOMImplementation.IMPLEMENTATION, new Dependency()), DOMSpec.LEVEL_1);
		// NB: The documentElement property is written by HTMLBuilder (due to cyclic dependency).

		// DOM LEVEL 2
		// None.

		// DOM LEVEL 3
		createDOMProperty(s, DOCUMENT, "inputEncoding", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, DOCUMENT, "xmlEncoding", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, DOCUMENT, "xmlStandalone", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_3);
		createDOMProperty(s, DOCUMENT, "xmlVersion", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_3);
		createDOMProperty(s, DOCUMENT, "strictErrorChecking", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_3);
		createDOMProperty(s, DOCUMENT, "documentURI", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_3);
		createDOMProperty(s, DOCUMENT, "domConfig", Value.makeObject(DOMConfiguration.CONFIGURATION, new Dependency()), DOMSpec.LEVEL_3);

		s.multiplyObject(DOCUMENT);
		DOCUMENT = DOCUMENT.makeSingleton().makeSummary();

		/**
		 * Functions.
		 */
		// DOM LEVEL 1
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ELEMENT, "createElement", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_CREATE_DOCUMENTFRAGMENT, "createDocumentFragment", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_CREATE_TEXTNODE, "createTextNode", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_CREATE_COMMENT, "createComment", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_CREATE_CDATASECTION, "createCDATASection", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_CREATEPROCESSINGINSTRUCTION, "createProcessingInstruction", 2, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ATTRIBUTE, "createAttribute", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ENTITYREFERENCE, "createEntityReference", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_GET_ELEMENTS_BY_TAGNAME, "getElementsByTagName", 1, DOMSpec.LEVEL_1);

		// DOM LEVEL 2
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_IMPORT_NODE, "importNode", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ELEMENT_NS, "createElementNS", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ATTRIBUTE_NS, "createAttributeNS", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_GET_ELEMENTS_BY_TAGNAME_NS, "getElementsByTagNameNS", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_GET_ELEMENT_BY_ID, "getElementById", 1, DOMSpec.LEVEL_2);

		// DOM LEVEL 3
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_ADOPT_NODE, "adoptNode", 1, DOMSpec.LEVEL_3);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_NORMALIZEDOCUMENT, "normalizeDocument", 0, DOMSpec.LEVEL_3);
		createDOMFunction(s, DOCUMENT_PROTOTYPE, DOMObjects.DOCUMENT_RENAME_NODE, "renameNode", 3, DOMSpec.LEVEL_3);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeobject, CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeobject) {
		case DOCUMENT_ADOPT_NODE: {
			throw new UnsupportedOperationException("DOCUMENT_ADOPT_NODE not supported.");
		}
		case DOCUMENT_CREATE_ATTRIBUTE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value name = Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			return Value.makeObject(DOMAttr.ATTR, new Dependency());
		}
		case DOCUMENT_CREATE_ATTRIBUTE_NS: {
			throw new UnsupportedOperationException("DOCUMENT_CREATE_ATTRIBUTE_NS not supported.");
		}
		case DOCUMENT_CREATE_CDATASECTION: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			ObjectLabel label = new ObjectLabel(DOMObjects.CDATASECTION, Kind.OBJECT);
			s.newObject(label);
			return Value.makeObject(label, new Dependency());
		}
		case DOCUMENT_CREATE_COMMENT: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			ObjectLabel label = new ObjectLabel(DOMObjects.COMMENT, Kind.OBJECT);
			s.newObject(label);
			return Value.makeObject(label, new Dependency());
		}
		case DOCUMENT_CREATE_ELEMENT: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value tagname = Conversion.toString(call.getArg(0), c);
			if (tagname.isMaybeSingleStr()) {
				String t = tagname.getStr();
				return Value.makeObject(DOMFunctions.getHTMLObjectLabel(t), new Dependency());
			} else if (tagname.isMaybeFuzzyStr()) {
				return Value.makeObject(HTMLBuilder.HTML_OBJECT_LABELS, new Dependency());
			} else {
				return DOMFunctions.makeAnyHTMLElement();
			}
		}
		case DOCUMENT_CREATE_ELEMENT_NS: {
			throw new UnsupportedOperationException("DOCUMENT_CREATE_ELEMENT_NS not supported.");
		}
		case DOCUMENT_CREATE_ENTITYREFERENCE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value name = Conversion.toString(call.getArg(0), c);
			return Value.makeObject(DOMEntityReference.ENTITYREFERENCE, new Dependency());
		}
		case DOCUMENT_CREATEPROCESSINGINSTRUCTION: {
			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
			Value target = Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			Value data = Conversion.toString(NativeFunctions.readParameter(call, 1), c);
			return Value.makeObject(DOMProcessingInstruction.PROCESSINGINSTRUCTION, new Dependency());
		}
		case DOCUMENT_CREATE_TEXTNODE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value data = Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			return Value.makeObject(DOMText.TEXT, new Dependency());
		}
		case DOCUMENT_CREATE_DOCUMENTFRAGMENT: {
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			return Value.makeObject(DOMDocumentFragment.DOCUMENTFRAGMENT, new Dependency());
		}
		case DOCUMENT_GET_ELEMENT_BY_ID: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value id = Conversion.toString(call.getArg(0), c);
			if (id.isMaybeSingleStr()) {
				Set<ObjectLabel> labels = s.getElementById(id.getStr());
				if (labels.size() > 0) {
					return Value.makeObject(labels, new Dependency());
				}
				return Value.makeNull(new Dependency());
			} else if (id.isMaybeFuzzyStr()) {
				return Value.makeObject(s.getAllElementById(), new Dependency());
			} else {
				return DOMFunctions.makeAnyHTMLElement();
			}
		}
		case DOCUMENT_GET_ELEMENTS_BY_TAGNAME: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value tagname = Conversion.toString(call.getArg(0), c);
			if (tagname.isMaybeSingleStr()) {
				Set<ObjectLabel> labels = s.getElementsByTagName(tagname.getStr());
				Value v = Value.makeObject(labels, new Dependency());
				ObjectLabel nodeList = DOMFunctions.makeEmptyNodeList(s);
				if (labels.size() > 0) {
					s.writeUnknownArrayProperty(nodeList, v);
				}
				return Value.makeObject(nodeList, new Dependency());
			} else if (tagname.isMaybeFuzzyStr()) {
				Set<ObjectLabel> labels = s.getAllElementsByTagName();
				Value v = Value.makeObject(labels, new Dependency());
				ObjectLabel nodeList = DOMFunctions.makeEmptyNodeList(s);
				if (labels.size() > 0) {
					s.writeUnknownArrayProperty(nodeList, v);
				}
				return Value.makeObject(nodeList, new Dependency());
			} else {
				return DOMFunctions.makeAnyHTMLElement();
			}
		}
		case DOCUMENT_GET_ELEMENTS_BY_TAGNAME_NS: {
			throw new UnsupportedOperationException("DOCUMENT_GET_ELEMENTS_BY_TAGNAME_NS not supported.");
		}
		case DOCUMENT_IMPORT_NODE: {
			throw new UnsupportedOperationException("DOCUMENT_IMPORT_NODE not supported.");
		}
		case DOCUMENT_NORMALIZEDOCUMENT: {
			throw new UnsupportedOperationException("DOCUMENT_NORMALIZEDOCUMENT not supported.");
		}
		case DOCUMENT_RENAME_NODE: {
			throw new UnsupportedOperationException("DOCUMENT_RENAME_NODE not supported.");
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeobject);
		}
		}
	}
}
