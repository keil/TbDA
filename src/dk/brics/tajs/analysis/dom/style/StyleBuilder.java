package dk.brics.tajs.analysis.dom.style;

import dk.brics.tajs.analysis.State;

public class StyleBuilder {

	public static void build(State s) {
		CSSStyleDeclaration.build(s);
		ClientBoundingRect.build(s);
	}

}
