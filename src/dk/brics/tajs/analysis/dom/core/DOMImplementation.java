package dk.brics.tajs.analysis.dom.core;

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
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

/**
 * The DOMImplementation interface provides a number of methods for performing
 * operations that are independent of any particular instance of the document
 * object model.
 */
public class DOMImplementation {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.DOMIMPLEMENTATION_CONSTRUCTOR, Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.DOMIMPLEMENTATION_PROTOTYPE, Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.DOMIMPLEMENTATION_INSTANCES, Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "DOMImplementation", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype object.
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		// DOMFunctions.createDOMProperty(DOMDocument.INSTANCES,
		// "implementation", Value.makeObject(INSTANCES)); // TODO: should be
		// here??
		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		/*
		 * Properties.
		 */
		// None

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOMIMPLEMENTATION_HASFEATURE, "hasFeature", 2, DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOMIMPLEMENTATION_CREATEDOCUMENTTYPE, "createDocumentType", 3, DOMSpec.LEVEL_2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.DOMIMPLEMENTATION_CREATEDOCUMENT, "createDocument", 3, DOMSpec.LEVEL_2);

		createDOMProperty(s, DOMDocument.INSTANCES, "implementation",
				Value.makeObject(DOMImplementation.INSTANCES, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeobject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeobject) {
		case DOMIMPLEMENTATION_HASFEATURE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
			/* Value feature = */Conversion.toString(call.getArg(0), c);
			/* Value version = */Conversion.toString(call.getArg(1), c);
			return Value.makeAnyBool(new Dependency(), new DependencyGraphReference());
		}
		case DOMIMPLEMENTATION_CREATEDOCUMENTTYPE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 3, 3);
			/* Value qualifiedName = */Conversion.toString(call.getArg(0), c);
			/* Value publicId = */Conversion.toString(call.getArg(1), c);
			/* Value systemId = */Conversion.toString(call.getArg(2), c);
			return Value.makeObject(DOMDocumentType.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		case DOMIMPLEMENTATION_CREATEDOCUMENT: {
			NativeFunctions.expectParameters(nativeobject, call, c, 3, 3);
			/* Value namespaceURI = */Conversion.toString(call.getArg(0), c);
			/* Value qualifiedName = */Conversion.toString(call.getArg(1), c);
			/* Value docType = */Conversion.toString(call.getArg(2), c);
			return Value.makeObject(DOMDocument.INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		default:
			throw new RuntimeException("Unknown Native Object: " + nativeobject);
		}
	}

}
