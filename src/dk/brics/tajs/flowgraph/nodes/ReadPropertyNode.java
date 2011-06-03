package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Read property node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = <i>v</i><sub><i>base</i></sub>.<i>property</i></sub><br>
 * or<br>
 * <i>v</i><sub><i>result</i></sub> = <i>v</i><sub><i>base</i></sub>[<i>v</i><sub><i>property</i></sub>]<br>
 */
public class ReadPropertyNode extends AssignmentNode {

	private int base_var;

	private int property_var = Node.NO_VALUE;
	
	private String property_str;
	
	/**
	 * Constructs a new read property node with variable property name.
	 */
	public ReadPropertyNode(int base_var, int property_var, int result_var, SourceLocation location) {
		super(result_var, location);
		this.base_var = base_var;
		this.property_var = property_var;
	}

	/**
	 * Constructs a new read property node with fixed property name.
	 */
	public ReadPropertyNode(int base_var, String property_str, int result_var, SourceLocation location) {
		super(result_var, location);
		this.base_var = base_var;
		this.property_str = property_str;
	}

	/**
	 * Returns the base variable number.
	 */
	public int getBaseVar() {
		return base_var;
	}

    /**
     * Returns the base variable number.
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
     * Set the property variable number.
     */
    public void setPropertyVar(int var) {
        this.property_var = var;
    }
	
	/**
	 * Returns the property string, or null if not fixed.
	 */
	public String getPropertyStr() {
		return property_str;
	}
	
	/**
	 * Sets the property string. Set to null if not fixed.
	 */
	public void setPropertyStr(String property_str) {
        this.property_str = property_str;
    }
	
	/**
	 * Returns true if the property is a fixed string.
	 */
	public boolean isPropertyFixed() {
		return property_str != null;
	}

	@Override
	public String toString() {
		return "read-property[v" + base_var 
		+ (property_str != null ? ",'" + property_str + "'" : ",v" + property_var) 
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
