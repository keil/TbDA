package dk.brics.tajs.dependency.graph.visitor;

import java.util.Stack;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor;
import dk.brics.tajs.dependency.graph.nodes.DependencyExpressionNode;
import dk.brics.tajs.dependency.graph.nodes.DependencyObjectNode;
import dk.brics.tajs.dependency.graph.nodes.DependencyRootNode;

public class DependencyVisitor implements IDependencyGraphVisitor {

	/**
	 * calculated dependency
	 */
	private Dependency mDependency;

	/**
	 * list of visited nodes
	 */
	private Stack<DependencyNode> mVisited;

	/**
	 * 
	 */
	public DependencyVisitor() {
		mDependency = new Dependency();
		mVisited = new Stack<DependencyNode>();
	}

	/**
	 * @return
	 */
	public Dependency getDependency() {
		return mDependency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(mDependency);
		return buffer.toString();
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
		mVisited.add(node);
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
		mDependency.join(node.getDependencyObject());

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
		mVisited.push(node);
		for (DependencyNode n : node.getParentNodes()) {
			if (!visited(n)) {
				n.accept(this);
			}
		}
		mVisited.pop();
	}
}