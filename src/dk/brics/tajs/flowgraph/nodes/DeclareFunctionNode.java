package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Function declaration/expression node.
 * <p>
 * <i>v</i> = function [ <i>name</i> ] ( <i>args</i> ) { <i>body</i> )
 */
public class DeclareFunctionNode extends AssignmentNode {
	
	private Function f;
	
	private boolean expression;
	
	/**
	 * Constructs a new function declaration node.
	 */
	public DeclareFunctionNode(Function f, boolean expression, int result_var, SourceLocation location) {
		super(result_var, location);
		this.f = f;
		this.expression = expression;
	}
	
	/**
	 * Returns the function.
	 */
	public Function getFunction() {
		return f;
	}
	
	/**
	 * Returns true if this is an expression, false if it is a declaration.
	 */
	public boolean isExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return "function-" + (expression ? "expr" : "decl") + "[" + f + ",v" + getResultVar() + "]";
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
