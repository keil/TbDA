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

public class HTMLOptionElement {

	public static ObjectLabel OPTION = new ObjectLabel(DOMObjects.HTMLOPTIONELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel OPTION_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLOPTIONELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(OPTION_PROTOTYPE);
		createDOMInternalPrototype(s, OPTION_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(OPTION);
		createDOMInternalPrototype(s, OPTION, Value.makeObject(OPTION_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLOptionElement", Value.makeObject(OPTION, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, OPTION, "form", Value.makeObject(HTMLFormElement.FORM, new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, OPTION, "text", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, OPTION, "disabled", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OPTION, "label", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OPTION, "selected", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OPTION, "value", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, OPTION, "defaultSelected", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_2);
		createDOMProperty(s, OPTION, "index", Value.makeAnyNum(new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);

		s.multiplyObject(OPTION);
		OPTION = OPTION.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
