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
 * Client-side image map area definition. See the AREA element definition in
 * HTML 4.0.
 */
public class HTMLAreaElement {

	public static ObjectLabel AREA = new ObjectLabel(DOMObjects.HTMLAREAELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel AREA_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLAREAELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(AREA_PROTOTYPE);
		createDOMInternalPrototype(s, AREA_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(AREA);
		createDOMInternalPrototype(s, AREA, Value.makeObject(AREA_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLAreaElement", Value.makeObject(AREA, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, AREA, "accessKey", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, AREA, "alt", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, AREA, "coords", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, AREA, "href", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, AREA, "noHref", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, AREA, "shape", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, AREA, "tabIndex", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, AREA, "target", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(AREA);
		AREA = AREA.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
