package dk.brics.tajs.analysis.dom.html5;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

import java.util.Set;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Collections;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

public class HTML5Builder {

	public static Set<ObjectLabel> HTML5_OBJECT_LABELS = Collections.newSet();

	public static void build(State s) {
		CanvasRenderingContext2D.build(s);
		HTMLCanvasElement.build(s);
		StorageElement.build(s);

		// HTML5 properties on Window
		createDOMProperty(s, DOMWindow.WINDOW, "localStorage", Value.makeObject(StorageElement.INSTANCES, new Dependency(), new DependencyGraphReference()),
				DOMSpec.HTML5);
		createDOMProperty(s, DOMWindow.WINDOW, "sessionStorage", Value.makeObject(StorageElement.INSTANCES, new Dependency(), new DependencyGraphReference()),
				DOMSpec.HTML5);

		HTML5_OBJECT_LABELS.add(HTMLCanvasElement.INSTANCES);
	}

}
