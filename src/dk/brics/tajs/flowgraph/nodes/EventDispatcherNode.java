package dk.brics.tajs.flowgraph.nodes;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.NodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Event dispatcher node.
 */
public class EventDispatcherNode extends Node {

    public enum Type {
        LOAD, UNLOAD, OTHER
    }

    private Type type;

	/**
	 * Constructs a new event dispatcher node.
	 */
	public EventDispatcherNode(Type type, SourceLocation location) {
		super(location);
        this.type = type;
	}

    public Type getType() {
        return type;
    }

	@Override
	public boolean canThrowExceptions() {
		return true;
	}

    @Override
	public String toString() {
		return "event-dispatcher <" + type + ">";
	}

	@Override
	public <Arg> void visitBy(NodeVisitor<Arg> v, Arg a) {
		v.visit(this, a);
	}
}
