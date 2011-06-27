package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

/**
 * Create a grid of frames. See the FRAMESET element definition in HTML 4.01.
 */
public class HTMLFrameSetElement {

	public static ObjectLabel FRAMESET = new ObjectLabel(DOMObjects.HTMLFRAMESETELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel FRAMESET_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLFRAMESETELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(FRAMESET_PROTOTYPE);
		createDOMInternalPrototype(s, FRAMESET_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(FRAMESET);
		createDOMInternalPrototype(s, FRAMESET, Value.makeObject(FRAMESET_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLFrameSetElement", Value.makeObject(FRAMESET, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, FRAMESET, "cols", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FRAMESET, "rows", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(FRAMESET);
		FRAMESET = FRAMESET.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
