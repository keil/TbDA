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
 * List item. See the LI element definition in HTML 4.01.
 */
public class HTMLLIElement {

	public static ObjectLabel LI = new ObjectLabel(DOMObjects.HTMLLIELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel LI_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLLIELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(LI_PROTOTYPE);
		createDOMInternalPrototype(s, LI_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(LI);
		createDOMInternalPrototype(s, LI, Value.makeObject(LI_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLLIElement", Value.makeObject(LI, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, LI, "type", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, LI, "value", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(LI);
		LI = LI.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
