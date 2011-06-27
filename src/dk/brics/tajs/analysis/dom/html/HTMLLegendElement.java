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

public class HTMLLegendElement {

	public static ObjectLabel LEGEND = new ObjectLabel(DOMObjects.HTMLLEGENDELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel LEGEND_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLLEGENDELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(LEGEND_PROTOTYPE);
		createDOMInternalPrototype(s, LEGEND_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(LEGEND);
		createDOMInternalPrototype(s, LEGEND, Value.makeObject(LEGEND_PROTOTYPE,new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLLegendElement", Value.makeObject(LEGEND, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, LEGEND, "form", Value.makeObject(HTMLFormElement.FORM, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, LEGEND, "accessKey", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, LEGEND, "align", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(LEGEND);
		LEGEND = LEGEND.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
