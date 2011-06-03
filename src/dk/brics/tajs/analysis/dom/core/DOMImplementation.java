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
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * The DOMImplementation interface provides a number of methods for performing
 * operations that are independent of any particular instance of the document
 * object model.
 */
public class DOMImplementation {

	public static ObjectLabel IMPLEMENTATION = new ObjectLabel(DOMObjects.DOMIMPLEMENTATION, Kind.OBJECT);
	public static ObjectLabel IMPLEMENTATION_PROTOTYPE = new ObjectLabel(DOMObjects.DOMIMPLEMENTATION_PROTOTYPE, Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(IMPLEMENTATION_PROTOTYPE);
		createDOMInternalPrototype(s, IMPLEMENTATION_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "DOMImplementation", Value.makeObject(IMPLEMENTATION_PROTOTYPE, new Dependency()));

		// Multiplied object.
		s.newObject(IMPLEMENTATION);
		createDOMInternalPrototype(s, IMPLEMENTATION, Value.makeObject(IMPLEMENTATION_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMDocument.DOCUMENT, "implementation", Value.makeObject(IMPLEMENTATION, new Dependency()));
		s.multiplyObject(IMPLEMENTATION);
		IMPLEMENTATION = IMPLEMENTATION.makeSingleton().makeSummary();

		/*
		 * Properties.
		 */
		// None

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, IMPLEMENTATION_PROTOTYPE, DOMObjects.DOMIMPLEMENTATION_HASFEATURE, "hasFeature", 2, DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMFunction(s, IMPLEMENTATION_PROTOTYPE, DOMObjects.DOMIMPLEMENTATION_CREATEDOCUMENTTYPE, "createDocumentType", 3, DOMSpec.LEVEL_2);
		createDOMFunction(s, IMPLEMENTATION_PROTOTYPE, DOMObjects.DOMIMPLEMENTATION_CREATEDOCUMENT, "createDocument", 3, DOMSpec.LEVEL_2);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeobject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeobject) {
		case DOMIMPLEMENTATION_HASFEATURE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
			Value feature = Conversion.toString(call.getArg(0), c);
			Value version = Conversion.toString(call.getArg(1), c);
			return Value.makeAnyBool(new Dependency());
		}
		case DOMIMPLEMENTATION_CREATEDOCUMENTTYPE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 3, 3);
			Value qualifiedName = Conversion.toString(call.getArg(0), c);
			Value publicId = Conversion.toString(call.getArg(1), c);
			Value systemId = Conversion.toString(call.getArg(2), c);
			return Value.makeObject(DOMDocumentType.TYPE, new Dependency());
		}
		case DOMIMPLEMENTATION_CREATEDOCUMENT: {
			NativeFunctions.expectParameters(nativeobject, call, c, 3, 3);
			Value namespaceURI = Conversion.toString(call.getArg(0), c);
			Value qualifiedName = Conversion.toString(call.getArg(1), c);
			Value docType = Conversion.toString(call.getArg(2), c);
			return Value.makeObject(DOMDocument.DOCUMENT, new Dependency());
		}
		default:
			throw new RuntimeException("Unknown Native Object: " + nativeobject);
		}
	}

}
