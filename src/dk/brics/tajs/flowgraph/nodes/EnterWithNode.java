package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Enter-with node.
 * <p>
 * with(<i>v</i>) { ... }
 */
public class EnterWithNode extends Node {

	private int object_var;

	/**
	 * Constructs a new enter with node.
	 */
	public EnterWithNode(int object_var, SourceLocation location) {
		super(location);
		this.object_var = object_var;
	}

	/**
	 * Returns the object variable number.
	 */
	public int getObjectVar() {
		return object_var;
	}

    /**
     * Sets the object variable number.
     */
    public void setObjectVar(int var) {
        this.object_var = var;
    }
	
	@Override
	public String toString() {
		return "enter-with[v" + object_var + "]";
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
