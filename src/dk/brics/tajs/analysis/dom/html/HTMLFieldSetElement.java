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
 * Organizes form controls into logical groups. See the FIELDSET element
 * definition in HTML 4.01.
 */
public class HTMLFieldSetElement {

	public static ObjectLabel FIELDSET = new ObjectLabel(DOMObjects.HTMLFIELDSETELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel FIELDSET_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLFIELDSETELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(FIELDSET_PROTOTYPE);
		createDOMInternalPrototype(s, FIELDSET_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(FIELDSET);
		createDOMInternalPrototype(s, FIELDSET, Value.makeObject(FIELDSET_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLFieldSetElement", Value.makeObject(FIELDSET, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, FIELDSET, "form", Value.makeObject(HTMLFormElement.FORM, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);

		s.multiplyObject(FIELDSET);
		FIELDSET = FIELDSET.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
