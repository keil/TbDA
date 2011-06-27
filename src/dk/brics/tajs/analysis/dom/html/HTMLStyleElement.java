package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

/**
 * Style information. See the STYLE element definition in HTML 4.01, the CSS
 * module [DOM Level 2 Style Sheets and CSS] and the LinkStyle interface in the
 * StyleSheets module [DOM Level 2 Style Sheets and CSS].
 */
public class HTMLStyleElement {

	public static ObjectLabel STYLE = new ObjectLabel(DOMObjects.HTMLSTYLEELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel STYLE_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLSTYLEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(STYLE_PROTOTYPE);
		createDOMInternalPrototype(s, STYLE_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multipled Object
		s.newObject(STYLE);
		createDOMInternalPrototype(s, STYLE, Value.makeObject(STYLE_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLStyleElement", Value.makeObject(STYLE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, STYLE, "disabled", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, STYLE, "media", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, STYLE, "type", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(STYLE);
		STYLE = STYLE.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions
	}

}
