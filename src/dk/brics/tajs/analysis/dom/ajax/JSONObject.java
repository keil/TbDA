package dk.brics.tajs.analysis.dom.ajax;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.flowgraph.ObjectLabel;

public class JSONObject {

	public static ObjectLabel JSON_OBJECT;

	public static void build(State s) {
		JSON_OBJECT = new ObjectLabel(DOMObjects.JSON_OBJECT, ObjectLabel.Kind.OBJECT);
	}

}
