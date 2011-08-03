package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.*;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

/**
 * The Node interface is the primary datatype for the entire Document Object
 * Model. It represents a single node in the document tree.
 */
public class DOMNode {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.NODE_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.NODE_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.NODE_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "Node", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype object.
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Constants.
		 */
		createDOMProperty(s, PROTOTYPE, "ELEMENT_NODE", Value.makeNum(1, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "ATTRIBUTE_NODE", Value.makeNum(2, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "TEXT_NODE", Value.makeNum(3, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "CDATA_SECTION_NODE", Value.makeNum(4, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "ENTITY_REFERENCE_NODE", Value.makeNum(5, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "ENTITY_NODE", Value.makeNum(6, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "PROCESSING_INSTRUCTION_NODE", Value.makeNum(7, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "COMMENT_NODE", Value.makeNum(8, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "DOCUMENT_NODE", Value.makeNum(9, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "DOCUMENT_TYPE_NODE", Value.makeNum(10, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "DOCUMENT_FRAGMENT_NODE", Value.makeNum(11, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "NOTATION_NODE", Value.makeNum(12, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);

		/*
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, PROTOTYPE, "nodeName", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "nodeValue", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "nodeType", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "parentNode", Value.makeObject(INSTANCES, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "childNodes", Value.makeObject(DOMNodeList.INSTANCES, new Dependency(), new DependencyGraphReference()),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "firstChild", Value.makeObject(INSTANCES, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "lastChild", Value.makeObject(INSTANCES, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "previousSibling", Value.makeObject(INSTANCES, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROTOTYPE, "nextSibling", Value.makeObject(INSTANCES, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		// NB: 'attributes' and 'ownerDocument' are set in CoreBuilder due to
		// circularity

		// DOM LEVEL 2
		createDOMProperty(s, PROTOTYPE, "prefix", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "localName", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, PROTOTYPE, "namespaceURI", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);

		/*
		 * Functions.
		 */
		// DOM LEVEL 1
		createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_APPEND_CHILD, "appendChild", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_CLONE_NODE, "cloneNode", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_HAS_CHILD_NODES, "hasChildNodes", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_INSERT_BEFORE, "insertBefore", 2, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_REMOVE_CHILD, "removeChild", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_REPLACE_CHILD, "replaceChild", 2, DOMSpec.LEVEL_1);

		// DOM LEVEL 2
		createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_IS_SUPPORTED, "isSupported", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_HAS_ATTRIBUTES, "hasAttributes", 0, DOMSpec.LEVEL_2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_NORMALIZE, "normalize", 0, DOMSpec.LEVEL_2);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case NODE_APPEND_CHILD: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			return call.getArg(0);
		}
		case NODE_CLONE_NODE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value deep = */Conversion.toBoolean(call.getArg(0));
			int baseVar = call.getBaseVar();
			Set<ObjectLabel> cloneLabels = s.readTemporary(baseVar).getObjectLabels();
			return Value.makeObject(cloneLabels, new Dependency(), new DependencyGraphReference());
		}
		case NODE_HAS_CHILD_NODES: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		}
		case NODE_INSERT_BEFORE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Value newChild = DOMConversion.toNode(call.getArg(0), c);
			/* Value refChild = */DOMConversion.toNode(call.getArg(1), c);
			return newChild;
		}
		case NODE_REMOVE_CHILD: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value oldChild = DOMConversion.toNode(call.getArg(0), c);
			return oldChild;
		}
		case NODE_REPLACE_CHILD: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			/* Value newChild = */DOMConversion.toNode(call.getArg(0), c);
			Value oldChild = DOMConversion.toNode(call.getArg(1), c);
			return oldChild;
		}
		case NODE_IS_SUPPORTED: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			/* Value feature = */Conversion.toString(call.getArg(0), c);
			/* Value version = */Conversion.toString(call.getArg(0), c);
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
