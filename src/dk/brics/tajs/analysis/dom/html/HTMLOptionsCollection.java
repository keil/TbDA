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
 * An HTMLOptionsCollection is a list of nodes representing HTML option element.
 * An individual node may be accessed by either ordinal index or the node's name
 * or id attributes.
 */
public class HTMLOptionsCollection {

	public static ObjectLabel OPTIONSCOLLECTION = new ObjectLabel(DOMObjects.HTMLOPTIONSCOLLECTION, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel OPTIONSCOLLECTION_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLOPTIONSCOLLECTION_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(OPTIONSCOLLECTION_PROTOTYPE);
		createDOMInternalPrototype(s, OPTIONSCOLLECTION_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(OPTIONSCOLLECTION);
		createDOMInternalPrototype(s, OPTIONSCOLLECTION, Value.makeObject(OPTIONSCOLLECTION_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLOptionsCollection", Value.makeObject(OPTIONSCOLLECTION, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 2
		createDOMProperty(s, OPTIONSCOLLECTION, "length", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);

		s.multiplyObject(OPTIONSCOLLECTION);
		OPTIONSCOLLECTION = OPTIONSCOLLECTION.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 2
		createDOMFunction(s, OPTIONSCOLLECTION_PROTOTYPE, DOMObjects.HTMLOPTIONSCOLLECTION_ITEM, "item", 1, DOMSpec.LEVEL_2);
		createDOMFunction(s, OPTIONSCOLLECTION_PROTOTYPE, DOMObjects.HTMLOPTIONSCOLLECTION_NAMEDITEM, "namedItem", 1, DOMSpec.LEVEL_2);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLOPTIONSCOLLECTION_ITEM: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value index = Conversion.toNumber(call.getArg(0), c);
			return DOMFunctions.makeAnyHTMLElement().joinNull();
		}
		case HTMLOPTIONSCOLLECTION_NAMEDITEM: {
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
