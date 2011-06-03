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
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

/**
 * The create* and delete* methods on the table allow authors to construct and
 * modify tables. [HTML 4.01] specifies that only one of each of the CAPTION,
 * THEAD, and TFOOT elements may exist in a table. Therefore, if one exists, and
 * the createTHead() or createTFoot() method is called, the method returns the
 * existing THead or TFoot element. See the TABLE element definition in HTML
 * 4.01.
 */
public class HTMLTableElement {

	public static ObjectLabel TABLE = new ObjectLabel(DOMObjects.HTMLTABLEELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel TABLE_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLTABLEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(TABLE_PROTOTYPE);
		createDOMInternalPrototype(s, TABLE_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(TABLE);
		createDOMInternalPrototype(s, TABLE, Value.makeObject(TABLE_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLTableElement", Value.makeObject(TABLE, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, TABLE, "rows", Value.makeObject(HTMLCollection.COLLECTION, new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLE, "tBodies", Value.makeObject(HTMLCollection.COLLECTION, new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLE, "align", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLE, "bgColor", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLE, "border", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLE, "cellPadding", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLE, "cellSpacing", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLE, "frame", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLE, "rules", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLE, "summary", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLE, "width", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, TABLE, "caption", Value.makeObject(HTMLTableCaptionElement.TABLECAPTION, new Dependency()), DOMSpec.LEVEL_2);
		createDOMProperty(s, TABLE, "tHead", Value.makeObject(HTMLTableSectionElement.TABLESECTION, new Dependency()), DOMSpec.LEVEL_2);
		createDOMProperty(s, TABLE, "tFoot", Value.makeObject(HTMLTableSectionElement.TABLESECTION, new Dependency()), DOMSpec.LEVEL_2);

		s.multiplyObject(TABLE);
		TABLE = TABLE.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, TABLE_PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_CREATETHEAD, "createTHead", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, TABLE_PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_DELETETHEAD, "deleteTHead", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, TABLE_PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_CREATETFOOT, "createTFoot", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, TABLE_PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_DELETETFOOT, "deleteTFoot", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, TABLE_PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_CREATECAPTION, "createCaption", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, TABLE_PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_DELETECAPTION, "deleteCaption", 0, DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMFunction(s, TABLE_PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_INSERTROW, "insertRow", 1, DOMSpec.LEVEL_2);
		createDOMFunction(s, TABLE_PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_DELETEROW, "deleteRow", 1, DOMSpec.LEVEL_2);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLTABLEELEMENT_CREATETHEAD:
		case HTMLTABLEELEMENT_CREATETFOOT:
		case HTMLTABLEELEMENT_CREATECAPTION: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency());
		}
		case HTMLTABLEELEMENT_DELETETHEAD:
		case HTMLTABLEELEMENT_DELETETFOOT:
		case HTMLTABLEELEMENT_DELETECAPTION: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency());
		}
		case HTMLTABLEELEMENT_INSERTROW: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value index = Conversion.toNumber(call.getArg(0), c);
			return Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency());
		}
		case HTMLTABLEELEMENT_DELETEROW: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value index = Conversion.toNumber(call.getArg(0), c);
			return Value.makeUndef(new Dependency());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object " + nativeObject);
		}
		}
	}

}
