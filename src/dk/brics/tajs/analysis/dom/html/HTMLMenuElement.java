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
 * Menu list. See the MENU element definition in HTML 4.01. This element is
 * deprecated in HTML 4.01.
 */
public class HTMLMenuElement {

	public static ObjectLabel MENU = new ObjectLabel(DOMObjects.HTMLMENUELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel MENU_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLMENUELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(MENU_PROTOTYPE);
		createDOMInternalPrototype(s, MENU_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(MENU);
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLMenuElement", Value.makeObject(MENU, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, MENU, "compact", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		createDOMInternalPrototype(s, MENU, Value.makeObject(MENU_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		s.multiplyObject(MENU);
		MENU = MENU.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions
	}

}
