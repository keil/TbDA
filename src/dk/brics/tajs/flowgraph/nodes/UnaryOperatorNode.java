package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Unary operator node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = &lt;<i>op</i>&gt;(<i>v</i>)
 */
public class UnaryOperatorNode extends AssignmentNode {

	/**
	 * Unary operator.
	 */
	public enum Op {

		/**
		 * ~
		 */
		COMPLEMENT,
		
		/**
		 * !
		 */
		NOT,
		
		/**
		 * -
		 */
		MINUS,
		
		/**
		 * +
		 */
		PLUS
	};
	
	private int arg_var;
	
	private Op op;

	/**
	 * Constructs a new unary operator node.
	 */
	public UnaryOperatorNode(Op op, int arg_var, int result_var, SourceLocation location) {
		super(result_var, location);
		this.arg_var = arg_var;
		this.op = op;
	}
	
	/**
	 * Returns the argument variable number.
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
	 * Returns the operator.
	 */
	public Op getOperator() {
		return op;
	}
	
	private String operatorToString() {
		switch (op) {
		case COMPLEMENT:
			return "~";
		case NOT:
			return "!";
		case MINUS:
			return "-";
		case PLUS:
			return "+";
		default:
			throw new RuntimeException("Unexpected operator");
		}
	}
	
	@Override
	public String toString() {
		return operatorToString() + "[v" + arg_var + ",v" + getResultVar() + "]";
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
