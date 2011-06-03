package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Binary operator node. 
 * <p>
 * <i>v</i><sub><i>result</i></sub> = &lt;<i>op</i>&gt;(<i>v</i><sub>1</sub>,<i>v</i><sub>1</sub>)
 */
public class BinaryOperatorNode extends AssignmentNode {

	/**
	 * Binary operator.
	 */
	public enum Op {
		
		/**
		 * +
		 */
		ADD,

		/**
		 * -
		 */
		SUB,
		
		/**
		 * *
		 */
		MUL,
		
		/**
		 * /
		 */
		DIV,
		
		/**
		 * %
		 */
		REM,
		
		/**
		 * &amp;
		 */
		AND,
		
		/**
		 * |
		 */
		OR,

		/**
		 * ^
		 */
		XOR,

		/**
		 * ==
		 */
		EQ,

		/**
		 * !=
		 */
		NE,

		/**
		 * &lt;
		 */
		LT,

		/**
		 * >=
		 */
		GE,

		/**
		 * &lt;=
		 */
		LE,

		/**
		 * >
		 */
		GT,

		/**
		 * &lt;&lt;
		 */
		SHL,

		/**
		 * >>
		 */
		SHR,

		/**
		 * >>>
		 */
		USHR,

		/**
		 * ===
		 */
		SEQ,

		/**
		 * !==
		 */
		SNE,

		/**
		 * in
		 */
		IN,

		/**
		 * instanceof
		 */
		INSTANCEOF
	};

	private int arg1_var;

	private int arg2_var;

	private Op op;

	/**
	 * Constructs a new binary operator node.
	 */
	public BinaryOperatorNode(Op op, int arg1_var, int arg2_var, int result_var, SourceLocation location) {
		super(result_var, location);
		this.arg1_var = arg1_var;
		this.arg2_var = arg2_var;
		this.op = op;
	}

	/**
	 * Returns the first argument variable number.
	 */
	public int getArg1Var() {
		return arg1_var;
	}

    /**
     * Sets the first argument variable number.
     */
    public void setArg1Var(int arg1_var) {
        this.arg1_var = arg1_var;
    }

    /**
	 * Returns the second argument variable number.
	 */
	public int getArg2Var() {
		return arg2_var;
	}

    /**
     * Sets the second argument variable number.
     */
    public void setArg2Var(int arg2_var) {
        this.arg2_var = arg2_var;
    }

    /**
	 * Returns the operator.
	 */
	public Op getOperator() {
		return op;
	}

	private String operatorToString() {
		switch (op) {
		case ADD:
			return "+";
		case SUB:
			return "-";
		case MUL:
			return "*";
		case DIV:
			return "/";
		case REM:
			return "%";
		case AND:
			return "&";
		case OR:
			return "|";
		case XOR:
			return "^";
		case EQ:
			return "==";
		case NE:
			return "!=";
		case LT:
			return "<";
		case GE:
			return ">=";
		case LE:
			return "<=";
		case GT:
			return ">";
		case SHL:
			return "<<";
		case SHR:
			return ">>";
		case USHR:
			return ">>>";
		case SEQ:
			return "===";
		case SNE:
			return "!==";
		case IN:
			return "in";
		case INSTANCEOF:
			return "instanceof";
		default:
			throw new RuntimeException("Unexpected operator");
		}
	}
	
	@Override
	public String toString() {
		return operatorToString() + "[v" + arg1_var + ",v" + arg2_var + ",v" + getResultVar() + "]";
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
