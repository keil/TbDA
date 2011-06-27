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

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

public class HTMLFormElement {

	public static ObjectLabel FORM = new ObjectLabel(DOMObjects.HTMLFORMELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel FORM_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLFORMELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(FORM_PROTOTYPE);
		createDOMInternalPrototype(s, FORM_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(FORM);
		createDOMInternalPrototype(s, FORM, Value.makeObject(FORM_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLFormElement", Value.makeObject(FORM, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, FORM, "elements", Value.makeObject(HTMLCollection.COLLECTION, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, FORM, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, FORM, "name", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FORM, "acceptCharset", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FORM, "action", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FORM, "enctype", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FORM, "method", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FORM, "target", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(FORM);
		FORM = FORM.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, FORM_PROTOTYPE, DOMObjects.HTMLFORMELEMENT_SUBMIT, "submit", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, FORM_PROTOTYPE, DOMObjects.HTMLFORMELEMENT_RESET, "reset", 0, DOMSpec.LEVEL_1);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
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
