package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.InitialStateBuilder;
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


/**
 * DOM operations only raise exceptions in "exceptional" circumstances, i.e.,
 * when an operation is impossible to perform (either for logical reasons,
 * because data is lost, or because the implementation has become unstable). In
 * general, DOM methods return specific error values in ordinary processing
 * situations, such as out-of-bound errors when using NodeList.
 * <p/>
 * http://www.w3.org/TR/DOM-Level-2-Core/core.html#ID-17189187
 */
public class DOMException {

	public static final ObjectLabel DOMEXCEPTION = new ObjectLabel(DOMObjects.DOMEXCEPTION, Kind.OBJECT);
	public static final ObjectLabel DOMEXCEPTION_PROTOTYPE = new ObjectLabel(DOMObjects.DOMEXCEPTION_PROTOTYPE, Kind.OBJECT);

	/**
	 * Creates a DOMException.
	 */
	public static Value newDOMException(State s, int code) {
		s.newObject(DOMEXCEPTION);
		createDOMInternalPrototype(s, DOMEXCEPTION, Value.makeObject(DOMEXCEPTION_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMEXCEPTION, "code", Value.makeNum(code, new Dependency(), new DependencyGraphReference()));
		return Value.makeObject(DOMEXCEPTION, new Dependency(), new DependencyGraphReference());
	}

	public static void build(State s) {
		s.newObject(DOMEXCEPTION_PROTOTYPE);
		createDOMInternalPrototype(s, DOMEXCEPTION_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMProperty(s, DOMWindow.WINDOW, "DOMException", Value.makeObject(DOMEXCEPTION_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		/*
		 * Properties.
		 */
		// DOM Level 1
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "INDEX_SIZE_ERR", Value.makeNum(1, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "DOMSTRING_SIZE_ERR", Value.makeNum(2, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "HIERARCHY_REQUEST_ERR", Value.makeNum(3, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "WRONG_DOCUMENT_ERR", Value.makeNum(4, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "INVALID_CHARACTER_ERR", Value.makeNum(5, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "NO_DATA_ALLOWED_ERR", Value.makeNum(6, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "NO_MODIFICATION_ALLOWED_ERR", Value.makeNum(7, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "NOT_FOUND_ERR", Value.makeNum(8, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "NOT_SUPPORTED_ERR", Value.makeNum(9, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "INUSE_ATTRIBUTE_ERR", Value.makeNum(10, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_1);

		// DOM Level 2
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "INVALID_STATE_ERR", Value.makeNum(11, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "SYNTAX_ERR", Value.makeNum(12, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "INVALID_MODIFICATION_ERR", Value.makeNum(13, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "NAMESPACE_ERR", Value.makeNum(14, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);
		createDOMProperty(s, DOMEXCEPTION_PROTOTYPE, "INVALID_ACCESS_ERR", Value.makeNum(15, new Dependency(), new DependencyGraphReference()), DOMSpec.LEVEL_2);

	}

}
