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
 * Local change to font. See the FONT element definition in HTML 4.01. This
 * element is deprecated in HTML 4.01.
 */
public class HTMLFontElement {

	public static ObjectLabel FONT = new ObjectLabel(DOMObjects.HTMLFONTELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel FONT_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLFONTELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(FONT_PROTOTYPE);
		createDOMInternalPrototype(s, FONT_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(FONT);
		createDOMInternalPrototype(s, FONT, Value.makeObject(FONT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLFontElement", Value.makeObject(FONT, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, FONT, "color", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FONT, "face", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FONT, "size", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(FONT);
		FONT = FONT.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
