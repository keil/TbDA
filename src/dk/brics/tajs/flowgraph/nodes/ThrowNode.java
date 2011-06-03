package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Throw node.
 * <p>
 * throw <i>v</i>
 * <p>
 * Must be the last node in its block.
 */
public class ThrowNode extends Node {

	private int value_var;

	/**
	 * Constructs a new throw node.
	 */
	public ThrowNode(int value_var, SourceLocation location) {
		super(location);
		this.value_var = value_var;
	}

	/**
	 * Returns the value variable number.
	 */
	public int getValueVar() {
		return value_var;
	}

    /**
     * Sets the value variable number.
     */
    public void setValueVar(int var) {
        this.value_var = var;
    }
	
	@Override
	public String toString() {
		return "throw[v" + value_var + "]";
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
