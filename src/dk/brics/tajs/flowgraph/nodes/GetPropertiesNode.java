package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Get-properties node.
 * <p>
 * for (<i>v</i><sub><i>property</i></sub> in <i>v</i><sub><i>object</i></sub>) { ... }
 */
public class GetPropertiesNode extends Node {

	private int object_var;

	private int propertyqueue_var;
	
	/**
	 * Constructs a new get-properties node.
	 */
	public GetPropertiesNode(int object_var, int propertyqueue_var, SourceLocation location) {
		super(location);
		this.object_var = object_var;
		this.propertyqueue_var = propertyqueue_var;
	}

	/**
	 * Returns the object variable number.
	 */
	public int getObjectVar() {
		return object_var;
	}

    /**
     * Sets the object variable number.
     */
    public void setObjectVar(int var) {
        this.object_var = var;
    }
	
	/**
	 * Returns the property queue variable number.
	 */
	public int getPropertyQueueVar() {
		return propertyqueue_var;
	}
	
	@Override
	public String toString() {
		return "get-properties[v" + object_var + ",v" + propertyqueue_var + "]";
	}

	@Override
	public <Arg> void visitBy(NodeVisitor<Arg> v, Arg a) {
		v.visit(this, a);
	}

	@Override
	public boolean canThrowExceptions() {
		return true;
	}
}
