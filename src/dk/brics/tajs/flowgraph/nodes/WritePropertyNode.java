package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Write property node.
 * <p>
 * <i>v</i><sub><i>base</i></sub>[<i>v</i><sub><i>property</i></sub>] = <i>v</i><br>
 * or<br>
 * <i>v</i><sub><i>base</i></sub>.<i>property</i> = <i>v</i><br>
 */
public class WritePropertyNode extends Node {

	private int base_var;

	private int property_var = Node.NO_VALUE;
	
	private String property_str;
	
	private int value_var;

	/**
	 * Constructs a new write property node with variable property name.
	 */
	public WritePropertyNode(int base_var, int property_var, int value_var, SourceLocation location) {
		super(location);
		this.base_var = base_var;
		this.property_var = property_var;
		this.value_var = value_var;
	}

	/**
	 * Constructs a new write property node with fixed property name.
	 */
	public WritePropertyNode(int base_var, String property_str, int value_var, SourceLocation location) {
		super(location);
		this.base_var = base_var;
		this.property_str = property_str;
		this.value_var = value_var;
	}

	/**
	 * Returns the base variable number.
	 */
	public int getBaseVar() {
		return base_var;
	}

    /**
     * Sets the base variable number.
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
	 * Returns the property string, or null if not fixed.
	 */
	public String getPropertyStr() {
		return property_str;
	}
	
	public void setPropertyStr(String property_str) {
        this.property_str = property_str;
    }
	
	/**
	 * Returns true if the property is a fixed string.
	 */
	public boolean isPropertyFixed() {
		return property_str != null;
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
		return "write-property[v" + base_var 
		+ (property_str != null ? ",'" + property_str + "'" : ",v" + property_var) 
		+ ",v" + value_var + "]";
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
