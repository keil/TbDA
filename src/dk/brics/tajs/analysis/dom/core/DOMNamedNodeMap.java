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

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

/**
 * Objects implementing the NamedNodeMap interface are used to represent
 * collections of nodes that can be accessed by name. Note that NamedNodeMap
 * does not inherit from NodeList; NamedNodeMaps are not maintained in any
 * particular order. Objects contained in an object implementing NamedNodeMap
 * may also be accessed by an ordinal index, but this is simply to allow
 * convenient enumeration of the contents of a NamedNodeMap, and does not imply
 * that the DOM specifies an order to these Nodes.
 */
public class DOMNamedNodeMap {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.NAMEDNODEMAP_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.NAMEDNODEMAP_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.NAMEDNODEMAP_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "NamedNodeMap", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype object.
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, INSTANCES, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);

		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_GETNAMEDITEM, "getNamedItem", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_SETNAMEDITEM, "setNamedItem", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_REMOVENAMEDITEM, "removeNamedItem", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_ITEM, "item", 1, DOMSpec.LEVEL_1);

		createDOMFunction(s, PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_GETNAMEDITEMNS, "getNamedItemNS", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_SETNAMEDITEMNS, "setNamedItemNS", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_REMOVEDNAMEDITEMNS, "removeNamedItemNS", 2, DOMSpec.LEVEL_2);
	}

	/**
	 * Transfer Functions.
	 */
	// TODO: Figure out how to modely this correctly
	public static Value evaluate(DOMObjects nativeobject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeobject) {
		case NAMEDNODEMAP_PROTOTYPE_GETNAMEDITEM: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			/* Value name = */Conversion.toString(call.getArg(0), c);
			return Value.makeObject(DOMNode.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		case NAMEDNODEMAP_PROTOTYPE_SETNAMEDITEM: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			/* Value node = */DOMConversion.toNode(call.getArg(0), c);
			return Value.makeObject(DOMNode.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		case NAMEDNODEMAP_PROTOTYPE_REMOVENAMEDITEM: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			/* Value name = */Conversion.toString(call.getArg(0), c);
			return Value.makeObject(DOMNode.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		case NAMEDNODEMAP_PROTOTYPE_ITEM: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			/* Value index = */Conversion.toNumber(call.getArg(0), c);
			return Value.makeObject(DOMNode.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		case NAMEDNODEMAP_PROTOTYPE_GETNAMEDITEMNS: {
			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
			/* Value namespaceURI = */Conversion.toString(call.getArg(0), c);
			/* Value localName = */Conversion.toString(call.getArg(1), c);
			return Value.makeObject(DOMNode.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		case NAMEDNODEMAP_PROTOTYPE_SETNAMEDITEMNS: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			/* Value node = */DOMConversion.toNode(call.getArg(0), c);
			return Value.makeObject(DOMNode.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		case NAMEDNODEMAP_PROTOTYPE_REMOVEDNAMEDITEMNS: {
			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
			/* Value namespaceURI = */Conversion.toString(call.getArg(0), c);
			/* Value localName = */Conversion.toString(call.getArg(1), c);
			return Value.makeObject(DOMNode.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeobject);
		}
		}
	}

}
