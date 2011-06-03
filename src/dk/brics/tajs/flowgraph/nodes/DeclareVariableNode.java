package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.Strings;

/**
 * Variable declaration node.
 * <p>
 * var <i>x</i>
 */
public class DeclareVariableNode extends Node {
	
	private String varname;

	/**
	 * Constructs a new variable declaration node.
	 */
	public DeclareVariableNode(String varname, SourceLocation location) {
		super(location);
		this.varname = varname;
	}

	/**
	 * Returns the variable name.
	 */
	public String getVarName() {
		return varname;
	}
	
	@Override
	public String toString() {
		return "vardecl['" + Strings.escape(varname) + "']";
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
