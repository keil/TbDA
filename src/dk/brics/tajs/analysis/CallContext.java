package dk.brics.tajs.analysis;

import static dk.brics.tajs.util.Collections.newSet;

import java.util.Set;

import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.ICallContext;

/**
 * Call context for context sensitive analysis.
 * <p>
 * This particular context sensitivity strategy distinguishes between abstract states
 * that have different possible values of 'this'.
 */
public final class CallContext implements ICallContext { // TODO: try other context sensitivity heuristics?
	
	// TODO: more experiments with k-CFA (commented out below)
//	private final int K = 3;
	
	// TODO: use TVLA-like focus/blur at entry/exit to ensure (fixed?) singleton object label for 'this'
	
	// TODO: try merging @/* in thisval set?
	// FIXME: ===> remove subsumed contexts and replace them by the subsuming ones?
	
	private Set<ObjectLabel> thisval; // TODO: canonicalize?
	
//	private List<CallNode> call_nodes;
		
	/**
	 * Constructs a new context.
	 * caller_context and call_node are null when the initial context is constructed.
	 */
	public CallContext(State callee_state, Function f, CallContext caller_context, Node call_node) {
		if (!Options.isContextSensitivityDisabled()) {
			thisval = newSet();
			for (ExecutionContext es : callee_state.getExecutionContext())
				thisval.add(es.getThisObject());
//			call_nodes = new ArrayList<CallNode>();
//			if (caller_context != null)
//				call_nodes.addAll(caller_context.call_nodes);
//			call_nodes.add(call_node);
//			if (call_nodes.size() > K)
//				call_nodes.remove(0);
			if (Options.isDebugEnabled())
				if (thisval.size() > 1)
					System.out.println("creating CallContext with " + thisval.size()
							+ " object labels at " + f + " " + f.getSourceLocation()
							+ ": " + thisval);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!Options.isContextSensitivityDisabled()) {
			if (!(obj instanceof CallContext))
				return false;
			CallContext c = (CallContext) obj;
//			if ((call_nodes == null) != (c.call_nodes == null))
//				return false;
//			if (call_nodes != null && !call_nodes.equals(c.call_nodes))
//				return false;
			// strategy: keep states separate if different possible values of 'this' (using set equality)
			if (thisval == null && c.thisval == null)
				return true;
			if ((thisval == null) != (c.thisval == null))
				return false;
			return thisval.equals(c.thisval);
		}
		// default: join
		return true;
	}

	@Override
	public int hashCode() {
		// see equals
		return (thisval != null ? thisval.hashCode() : 0) 
//		+ (call_nodes != null ? call_nodes.hashCode() : 0)
		;
	}

	@Override
	public String toString() {
		if (!Options.isContextSensitivityDisabled()) 
			return "this="+thisval.toString();
		return "<any>";
	}
}
