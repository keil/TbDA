package dk.brics.tajs.analysis.dom.html5;

import dk.brics.tajs.analysis.State;

public class HTML5Builder {

    public static void build(State s) {
        CanvasRenderingContext2D.build(s);
        HTMLCanvasElement.build(s);
    }

}
