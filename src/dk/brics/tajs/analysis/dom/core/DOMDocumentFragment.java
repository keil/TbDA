package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * DocumentFragment is a "lightweight" or "minimal" Document object.
 */
public class DOMDocumentFragment {

	public static ObjectLabel DOCUMENTFRAGMENT = new ObjectLabel(DOMObjects.DOCUMENTFRAGMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel DOCUMENTFRAGMENT_PROTOTYPE = new ObjectLabel(DOMObjects.DOCUMENTFRAGMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(DOCUMENTFRAGMENT_PROTOTYPE);
		createDOMInternalPrototype(s, DOCUMENTFRAGMENT_PROTOTYPE, Value.makeObject(DOMNode.NODE_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(DOCUMENTFRAGMENT);
		createDOMInternalPrototype(s, DOCUMENTFRAGMENT, Value.makeObject(DOCUMENTFRAGMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "DocumentFragment", Value.makeObject(DOCUMENTFRAGMENT, new Dependency(), new DependencyGraphReference()));
		s.multiplyObject(DOCUMENTFRAGMENT);
		DOCUMENTFRAGMENT = DOCUMENTFRAGMENT.makeSingleton().makeSummary();

		/*
		 * Properties.
		 */
		// No properties.

		/*
		 * Functions.
		 */
		// No functions.

	}

}
