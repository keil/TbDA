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

/**
 * An embedded Java applet. See the APPLET element definition in HTML 4.0. This
 * element is deprecated in HTML 4.0.
 */
public class HTMLAppletElement {

	public static ObjectLabel APPLET = new ObjectLabel(DOMObjects.HTMLAPPLETELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel APPLET_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLAPPLETELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(APPLET_PROTOTYPE);
		createDOMInternalPrototype(s, APPLET_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(APPLET);
		createDOMInternalPrototype(s, APPLET, Value.makeObject(APPLET_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLAppletElement", Value.makeObject(APPLET, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM LEVEL 1
		createDOMProperty(s, APPLET, "align", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, APPLET, "alt", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, APPLET, "archive", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, APPLET, "code", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, APPLET, "codeBase", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, APPLET, "height", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, APPLET, "hspace", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, APPLET, "name", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, APPLET, "object", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, APPLET, "vspace", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, APPLET, "width", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		s.multiplyObject(APPLET);
		APPLET = APPLET.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
