package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMWindow;

public class CoreBuilder {

    public static void build(State s) {
        DOMNode.build(s);
        DOMNodeList.build(s);
        DOMAttr.build(s);
        DOMNamedNodeMap.build(s);
        DOMDocument.build(s);
        DOMDocumentType.build(s);
        DOMException.build(s);
        DOMImplementation.build(s);
        DOMWindow.build(s);
        DOMElement.build(s);
        DOMCharacterData.build(s);
        DOMText.build(s);
        DOMConfiguration.build(s);
        DOMNotation.build(s);
        DOMCDataSection.build(s);
        DOMComment.build(s);
        DOMEntity.build(s);
        DOMEntityReference.build(s);
        DOMProcessingInstruction.build(s);
        DOMStringList.build(s);
    }

}
