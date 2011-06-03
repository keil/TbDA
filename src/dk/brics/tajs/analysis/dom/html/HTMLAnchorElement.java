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

/**
 * The anchor element. See the A element definition in HTML 4.01
 */
public class HTMLAnchorElement {

	public static ObjectLabel ANCHOR = new ObjectLabel(DOMObjects.HTMLANCHORELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel ANCHOR_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLANCHORELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(ANCHOR_PROTOTYPE);
		createDOMInternalPrototype(s, ANCHOR_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(ANCHOR);
		createDOMInternalPrototype(s, ANCHOR, Value.makeObject(ANCHOR_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLAnchorElement", Value.makeObject(ANCHOR, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, ANCHOR, "accessKey", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, ANCHOR, "charset", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, ANCHOR, "coords", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, ANCHOR, "href", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, ANCHOR, "hreflang", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, ANCHOR, "name", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, ANCHOR, "rel", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, ANCHOR, "rev", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, ANCHOR, "shape", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, ANCHOR, "tabIndex", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, ANCHOR, "target", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, ANCHOR, "type", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(ANCHOR);
		ANCHOR = ANCHOR.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM LEVEL 1
		createDOMFunction(s, ANCHOR_PROTOTYPE, DOMObjects.HTMLANCHORELEMENT_BLUR, "blur", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, ANCHOR_PROTOTYPE, DOMObjects.HTMLANCHORELEMENT_FOCUS, "focus", 0, DOMSpec.LEVEL_1);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLANCHORELEMENT_BLUR: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency());
		}
		case HTMLANCHORELEMENT_FOCUS: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
