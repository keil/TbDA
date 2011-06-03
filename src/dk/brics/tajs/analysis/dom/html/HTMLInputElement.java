package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

public class HTMLInputElement {

	public static ObjectLabel INPUT = new ObjectLabel(DOMObjects.HTMLINPUTELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel INPUT_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLINPUTELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(INPUT_PROTOTYPE);
		createDOMInternalPrototype(s, INPUT_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(INPUT);
		createDOMInternalPrototype(s, INPUT, Value.makeObject(INPUT_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLInputElement", Value.makeObject(INPUT, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, INPUT, "defaultValue", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "defaultChecked", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "form", Value.makeObject(HTMLFormElement.FORM, new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "accept", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "accessKey", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "align", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "alt", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "checked", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "disabled", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "maxLength", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "name", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "readOnly", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "src", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "tabIndex", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "useMap", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INPUT, "value", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, INPUT, "size", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_2);
		createDOMProperty(s, INPUT, "type", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_2);

		s.multiplyObject(INPUT);
		INPUT = INPUT.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, INPUT_PROTOTYPE, DOMObjects.HTMLINPUTELEMENT_BLUR, "blur", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, INPUT_PROTOTYPE, DOMObjects.HTMLINPUTELEMENT_FOCUS, "focus", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, INPUT_PROTOTYPE, DOMObjects.HTMLINPUTELEMENT_SELECT, "select", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, INPUT_PROTOTYPE, DOMObjects.HTMLINPUTELEMENT_CLICK, "click", 0, DOMSpec.LEVEL_1);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLINPUTELEMENT_BLUR: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency());
		}
		case HTMLINPUTELEMENT_CLICK: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency());
		}
		case HTMLINPUTELEMENT_FOCUS: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency());
		}
		case HTMLINPUTELEMENT_SELECT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
