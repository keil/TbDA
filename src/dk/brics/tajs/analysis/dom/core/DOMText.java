package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;


/**
 * The Text interface inherits from CharacterData and represents the textual
 * content (termed character data in XML) of an Element or Attr. If there is no
 * markup inside an element's content, the text is contained in a single object
 * implementing the Text interface that is the only child of the element. If
 * there is markup, it is parsed into the information items (elements, comments,
 * etc.) and Text nodes that form the list of children of the element.
 */
public class DOMText {

	public static ObjectLabel TEXT = new ObjectLabel(DOMObjects.TEXT, ObjectLabel.Kind.OBJECT);
	public static ObjectLabel TEXT_PROTOTYPE = new ObjectLabel(DOMObjects.TEXT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		// Prototype Object
		s.newObject(TEXT_PROTOTYPE);
		createDOMInternalPrototype(s, TEXT_PROTOTYPE, Value.makeObject(DOMCharacterData.CHARACTER_DATA_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Multiplied Object
		s.newObject(TEXT);
		createDOMInternalPrototype(s, TEXT, Value.makeObject(TEXT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "Text", Value.makeObject(TEXT, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		createDOMProperty(s, TEXT, "isElementContentWhitespace", Value.makeAnyBool(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);
		createDOMProperty(s, TEXT, "wholeText", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_3);

		s.multiplyObject(TEXT);
		TEXT = TEXT.makeSingleton().makeSummary();

		/*
		 * Functions.
		 */
		createDOMFunction(s, TEXT_PROTOTYPE, DOMObjects.TEXT_SPLIT_TEXT, "splitText", 1, DOMSpec.LEVEL_1);
		createDOMFunction(s, TEXT_PROTOTYPE, DOMObjects.TEXT_REPLACE_WHOLE_TEXT, "replaceWholeText", 1, DOMSpec.LEVEL_3);
	}

	/**
	 * Transfer Functions.
	 */
	public static Value evaluate(DOMObjects nativeObjects, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		switch (nativeObjects) {
		case TEXT_SPLIT_TEXT: {
			NativeFunctions.expectParameters(nativeObjects, call, c, 1, 1);
			Value offset = Conversion.toNumber(call.getArg(0), c);
			return Value.makeObject(TEXT_PROTOTYPE, new Dependency(), new DependencyGraphReference());
		}
		case TEXT_REPLACE_WHOLE_TEXT: {
			NativeFunctions.expectParameters(nativeObjects, call, c, 1, 1);
			Value content = Conversion.toString(call.getArg(0), c);
			return Value.makeObject(TEXT_PROTOTYPE, new Dependency(), new DependencyGraphReference()).joinNull();
		}
		default: {
			throw new RuntimeException("Unknown Native Object: " + nativeObjects);
		}
		}
	}

}
