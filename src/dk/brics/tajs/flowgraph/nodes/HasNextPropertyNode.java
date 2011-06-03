package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Has-next-property node.
 * <p>
 * for (<i>v</i><sub><i>property</i></sub> in <i>v</i><sub><i>object</i></sub>) { ... }
 */
public class HasNextPropertyNode extends AssignmentNode {

	private int propertyqueue_var;

	/**
	 * Constructs a new has-next-property node.
	 */
	public HasNextPropertyNode(int propertyqueue_var, int result_var, SourceLocation location) {
		super(result_var, location);
		this.propertyqueue_var = propertyqueue_var;
	}

	/**
	 * Returns the property queue variable number.
	 */
	public int getPropertyQueueVar() {
		return propertyqueue_var;
	}
	
	@Override
	public boolean canThrowExceptions() {
		return false;
	}

	@Override
	public String toString() {
		return "has-next[v" + propertyqueue_var + ",v" + getResultVar() + "]";
	}

	@Override
	public <Arg> void visitBy(NodeVisitor<Arg> v, Arg a) {
		v.visit(this, a);
	}
}
