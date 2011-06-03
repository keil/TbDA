package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Nop node.
 */
public class NopNode extends Node { // TODO: remove?

	/**
	 * Constructs a new nop node.
	 */
	public NopNode(SourceLocation location) {
		super(location);
	}

	@Override
	public String toString() {
		return "nop";
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
