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

public class HTMLPreElement {

	public static ObjectLabel PRE = new ObjectLabel(DOMObjects.HTMLPREELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel PRE_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLPREELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(PRE_PROTOTYPE);
		createDOMInternalPrototype(s, PRE_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(PRE);
		createDOMInternalPrototype(s, PRE, Value.makeObject(PRE_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLPreElement", Value.makeObject(PRE, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, PRE, "width", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(PRE);
		PRE = PRE.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
