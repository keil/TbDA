package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

/**
 * Push button. See the BUTTON element definition in HTML 4.0.
 */
public class HTMLButtonElement {

	public static ObjectLabel BUTTON = new ObjectLabel(DOMObjects.HTMLBUTTONELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel BUTTON_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLBUTTONELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(BUTTON_PROTOTYPE);
		createDOMInternalPrototype(s, BUTTON_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(BUTTON);
		createDOMInternalPrototype(s, BUTTON, Value.makeObject(BUTTON_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLButtonElement", Value.makeObject(BUTTON, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, BUTTON, "form", Value.makeObject(HTMLFormElement.FORM, new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, BUTTON, "accessKey", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, BUTTON, "disabled", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, BUTTON, "name", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, BUTTON, "tabIndex", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, BUTTON, "type", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, BUTTON, "value", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(BUTTON);
		BUTTON = BUTTON.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
