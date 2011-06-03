package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.*;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


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

	public static ObjectLabel NAMED_NODE_MAP = new ObjectLabel(DOMObjects.NAMEDNODEMAP, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel NAMED_NODE_MAP_PROTOTYPE = new ObjectLabel(DOMObjects.NAMEDNODEMAP_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(NAMED_NODE_MAP_PROTOTYPE);
		createDOMInternalPrototype(s, NAMED_NODE_MAP_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency()));

		// Multiplied object.
		s.newObject(NAMED_NODE_MAP);
		createDOMInternalPrototype(s, NAMED_NODE_MAP, Value.makeObject(NAMED_NODE_MAP_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "NamedNodeMap", Value.makeObject(NAMED_NODE_MAP, new Dependency()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, NAMED_NODE_MAP, "length", Value.makeNum(0, new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);

		s.multiplyObject(NAMED_NODE_MAP);
		NAMED_NODE_MAP = NAMED_NODE_MAP.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, NAMED_NODE_MAP_PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_GETNAMEDITEM, "getNamedItem", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, NAMED_NODE_MAP_PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_SETNAMEDITEM, "setNamedItem", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, NAMED_NODE_MAP_PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_REMOVENAMEDITEM, "removeNamedItem", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, NAMED_NODE_MAP_PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_ITEM, "item", 1, DOMSpec.LEVEL_1);

		createDOMFunction(s, NAMED_NODE_MAP_PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_GETNAMEDITEMNS, "getNamedItemNS", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, NAMED_NODE_MAP_PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_SETNAMEDITEMNS, "setNamedItemNS", 2, DOMSpec.LEVEL_2);
		createDOMFunction(s, NAMED_NODE_MAP_PROTOTYPE, DOMObjects.NAMEDNODEMAP_PROTOTYPE_REMOVEDNAMEDITEMNS, "removeNamedItemNS", 2, DOMSpec.LEVEL_2);
	}

	/**
	 * Transfer Functions.
	 */
	// TODO: Figure out how to modely this correctly
	public static Value evaluate(DOMObjects nativeobject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeobject) {
		case NAMEDNODEMAP_PROTOTYPE_GETNAMEDITEM: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value name = Conversion.toString(call.getArg(0), c);
			return Value.makeObject(DOMNode.NODE, new Dependency());
		}
		case NAMEDNODEMAP_PROTOTYPE_SETNAMEDITEM: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value node = DOMConversion.toNode(call.getArg(0), c);
			return Value.makeObject(DOMNode.NODE, new Dependency());
		}
		case NAMEDNODEMAP_PROTOTYPE_REMOVENAMEDITEM: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value name = Conversion.toString(call.getArg(0), c);
			return Value.makeObject(DOMNode.NODE, new Dependency());
		}
		case NAMEDNODEMAP_PROTOTYPE_ITEM: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value index = Conversion.toNumber(call.getArg(0), c);
			return Value.makeObject(DOMNode.NODE, new Dependency());
		}
		case NAMEDNODEMAP_PROTOTYPE_GETNAMEDITEMNS: {
			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
			Value namespaceURI = Conversion.toString(call.getArg(0), c);
			Value localName = Conversion.toString(call.getArg(1), c);
			return Value.makeObject(DOMNode.NODE, new Dependency());
		}
		case NAMEDNODEMAP_PROTOTYPE_SETNAMEDITEMNS: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value node = DOMConversion.toNode(call.getArg(0), c);
			return Value.makeObject(DOMNode.NODE, new Dependency());
		}
		case NAMEDNODEMAP_PROTOTYPE_REMOVEDNAMEDITEMNS: {
			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
			Value namespaceURI = Conversion.toString(call.getArg(0), c);
			Value localName = Conversion.toString(call.getArg(1), c);
			return Value.makeObject(DOMNode.NODE, new Dependency());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeobject);
		}
		}
	}

}
