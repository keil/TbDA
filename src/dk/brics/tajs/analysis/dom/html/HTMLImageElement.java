package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

public class HTMLImageElement {

	public static ObjectLabel INSTANCES;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel CONSTRUCTOR;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLIMAGEELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		INSTANCES = new ObjectLabel(DOMObjects.HTMLIMAGEELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);
		PROTOTYPE = new ObjectLabel(DOMObjects.HTMLIMAGEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLImageElement", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype Object
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM LEVEL 0
		createDOMProperty(s, INSTANCES, "complete", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		// DOM Level 1
		createDOMProperty(s, INSTANCES, "lowSrc", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "name", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "align", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "alt", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "border", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "isMap", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "longDesc", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "src", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "useMap", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, INSTANCES, "height", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "hspace", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "vspace", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "width", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);

		// HTML5
		createDOMProperty(s, HTMLImageElement.INSTANCES, "naturalWidth", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.HTML5);
		createDOMProperty(s, HTMLImageElement.INSTANCES, "naturalHeight", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.HTML5);

		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		// DOM LEVEL 0
		createDOMProperty(s, DOMWindow.WINDOW, "Image", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		/*
		 * Functions.
		 */
		// No functions.
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLIMAGEELEMENT_CONSTRUCTOR: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeObject(INSTANCES, new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
