package dk.brics.tajs.analysis.dom.core;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.lattice.Value;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;

public class CoreBuilder {

	public static void build(State s) {
		DOMNodeList.build(s);
		DOMNode.build(s);
		DOMAttr.build(s);
		DOMNamedNodeMap.build(s);
		DOMDocumentType.build(s);
		DOMException.build(s);
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
		DOMDocumentFragment.build(s);

		// Document
		DOMDocument.build(s);
		DOMImplementation.build(s);

		// Set the remaining properties on DOMNode, due to circularity, and
		// summarize.
		createDOMProperty(s, DOMNode.PROTOTYPE, "attributes", Value.makeObject(DOMNamedNodeMap.INSTANCES, new Dependency(), new DependencyGraphReference()),
				DOMSpec.LEVEL_1);
		createDOMProperty(s, DOMNode.PROTOTYPE, "ownerDocument", Value.makeObject(DOMDocument.INSTANCES, new Dependency(), new DependencyGraphReference()),
				DOMSpec.LEVEL_1);
		s.multiplyObject(DOMNode.INSTANCES);
		DOMNode.INSTANCES = DOMNode.INSTANCES.makeSingleton().makeSummary();
	}

}
