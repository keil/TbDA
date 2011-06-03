package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.Strings;

/**
 * Catch node.
 * <p>
 * catch(<i>x</i>)
 * <p>
 * Must be the first node in its block.
 */
public class CatchNode extends Node { // FIXME: =============> extend AssignmentNode? or move the temp_var variant to a separate node class? (e.g. CopyNode?) which could later be removed by the optimizer 

	private String varname;
	
	private int temp_var = Node.NO_VALUE;
	
	private int scopeobj_var = Node.NO_VALUE;
	
	/**
	 * Constructs a new catch node where the exception is stored in a program variable.
	 */
	public CatchNode(String varname, int scopeobj_var, SourceLocation location) {
		super(location);
		this.varname = varname;
		this.scopeobj_var = scopeobj_var;
	}
	
	/**
	 * Construct a new catch node where the exception is stored in a temporary variable.
	 */
	public CatchNode(int temp_var, SourceLocation location) {
		super(location);
		this.temp_var = temp_var;
	} 

	/**
	 * Returns the variable name, or null if not using a program variable.
	 */
	public String getVarName() {
		return varname;
	}

	/**
	 * Returns the object to be added to the scope chain when entering the catch block,
	 * or {@link Node#NO_VALUE} if not using a program variable.
	 */
	public int getScopeObjVar() {
		return scopeobj_var;
	}

	/**
	 * Returns the result variable number, 
	 * or {@link Node#NO_VALUE} if not using a temporary variable.
	 */
	public int getTempVar() {
		return temp_var;
	}

	@Override
	public String toString() {
		if (varname != null)
			return "catch[" + Strings.escape(varname) + ",v" + scopeobj_var+"]";
		else
			return "catch[v" + temp_var +"]";
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
