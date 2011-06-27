package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.*;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * The Node interface is the primary datatype for the entire Document Object
 * Model. It represents a single node in the document tree.
 */
public class DOMNode {

	public static ObjectLabel NODE = new ObjectLabel(DOMObjects.NODE, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel NODE_PROTOTYPE = new ObjectLabel(DOMObjects.NODE_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(NODE_PROTOTYPE);
		createDOMInternalPrototype(s, NODE_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(NODE);
		createDOMInternalPrototype(s, NODE, Value.makeObject(NODE_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "Node", Value.makeObject(NODE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Constants.
		 */
		createDOMProperty(s, NODE, "ELEMENT_NODE", Value.makeNum(1, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "ATTRIBUTE_NODE", Value.makeNum(2, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "TEXT_NODE", Value.makeNum(3, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "CDATA_SECTION_NODE", Value.makeNum(4, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "ENTITY_REFERENCE_NODE", Value.makeNum(5, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "ENTITY_NODE", Value.makeNum(6, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "PROCESSING_INSTRUCTION_NODE", Value.makeNum(7, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "COMMENT_NODE", Value.makeNum(8, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "DOCUMENT_NODE", Value.makeNum(9, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "DOCUMENT_TYPE_NODE", Value.makeNum(10, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "DOCUMENT_FRAGMENT_NODE", Value.makeNum(11, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "NOTATION_NODE", Value.makeNum(12, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);

		/*
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, NODE, "nodeName", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "nodeValue", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "nodeType", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "parentNode", Value.makeObject(NODE, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "childNodes", Value.makeObject(DOMNodeList.NODELIST, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "firstChild", Value.makeObject(NODE, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "lastChild", Value.makeObject(NODE, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "previousSibling", Value.makeObject(NODE, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "nextSibling", Value.makeObject(NODE, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "attributes", Value.makeObject(DOMNamedNodeMap.NAMED_NODE_MAP, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, NODE, "ownerDocument", Value.makeObject(DOMDocument.DOCUMENT, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		// DOM LEVEL 2
		createDOMProperty(s, NODE, "prefix", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, NODE, "localName", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, NODE, "namespaceURI", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);

		s.multiplyObject(NODE);
		NODE = NODE.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM LEVEL 1
		createDOMFunction(s, NODE_PROTOTYPE, DOMObjects.NODE_APPEND_CHILD, "appendChild", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, NODE_PROTOTYPE, DOMObjects.NODE_CLONE_NODE, "cloneNode", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, NODE_PROTOTYPE, DOMObjects.NODE_HAS_CHILD_NODES, "hasChildNodes", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, NODE_PROTOTYPE, DOMObjects.NODE_INSERT_BEFORE, "insertBefore", 2, DOMSpec.LEVEL_1);
		createDOMFunction(s, NODE_PROTOTYPE, DOMObjects.NODE_REMOVE_CHILD, "removeChild", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, NODE_PROTOTYPE, DOMObjects.NODE_REPLACE_CHILD, "replaceChild", 2, DOMSpec.LEVEL_1);

		// DOM LEVEL 2
		createDOMFunction(s, NODE_PROTOTYPE, DOMObjects.NODE_IS_SUPPORTED, "isSupported", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, NODE_PROTOTYPE, DOMObjects.NODE_HAS_ATTRIBUTES, "hasAttributes", 0, DOMSpec.LEVEL_2);
		createDOMFunction(s, NODE_PROTOTYPE, DOMObjects.NODE_NORMALIZE, "normalize", 0, DOMSpec.LEVEL_2);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case NODE_APPEND_CHILD: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value newChild = DOMConversion.toNode(call.getArg(0), c);
			return newChild;
		}
		case NODE_CLONE_NODE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value deep = Conversion.toBoolean(call.getArg(0));
			return Value.makeObject(NODE, new Dependency(), new DependencyGraphReference());
		}
		case NODE_HAS_CHILD_NODES: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		}
		case NODE_INSERT_BEFORE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Value newChild = DOMConversion.toNode(call.getArg(0), c);
			Value refChild = DOMConversion.toNode(call.getArg(1), c);
			return newChild;
		}
		case NODE_REMOVE_CHILD: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value oldChild = DOMConversion.toNode(call.getArg(0), c);
			return oldChild;
		}
		case NODE_REPLACE_CHILD: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Value newChild = DOMConversion.toNode(call.getArg(0), c);
			Value oldChild = DOMConversion.toNode(call.getArg(1), c);
			return oldChild;
		}
		case NODE_IS_SUPPORTED: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Value feature = Conversion.toString(call.getArg(0), c);
			Value version = Conversion.toString(call.getArg(0), c);
			return Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		}
		case NODE_HAS_ATTRIBUTES: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		}
		case NODE_NORMALIZE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
