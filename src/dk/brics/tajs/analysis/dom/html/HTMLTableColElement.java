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

public class HTMLTableColElement {

	public static ObjectLabel TABLECOL = new ObjectLabel(DOMObjects.HTMLTABLECOLELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel TABLECOL_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLTABLECOLELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(TABLECOL_PROTOTYPE);
		createDOMInternalPrototype(s, TABLECOL_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(TABLECOL);
		createDOMInternalPrototype(s, TABLECOL, Value.makeObject(TABLECOL_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLTableColElement", Value.makeObject(TABLECOL, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, TABLECOL, "align", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLECOL, "ch", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLECOL, "chOff", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLECOL, "span", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLECOL, "vAlign", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, TABLECOL, "width", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(TABLECOL);
		TABLECOL = TABLECOL.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions
	}

}
