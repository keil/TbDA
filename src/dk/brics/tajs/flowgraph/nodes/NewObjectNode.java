package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * New object node.
 * <p>
 * <i>v</i> = {}
 */
public class NewObjectNode extends AssignmentNode {

	/**
	 * New object node.
	 */
	public NewObjectNode(Function function, int result_var, SourceLocation location) {
		super(result_var, location);
	}

	@Override
	public String toString() {
		return "new[v" + getResultVar() + "]";
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
