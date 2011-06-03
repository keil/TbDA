package dk.brics.tajs.analysis.dom.view;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.core.DOMNode;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The DocumentView interface is implemented by Document objects in DOM implementations supporting DOM Views.
 * It provides an attribute to retrieve the default view of a document.
 */
public class DocumentView {

    public static void build(State s) {
        // DocumentView has no native object... see class comment.

        /*
         * Properties.
         */
        createDOMProperty(s, DOMNode.NODE, "defaultView", Value.makeObject(DOMWindow.WINDOW, new Dependency()).setReadOnly(), DOMSpec.LEVEL_2);
    }
}
