package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

public class HTMLDirectoryElement {

	public static ObjectLabel DIRECTORY = new ObjectLabel(DOMObjects.HTMLDIRECTORYELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel DIRECTORY_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLDIRECTORYELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(DIRECTORY_PROTOTYPE);
		createDOMInternalPrototype(s, DIRECTORY_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(DIRECTORY);
		createDOMInternalPrototype(s, DIRECTORY, Value.makeObject(DIRECTORY_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLDirectoryElement", Value.makeObject(DIRECTORY, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, DIRECTORY, "compact", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(DIRECTORY);
		DIRECTORY = DIRECTORY.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
