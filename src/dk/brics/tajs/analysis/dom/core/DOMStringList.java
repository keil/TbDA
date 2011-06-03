package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
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
 * The DOMStringList interface provides the abstraction of an ordered collection
 * of DOMString values, without defining or constraining how this collection is
 * implemented.
 * <p/>
 * Introduced in DOM Level 3.
 */
public class DOMStringList {

	public static ObjectLabel STRINGLIST = new ObjectLabel(DOMObjects.STRINGLIST, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel STRINGLIST_PROTOTYPE = new ObjectLabel(DOMObjects.STRINGLIST_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(STRINGLIST_PROTOTYPE);
		createDOMInternalPrototype(s, STRINGLIST_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency()));

		// Multiplied object.
		s.newObject(STRINGLIST);
		createDOMInternalPrototype(s, STRINGLIST, Value.makeObject(STRINGLIST_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "StringList", Value.makeObject(STRINGLIST, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 3
		createDOMProperty(s, STRINGLIST, "length", Value.makeAnyNumUInt(new Dependency()).setReadOnly(), DOMSpec.LEVEL_3);

		s.multiplyObject(STRINGLIST);
		STRINGLIST = STRINGLIST.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// DOM Level 3
		createDOMFunction(s, STRINGLIST_PROTOTYPE, DOMObjects.STRINGLIST_ITEM, "item", 1, DOMSpec.LEVEL_3);
		createDOMFunction(s, STRINGLIST_PROTOTYPE, DOMObjects.STRINGLIST_CONTAINS, "contains", 1, DOMSpec.LEVEL_3);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case STRINGLIST_ITEM: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value index = Conversion.toInteger(call.getArg(0), c);
			return Value.makeAnyStr(new Dependency()).joinNull();
		}
		case STRINGLIST_CONTAINS: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			Value str = Conversion.toString(call.getArg(0), c);
			return Value.makeAnyBool(new Dependency());
		}
		default: {
			throw new UnsupportedOperationException("Unsupported Native Object " + nativeObject);
		}
		}
	}

}
