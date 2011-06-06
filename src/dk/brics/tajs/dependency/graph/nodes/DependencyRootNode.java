package dk.brics.tajs.dependency.graph.nodes;

import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor;

public class DependencyRootNode extends DependencyNode {

	public DependencyRootNode() {
	}

	/* (non-Javadoc)
	 * @see dk.brics.tajs.dependency.graph.DependencyNode#accept(dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor)
	 */
	@Override
	public void accept(IDependencyGraphVisitor visitor) {
		visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see dk.brics.tajs.dependency.graph.DependencyNode#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("[");
		buffer.append("ROOT");
		buffer.append("]");

		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see dk.brics.tajs.dependency.graph.DependencyNode#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{ROOT}");
		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see dk.brics.tajs.dependency.graph.DependencyNode#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		return result;
	}

	/* (non-Javadoc)
	 * @see dk.brics.tajs.dependency.graph.DependencyNode#equals(java.lang.Object)
	 */
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

	/* (non-Javadoc)
	 * @see dk.brics.tajs.dependency.graph.DependencyNode#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return null;
	}

	/* (non-Javadoc)
	 * @see dk.brics.tajs.dependency.graph.DependencyNode#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
	}
}