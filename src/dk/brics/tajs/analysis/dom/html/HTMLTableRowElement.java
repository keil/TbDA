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
 * A row in a table. See the TR element definition in HTML 4.01.
 */
public class HTMLTableRowElement {

	public static ObjectLabel TABLEROW = new ObjectLabel(DOMObjects.HTMLTABLEROWELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel TABLEROW_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLTABLEROWELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(TABLEROW_PROTOTYPE);
		createDOMInternalPrototype(s, TABLEROW_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(TABLEROW);
		createDOMInternalPrototype(s, TABLEROW, Value.makeObject(TABLEROW_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLTableRowElement", Value.makeObject(TABLEROW, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, TABLEROW, "align", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLEROW, "bgColor", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLEROW, "ch", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLEROW, "chOff", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLEROW, "vAlign", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, TABLEROW, "rowIndex", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, TABLEROW, "sectionRowIndex", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, TABLEROW, "cells", Value.makeObject(HTMLCollection.COLLECTION, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_2);

		s.multiplyObject(TABLEROW);
		TABLEROW = TABLEROW.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 2
		createDOMFunction(s, TABLEROW_PROTOTYPE, DOMObjects.HTMLTABLEROWELEMENT_INSERTCELL, "insertCell", 1, DOMSpec.LEVEL_2);
		createDOMFunction(s, TABLEROW_PROTOTYPE, DOMObjects.HTMLTABLEROWELEMENT_DELETECELL, "deleteCell", 1, DOMSpec.LEVEL_2);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLTABLEROWELEMENT_INSERTCELL: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value index = Conversion.toNumber(call.getArg(0), c);
			return Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference());
		}
		case HTMLTABLEROWELEMENT_DELETECELL: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value index = Conversion.toNumber(call.getArg(0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object " + nativeObject);
		}
		}
	}

}
