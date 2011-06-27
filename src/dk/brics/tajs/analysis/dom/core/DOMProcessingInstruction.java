package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * The ProcessingInstruction interface represents a "processing instruction",
 * used in XML as a way to keep processor-specific information in the text of
 * the document.
 */
public class DOMProcessingInstruction {

	public static ObjectLabel PROCESSINGINSTRUCTION = new ObjectLabel(DOMObjects.PROCESSINGINSTRUCTION, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel PROCESSINGINSTRUCTION_PROTOTYPE = new ObjectLabel(DOMObjects.PROCESSINGINSTRUCTION_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype object.
		s.newObject(PROCESSINGINSTRUCTION_PROTOTYPE);
		createDOMInternalPrototype(s, PROCESSINGINSTRUCTION_PROTOTYPE, Value.makeObject(DOMNode.NODE_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied object.
		s.newObject(PROCESSINGINSTRUCTION);
		createDOMInternalPrototype(s, PROCESSINGINSTRUCTION, Value.makeObject(PROCESSINGINSTRUCTION_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "ProcessingInstruction", Value.makeObject(PROCESSINGINSTRUCTION, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, PROCESSINGINSTRUCTION, "target", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_1);
		createDOMProperty(s, PROCESSINGINSTRUCTION, "data", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		s.multiplyObject(PROCESSINGINSTRUCTION);
		PROCESSINGINSTRUCTION = PROCESSINGINSTRUCTION.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
	}

}
