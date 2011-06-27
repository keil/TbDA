package dk.brics.tajs.analysis.dom.style;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMUnknownArrayProperty;

public class CSSStyleDeclaration {

	public static ObjectLabel STYLEDECLARATION = new ObjectLabel(DOMObjects.CSSSTYLEDECLARATION, ObjectLabel.Kind.OBJECT);

	public static void build(State s) {
		s.newObject(STYLEDECLARATION);
		createDOMInternalPrototype(s, STYLEDECLARATION, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));
		createDOMUnknownArrayProperty(s, STYLEDECLARATION, Value.makeAnyStr(new Dependency(), new DependencyGraphReference()));
		s.multiplyObject(STYLEDECLARATION);
		STYLEDECLARATION = STYLEDECLARATION.makeSingleton().makeSummary();
	}

}
