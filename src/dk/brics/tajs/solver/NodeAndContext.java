package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.Node;

/**
 * Pair of a node and a call context.
 */
public final class NodeAndContext<CallContextType extends ICallContext> { // TODO: use NodeAndContext elsewhere?

	private Node n;
	
	private CallContextType c;
	
	public NodeAndContext(Node n, CallContextType c) {
		this.n = n;
		this.c = c;
	}
	
	public Node getNode() {
		return n;
	}
	
	public CallContextType getContext() {
		return c;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NodeAndContext))
			return false;
		NodeAndContext<CallContextType> ncp = (NodeAndContext<CallContextType>) obj;
		return ncp.n == n && ncp.c.equals(c);
	}

	@Override
	public int hashCode() {
		return n.getIndex() * 13 + c.hashCode() * 3;
	}
}
