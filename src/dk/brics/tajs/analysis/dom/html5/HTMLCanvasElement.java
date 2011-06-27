package dk.brics.tajs.analysis.dom.html5;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.html.HTMLElement;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

public class HTMLCanvasElement {

	public static ObjectLabel CANVAS = new ObjectLabel(DOMObjects.HTMLCANVASELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel CANVAS_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLCANVASELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(CANVAS_PROTOTYPE);
		createDOMInternalPrototype(s, CANVAS_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLCanvasElement", Value.makeObject(CANVAS_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(CANVAS);
		createDOMInternalPrototype(s, CANVAS, Value.makeObject(CANVAS_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		s.multiplyObject(CANVAS);
		CANVAS = CANVAS.makeSingleton().makeSummary();

		/**
		 * Properties.
		 */
		createDOMProperty(s, CANVAS_PROTOTYPE, "height", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);
		createDOMProperty(s, CANVAS_PROTOTYPE, "width", Value.makeAnyNumUInt(new Dependency(), new DependencyGraphReference()), DOMSpec.HTML5);

		/**
		 * Functions.
		 */
		createDOMFunction(s, CANVAS_PROTOTYPE, DOMObjects.HTMLCANVASELEMENT_GET_CONTEXT, "getContext", 1, DOMSpec.HTML5);
		createDOMFunction(s, CANVAS_PROTOTYPE, DOMObjects.HTMLCANVASELEMENT_TO_DATA_URL, "toDataURL", 1, DOMSpec.HTML5);
	}

	public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObject) {
		case HTMLCANVASELEMENT_GET_CONTEXT: {
			return Value.makeObject(CanvasRenderingContext2D.CONTEXT2D, new Dependency(), new DependencyGraphReference());
		}
		default: {
			throw new RuntimeException("Unsupported Native Object: " + nativeObject);
		}
		}
	}

}
