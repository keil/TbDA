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
 * The LINK element specifies a link to an external resource, and defines this
 * document's relationship to that resource (or vice versa). See the LINK
 * element definition in HTML 4.01 (see also the LinkStyle interface in the
 * StyleSheet module [DOM Level 2 Style Sheets and CSS]).
 */
public class HTMLLinkElement {

	public static ObjectLabel LINK = new ObjectLabel(DOMObjects.HTMLLINKELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel LINK_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLLINKELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(LINK_PROTOTYPE);
		createDOMInternalPrototype(s, LINK_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(LINK);
		createDOMInternalPrototype(s, LINK, Value.makeObject(LINK_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLLinkElement", Value.makeObject(LINK, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, LINK, "disabled", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, LINK, "charset", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, LINK, "href", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, LINK, "hreflang", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, LINK, "media", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, LINK, "rel", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, LINK, "rev", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, LINK, "target", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, LINK, "type", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(LINK);
		LINK = LINK.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
