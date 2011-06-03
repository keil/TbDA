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
 * Generic embedded object.
 * <p/>
 * Note: In principle, all properties on the object element are read-write but
 * in some environments some properties may be read-only once the underlying
 * object is instantiated. See the OBJECT element definition in [HTML 4.01].
 */
public class HTMLObjectElement {

	public static ObjectLabel OBJECT = new ObjectLabel(DOMObjects.HTMLOBJECTELEMENT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel OBJECT_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLOBJECTELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(OBJECT_PROTOTYPE);
		createDOMInternalPrototype(s, OBJECT_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE, new Dependency()));

		// Multiplied Object
		s.newObject(OBJECT);
		createDOMInternalPrototype(s, OBJECT, Value.makeObject(OBJECT_PROTOTYPE, new Dependency()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLObjectElement", Value.makeObject(OBJECT, new Dependency()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, OBJECT, "form", Value.makeObject(HTMLFormElement.FORM, new Dependency()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "code", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "align", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "archive", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "border", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "codeBase", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "codeType", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "data", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "declare", Value.makeAnyBool(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "height", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "hspace", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "name", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "standby", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "tabIndex", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "type", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "useMap", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "vspace", Value.makeAnyNum(new Dependency()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OBJECT, "width", Value.makeAnyStr(new Dependency()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, OBJECT, "contentDocument", Value.makeObject(DOMDocument.DOCUMENT, new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);

		s.multiplyObject(OBJECT);
		OBJECT = OBJECT.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		// No functions.
	}

}
