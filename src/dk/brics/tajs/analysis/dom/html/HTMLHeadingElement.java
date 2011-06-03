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
 * For the H1 to H6 elements. See the H1 element definition in HTML 4.01.
 */
public class HTMLHeadingElement {

	public static ObjectLabel HEADING = new ObjectLabel(DOMObjects.HTMLHEADINGELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel HEADING_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLHEADINGELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(HEADING_PROTOTYPE);
		createDOMInternalPrototype(s, HEADING_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(HEADING);
		createDOMInternalPrototype(s, HEADING, Value.makeObject(HEADING_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLHeadingElement", Value.makeObject(HEADING, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, HEADING, "align", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(HEADING);
		HEADING = HEADING.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
