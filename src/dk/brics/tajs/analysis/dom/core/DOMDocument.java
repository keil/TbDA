package dk.brics.tajs.analysis.dom.core;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMUnknownArrayProperty;

import java.util.Set;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.html.HTMLBuilder;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;

/**
 * The Document interface represents the entire HTML or XML document.
 * Conceptually, it is the root of the document tree, and provides the primary
 * access to the document's data.
 */
public class DOMDocument {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.DOCUMENT_CONSTRUCTOR, Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.DOCUMENT_PROTOTYPE, Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.DOCUMENT_INSTANCES, Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "Document", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype object.
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(DOMNode.PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/**
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, INSTANCES, "doctype", Value.makeObject(DOMDocumentType.INSTANCES, new Dependency(), new DependencyGraphReference()),
				DOMSpec.LEVEL_1);
		// NB: The documentElement property is written by HTMLBuilder (due to
		// cyclic dependency).

		// DOM LEVEL 2
		// None.

		// DOM LEVEL 3
		createDOMProperty(s, INSTANCES, "inputEncoding", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "xmlEncoding", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "xmlStandalone", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "xmlVersion", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "strictErrorChecking", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "documentURI", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "domConfig", Value.makeObject(DOMConfiguration.INSTANCES, new Dependency(), new DependencyGraphReference()),
				DOMSpec.LEVEL_3);

		// Summarize:
		// NB: The objectlabel is summarized in HTMLBuilder, because a property
		// is added to it there.

		/**
		 * Properties from DOMWindow
		 */
		createDOMProperty(s, INSTANCES, "location", Value.makeObject(DOMWindow.LOCATION, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		/**
		 * Functions.
		 */
		// DOM LEVEL 1
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ELEMENT, "createElement", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_DOCUMENTFRAGMENT, "createDocumentFragment", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_TEXTNODE, "createTextNode", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_COMMENT, "createComment", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_CDATASECTION, "createCDATASection", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATEPROCESSINGINSTRUCTION, "createProcessingInstruction", 2, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ATTRIBUTE, "createAttribute", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ENTITYREFERENCE, "createEntityReference", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_GET_ELEMENTS_BY_TAGNAME, "getElementsByTagName", 1, DOMSpec.LEVEL_1);

		// DOM LEVEL 2
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_IMPORT_NODE, "importNode", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ELEMENT_NS, "createElementNS", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ATTRIBUTE_NS, "createAttributeNS", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_GET_ELEMENTS_BY_TAGNAME_NS, "getElementsByTagNameNS", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_GET_ELEMENT_BY_ID, "getElementById", 1, DOMSpec.LEVEL_2);

		// DOM LEVEL 3
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_ADOPT_NODE, "adoptNode", 1, DOMSpec.LEVEL_3);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_NORMALIZEDOCUMENT, "normalizeDocument", 0, DOMSpec.LEVEL_3);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_RENAME_NODE, "renameNode", 3, DOMSpec.LEVEL_3);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeobject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeobject) {
		case DOCUMENT_ADOPT_NODE: {
			throw new UnsupportedOperationException("DOCUMENT_ADOPT_NODE not supported.");
		}
		case DOCUMENT_CREATE_ATTRIBUTE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			/* Value name = */Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			return Value.makeObject(DOMAttr.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		case DOCUMENT_CREATE_ATTRIBUTE_NS: {
			throw new UnsupportedOperationException("DOCUMENT_CREATE_ATTRIBUTE_NS not supported.");
		}
		case DOCUMENT_CREATE_CDATASECTION: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			ObjectLabel label = new ObjectLabel(DOMObjects.CDATASECTION_INSTANCES, Kind.OBJECT);
			s.newObject(label);
			return Value.makeObject(label, new Dependency(), new DependencyGraphReference());
		}
		case DOCUMENT_CREATE_COMMENT: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			ObjectLabel label = new ObjectLabel(DOMObjects.COMMENT_INSTANCES, Kind.OBJECT);
			s.newObject(label);
			return Value.makeObject(label, new Dependency(), new DependencyGraphReference());
		}
		case DOCUMENT_CREATE_ELEMENT: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value tagname = Conversion.toString(call.getArg(0), c);
			if (tagname.isMaybeSingleStr()) {
				String t = tagname.getStr();
				return Value.makeObject(DOMFunctions.getHTMLObjectLabel(t), new Dependency(), new DependencyGraphReference());
			} else if (tagname.isMaybeFuzzyStr()) {
				return Value.makeObject(HTMLBuilder.HTML_OBJECT_LABELS, new Dependency(), new DependencyGraphReference());
			} else {
				return DOMFunctions.makeAnyHTMLElement();
			}
		}
		case DOCUMENT_CREATE_ELEMENT_NS: {
			throw new UnsupportedOperationException("DOCUMENT_CREATE_ELEMENT_NS not supported.");
		}
		case DOCUMENT_CREATE_ENTITYREFERENCE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			/* Value name = */Conversion.toString(call.getArg(0), c);
			return Value.makeObject(DOMEntityReference.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		case DOCUMENT_CREATEPROCESSINGINSTRUCTION: {
			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
			/* Value target = */Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			/* Value data = */Conversion.toString(NativeFunctions.readParameter(call, 1), c);
			return Value.makeObject(DOMProcessingInstruction.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		case DOCUMENT_CREATE_TEXTNODE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			/* Value data = */Conversion.toString(NativeFunctions.readParameter(call, 0), c);
			return Value.makeObject(DOMText.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		case DOCUMENT_CREATE_DOCUMENTFRAGMENT: {
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			return Value.makeObject(DOMDocumentFragment.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		case DOCUMENT_GET_ELEMENT_BY_ID: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value id = Conversion.toString(call.getArg(0), c);
			if (id.isMaybeSingleStr()) {
				Set<ObjectLabel> labels = s.getElementById(id.getStr());
				if (labels.size() > 0) {
					return Value.makeObject(labels, new Dependency(), new DependencyGraphReference());
				}
				return DOMFunctions.makeAnyHTMLElement().joinNull();
			}
			return DOMFunctions.makeAnyHTMLElement().joinNull();
		}
		case DOCUMENT_GET_ELEMENTS_BY_TAGNAME: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value tagname = Conversion.toString(call.getArg(0), c);
			if (tagname.isMaybeSingleStr()) {
				Set<ObjectLabel> labels = s.getElementsByTagName(tagname.getStr());
				Value v = Value.makeObject(labels, new Dependency(), new DependencyGraphReference());
				ObjectLabel nodeList = DOMFunctions.makeEmptyNodeList(s);
				if (labels.size() > 0) {
					createDOMUnknownArrayProperty(s, nodeList, v);
				}
				return Value.makeObject(nodeList, new Dependency(), new DependencyGraphReference());
			}
			return DOMFunctions.makeAnyHTMLNodeList(s);
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
