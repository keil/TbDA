package dk.brics.tajs.dependency.graph.nodes;

import java.util.Iterator;

import dk.brics.tajs.dependency.graph.DependencyLabel;
import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.Label;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphVisitor;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.SourceLocation;

public class DependencyExpressionNode extends DependencyNode {

	/**
	 * DependencyLabel
	 */
	private DependencyLabel mLabel;
	/**
	 * identifier
	 */
	private int id;

	/**
	 * @param label
	 */
	public DependencyExpressionNode(DependencyLabel label) {
		this.mLabel = label;
		this.id = DependencyNode.nextNumber(label.getLabel());
	}

	/**
	 * @return Label
	 */
	public Label getLabel() {
		return mLabel.getLabel();
	}

	/**
	 * @return SourceLocation
	 */
	public SourceLocation getSourceLocation() {
		return mLabel.getSourceLocation();
	}

	/**
	 * @return Node
	 */
	public Node getNode() {
		return mLabel.getNode();
	}

	/**
	 * @return DependencyLabel
	 */
	public DependencyLabel getDependencyLabel() {
		return mLabel;
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
		buffer.append(mLabel);
		buffer.append("'");
		buffer.append(this.id);
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
		buffer.append(mLabel);
		buffer.append("'");
		buffer.append(this.id);
		buffer.append(" ");

		Iterator<DependencyNode> iterator = this.getParentNodes().iterator();
		while (iterator.hasNext()) {
			DependencyNode next = iterator.next();
			buffer.append(next.getIdentifier());

			if (iterator.hasNext()) {
				buffer.append(", ");
			}
		}
		buffer.append("}");

		return buffer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		// FIXME
//		result = prime * result + id;
		result = prime * result + ((mLabel == null) ? 0 : mLabel.hashCode());
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
		DependencyExpressionNode other = (DependencyExpressionNode) obj;
//		FIXME
//		if (id != other.id)
//			return false;
		if (mLabel == null) {
			if (other.mLabel != null)
				return false;
		} else if (!mLabel.equals(other.mLabel))
			return false;
		return true;
	}
}