package dk.brics.tajs.analysis;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.flowgraph.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;

/**
 * Models exceptions.
 */
public class Exceptions {

	private Exceptions() {}
	
	/**
	 * Models a TypeError exception being thrown at the current node.
	 * Does not modify the given state.
	 * Don't forget to set the ordinary state to bottom if the exception will definitely occur.
	 */
	public static void throwTypeError(State state, Solver.SolverInterface c) {
		if (Options.isExceptionsDisabled())
			return;
		state = state.clone();
		throwException(state, makeException(state, InitialStateBuilder.TYPE_ERROR_PROTOTYPE, c), c, c.getCurrentNode(), c.getCurrentContext());
	}
	
	/**
	 * Models a ReferenceError exception being thrown at the current node.
	 * Does not modify the given state.
	 * Don't forget to set the ordinary state to bottom if the exception will definitely occur.
	 */
	public static void throwReferenceError(State state, Solver.SolverInterface c) {
		if (Options.isExceptionsDisabled())
			return;
		state = state.clone();
		throwException(state, makeException(state, InitialStateBuilder.REFERENCE_ERROR_PROTOTYPE, c), c, c.getCurrentNode(), c.getCurrentContext());
	}
	
	/**
	 * Models a RangeError exception being thrown at the current node.
	 * Does not modify the given state.
	 * Don't forget to set the ordinary state to bottom if the exception will definitely occur.
	 */
	public static void throwRangeError(State state, Solver.SolverInterface c) {
		if (Options.isExceptionsDisabled())
			return;
		state = state.clone();
		throwException(state, makeException(state, InitialStateBuilder.RANGE_ERROR_PROTOTYPE, c), c, c.getCurrentNode(), c.getCurrentContext());
	}
	
	/**
	 * Models a SyntaxError exception being thrown at the current node.
	 * Does not modify the given state.
	 * Don't forget to set the ordinary state to bottom if the exception will definitely occur.
	 */
	public static void throwSyntaxError(State state, Solver.SolverInterface c) {
		if (Options.isExceptionsDisabled())
			return;
		state = state.clone();
		throwException(state, makeException(state, InitialStateBuilder.SYNTAX_ERROR_PROTOTYPE, c), c, c.getCurrentNode(), c.getCurrentContext());
	}
	
	/**
	 * Constructs an exception value.
	 * Does not modify the given state.
	 */
	private static Value makeException(State state, ObjectLabel prototype, Solver.SolverInterface c) {
		ObjectLabel ex = new ObjectLabel(c.getCurrentNode(), Kind.ERROR);
		state.newObject(ex);
		state.writeInternalPrototype(ex, Value.makeObject(prototype, new Dependency(), new DependencyGraphReference()));
		state.writeProperty(ex, "message", Value.makeAnyStr(new Dependency(), new DependencyGraphReference()));
		return Value.makeObject(ex, new Dependency(), new DependencyGraphReference());
	}
	
	/**
	 * Models an exception being thrown at the current node.
	 * Does not modify the given state.
	 * Don't forget to set the ordinary state to bottom if the exception will definitely occur.
	 */
	public static void throwException(State state, Value v, Solver.SolverInterface c) {
		state = state.clone();
		throwException(state, v, c, c.getCurrentNode(), c.getCurrentContext());
	}
	
	/**
	 * Models an exception being thrown at the given node.
	 * Modifies the given state.
	 * Don't forget to set the state to bottom if the exception will definitely occur.
	 */
	public static void throwException(State state, Value v, Solver.SolverInterface c, Node source, CallContext context) {
		if (!source.canThrowExceptions())
			throw new RuntimeException("Exception at non-throwing-exception node!?");
		BasicBlock handlerblock = source.getBlock().getExceptionHandler();
		if (handlerblock == null)
			throw new RuntimeException("No exception handler for block " + source.getBlock().getIndex());
		state.writeTemporary(Node.EXCEPTION_VAR, v);
		c.joinBlockEntry(state, handlerblock, context);
	}
}
