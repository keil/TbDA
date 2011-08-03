package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class HTMLFormElement {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLFORMELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.HTMLFORMELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.HTMLFORMELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLFormElement", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype Object
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, INSTANCES, "elements", Value.makeObject(HTMLCollection.INSTANCES, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "name", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "acceptCharset", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "action", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "enctype", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "method", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "target", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLFORMELEMENT_SUBMIT, "submit", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLFORMELEMENT_RESET, "reset", 0, DOMSpec.LEVEL_1);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLFORMELEMENT_SUBMIT:
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		case HTMLFORMELEMENT_RESET:
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		default:
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
	}

}
