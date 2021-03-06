package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

public class HTMLTextAreaElement {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLTEXTAREAELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.HTMLTEXTAREAELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.HTMLTEXTAREAELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLTextAreaElement", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

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
		createDOMProperty(s, INSTANCES, "form", Value.makeObject(HTMLFormElement.INSTANCES, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "accessKey", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "cols", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "disabled", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "name", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "readOnly", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "rows", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "tabIndex", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "type", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "value", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, INSTANCES, "defaultValue", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);

		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTEXTAREAELEMENT_BLUR, "blur", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTEXTAREAELEMENT_FOCUS, "focus", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTEXTAREAELEMENT_SELECT, "select", 0, DOMSpec.LEVEL_1);
	}

	/**
	 * Transfer functions
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLTEXTAREAELEMENT_BLUR: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case HTMLTEXTAREAELEMENT_FOCUS: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case HTMLTEXTAREAELEMENT_SELECT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}
}
