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

/**
 * The create* and delete* methods on the table allow authors to construct and
 * modify tables. [HTML 4.01] specifies that only one of each of the CAPTION,
 * THEAD, and TFOOT elements may exist in a table. Therefore, if one exists, and
 * the createTHead() or createTFoot() method is called, the method returns the
 * existing THead or TFoot element. See the TABLE element definition in HTML
 * 4.01.
 */
public class HTMLTableElement {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLTABLEELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.HTMLTABLEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.HTMLTABLEELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLTableElement", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

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
		createDOMProperty(s, INSTANCES, "rows", Value.makeObject(HTMLCollection.INSTANCES, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "tBodies", Value.makeObject(HTMLCollection.INSTANCES, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "align", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "bgColor", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "border", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "cellPadding", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "cellSpacing", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "frame", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "rules", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "summary", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, INSTANCES, "width", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, INSTANCES, "caption", Value.makeObject(HTMLTableCaptionElement.INSTANCES, new Dependency(), new DependencyGraphReference()),
				DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "tHead", Value.makeObject(HTMLTableSectionElement.INSTANCES, new Dependency(), new DependencyGraphReference()),
				DOMSpec.LEVEL_2);
		createDOMProperty(s, INSTANCES, "tFoot", Value.makeObject(HTMLTableSectionElement.INSTANCES, new Dependency(), new DependencyGraphReference()),
				DOMSpec.LEVEL_2);

		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 1
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_CREATETHEAD, "createTHead", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_DELETETHEAD, "deleteTHead", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_CREATETFOOT, "createTFoot", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_DELETETFOOT, "deleteTFoot", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_CREATECAPTION, "createCaption", 0, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_DELETECAPTION, "deleteCaption", 0, DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_INSERTROW, "insertRow", 1, DOMSpec.LEVEL_2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_DELETEROW, "deleteRow", 1, DOMSpec.LEVEL_2);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLTABLEELEMENT_CREATETHEAD:
		case HTMLTABLEELEMENT_CREATETFOOT:
		case HTMLTABLEELEMENT_CREATECAPTION: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference());
		}
		case HTMLTABLEELEMENT_DELETETHEAD:
		case HTMLTABLEELEMENT_DELETETFOOT:
		case HTMLTABLEELEMENT_DELETECAPTION: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		case HTMLTABLEELEMENT_INSERTROW: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value index = */
			Conversion.toNumber(call.getArg(0), c);
			return Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference());
		}
		case HTMLTABLEELEMENT_DELETEROW: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value index = */
			Conversion.toNumber(call.getArg(0), c);
			return Value.makeUndef(new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object " + nativeObject);
		}
		}
	}

}
