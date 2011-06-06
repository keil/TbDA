package dk.brics.tajs.dependency.graph.nodes;

import dk.brics.tajs.dependency.DependencyObject;
import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

public class DependencyObjectNode extends DependencyNode {

	/**
	 * referenced Dependency
	 */
	private DependencyObject mDependency;

	/**
	 * @param dependency
	 */
	public DependencyObjectNode(DependencyObject dependency) {
		this.mDependency = dependency;
	}

	/**
	 * @param dependency
	 * @param root
	 */
	public DependencyObjectNode(DependencyObject dependency,
			DependencyRootNode root) {
		this.mDependency = dependency;

		addParentNode(root);
	}

	/**
	 * @return DependencyObject
	 */
	public DependencyObject getDependencyObject() {
		return mDependency;
	}

	/**
	 * @return SourceLocation
	 */
	public SourceLocation getSourceLocation() {
		return mDependency.getSourceLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dk.brics.tajs.dependency.graph.DependencyNode#accept(dk.brics.tajs.dependency
	 * .graph.interfaces.IDependencyGraphVisitor)
	 */
	@Override
	public void accept(IDependencyGraphVisitor visitor) {
		visitor.visit(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brics.tajs.dependency.graph.DependencyNode#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("D");
		buffer.append("[");
		buffer.append(mDependency.getTrace());
		buffer.append("]");

		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brics.tajs.dependency.graph.DependencyNode#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("{");
		buffer.append(this.mDependency);
		buffer.append("}");

		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brics.tajs.dependency.graph.DependencyNode#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mDependency == null) ? 0 : mDependency.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dk.brics.tajs.dependency.graph.DependencyNode#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DependencyObjectNode other = (DependencyObjectNode) obj;
		if (mDependency == null) {
			if (other.mDependency != null)
				return false;
		} else if (!mDependency.equals(other.mDependency))
			return false;
		return true;
	}
}