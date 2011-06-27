package dk.brics.tajs.analysis.dom.html;

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
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

/**
 * An HTMLCollection is a list of nodes. An individual node may be accessed by
 * either ordinal index or the node's name or id attributes. Note: Collections
 * in the HTML DOM are assumed to be live meaning that they are automatically
 * updated when the underlying document is changed.
 */
public class HTMLCollection {

	public static ObjectLabel COLLECTION = new ObjectLabel(DOMObjects.HTMLCOLLECTION, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel COLLECTION_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLCOLLECTION_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(COLLECTION_PROTOTYPE);
		createDOMInternalPrototype(s, COLLECTION_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(COLLECTION);
		createDOMInternalPrototype(s, COLLECTION, Value.makeObject(COLLECTION_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLCollection", Value.makeObject(COLLECTION, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, COLLECTION, "length", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);

		s.multiplyObject(COLLECTION);
		COLLECTION = COLLECTION.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM LEVEL 1
		createDOMFunction(s, COLLECTION_PROTOTYPE, DOMObjects.HTMLCOLLECTION_ITEM, "item", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, COLLECTION_PROTOTYPE, DOMObjects.HTMLCOLLECTION_NAMEDITEM, "namedItem", 1, DOMSpec.LEVEL_1);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLCOLLECTION_ITEM: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value index = Conversion.toNumber(call.getArg(0), c);
			return DOMFunctions.makeAnyHTMLElement().joinNull();
		}
		case HTMLCOLLECTION_NAMEDITEM: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value name = Conversion.toString(call.getArg(0), c);
			return DOMFunctions.makeAnyHTMLElement().joinNull();
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
