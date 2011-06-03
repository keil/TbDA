package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * This represents the content of a comment, i.e., all the characters between
 * the starting '<!--' and ending '-->'. Note that this is the definition of a
 * comment in XML, and, in practice, HTML, although some HTML tools may
 * implement the full SGML comment structure.
 */
public class DOMComment {

	public static ObjectLabel COMMENT = new ObjectLabel(DOMObjects.COMMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel COMMENT_PROTOTYPE = new ObjectLabel(DOMObjects.COMMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(COMMENT_PROTOTYPE);
		createDOMInternalPrototype(s, COMMENT_PROTOTYPE, Value.makeObject(DOMCharacterData.CHARACTER_DATA_PROTOTYPE, new Dependency()));

		// Multiplied object.
		s.newObject(COMMENT);
		createDOMInternalPrototype(s, COMMENT, Value.makeObject(COMMENT_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "Comment", Value.makeObject(COMMENT, new Dependency()));
		s.multiplyObject(COMMENT);
		COMMENT = COMMENT.makeSingleton().makeSummary();

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
