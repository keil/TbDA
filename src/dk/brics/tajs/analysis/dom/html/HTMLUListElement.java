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

public class HTMLUListElement {

	public static ObjectLabel ULIST = new ObjectLabel(DOMObjects.HTMLULISTELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel ULIST_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLULISTELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(ULIST_PROTOTYPE);
		createDOMInternalPrototype(s, ULIST_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(ULIST);
		createDOMInternalPrototype(s, ULIST, Value.makeObject(ULIST_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLUListElement", Value.makeObject(ULIST, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, ULIST, "compact", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, ULIST, "type", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(ULIST);
		ULIST = ULIST.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions
	}

}
