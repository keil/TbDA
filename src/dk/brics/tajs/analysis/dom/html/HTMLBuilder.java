package dk.brics.tajs.analysis.dom.html;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

import java.util.Set;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Collections;

public class HTMLBuilder {

	public static final Set<ObjectLabel> HTML_OBJECT_LABELS = Collections.newSet();

	/**
	 * Build HTML objects
	 */
	public static void build(State s) {
		HTMLCollection.build(s);
		HTMLOptionsCollection.build(s);
		HTMLDocument.build(s);
		HTMLElement.build(s);
		HTMLFormElement.build(s);

		HTMLAnchorElement.build(s);
		HTMLAppletElement.build(s);
		HTMLAreaElement.build(s);
		HTMLBaseElement.build(s);
		HTMLBaseFontElement.build(s);
		HTMLBodyElement.build(s);
		HTMLBRElement.build(s);
		HTMLButtonElement.build(s);
		HTMLDirectoryElement.build(s);
		HTMLDivElement.build(s);
		HTMLDListElement.build(s);
		HTMLFieldSetElement.build(s);
		HTMLFontElement.build(s);
		HTMLFrameElement.build(s);
		HTMLFrameSetElement.build(s);
		HTMLHeadElement.build(s);
		HTMLHeadingElement.build(s);
		HTMLHRElement.build(s);
		HTMLHtmlElement.build(s);
		HTMLIFrameElement.build(s);
		HTMLImageElement.build(s);
		HTMLInputElement.build(s);
		HTMLIsIndexElement.build(s);
		HTMLLabelElement.build(s);
		HTMLLegendElement.build(s);
		HTMLLIElement.build(s);
		HTMLLinkElement.build(s);
		HTMLMapElement.build(s);
		HTMLMenuElement.build(s);
		HTMLMetaElement.build(s);
		HTMLModElement.build(s);
		HTMLObjectElement.build(s);
		HTMLOListElement.build(s);
		HTMLOptGroupElement.build(s);
		HTMLOptionElement.build(s);
		HTMLParagraphElement.build(s);
		HTMLParamElement.build(s);
		HTMLPreElement.build(s);
		HTMLQuoteElement.build(s);
		HTMLScriptElement.build(s);
		HTMLSelectElement.build(s);
		HTMLStyleElement.build(s);
		HTMLTableCaptionElement.build(s);
		HTMLTableCellElement.build(s);
		HTMLTableColElement.build(s);
		HTMLTableElement.build(s);
		HTMLTableRowElement.build(s);
		HTMLTableSectionElement.build(s);
		HTMLTextAreaElement.build(s);
		HTMLTitleElement.build(s);
		HTMLUListElement.build(s);

		HTML_OBJECT_LABELS.add(HTMLAnchorElement.ANCHOR);
		HTML_OBJECT_LABELS.add(HTMLAppletElement.APPLET);
		HTML_OBJECT_LABELS.add(HTMLAreaElement.AREA);
		HTML_OBJECT_LABELS.add(HTMLBaseElement.BASE);
		HTML_OBJECT_LABELS.add(HTMLBaseFontElement.BASEFONT);
		HTML_OBJECT_LABELS.add(HTMLBodyElement.BODY);
		HTML_OBJECT_LABELS.add(HTMLBRElement.BR);
		HTML_OBJECT_LABELS.add(HTMLButtonElement.BUTTON);
		HTML_OBJECT_LABELS.add(HTMLCollection.COLLECTION);
		HTML_OBJECT_LABELS.add(HTMLDirectoryElement.DIRECTORY);
		HTML_OBJECT_LABELS.add(HTMLDivElement.DIV);
		HTML_OBJECT_LABELS.add(HTMLDListElement.DLIST);
		HTML_OBJECT_LABELS.add(HTMLDocument.DOCUMENT);
		HTML_OBJECT_LABELS.add(HTMLElement.ELEMENT);
		HTML_OBJECT_LABELS.add(HTMLFieldSetElement.FIELDSET);
		HTML_OBJECT_LABELS.add(HTMLFontElement.FONT);
		HTML_OBJECT_LABELS.add(HTMLFormElement.FORM);
		HTML_OBJECT_LABELS.add(HTMLFrameElement.FRAME);
		HTML_OBJECT_LABELS.add(HTMLFrameSetElement.FRAMESET);
		HTML_OBJECT_LABELS.add(HTMLHeadElement.HEAD_PROTOTPE);
		HTML_OBJECT_LABELS.add(HTMLHeadingElement.HEADING);
		HTML_OBJECT_LABELS.add(HTMLHRElement.HR);
		HTML_OBJECT_LABELS.add(HTMLHtmlElement.HTML);
		HTML_OBJECT_LABELS.add(HTMLIFrameElement.IFRAME);
		HTML_OBJECT_LABELS.add(HTMLImageElement.IMAGE);
		HTML_OBJECT_LABELS.add(HTMLInputElement.INPUT);
		HTML_OBJECT_LABELS.add(HTMLIsIndexElement.ISINDEX);
		HTML_OBJECT_LABELS.add(HTMLLabelElement.LABEL);
		HTML_OBJECT_LABELS.add(HTMLLegendElement.LEGEND);
		HTML_OBJECT_LABELS.add(HTMLLIElement.LI);
		HTML_OBJECT_LABELS.add(HTMLLinkElement.LINK);
		HTML_OBJECT_LABELS.add(HTMLMapElement.MAP);
		HTML_OBJECT_LABELS.add(HTMLMenuElement.MENU);
		HTML_OBJECT_LABELS.add(HTMLMetaElement.META);
		HTML_OBJECT_LABELS.add(HTMLModElement.MOD);
		HTML_OBJECT_LABELS.add(HTMLObjectElement.OBJECT);
		HTML_OBJECT_LABELS.add(HTMLOListElement.OLIST);
		HTML_OBJECT_LABELS.add(HTMLOptGroupElement.OPTGROUP);
		HTML_OBJECT_LABELS.add(HTMLOptionElement.OPTION);
		HTML_OBJECT_LABELS.add(HTMLOptionsCollection.OPTIONSCOLLECTION);
		HTML_OBJECT_LABELS.add(HTMLParagraphElement.PARAGRAPH);
		HTML_OBJECT_LABELS.add(HTMLParamElement.PARAM);
		HTML_OBJECT_LABELS.add(HTMLPreElement.PRE);
		HTML_OBJECT_LABELS.add(HTMLQuoteElement.QUOTE);
		HTML_OBJECT_LABELS.add(HTMLScriptElement.SCRIPT);
		HTML_OBJECT_LABELS.add(HTMLSelectElement.SELECT);
		HTML_OBJECT_LABELS.add(HTMLStyleElement.STYLE);
		HTML_OBJECT_LABELS.add(HTMLTableCaptionElement.TABLECAPTION);
		HTML_OBJECT_LABELS.add(HTMLTableCellElement.TABLECELL);
		HTML_OBJECT_LABELS.add(HTMLTableColElement.TABLECOL);
		HTML_OBJECT_LABELS.add(HTMLTableElement.TABLE);
		HTML_OBJECT_LABELS.add(HTMLTableRowElement.TABLEROW);
		HTML_OBJECT_LABELS.add(HTMLTableSectionElement.TABLESECTION);
		HTML_OBJECT_LABELS.add(HTMLTextAreaElement.TEXTAREA);
		HTML_OBJECT_LABELS.add(HTMLTitleElement.TITLE);
		HTML_OBJECT_LABELS.add(HTMLUListElement.ULIST);

		// Write documentElement (due to cyclic dependency)
		createDOMProperty(s, DOMDocument.DOCUMENT, "documentElement", Value.makeObject(HTMLHtmlElement.HTML, new Dependency()), DOMSpec.LEVEL_1);
	}

}
