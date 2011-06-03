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

public class HTMLParamElement {

	public static ObjectLabel PARAM = new ObjectLabel(DOMObjects.HTMLPARAMELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel PARAM_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLPARAMELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(PARAM_PROTOTYPE);
		createDOMInternalPrototype(s, PARAM_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(PARAM);
		createDOMInternalPrototype(s, PARAM, Value.makeObject(PARAM_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLParamElement", Value.makeObject(PARAM, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, PARAM, "name", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, PARAM, "type", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, PARAM, "value", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, PARAM, "valueType", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(PARAM);
		PARAM = PARAM.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
