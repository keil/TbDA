package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
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
 * The NodeList interface provides the abstraction of an ordered collection of
 * nodes, without defining or constraining how this collection is implemented.
 * NodeList objects in the DOM are live.
 * <p/>
 * The items in the NodeList are accessible via an integral index, starting from
 * 0.
 * 
 * @author magnusm
 */
public class DOMNodeList {

	public static ObjectLabel NODELIST_PROTOTYPE = new ObjectLabel(DOMObjects.NODELIST_PROTOTYPE, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel NODELIST = new ObjectLabel(DOMObjects.NODELIST, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object
		s.newObject(NODELIST_PROTOTYPE);
		createDOMInternalPrototype(s, NODELIST_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency()));

		// Multiplied object
		s.newObject(NODELIST);
		createDOMInternalPrototype(s, NODELIST, Value.makeObject(NODELIST_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "NodeList", Value.makeObject(NODELIST, new Dependency()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, NODELIST, "length", Value.makeNum(0, new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);

		s.multiplyObject(NODELIST);
		NODELIST = NODELIST.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		createDOMFunction(s, NODELIST_PROTOTYPE, DOMObjects.NODELIST_ITEM, "item", 1, DOMSpec.LEVEL_1);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeobject, CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeobject) {
		case NODELIST_ITEM:
			throw new UnsupportedOperationException("NODELIST_ITEM not supported: "
					+ nativeobject);
		default: {
			throw new RuntimeException("Unknown Native Object: " + nativeobject);
		}
		}
	}

}
