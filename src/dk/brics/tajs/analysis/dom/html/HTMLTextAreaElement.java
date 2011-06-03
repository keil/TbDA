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

public class HTMLTextAreaElement {

	public static ObjectLabel TEXTAREA = new ObjectLabel(DOMObjects.HTMLTEXTAREAELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel TEXTAREA_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLTEXTAREAELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(TEXTAREA_PROTOTYPE);
		createDOMInternalPrototype(s, TEXTAREA_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(TEXTAREA);
		createDOMInternalPrototype(s, TEXTAREA, Value.makeObject(TEXTAREA_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLTextAreaElement", Value.makeObject(TEXTAREA, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, TEXTAREA, "form", Value.makeObject(HTMLFormElement.FORM, new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, TEXTAREA, "accessKey", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TEXTAREA, "cols", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TEXTAREA, "disabled", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TEXTAREA, "name", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TEXTAREA, "readOnly", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TEXTAREA, "rows", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TEXTAREA, "tabIndex", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TEXTAREA, "type", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, TEXTAREA, "value", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, TEXTAREA, "defaultValue", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_2);

		s.multiplyObject(TEXTAREA);
		TEXTAREA = TEXTAREA.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, TEXTAREA_PROTOTYPE, DOMObjects.HTMLTEXTAREAELEMENT_BLUR, "blur", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, TEXTAREA_PROTOTYPE, DOMObjects.HTMLTEXTAREAELEMENT_FOCUS, "focus", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, TEXTAREA_PROTOTYPE, DOMObjects.HTMLTEXTAREAELEMENT_SELECT, "select", 0, DOMSpec.LEVEL_1);
	}

	/**
	 * Transfer functions
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLTEXTAREAELEMENT_BLUR: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency());
		}
		case HTMLTEXTAREAELEMENT_FOCUS: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency());
		}
		case HTMLTEXTAREAELEMENT_SELECT: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}
}
