package dk.brics.tajs.dependency.graph.nodes;

import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor;

public class DependencyRootNode extends DependencyNode {

	public DependencyRootNode() {
	}

	@Override
	public void accept(IDependencyGraphVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{}");
		return buffer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
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
		DependencyRootNode other = (DependencyRootNode) obj;
		if (mParentNodes == null) {
			if (other.mParentNodes != null)
				return false;
		} else if (!mParentNodes.equals(other.mParentNodes))
			return false;
		return true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return null;
	}

	@Override
	protected void finalize() throws Throwable {
	}
}