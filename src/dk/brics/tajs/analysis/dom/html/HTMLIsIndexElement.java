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
 * This element is used for single-line text input. See the ISINDEX element
 * definition in HTML 4.01. This element is deprecated in HTML 4.01.
 */
public class HTMLIsIndexElement {

	public static ObjectLabel ISINDEX = new ObjectLabel(DOMObjects.HTMLISINDEXELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel ISINDEX_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLISINDEXELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(ISINDEX_PROTOTYPE);
		createDOMInternalPrototype(s, ISINDEX_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(ISINDEX);
		createDOMInternalPrototype(s, ISINDEX, Value.makeObject(ISINDEX_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLIsIndexElement", Value.makeObject(ISINDEX, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, ISINDEX, "form", Value.makeObject(HTMLFormElement.FORM, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, ISINDEX, "prompt", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(ISINDEX);
		ISINDEX = ISINDEX.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
