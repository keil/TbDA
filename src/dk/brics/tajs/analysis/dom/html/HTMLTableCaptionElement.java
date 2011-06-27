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

public class HTMLTableCaptionElement {

	public static ObjectLabel TABLECAPTION = new ObjectLabel(DOMObjects.HTMLTABLECAPTIONELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel TABLECAPTION_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLTABLECAPTIONELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(TABLECAPTION_PROTOTYPE);
		createDOMInternalPrototype(s, TABLECAPTION_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(TABLECAPTION);
		createDOMInternalPrototype(s, TABLECAPTION, Value.makeObject(TABLECAPTION_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLTableCaptionElement", Value.makeObject(TABLECAPTION, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, TABLECAPTION, "align", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(TABLECAPTION);
		TABLECAPTION = TABLECAPTION.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions
	}

}
