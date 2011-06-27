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

public class HTMLParagraphElement {

	public static ObjectLabel PARAGRAPH = new ObjectLabel(DOMObjects.HTMLPARAGRAPHELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel PARAGRAPH_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLPARAGRAPHELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(PARAGRAPH_PROTOTYPE);
		createDOMInternalPrototype(s, PARAGRAPH_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(PARAGRAPH);
		createDOMInternalPrototype(s, PARAGRAPH, Value.makeObject(PARAGRAPH_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLParagraphElement", Value.makeObject(PARAGRAPH, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, PARAGRAPH, "align", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(PARAGRAPH);
		PARAGRAPH = PARAGRAPH.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
