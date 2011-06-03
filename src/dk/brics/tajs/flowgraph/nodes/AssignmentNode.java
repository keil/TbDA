package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Abstract base class for nodes that are assignments to temporary variables.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = ...
 * <p>
 * Temporary variables must be defined before used on all intra-procedural paths.
 */
public abstract class AssignmentNode extends Node {

	private int result_var;

	/**
	 * Constructs a new assignment node.
	 */
	public AssignmentNode(int result_var, SourceLocation location) {
		super(location);
		this.result_var = result_var;
	}

	/**
	 * Returns the result variable number.
	 */
	public int getResultVar() {
		return result_var;
	}
}
