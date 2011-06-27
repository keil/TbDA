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
 * Client-side image map. See the MAP element definition in HTML 4.01.
 */
public class HTMLMapElement {

	public static ObjectLabel MAP = new ObjectLabel(DOMObjects.HTMLMAPELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel MAP_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLMAPELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(MAP_PROTOTYPE);
		createDOMInternalPrototype(s, MAP_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(MAP);
		createDOMInternalPrototype(s, MAP, Value.makeObject(MAP_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLMapElement", Value.makeObject(MAP, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, MAP, "areas", Value.makeObject(HTMLCollection.COLLECTION, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, MAP, "name", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(MAP);
		MAP = MAP.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
