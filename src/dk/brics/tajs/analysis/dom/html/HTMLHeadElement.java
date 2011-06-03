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
 * Document head information. See the HEAD element definition in HTML 4.01.
 */
public class HTMLHeadElement {

	public static ObjectLabel HEAD = new ObjectLabel(DOMObjects.HTMLHEADELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel HEAD_PROTOTPE = new ObjectLabel(DOMObjects.HTMLHEADELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(HEAD_PROTOTPE);
		createDOMInternalPrototype(s, HEAD_PROTOTPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(HEAD);
		createDOMInternalPrototype(s, HEAD, Value.makeObject(HEAD_PROTOTPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLHeadElement", Value.makeObject(HEAD, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, HEAD, "profile", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(HEAD);
		HEAD = HEAD.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
