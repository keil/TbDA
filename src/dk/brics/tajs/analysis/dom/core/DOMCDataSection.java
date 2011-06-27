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
 * CDATA sections are used to escape blocks of text containing characters that
 * would otherwise be regarded as markup. The only delimiter that is recognized
 * in a CDATA section is the "]]>" string that ends the CDATA section. CDATA
 * sections cannot be nested. Their primary purpose is for including material
 * such as XML fragments, without needing to escape all the delimiters.
 */
public class DOMCDataSection {

	public static ObjectLabel CDATASECTION = new ObjectLabel(DOMObjects.CDATASECTION, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel CDATASECTION_PROTOTYPE = new ObjectLabel(DOMObjects.CDATASECTION_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object
		s.newObject(CDATASECTION_PROTOTYPE);
		createDOMInternalPrototype(s, CDATASECTION_PROTOTYPE, Value.makeObject(DOMText.TEXT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(CDATASECTION);
		createDOMInternalPrototype(s, CDATASECTION, Value.makeObject(CDATASECTION_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "CDataSection", Value.makeObject(CDATASECTION, new Dependency(), new DependencyGraphReference()));
		s.multiplyObject(CDATASECTION);
		CDATASECTION = CDATASECTION.makeSingleton().makeSummary();

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
