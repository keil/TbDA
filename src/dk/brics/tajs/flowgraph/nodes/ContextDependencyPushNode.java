package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Leave-context node.
 */
public class ContextDependencyPushNode extends Node {

	/**
	 * static counter
	 */
	private static int index = 0;

	/**
	 * @return next index
	 */
	public static int nextIndex() {
		return index++;
	}

	/**
	 * context index
	 */
	private int mIndex = -1;

	/**
	 * reset node
	 */
	public ContextDependencyPopNode mContextDependencyPopNode;

	/**
	 * Constructs a new context dependency node.
	 */
	public ContextDependencyPushNode(
			ContextDependencyPopNode contextDependecyPopNode,
			SourceLocation location) {
		super(location);

		mIndex = ContextDependencyPushNode.nextIndex();
		contextDependecyPopNode.setContectIndex(mIndex);

		// ##################################################
		mContextDependencyPopNode = contextDependecyPopNode;
		// ##################################################
	}

	/**
	 * Set an dependency to referenced ContextDependencyPopNode
	 * 
	 * @param dependency
	 */
	public void setContextDependency(Dependency dependency) {
		mContextDependencyPopNode.setContextDependency(dependency);
	}

	/**
	 * Set an dependency graph reference to referenced ContextDependencyPopNode
	 * 
	 * @param dependency
	 */
	public void setContextDependencyGraphReference(DependencyGraphReference reference) {
		mContextDependencyPopNode.setContextDependencyGraphReference(reference);
	}

	/**
	 * @return index
	 */
	public int getID() {
		return mIndex;
	}
	
	@Override
	public String toString() {
		if (mContextDependencyPopNode.getContextDependency().isEmpty())
			return "context depenedency [push #" + mIndex + "]";
		else
			return "context depenedency [push" + mIndex + "]:"
					+ mContextDependencyPopNode.getContextDependency();
	}

	@Override
	public <Arg> void visitBy(NodeVisitor<Arg> v, Arg a) {
		v.visit(this, a);
	}

	@Override
	public boolean canThrowExceptions() {
		return false;
	}
}