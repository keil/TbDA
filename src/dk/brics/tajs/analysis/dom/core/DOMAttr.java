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
 * The Attr interface represents an attribute in an Element object. Attr objects
 * inherit the Node interface, but since they are not actually child nodes of
 * the element they describe, the DOM does not consider them part of the
 * document tree. Thus, the Node attributes parentNode, previousSibling, and
 * nextSibling have a null value for Attr objects.
 * <p/>
 * http://www.w3.org/TR/DOM-Level-2-Core/core.html#ID-637646024
 */
public class DOMAttr {

	public static ObjectLabel ATTR = new ObjectLabel(DOMObjects.ATTR, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel ATTR_PROTOTYPE = new ObjectLabel(DOMObjects.ATTR_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(ATTR_PROTOTYPE);
		createDOMInternalPrototype(s, ATTR_PROTOTYPE, Value.makeObject(DOMNode.NODE_PROTOTYPE, new Dependency()));

		// Multiplied object.
		s.newObject(ATTR);
		createDOMInternalPrototype(s, ATTR, Value.makeObject(ATTR_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "Attr", Value.makeObject(ATTR, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, ATTR, "name", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, ATTR, "specified", Value.makeAnyBool(new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, ATTR, "value", Value.makeAnyStr(new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, ATTR, "ownerElement", Value.makeObject(DOMElement.ELEMENT, new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);

		// DOM Level 3
		createDOMProperty(s, ATTR, "isId", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_3);

		s.multiplyObject(ATTR);
		ATTR = ATTR.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No Functions.
	}
}
