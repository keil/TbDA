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

public class HTMLDivElement {

	public static ObjectLabel DIV = new ObjectLabel(DOMObjects.HTMLDIVELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel DIV_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLDIVELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(DIV_PROTOTYPE);
		createDOMInternalPrototype(s, DIV_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(DIV);
		createDOMInternalPrototype(s, DIV, Value.makeObject(DIV_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLDivElement", Value.makeObject(DIV, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, DIV, "align", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(DIV);
		DIV = DIV.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
