package dk.brics.tajs.analysis.dom.core;

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
 * Each Document has a doctype attribute whose value is either null or a
 * DocumentType object. The DocumentType interface in the DOM Core provides an
 * interface to the list of entities that are defined for the document, and
 * little else because the effect of namespaces and the various XML schema
 * efforts on DTD representation are not clearly understood as of this writing.
 */
public class DOMDocumentType {

	public static ObjectLabel TYPE = new ObjectLabel(DOMObjects.DOCUMENTTYPE, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel TYPE_PROTOTYPE = new ObjectLabel(DOMObjects.DOCUMENTTYPE_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(TYPE_PROTOTYPE);
		createDOMInternalPrototype(s, TYPE_PROTOTYPE, Value.makeObject(DOMNode.NODE_PROTOTYPE, new Dependency()));

		// Multiplied object.
		s.newObject(TYPE);
		createDOMInternalPrototype(s, TYPE, Value.makeObject(TYPE_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "DocumentType", Value.makeObject(TYPE, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, TYPE, "name", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, TYPE, "entities", Value.makeObject(DOMNamedNodeMap.NAMED_NODE_MAP, new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, TYPE, "notations", Value.makeObject(DOMNamedNodeMap.NAMED_NODE_MAP, new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, TYPE, "publicId", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, TYPE, "systemId", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);
		createDOMProperty(s, TYPE, "internalSubset", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);

		s.multiplyObject(TYPE);
		TYPE = TYPE.makeSingleton().makeSummary();

		/*
		 * Functions
		 */
		// No functions.
	}

}
