package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.Strings;

/**
 * Write variable node.
 * <p>
 * <i>x</i> = <i>v</i>
 */
public class WriteVariableNode extends Node {

	private String varname;
	
	private int value_var;

	/**
	 * Constructs a new write variable node.
	 */
	public WriteVariableNode(int value_var, String varname, SourceLocation location) {
		super(location);
		this.value_var = value_var;
		this.varname = varname;
	}

	/**
	 * Returns the destination variable name.
	 */
	public String getVarName() {
		return varname;
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
		return "write-variable[v" + value_var + ",'" + Strings.escape(varname) + "']";
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
