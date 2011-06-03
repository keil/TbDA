package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Return node.
 * <p>
 * return [<i>v</i>]
 * <p>
 * Must be the only node in its block.
 */
public class ReturnNode extends Node {

	private int value_var; // NO_VALUE if absent
	
	/**
	 * Constructs a new return node.
	 * Variable number {@link Node#NO_VALUE} represents absent value (implicitly 'undefined').
	 */
	public ReturnNode(int value_var, SourceLocation location) {
		super(location);
		this.value_var = value_var;
	}

	/**
	 * Returns the value variable number.
	 * Variable number {@link Node#NO_VALUE} represents absent value (implicitly 'undefined').
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
		return "return" + (value_var != NO_VALUE ? "[v" + value_var + "]" : "");
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
