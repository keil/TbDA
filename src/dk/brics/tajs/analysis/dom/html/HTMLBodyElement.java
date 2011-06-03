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
 * The HTML document body. This element is always present in the DOM API, even
 * if the tags are not present in the source document. See the BODY element
 * definition in HTML 4.0.
 */
public class HTMLBodyElement {

	public static ObjectLabel BODY = new ObjectLabel(DOMObjects.HTMLBODYELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel BODY_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLBODYELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(BODY_PROTOTYPE);
		createDOMInternalPrototype(s, BODY_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(BODY);
		createDOMInternalPrototype(s, BODY, Value.makeObject(BODY_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLBodyElement", Value.makeObject(BODY, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, BODY, "aLink", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, BODY, "background", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, BODY, "bgColor", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, BODY, "link", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, BODY, "text", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, BODY, "vLink", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(BODY);
		BODY = BODY.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
