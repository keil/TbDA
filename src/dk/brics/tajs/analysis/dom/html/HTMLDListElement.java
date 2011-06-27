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

public class HTMLDListElement {

	public static ObjectLabel DLIST = new ObjectLabel(DOMObjects.HTMLDLISTELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel DLIST_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLDLISTELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(DLIST_PROTOTYPE);
		createDOMInternalPrototype(s, DLIST_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(DLIST);
		createDOMInternalPrototype(s, DLIST, Value.makeObject(DLIST_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLDListElement", Value.makeObject(DLIST, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, DLIST, "compact", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(DLIST);
		DLIST = DLIST.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
