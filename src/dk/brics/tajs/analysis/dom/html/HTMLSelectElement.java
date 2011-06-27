package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.*;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

public class HTMLSelectElement {

	public static ObjectLabel SELECT = new ObjectLabel(DOMObjects.HTMLSELECTELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel SELECT_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLSELECTELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(SELECT_PROTOTYPE);
		createDOMInternalPrototype(s, SELECT_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(SELECT);
		createDOMInternalPrototype(s, SELECT, Value.makeObject(SELECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLSelectElement", Value.makeObject(SELECT, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, SELECT, "type", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, SELECT, "selectedIndex", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, SELECT, "value", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, SELECT, "form", Value.makeObject(HTMLFormElement.FORM, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, SELECT, "disabled", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, SELECT, "multiple", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, SELECT, "name", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, SELECT, "size", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, SELECT, "tabIndex", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, SELECT, "length", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, SELECT, "options", Value.makeObject(HTMLOptionsCollection.OPTIONSCOLLECTION, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);

		s.multiplyObject(SELECT);
		SELECT = SELECT.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, SELECT_PROTOTYPE, DOMObjects.HTMLSELECTELEMENT_ADD, "add", 2, DOMSpec.LEVEL_1);
		createDOMFunction(s, SELECT_PROTOTYPE, DOMObjects.HTMLSELECTELEMENT_REMOVE, "remove", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, SELECT_PROTOTYPE, DOMObjects.HTMLSELECTELEMENT_BLUR, "blur", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, SELECT_PROTOTYPE, DOMObjects.HTMLSELECTELEMENT_FOCUS, "focus", 0, DOMSpec.LEVEL_1);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLSELECTELEMENT_ADD: {
			NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			Value element = DOMConversion.toHTMLElement(call.getArg(0), c);
			Value before = DOMConversion.toHTMLElement(call.getArg(1), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case HTMLSELECTELEMENT_REMOVE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value index = Conversion.toInteger(call.getArg(0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case HTMLSELECTELEMENT_BLUR: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case HTMLSELECTELEMENT_FOCUS: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
