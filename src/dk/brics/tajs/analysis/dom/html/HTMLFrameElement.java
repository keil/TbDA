package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

/**
 * Create a frame. See the FRAME element definition in HTML 4.01.
 */
public class HTMLFrameElement {

	public static ObjectLabel FRAME = new ObjectLabel(DOMObjects.HTMLFRAMEELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel FRAME_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLFRAMEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(FRAME_PROTOTYPE);
		createDOMInternalPrototype(s, FRAME_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(FRAME);
		createDOMInternalPrototype(s, FRAME, Value.makeObject(FRAME_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLFrameElement", Value.makeObject(FRAME, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, FRAME, "frameBorder", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FRAME, "longDesc", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FRAME, "marginHeight", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FRAME, "marginWidth", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FRAME, "name", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FRAME, "noResize", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FRAME, "scrolling", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, FRAME, "src", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, FRAME, "contentDocument", Value.makeObject(DOMDocument.DOCUMENT, new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);

		s.multiplyObject(FRAME);
		FRAME = FRAME.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
