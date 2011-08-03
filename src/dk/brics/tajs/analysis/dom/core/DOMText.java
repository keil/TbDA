package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

/**
 * The Text interface inherits from CharacterData and represents the textual
 * content (termed character data in XML) of an Element or Attr. If there is no
 * markup inside an element's content, the text is contained in a single object
 * implementing the Text interface that is the only child of the element. If
 * there is markup, it is parsed into the information items (elements, comments,
 * etc.) and Text nodes that form the list of children of the element.
 */
public class DOMText {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
	public static ObjectLabel INSTANCES;

	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.TEXT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.TEXT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
		INSTANCES = new ObjectLabel(DOMObjects.TEXT_INSTANCES, ObjectLabel.Kind.OBJECT);

		// Constructor Object
		s.newObject(CONSTRUCTOR);
		createDOMSpecialProperty(s, CONSTRUCTOR, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, CONSTRUCTOR, "prototype",
				Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "Text", Value.makeObject(CONSTRUCTOR, new Dependency(), new DependencyGraphReference()));

		// Prototype Object
		s.newObject(PROTOTYPE);
		createDOMInternalPrototype(s, PROTOTYPE, Value.makeObject(DOMCharacterData.PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(INSTANCES);
		createDOMInternalPrototype(s, INSTANCES, Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, INSTANCES, "isElementContentWhitespace", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(),
				DOMSpec.LEVEL_3);
		createDOMProperty(s, INSTANCES, "wholeText", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);

		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		createDOMFunction(s, PROTOTYPE, DOMObjects.TEXT_SPLIT_TEXT, "splitText", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.TEXT_REPLACE_WHOLE_TEXT, "replaceWholeText", 1, DOMSpec.LEVEL_3);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObjects, FunctionCalls.CallInfo<? extends Node> call, State s, Solver.SolverInterface c) {
		switch (nativeObjects) {
		case TEXT_SPLIT_TEXT: {
			NativeFunctions.expectParameters(nativeObjects, call, c, 1, 1);
			/* Value offset = */Conversion.toNumber(call.getArg(0), c);
			return Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference());
		}
		case TEXT_REPLACE_WHOLE_TEXT: {
			NativeFunctions.expectParameters(nativeObjects, call, c, 1, 1);
			/* Value content = */Conversion.toString(call.getArg(0), c);
			return Value.makeObject(PROTOTYPE, new Dependency(), new DependencyGraphReference()).joinNull();
		}
		default: {
			throw new RuntimeException("Unknown Native Object: " + nativeObjects);
		}
		}
	}

}
