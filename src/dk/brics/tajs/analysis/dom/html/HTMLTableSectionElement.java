package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.Conversion;
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

/**
 * The THEAD, TFOOT, and TBODY elements.
 */
public class HTMLTableSectionElement {

	public static ObjectLabel TABLESECTION = new ObjectLabel(DOMObjects.HTMLTABLESECTIONELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel TABLESECTION_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLTABLESECTIONELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(TABLESECTION_PROTOTYPE);
		createDOMInternalPrototype(s, TABLESECTION_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(TABLESECTION);
		createDOMInternalPrototype(s, TABLESECTION, Value.makeObject(TABLESECTION_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLTableSectionElement", Value.makeObject(TABLESECTION, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, TABLESECTION, "align", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLESECTION, "ch", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLESECTION, "chOff", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLESECTION, "vAlign", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLESECTION, "rows", Value.makeObject(HTMLCollection.COLLECTION, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);

		s.multiplyObject(TABLESECTION);
		TABLESECTION = TABLESECTION.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 2
		createDOMFunction(s, TABLESECTION_PROTOTYPE, DOMObjects.HTMLTABLESECTIONELEMENT_INSERTROW, "insertRow", 1, DOMSpec.LEVEL_2);
		createDOMFunction(s, TABLESECTION_PROTOTYPE, DOMObjects.HTMLTABLESECTIONELEMENT_DELETEROW, "deleteRow", 1, DOMSpec.LEVEL_2);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLTABLESECTIONELEMENT_INSERTROW: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value index = Conversion.toNumber(call.getArg(0), c);
			return Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference());
		}
		case HTMLTABLESECTIONELEMENT_DELETEROW: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value index = Conversion.toNumber(call.getArg(0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}
}
