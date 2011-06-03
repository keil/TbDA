package dk.brics.tajs.dependency.graph.visitor;

import java.util.Stack;

import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor;
import dk.brics.tajs.dependency.graph.nodes.DependencyExpressionNode;
import dk.brics.tajs.dependency.graph.nodes.DependencyObjectNode;
import dk.brics.tajs.dependency.graph.nodes.DependencyRootNode;

public class ToStringVisitor implements IDependencyGraphVisitor {

	/**
	 * string buffer
	 */
	private StringBuffer mBuffer;

	/**
	 * visited nodes
	 */
	private Stack<DependencyNode> mVisited;

	/**
	 * 
	 */
	public ToStringVisitor() {
		mBuffer = new StringBuffer();
		mVisited = new Stack<DependencyNode>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return mBuffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor#visited
	 * (dk.brics.tajs.dependency.graph.DependencyNode)
	 */
	public boolean visited(DependencyNode node) {
		return mVisited.contains(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor#visit
	 * (dk.brics.tajs.dependency.graph.nodes.DependencyRootNode)
	 */
	@Override
	public void visit(DependencyRootNode node) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor#visit
	 * (dk.brics.tajs.dependency.graph.nodes.DependencyObjectNode)
	 */
	@Override
	public void visit(DependencyObjectNode node) {
		mBuffer.append("{");
		mBuffer.append(node.getDependencyObject());
		mBuffer.append("}");

		mVisited.push(node);
		for (DependencyNode n : node.getParentNodes()) {
			if (!visited(n)) {
				n.accept(this);
			}
		}
		mVisited.pop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor#visit
	 * (dk.brics.tajs.dependency.graph.nodes.DependencyExpressionNode)
	 */
	@Override
	public void visit(DependencyExpressionNode node) {
		mBuffer.append("{");
		mBuffer.append(node.getLabel());
		mBuffer.append(" ");

		mVisited.push(node);
		for (DependencyNode n : node.getParentNodes()) {
			if (!visited(n)) {
				n.accept(this);
			}
		}
		mVisited.pop();

		mBuffer.append("}");
	}
}