package dk.brics.tajs.flowgraph;

import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.dependency.graph.DependencyNode;
import dk.brics.tajs.dependency.graph.interfaces.IDependencyGraphReference;

/**
 * Abstract base class for all nodes.
 */
public abstract class Node implements IDependencyGraphReference<Node> {

	/**
	 * Temporary variable number used for absent values.
	 */
	public static final int NO_VALUE = -1;

	/**
	 * Temporary variable number used for exception values.
	 */
	public static final int EXCEPTION_VAR = 0;

	/**
	 * First temporary variable number used for ordinary values.
	 */
	public static final int FIRST_ORDINARY_VAR = 1;

	private int index = -1;

	private SourceLocation location;

	private BasicBlock block;

	/**
	 * dependency graph reference
	 */
	private DependencyGraphReference mDependencyGraphReference = new DependencyGraphReference();

	/**
	 * Constructs a new node.
	 */
	public Node(SourceLocation location) {
		this.location = location;
		index = -1;
	}

	/**
	 * Sets the node index. Called when the node is added to a flow graph.
	 */
	void setIndex(int index) {
		if (this.index != -1)
			throw new IllegalArgumentException(
					"Node already belongs to a flow graph: " + this);
		this.index = index;
	}

	/**
	 * Sets the basic block containing this node.
	 */
	public void setBlock(BasicBlock block) {
		this.block = block;
	}

	/**
	 * Returns the node index. The node index uniquely identifies the node
	 * within the flow graph.
	 */
	public int getIndex() {
		if (index == -1)
			throw new RuntimeException(
					"Node has not been added to flow graph: " + this);
		return index;
	}

	/**
	 * Returns the source location.
	 */
	public SourceLocation getSourceLocation() {
		return location;
	}

	/**
	 * Returns the block containing this node.
	 */
	public BasicBlock getBlock() {
		return block;
	}

	/**
	 * Returns a string description of this node.
	 */
	@Override
	abstract public String toString();

	/**
	 * Visits this node with the given visitor.
	 */
	abstract public <Arg> void visitBy(NodeVisitor<Arg> v, Arg a);

	/**
	 * Returns true if this node may throw exceptions.
	 */
	abstract public boolean canThrowExceptions();

	@Override
	public DependencyGraphReference getDependencyGraphReference() {
		return (mDependencyGraphReference != null) ? mDependencyGraphReference
				: null;
	}

	@Override
	public Node setDependencyGraphReference(DependencyGraphReference reference) {
		mDependencyGraphReference = new DependencyGraphReference(reference);
		return this;
	}

	@Override
	public boolean hasDependencyGraphReference() {
		return (mDependencyGraphReference != null) ? true : false;
	}

	@Override
	public String printDependencyGraphReference() {
		return (mDependencyGraphReference != null) ? mDependencyGraphReference
				.toString() : "";
	}

	@Override
	public Node joinDependencyGraphReference(DependencyGraphReference reference) {
		DependencyGraphReference newReference = new DependencyGraphReference();
		newReference.join(mDependencyGraphReference);
		newReference.join(reference);
		mDependencyGraphReference  = newReference;
		return this;
	}

	@Override
	public Node joinDependencyGraphReference(DependencyNode node) {
		return joinDependencyGraphReference(node.getReference());
	}
}