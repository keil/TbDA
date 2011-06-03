package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.Strings;

/**
 * Constant node.
 * <p>
 * <i>v</i> = <i>constant</i>
 */
public class ConstantNode extends AssignmentNode {
	
	/**
	 * Type of constant value.
	 */
	public enum Type {
		
		/**
		 * Number.
		 */
		NUMBER,
		
		/**
		 * String.
		 */
		STRING,
		
		/**
		 * Boolean.
		 */
		BOOLEAN,
		
		/**
		 * Undefined.
		 */
		UNDEFINED,
		
		/**
		 * Null.
		 */
		NULL,
		
		/**
		 * User-defined function.
		 */
		FUNCTION
	}
	
	private Type type;
	
	private double number;
	
	private String string;
	
	private boolean bool;
	
	private Function function;

	private ConstantNode(Type type, double number, String string, boolean bool,
			Function function,
			int result_var, SourceLocation location) {
		super(result_var, location);
		this.type = type;
		this.number = number;
		this.string = string;
		this.bool = bool;
		this.function = function;
	}

	/**
	 * Constructs a new constant number node.
	 */
	public static ConstantNode makeNumber(double number, int result_var, SourceLocation location) {
		return new ConstantNode(Type.NUMBER, number, null, false, null, result_var, location);
	}

	/**
	 * Constructs a new constant string node.
	 */
	public static ConstantNode makeString(String string, int result_var, SourceLocation location) {
		return new ConstantNode(Type.STRING, 0.0d, string, false, null, result_var, location);
	}

	/**
	 * Constructs a new constant boolean node.
	 */
	public static ConstantNode makeBoolean(boolean bool, int result_var, SourceLocation location) {
		return new ConstantNode(Type.BOOLEAN, 0.0d, null, bool, null, result_var, location);
	}

	/**
	 * Constructs a new constant 'null' node.
	 */
	public static ConstantNode makeNull(int result_var, SourceLocation location) {
		return new ConstantNode(Type.NULL, 0.0d, null, false, null, result_var, location);
	}

	/**
	 * Constructs a new constant 'undefined' node.
	 */
	public static ConstantNode makeUndefined(int result_var, SourceLocation location) {
		return new ConstantNode(Type.UNDEFINED, 0.0d, null, false, null, result_var, location);
	}
	
	/**
	 * Constructs a new constant user-defined function node.
	 */
	public static ConstantNode makeUserFunction(int result_var, Function function, SourceLocation location) {
		return new ConstantNode(Type.FUNCTION, 0.0d, null, false, function, result_var, location);
	}
	
	/**
	 * Returns the type.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Returns the number (for Type.NUMBER only).
	 */
	public double getNumber() {
		return number;
	}

	/**
	 * Returns the string (for Type.STRING only).
	 */
	public String getString() {
		return string;
	}

	/**
	 * Returns the boolean (for Type.BOOLEAN only).
	 */
	public boolean getBoolean() {
		return bool;
	}
	
	/**
	 * Returns the user-defined function (for Type.FUNCTION only).
	 */
	public Function getFunction() {
		return function;
	}
	
	private String valueToString() {
		switch (type) {
		case NUMBER:
			return Double.toString(number);
		case STRING:
			return "\"" + Strings.escape(string) + "\"";
		case BOOLEAN:
			return Boolean.toString(bool);
		case NULL:
			return "null";
		case UNDEFINED:
			return "undefined";
		case FUNCTION:
			return function.toString();
		default:
			throw new RuntimeException("Unexpected type: " + type);
		}
	}

	@Override
	public String toString() {
		return "constant[" + valueToString() + ",v" + getResultVar() + "]";
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
