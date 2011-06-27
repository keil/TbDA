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
 * Base font. See the BASEFONT element definition in HTML 4.0. This element is
 * deprecated in HTML 4.0.
 */
public class HTMLBaseFontElement {

	public static ObjectLabel BASEFONT = new ObjectLabel(DOMObjects.HTMLBASEFONTELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel BASEFONT_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLBASEFONTELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(BASEFONT_PROTOTYPE);
		createDOMInternalPrototype(s, BASEFONT_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(BASEFONT);
		createDOMInternalPrototype(s, BASEFONT, Value.makeObject(BASEFONT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLBaseFontElement", Value.makeObject(BASEFONT, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, BASEFONT, "color", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, BASEFONT, "face", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, BASEFONT, "size", Value.makeAnyNum(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(BASEFONT);
		BASEFONT = BASEFONT.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
