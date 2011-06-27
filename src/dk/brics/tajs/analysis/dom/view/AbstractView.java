package dk.brics.tajs.analysis.dom.view;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.analysis.dom.html.HTMLDocument;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * A base interface that all views shall derive from.
 */
public class AbstractView {

    public static void build(State s) {
        // AbstractView has no native object.

        /*
         * Properties.
         */
        createDOMProperty(s, DOMWindow.WINDOW, "document", Value.makeObject(HTMLDocument.DOCUMENT, new Dependency(), new DependencyGraphReference()).setReadOnly(), DOMSpec.LEVEL_0);
    }
}
