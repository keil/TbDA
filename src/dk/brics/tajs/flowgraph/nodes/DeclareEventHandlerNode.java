package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Declare event handler node.
 */
public class DeclareEventHandlerNode extends Node {

    public enum Type {
        LOAD, UNLOAD, KEYBOARD, MOUSE, UNKNOWN
    }

	private int var;
    private Type type;

	/**
	 * Constructs a new event declaration node.
	 */
	public DeclareEventHandlerNode(int var, Type type, SourceLocation sl) {
		super(sl);
		this.var = var;
        this.type = type;
	}

	/**
	 * Returns the variable number for the event handler function.
	 */
	public int getFunction() {
		return var;
	}

    /**
     * The type of this event handler
     */
    public Type getType() {
        return type;
    }

    @Override
	public String toString() {
		return "declare-event-handler [v" + var + ", " + type + "]";
	}

	@Override
	public boolean canThrowExceptions() {
		return false;
	}

	@Override
	public <Arg> void visitBy(NodeVisitor<Arg> v, Arg a) {
		v.visit(this, a);
	}
}