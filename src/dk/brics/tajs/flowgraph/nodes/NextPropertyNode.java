package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Next-property node.
 * <p>
 * for (<i>v</i><sub><i>property</i></sub> in <i>v</i><sub><i>object</i></sub>) { ... }
 */
public class NextPropertyNode extends Node {

	private int propertyqueue_var;

	private int property_var;

	/**
	 * Constructs a new next-property node.
	 */
	public NextPropertyNode(int propertyqueue_var, int property_var, SourceLocation location) {
		super(location);
		this.propertyqueue_var = propertyqueue_var;
		this.property_var = property_var;
	}

	/**
	 * Returns the property queue variable number.
	 */
	public int getPropertyQueueVar() {
		return propertyqueue_var;
	}
	
	/**
	 * Returns the property variable number.
	 */
	public int getPropertyVar() {
		return property_var;
	}

    /**
     * Sets the property variable number.
     */
    public void setPropertyVar(int var) {
        this.property_var = var;
    }

	@Override
	public String toString() {
		return "next-property[v" + propertyqueue_var + ",v" + property_var + "]";
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
