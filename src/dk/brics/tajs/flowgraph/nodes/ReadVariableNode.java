package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.Strings;

/**
 * Read variable node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = <i>x</i>
 */
public class ReadVariableNode extends AssignmentNode implements IReadsVariable {

	private String varname;
	
	private int result_base_var; // NO_VALUE if varname is "this"

	/**
	 * Constructs a new read variable node.
	 */
	public ReadVariableNode(String varname, int result_var, int result_base_var, SourceLocation location) {
		super(result_var, location);
		this.varname = varname;
		this.result_base_var = result_base_var;
	}

	/**
	 * Returns the source variable name.
	 */
	@Override
	public String getVarName() {
		return varname;
	}
	
	/**
	 * Returns the result base variable number.
	 * Variable number {@link dk.brics.tajs.flowgraph.Node#NO_VALUE} means 'absent', e.g. for "this".
	 */
	public int getResultBaseVar() {
		return result_base_var;
	}

	@Override
	public String toString() {
		return "read-variable['" + Strings.escape(varname) + "',v" + getResultVar() + (result_base_var != NO_VALUE ? ",v" + result_base_var : "") + "]";
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
