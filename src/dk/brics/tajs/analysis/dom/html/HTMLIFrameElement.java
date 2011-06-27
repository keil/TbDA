package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

/**
 * Inline subwindows. See the IFRAME element definition in HTML 4.01.
 */
public class HTMLIFrameElement {

	public static ObjectLabel IFRAME = new ObjectLabel(DOMObjects.HTMLIFRAMEELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel IFRAME_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLIFRAMEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(IFRAME_PROTOTYPE);
		createDOMInternalPrototype(s, IFRAME_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(IFRAME);
		createDOMInternalPrototype(s, IFRAME, Value.makeObject(IFRAME_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLIFrameElement", Value.makeObject(IFRAME, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, IFRAME, "align", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IFRAME, "frameBorder", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IFRAME, "height", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IFRAME, "longDesc", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IFRAME, "marginHeight", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IFRAME, "marginWidth", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IFRAME, "name", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IFRAME, "scrolling", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IFRAME, "src", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, IFRAME, "width", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, IFRAME, "contentDocument", Value.makeObject(DOMDocument.DOCUMENT, new Dependency(), new DependencyGraphReference()).setReadOnly().setReadOnly(), DOMSpec.LEVEL_2);

		s.multiplyObject(IFRAME);
		IFRAME = IFRAME.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
