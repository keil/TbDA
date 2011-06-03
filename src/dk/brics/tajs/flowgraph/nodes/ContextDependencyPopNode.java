package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Leave-context node.
 */
public class ContextDependencyPopNode extends Node {

	/**
	 * context index
	 */
	private int mIndex = -1;

	/**
	 * dependency
	 */
	private Dependency mContextDependency = new Dependency();

	/**
	 * dependency graph reference
	 */
	private DependencyGraphReference mDependencyGraphReference = new DependencyGraphReference();

	/**
	 * Constructs a new context dependency node.
	 */
	public ContextDependencyPopNode(SourceLocation location) {
		super(location);
	}

	/**
	 * set context dependency
	 * 
	 * @param dependency
	 */
	public void setContextDependency(Dependency dependency) {
		mContextDependency = dependency;
	}

	/**
	 * set context dependency graph reference
	 * 
	 * @param dependency
	 */
	public void setContextDependencyGraphReference(
			DependencyGraphReference reference) {
		mDependencyGraphReference = reference;
	}

	/**
	 * return dependency of referenced state
	 * 
	 * @return dependency
	 */
	public Dependency getContextDependency() {
		return mContextDependency;
	}

	/**
	 * return graph reference of referenced state
	 * 
	 * @return dependency
	 */
	public DependencyGraphReference getContextDependencyGraphReference() {
		return mDependencyGraphReference;
	}

	/**
	 * set an index
	 * 
	 * @param index
	 */
	public void setContectIndex(int index) {
		mIndex = index;
	}

	/**
	 * @return index
	 */
	public int getID() {
		return mIndex;
	}
	
	@Override
	public String toString() {
		if (mContextDependency.isEmpty())
			return "context depenedency [pop #" + mIndex + "]";
		else
			return "context depenedency [pop #" + mIndex + "]:"
					+ mContextDependency;
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