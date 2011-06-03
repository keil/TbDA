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
 * For the Q and BLOCKQUOTE elements. See the Q element definition in HTML 4.01.
 */
public class HTMLQuoteElement {

	public static ObjectLabel QUOTE = new ObjectLabel(DOMObjects.HTMLQUOTEELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel QUOTE_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLQUOTEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(QUOTE_PROTOTYPE);
		createDOMInternalPrototype(s, QUOTE_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(QUOTE);
		createDOMInternalPrototype(s, QUOTE, Value.makeObject(QUOTE_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLQuoteElement", Value.makeObject(QUOTE, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, QUOTE, "cite", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(QUOTE);
		QUOTE = QUOTE.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
