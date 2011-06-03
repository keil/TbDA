package dk.brics.tajs.dependency.graph;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.SourceLocation;

import static dk.brics.tajs.dependency.graph.Label.*;

/**
 * label object for dependencies
 * 
 * @author Matthias Keil
 * 
 */
public class DependencyLabel implements Cloneable {

	/**
	 * the current label
	 */
	private Label mLabel;

	/**
	 * referenced source location
	 */
	private SourceLocation mLocation;

	/**
	 * referenced node
	 */
	private Node mNode;

	/**
	 * @param node
	 */
	public DependencyLabel(Node node) {
		super();
		mLabel = NONE;
		this.mLocation = node.getSourceLocation();
		this.mNode = node;
	}

	/**
	 * @param label
	 */
	public DependencyLabel(Label label, Node node) {
		super();
		this.mLabel = label;
		this.mLocation = node.getSourceLocation();
		this.mNode = node;
	}

	/**
	 * @param label
	 */
	public DependencyLabel(DependencyLabel label) {
		super();
		this.mLabel = label.getLabel();
	}

	/**
	 * @return
	 */
	public Label getLabel() {
		return mLabel;
	}

	/**
	 * @return
	 */
	public SourceLocation getSourceLocation() {
		return mLocation;
	}

	/**
	 * @return
	 */
	public Node getNode() {
		return mNode;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DependencyLabel other = (DependencyLabel) obj;
		if (mLabel != other.mLabel)
			return false;
		if (mLocation == null) {
			if (other.mLocation != null)
				return false;
		} else if (!mLocation.equals(other.mLocation))
			return false;
		if (mNode == null) {
			if (other.mNode != null)
				return false;
		} else if (!mNode.equals(other.mNode))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mLabel == null) ? 0 : mLabel.hashCode());
		result = prime * result
				+ ((mLocation == null) ? 0 : mLocation.hashCode());
		result = prime * result + ((mNode == null) ? 0 : mNode.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return mLabel.toString();
	}
}