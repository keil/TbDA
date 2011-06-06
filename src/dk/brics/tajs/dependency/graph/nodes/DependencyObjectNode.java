package dk.brics.tajs.dependency.graph.nodes;

import dk.brics.tajs.dependency.DependencyObject;
import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

public class DependencyObjectNode extends DependencyNode {

	private DependencyObject mDependency;

	public DependencyObjectNode(DependencyObject dependency) {
		this.mDependency = dependency;
	}

	public DependencyObjectNode(DependencyObject dependency,
			DependencyRootNode root) {
		this.mDependency = dependency;

		addParentNode(root);
	}

	public DependencyObject getDependencyObject() {
		return mDependency;
	}

	public SourceLocation getSourceLocation() {
		return mDependency.getSourceLocation();
	}

	@Override
	public void accept(IDependencyGraphVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String getIdentifier() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("D");
		buffer.append("[");
		buffer.append(mDependency.getTrace());
		buffer.append("]");

		return buffer.toString();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("{");
		buffer.append(this.mDependency);
		buffer.append("}");

		return buffer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mDependency == null) ? 0 : mDependency.hashCode());
		return result;
	}

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