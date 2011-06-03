package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.Strings;

/**
 * Assume node.
 */
public class AssumeNode extends Node implements IReadsVariable { // TODO: split different kinds into separate subclasses?
	
	/**
	 * Assumption kind.
	 */
	public enum Kind {
		
		/**
		 * Variable value is not null/undefined.
		 */
		VARIABLE_NON_NULL_UNDEF,
		
		/**
		 * Property value is not null/undefined.
		 */
		PROPERTY_NON_NULL_UNDEF
		
		// TODO: other assumption kinds?
	}
	
	private Kind kind;
	
	private String varname;
	
	private int base_var = Node.NO_VALUE;

	private int property_var = Node.NO_VALUE;
	
	private String property_str;
	
	private Node access_node;
	
	private AssumeNode(Kind kind, SourceLocation location) {
		super(location);
		this.kind = kind;
	}
	
	/**
	 * Constructs a new assume node for "variable value is not null/undefined".
	 */
	public static AssumeNode makeVariableNonNullUndef(String varname, SourceLocation location) {
		AssumeNode n = new AssumeNode(Kind.VARIABLE_NON_NULL_UNDEF, location);
		n.varname = varname;
		return n;
	}
	
	/**
	 * Constructs a new assume node for "property value is not null/undefined" with variable property.
	 */
	public static AssumeNode makePropertyNonNullUndef(int base_var, int property_var, Node access_node, SourceLocation location) {
		AssumeNode n = new AssumeNode(Kind.PROPERTY_NON_NULL_UNDEF, location);
		n.base_var = base_var;
		n.property_var = property_var;
		n.access_node = access_node;
		return n;
	}

	/**
	 * Constructs a new assume node for "property value is not null/undefined" with fixed property.
	 */
	public static AssumeNode makePropertyNonNullUndef(int base_var, String property_str, Node access_node, SourceLocation location) {
		AssumeNode n = new AssumeNode(Kind.PROPERTY_NON_NULL_UNDEF, location);
		n.base_var = base_var;
		n.property_str = property_str;
		n.access_node = access_node;
		return n;
	}
	
	/**
	 * Returns the kind.
	 */
	public Kind getKind() {
		return kind;
	}

	/**
	 * Returns the source variable name.
	 */
	@Override
	public String getVarName() {
		return varname;
	}

	/**
	 * Returns the base variable number, or {@link dk.brics.tajs.flowgraph.Node#NO_VALUE} if not applicable.
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
	 * Returns the property variable number, or  {@link dk.brics.tajs.flowgraph.Node#NO_VALUE} if not applicable.
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
	
	/**
	 * Returns the node where the property access occurred.
	 */
	public Node getAccessNode() {
		return access_node;
	}
	
	/**
	 * Returns true if the property is a fixed string.
	 */
	public boolean isPropertyFixed() {
		return property_str != null;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		switch (kind) {
			case VARIABLE_NON_NULL_UNDEF:
				b.append("<variable-non-null-undef>['").append(Strings.escape(varname)).append("']");
				break;
			case PROPERTY_NON_NULL_UNDEF:
				b.append("<property-non-null-undef>[v").append(base_var);
				if (property_str != null)
					b.append(",\'").append(Strings.escape(property_str)).append('\'');
				else
					b.append(",v").append(property_var);
				b.append(']');
				break;
		}
		return b.toString();
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
