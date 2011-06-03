package dk.brics.tajs.dependency.graph.interfaces;

import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.nodes.DependencyExpressionNode;
import dk.brics.tajs.dependency.graph.nodes.DependencyObjectNode;
import dk.brics.tajs.dependency.graph.nodes.DependencyRootNode;

public interface IDependencyGraphVisitor {

	/**
	 * @param node
	 * @return
	 */
	boolean visited(DependencyNode node);
	
	/**
	 * @param node
	 */
	void visit(DependencyRootNode node);

	/**
	 * @param node
	 */
	void visit(DependencyObjectNode node);

	/**
	 * @param node
	 */
	void visit(DependencyExpressionNode node);
}