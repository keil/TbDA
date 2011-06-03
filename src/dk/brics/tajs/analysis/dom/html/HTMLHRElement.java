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

public class HTMLHRElement {

	public static ObjectLabel HR = new ObjectLabel(DOMObjects.HTMLHRELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel HR_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLHRELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(HR_PROTOTYPE);
		createDOMInternalPrototype(s, HR_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(HR);
		createDOMInternalPrototype(s, HR, Value.makeObject(HR_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLHRElement", Value.makeObject(HR, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, HR, "align", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, HR, "noShade", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, HR, "size", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, HR, "width", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(HR);
		HR = HR.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
