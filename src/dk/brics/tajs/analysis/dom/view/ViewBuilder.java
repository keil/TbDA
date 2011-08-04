package dk.brics.tajs.analysis.dom.view;

import dk.brics.tajs.analysis.State;

public class ViewBuilder {

	public static void build(State s) {
		AbstractView.build(s);
		DocumentView.build(s);
	}
}
