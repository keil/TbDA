package dk.brics.tajs.analysis.dom.ajax;

import dk.brics.tajs.analysis.State;

public class AjaxBuilder {
	public static void build(State s) {
		XmlHttpRequest.build(s);
		ActiveXObject.build(s);
		ReadystateEvent.build(s);
		JSONObject.build(s);
	}
}
