package dk.brics.tajs.dependency.graph;

import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor;
import dk.brics.tajs.dependency.graph.nodes.DependencyRootNode;

public class DependencyGraph {

	/**
	 * graph root node
	 */
	private DependencyRootNode mRoot;

	/**
	 * initialize an new graph
	 */
	public DependencyGraph() {
		mRoot = new DependencyRootNode();
	}

	/**
	 * @return returns current root node
	 */
	public DependencyRootNode getRoot() {
		return mRoot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(mRoot);
		return buffer.toString();
	}

	/**
	 * @param visitor
	 */
	public void accept(IDependencyGraphVisitor visitor) {
		mRoot.accept(visitor);
	}
}