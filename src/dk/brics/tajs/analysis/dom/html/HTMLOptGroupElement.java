package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

public class HTMLOptGroupElement {

	public static ObjectLabel OPTGROUP = new ObjectLabel(DOMObjects.HTMLOPTGROUPELEMENT, Kind.OBJECT);
	public static ObjectLabel OPTGROUP_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLOPTGROUPELEMENT_PROTOTYPE, Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(OPTGROUP_PROTOTYPE);
		createDOMInternalPrototype(s, OPTGROUP_PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE,new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(OPTGROUP);
		createDOMInternalPrototype(s, OPTGROUP, Value.makeObject(OPTGROUP_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "HTMLOptGroupElement", Value.makeObject(OPTGROUP, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties
		 */
		// DOM Level 1
		createDOMProperty(s, OPTGROUP, "disabled", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, OPTGROUP, "label", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(OPTGROUP);
		OPTGROUP = OPTGROUP.makeSingleton().makeSummary();

		/*
		 * Functions
		 */
		// No functions.
	}

}
