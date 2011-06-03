package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * If node.
 * <p>
 * if (<i>v</i><sub><i>condition</i></sub>) {...} else {...}
 * <p>
 * Must be the last node in its block.
 */
public class IfNode extends Node {

	private int condition_var;

	private BasicBlock succ_true;

	private BasicBlock succ_false;

	/**
	 * Constructs a new if node.
	 */
	public IfNode(int condition_var, SourceLocation location) {
		super(location);
		this.condition_var = condition_var;
	}

	/**
	 * Sets the true/false successors.
	 */
	public void setSuccessors(BasicBlock succ_true, BasicBlock succ_false) {
		this.succ_true = succ_true;
		this.succ_false = succ_false;
	}

	/**
	 * Returns the condition variable number.
	 */
	public int getConditionVar() {
		return condition_var;
	}

	/**
	 * Sets the condition variable number.
	 */
	public void setConditionVar(int var) {
		this.condition_var = var;
	}

	/**
	 * Returns the 'true' successor.
	 */
	public BasicBlock getSuccTrue() {
		return succ_true;
	}

	/**
	 * Returns the 'false' successor.
	 */
	public BasicBlock getSuccFalse() {
		return succ_false;
	}

	@Override
	public String toString() {
		return "if[v" + condition_var + "](true-block:" + succ_true.getIndex()
				+ ",false-block:" + succ_false.getIndex() + ")";
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