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
 * Root of an HTML document. See the HTML element definition in HTML 4.01.
 */
public class HTMLHtmlElement {

	public static ObjectLabel HTML = new ObjectLabel(DOMObjects.HTMLHTMLELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel HTML_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLHTMLELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(HTML_PROTOTYPE);
		createDOMInternalPrototype(s, HTML_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(HTML);
		createDOMInternalPrototype(s, HTML, Value.makeObject(HTML_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLHtmlElement", Value.makeObject(HTML, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 0
		createDOMProperty(s, HTML, "clientWidth", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_0);
		createDOMProperty(s, HTML, "clientHeight", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_0);

		// DOM Level 1
		createDOMProperty(s, HTML, "version", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(HTML);
		HTML = HTML.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
