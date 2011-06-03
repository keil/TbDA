package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Call/construct node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = [new] [<i>v</i><sub><i>base</i></sub>.]
 * <i>
 * v</i><sub><i>function</i></sub>(<i>v</i><sub>0</sub>,...,<i>v</i><sub><i>n<
 * /i></sub>)
 * <p>
 * Note that <i>v</i><sub><i>function</i></sub> represents the function value
 * (not the property name), and <i>v</i><sub><i>base</i></sub> represents the
 * base object (or {@link dk.brics.tajs.flowgraph.Node#NO_VALUE} if absent).
 * <p>
 * Must be the only node in its block. The block must have precisely one
 * successor.
 */
public class CallNode extends AssignmentNode {

	private boolean constructor;

	private int base_var; // NO_VALUE if absent (i.e. implicitly the global
							// object)

	private int function_var;

	private int[] arg_vars;

	/**
	 * Constructs a new call/construct node.
	 */
	public CallNode(boolean constructor, int result_var, int base_var,
			int function_var, int[] arg_vars, SourceLocation location) {
		super(result_var, location);
		this.constructor = constructor;
		this.base_var = base_var;
		this.function_var = function_var;
		this.arg_vars = arg_vars.clone();
	}

	/**
	 * Checks whether this is a construct or an ordinary call.
	 */
	public boolean isConstructorCall() {
		return constructor;
	}

	/**
	 * Returns the base variable number. Variable number
	 * {@link dk.brics.tajs.flowgraph.Node#NO_VALUE} represents absent value
	 * (implicitly the global object).
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
	 * Returns the function variable number.
	 */
	public int getFunctionVar() {
		return function_var;
	}

	/**
	 * Sets the function variable number.
	 */
	public void setFunctionVar(int var) {
		this.function_var = var;
	}

	/**
	 * Returns the given argument variable number. Counts from 0.
	 */
	public int getArgVar(int i) {
		return arg_vars[i];
	}

	/**
	 * Sets the i'th argument to the given variable number. Counts from 0.
	 */
	public void setArgVar(int i, int var) {
		arg_vars[i] = var;
	}

	/**
	 * Returns the number of arguments.
	 */
	public int getNumberOfArgs() {
		return arg_vars.length;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		if (constructor)
			b.append("construct");
		else
			b.append("call");
		b.append('[');
		if (base_var == NO_VALUE)
			b.append('-');
		else
			b.append('v').append(base_var);
		b.append(",v").append(function_var);
		for (int v : arg_vars)
			b.append(",v").append(v);
		b.append(",v").append(getResultVar()).append("]");
		return b.toString();
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