package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestDOM {

    // TODO: Figure out a way to autogenerate file.

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestDOM");
    }

    @Before
    public void init() {
        Options.reset();
        Options.setTest(true);
        Options.setDOM(true);
    }

    @Test
    public void testDOMGetElementById01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/getElementById01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMGetElementsByName01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/getElementsByName01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMGetElementsByTagName01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/getElementsByTagName01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMGetElementsByClassName01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/getElementsByClassName01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMElement02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMElement02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMElement03() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMElement03.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMWindow01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/DOMWindow01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Ignore
    // TODO
    @Test
    public void testDOMWindow02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/DOMWindow02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMWindow03() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/DOMWindow03.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMWindow04() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/DOMWindow04.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMDocument01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMDocument01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMDocument02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMDocument02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMText01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMText01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMText02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMText02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMText03() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMText03.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMText04() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMText04.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMNotation01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMNotation01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMNode01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMNode01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMNode02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMNode02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMAttr01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMAttr01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMException01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMException01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMImplementation01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMImplementation01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMImplementation02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMImplementation02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMProcessingInstruction01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMProcessingInstruction01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMDocumentFragment01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMDocumentFragment01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMCharacterData01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMCharacterData01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMNamedNodeMap01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMNamedNodeMap01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMNamedNodeMap02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMNamedNodeMap02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMNodeList01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMNodeList01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMCDataSection01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMCDataSection01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMComment01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMComment01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMEntity01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMEntity01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMEntityReference01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMEntityReference01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMDocumentType01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMDocumentType01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMStringList01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMStringList01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMStringList02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMStringList02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMConfiguration01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMConfiguration01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMConfiguration02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/core/DOMConfiguration02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    /*
     * ********** HTML *************
     */
    @Test
    public void testHTMLElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLInputElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLInputElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLInputElement02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLInputElement02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLOptGroupElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLOptGroupElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLLegendElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLLegendElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLBodyElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLBodyElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLFormElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLFormElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLFormElement02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLFormElement02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLTitleElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLTitleElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLCollection01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLCollection01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLOptionsCollection01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLOptionsCollection01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLDocument01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLDocument01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLDocument02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLDocument02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLAnchorElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLAnchorElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLAnchorElement02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLAnchorElement02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLLinkElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLLinkElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLAppletElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLAppletElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLAreaElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLAreaElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLBaseElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLBaseElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLBaseFontElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLBaseFontElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLBRElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLBRElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLButtonElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLButtonElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLTableColElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLTableColElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLImageElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLImageElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLHRElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLHRElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLParamElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLParamElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLMetaElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLMetaElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLSelectElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLSelectElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLSelectElement02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLSelectElement02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLOptionElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLOptionElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLPreElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLPreElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLUListElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLUListElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLTableCaptionElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLTableCaptionElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLParagraphElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLParagraphElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLOListElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLOListElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLTextAreaElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLTextAreaElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLTextAreaElement02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLTextAreaElement02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLDirectoryElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLDirectoryElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLDivElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLDivElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLDListElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLDListElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLFieldSetElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLFieldSetElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLFontElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLFontElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLFrameElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLFrameElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLFrameSetElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLFrameSetElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLHeadElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLHeadElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLHeadingElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLHeadingElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLHtmlElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLHtmlElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLIFrameElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLIFrameElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLIsIndexElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLIsIndexElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLLIElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLLIElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLLabelElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLLabelElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLMapElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLMapElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLMenuElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLMenuElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLModElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLModElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLObjectElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLObjectElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLQuoteElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLQuoteElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLScriptElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLScriptElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLStyleElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLStyleElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLTableCellElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLTableCellElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLTableSectionElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLTableSectionElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLTableSectionElement02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLTableSectionElement02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLTableElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLTableElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLTableElement02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLTableElement02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLTableRowElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLTableRowElement01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLTableRowElement02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/html/HTMLTableRowElement02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    // /////////////////////////////////////////////////////////////////////////
    // / HTML 5 CANVAS ///
    // /////////////////////////////////////////////////////////////////////////
    @Test
    public void testHTMLCanvasElement01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/canvas/Canvas01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLCanvasElement02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/canvas/Canvas02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLCanvasElement03() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/canvas/Canvas03.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLCanvasElement04() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/canvas/Canvas04.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLCanvasElement05() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/canvas/Canvas05.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLCanvasElement06() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/canvas/Canvas06.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLCanvasElement07() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/canvas/Canvas07.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLCanvasElement08() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/canvas/Canvas08.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testHTMLCanvasElement09() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/canvas/Canvas09.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    // DOM Event:
    @Test
    @Ignore
    public void testDOMWrapEvents01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMWrapEvents01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMEvent01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMEvent01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMEvent02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMEvent02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMEventTarget01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMEventTarget01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMEventTarget02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMEventTarget02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMEventTarget03() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMEventTarget03.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMEventTarget04() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMEventTarget04.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMEventListener01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMEventListener01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMEventListener02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMEventListener02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMEventException01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMEventException01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMDocumentEvent01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMDocumentEvent01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMDocumentEvent02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMDocumentEvent02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMMutationEvent01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMMutationEvent01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMMutationEvent02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMMutationEvent02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMUIEvent01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMUIEvent01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMUIEvent02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMUIEvent02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMMouseEvent01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMMouseEvent01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMMouseEvent02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMMouseEvent02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMKeyboardEvent01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMKeyboardEvent01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMKeyboardEvent02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMKeyboardEvent02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMWheelEvent01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMWheelEvent01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMWheelEvent02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMWheelEvent02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testOnLoad01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMOnLoad01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testOnLoad02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMOnLoad02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testOnLoad03() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMOnLoad03.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testOnLoad04() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMOnLoad04.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testOnLoad05() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMOnLoad05.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testOnLoad06() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMOnLoad06.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testOnUnload01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMOnUnload01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testOnUnload02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMOnUnload02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testOnClick01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/DOMOnClick01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Ignore
    // TODO
    @Test
    public void testExceptionalFlow01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/events/ExceptionalFlow01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMMicro01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/DOMmicro01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testDOMMicro02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/DOMmicro02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    ///////////////////////////////////////////////////////////////
    // Random tests...
    ///////////////////////////////////////////////////////////////

    @Test
    public void testToNativeObject01() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/toNativeObject01.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testToNativeObject02() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/toNativeObject02.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testToNativeObject03() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/toNativeObject03.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testToNativeObject04() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/toNativeObject04.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testToNativeObject06() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/toNativeObject06.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testToNativeObject07() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/toNativeObject07.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void testToNativeObject08() throws Exception {
        Misc.start();
        Misc.captureSystemOutput();
        String[] args = {"test/dom/toNativeObject08.html"};
        Main.main(args);
        Misc.checkSystemOutput();
    }

}
