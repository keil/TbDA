package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.ajax.AjaxBuilder;
import dk.brics.tajs.analysis.dom.core.CoreBuilder;
import dk.brics.tajs.analysis.dom.event.EventBuilder;
import dk.brics.tajs.analysis.dom.html.HTMLBuilder;
import dk.brics.tajs.analysis.dom.html5.HTML5Builder;
import dk.brics.tajs.analysis.dom.style.StyleBuilder;
import dk.brics.tajs.analysis.dom.view.ViewBuilder;
import dk.brics.tajs.options.Options;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Setup the DOM browser model.
 * <p/>
 * An overview is available at:
 * <p/>
 * http://dsmith77.files.wordpress.com/2008/07/the-document-object-model-dom.gif
 * <p/>
 * DOM Spec: http://www.w3.org/DOM/DOMTR
 * <p/>
 * DOM LEVEL 1:
 * http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/ecma-script-language
 * -binding.html
 * <p/>
 * DOM Level 2 Core: http://www.w3.org/TR/DOM-Level-2-Core/core.html
 * http://www.w3.org/TR/DOM-Level-2-Core/ecma-script-binding.html
 * <p/>
 * DOM LEVEL 2 HTML:
 * http://www.w3.org/TR/DOM-Level-2-HTML/ecma-script-binding.html
 * <p/>
 * DOM LEVEL 2: Traversal
 * http://www.w3.org/TR/DOM-Level-2-Traversal-Range/Overview.html
 */
public class DOMBuilder {
	/**
	 * Construct the initial DOM objects. Its assumed that WINDOW is added to
	 * the state somewhere else before this function is invoked since its the
	 * global objects when running in DOM mode.
	 */
	public static void addInitialDOM(State s) {
		// Reset DOM Registry
		DOMRegistry.reset();

		// Build window object
		DOMWindow.WINDOW = InitialStateBuilder.GLOBAL;
		DOMWindow.build(s);

		// TODO: TEST TEST TEST TEST
		// ObjectDSLAdapter objectDSLAdapter = new ObjectDSLAdapter();
		// objectDSLAdapter.build(s);

		// Build initial core dom state
		CoreBuilder.build(s);

		// Build initial style state
		StyleBuilder.build(s);

		// Build initial html state
		HTMLBuilder.build(s);

		// Build initial html5 state
		HTML5Builder.build(s);

		// Build initial event state
		EventBuilder.build(s);

		// Build initial views state
		ViewBuilder.build(s);

		// Build initial AJAX state
		AjaxBuilder.build(s);

		// Debugging
		if (Options.isDebugEnabled()) {
			try {
				File outdir = new File("out");
				if (!outdir.exists()) {
					outdir.mkdir();
				}
				FileWriter fw = new FileWriter("out" + File.separator + "state.dot");
				fw.write(s.toDot(true));
				fw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
