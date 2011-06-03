package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.Strings;

/**
 * Delete property node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = delete <i>v</i><sub><i>base</i></sub>[<i>v</i><sub><i>property</i></sub>]<br>
 * or<br>
 * <i>v</i><sub><i>result</i></sub> = delete <i>v</i><sub><i>base</i></sub>.<i>property</i><br>
 * or<br>
 * <i>v</i><sub><i>result</i></sub> = delete <i>x</i> (for variables)
 */
public class DeletePropertyNode extends AssignmentNode implements IReadsVariable {

	private int base_var = Node.NO_VALUE;

	private int property_var = Node.NO_VALUE;
	
	private String property_str;
	
	private String varname;

	/**
	 * Constructs a new delete property node for a reference with variable property name.
	 */
	public DeletePropertyNode(int base_var, int property_var, int result_var, SourceLocation location) {
		super(result_var, location);
		this.base_var = base_var;
		this.property_var = property_var;
	}

	/**
	 * Constructs a new delete property node for a reference with fixed property name.
	 */
	public DeletePropertyNode(int base_var, String property_str, int result_var, SourceLocation location) { 
		super(result_var, location);
		this.base_var = base_var;
		this.property_str = property_str;
	}

	/**
	 * Constructs a new delete property node for a variable.
	 */
	public DeletePropertyNode(String varname, int result_var, SourceLocation location) {
		super(result_var, location);
		this.varname = varname;
	}

	/**
	 * Returns the base variable number, or {@link dk.brics.tajs.flowgraph.Node#NO_VALUE} if not applicable.
	 */
	public int getBaseVar() {
		return base_var;
	}

    /**
     * Sets the base variable number
     */
    public void setBaseVar(int var) {
        this.base_var = var;
    }

	/**
	 * Returns the property variable number, or {@link dk.brics.tajs.flowgraph.Node#NO_VALUE} if not applicable.
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
	
	/**
	 * Returns the property string, or null if not fixed or not a reference.
	 */
	public String getPropertyStr() {
		return property_str;
	}
	
	/**
	 * Returns true if the property is a fixed string.
	 */
	public boolean isPropertyFixed() {
		return property_str != null;
	}

	/**
	 * Returns the source variable name, or null if not a variable.
	 */
	@Override
	public String getVarName() {
		return varname;
	}
	
	/**
	 * Returns true if the argument is a variable, false if it is an ordinary reference.
	 */
	public boolean isVariable() {
		return varname != null;
	}

	@Override
	public String toString() {
		return "delete-property[" 
		+ (varname != null ? "'" + Strings.escape(varname) + "'" : "v" + base_var 
			+ (property_str != null ? ",'" + property_str + "'" : ",v" + property_var)) 
		+ ",v" + getResultVar() + "]";
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
