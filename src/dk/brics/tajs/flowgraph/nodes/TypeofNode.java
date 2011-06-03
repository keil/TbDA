package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.Strings;

/**
 * Typeof node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = typeof <i>v</i> (for syntactic property references)<br>
 * or<br>
 * <i>v</i><sub><i>result</i></sub> = typeof <i>x</i> (for variables)
 */
public class TypeofNode extends AssignmentNode implements IReadsVariable {

	private int arg_var = Node.NO_VALUE;
	
	private String varname;

	/**
	 * Constructs a new typeof node for a reference.
	 */
	public TypeofNode(int arg_var, int result_var, SourceLocation location) {
		super(result_var, location);
		this.arg_var = arg_var;
	}

	/**
	 * Constructs a new typeof node for a variable. 
	 */
	public TypeofNode(String varname, int result_var, SourceLocation location) {
		super(result_var, location);
		this.varname = varname;
	}

	/**
	 * Returns the argument variable number, or {@link dk.brics.tajs.flowgraph.Node#NO_VALUE} if not applicable.
	 */
	public int getArgVar() {
		return arg_var;
	}

    /**
     * Sets the argument variable number.
     */
    public void setArgVar(int var) {
        this.arg_var = var;
    }
	
	/**
	 * Returns the source variable name, or null if not a variable.
	 */
	@Override
	public String getVarName() {
		return varname;
	}

	/**
	 * Returns true if the argument is a variable, false if it is a reference.
	 */
	public boolean isVariable() {
		return varname != null;
	}

	@Override
	public String toString() {
		return "typeof[" 
		+ (varname != null ? "'" + Strings.escape(varname) + "'" : "v" + arg_var ) 
		+ ",v" + getResultVar() + "]";
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
