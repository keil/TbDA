package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Return exception node.
 * <p>
 * Must be the only node in its block.
 */
public class ExceptionalReturnNode extends Node {

	/**
	 * Constructs a new exceptional return node.
	 */
	public ExceptionalReturnNode(SourceLocation location) {
		super(location);
	}

	@Override
	public String toString() {
		return "exceptional-return";
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
