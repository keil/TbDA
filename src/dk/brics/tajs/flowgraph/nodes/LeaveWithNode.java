package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Leave-with node.
 * <p>
 * with(<i>v</i>) { ... }
 */
public class LeaveWithNode extends Node {

	/**
	 * Constructs a new leave with node.
	 */
	public LeaveWithNode(SourceLocation location) {
		super(location);
	}
	
	@Override
	public String toString() {
		return "leave-with";
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
