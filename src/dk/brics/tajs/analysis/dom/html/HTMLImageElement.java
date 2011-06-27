package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

public class HTMLImageElement {

	public static ObjectLabel IMAGE = new ObjectLabel(DOMObjects.HTMLIMAGEELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel IMAGE_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLIMAGEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel IMAGE_CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLIMAGEELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);

	public static void build(State s) {
		// Constructor Object
		s.newObject(IMAGE_CONSTRUCTOR);

		// Prototype Object
		s.newObject(IMAGE_PROTOTYPE);
		createDOMInternalPrototype(s, IMAGE_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(IMAGE);
		createDOMInternalPrototype(s, IMAGE, Value.makeObject(IMAGE_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLImageElement", Value.makeObject(IMAGE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM LEVEL 0
		createDOMProperty(s, IMAGE, "complete", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		// DOM Level 1
		createDOMProperty(s, IMAGE, "lowSrc", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IMAGE, "name", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IMAGE, "align", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IMAGE, "alt", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IMAGE, "border", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IMAGE, "isMap", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IMAGE, "longDesc", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IMAGE, "src", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IMAGE, "useMap", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, IMAGE, "height", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, IMAGE, "hspace", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, IMAGE, "vspace", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, IMAGE, "width", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);

		s.multiplyObject(IMAGE);
		IMAGE = IMAGE.makeSingleton().makeSummary();

		// DOM LEVEL 0
		createDOMProperty(s, DOMWindow.WINDOW, "Image", Value.makeObject(IMAGE_CONSTRUCTOR, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		/*
		 * Functions.
		 */
		// No functions.
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLIMAGEELEMENT_CONSTRUCTOR: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeObject(IMAGE, new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
