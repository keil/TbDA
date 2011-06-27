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
 * Form field label text. See the LABEL element definition in HTML 4.01.
 */
public class HTMLLabelElement {

	public static ObjectLabel LABEL = new ObjectLabel(DOMObjects.HTMLLABELELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel LABEL_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLLABELELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(LABEL_PROTOTYPE);
		createDOMInternalPrototype(s, LABEL_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(LABEL);
		createDOMInternalPrototype(s, LABEL, Value.makeObject(LABEL_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLLabelElement", Value.makeObject(LABEL, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, LABEL, "form", Value.makeObject(HTMLFormElement.FORM, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, LABEL, "accessKey", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, LABEL, "htmlFor", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(LABEL);
		LABEL = LABEL.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions
	}

}
