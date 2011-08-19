package dk.brics.tajs.analysis.dom.ajax;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMRegistry;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.event.Event;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

/**
 * To dispatch a readystatechange event means that an event with the name
 * readystatechange, which does not bubble and is not cancelable, and which uses
 * the Event interface, is to be dispatched at the XMLHttpRequest object.
 */
public class ReadystateEvent {

	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		PROTOTYPE = new ObjectLabel(DOMObjects.READY_STATE_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.READY_STATE_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Prototype object
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(Event.PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, INSTANCES, "cancelable", Value.makeBool(false, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_0);
		createDOMProperty(s, INSTANCES, "bubbles", Value.makeBool(false, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_0);
		createDOMProperty(s, INSTANCES, "target", Value.makeObject(XmlHttpRequest.INSTANCES, new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_0);

		/*
		 * ResponseText + ResponseXML (TODO)
		 */
		createDOMProperty(s, INSTANCES, "responseText", Value.makeJSONStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_0);

		// DOM Registry
		DOMRegistry.registerAjaxEventLabel(INSTANCES);
	}
}