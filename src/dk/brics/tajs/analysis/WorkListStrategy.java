package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.IWorkListStrategy;

/**
 * Work list strategy.
 */
public class WorkListStrategy implements IWorkListStrategy<CallContext> {
	
	private CallGraph<State,CallContext> call_graph;
	
	/**
	 * Constructs a new WorkListStrategy object.
	 */
	public WorkListStrategy() {}
	
	/**
	 * Sets the call graph.
	 */
	public void setCallGraph(CallGraph<State,CallContext> call_graph) {
		this.call_graph = call_graph;
	}

	@Override
	public int compare(IEntry<CallContext> e1, IEntry<CallContext> e2) { // TODO: good worklist ordering for fast convergence? 
		BasicBlock n1 = e1.getBlock();
		BasicBlock n2 = e2.getBlock();
		int serial1 = e1.getSerial();
		int serial2 = e2.getSerial();
//		int impact1 = e1.getImpact();
//		int impact2 = e2.getImpact();
		
		if (serial1 == serial2)
			return 0;
		
		final int E1_FIRST = -1;
		final int E2_FIRST = 1;
		
//		boolean exit1 = n1.isOrdinaryExit() || n1.isExceptionalExit();
//		boolean exit2 = n2.isOrdinaryExit() || n2.isExceptionalExit();
//		boolean entry1 = n1.isEntry();
//		boolean entry2 = n2.isEntry();
//		boolean call1 = n1.getFirstNode() instanceof CallNode;
//		boolean call2 = n2.getFirstNode() instanceof CallNode;

		if (n1.getFunction().equals(n2.getFunction()) && e1.getContext().equals(e2.getContext())) {
			// same function and same context: use block order
			if (n1.getOrder() < n2.getOrder())
				return E1_FIRST;
			else if (n2.getOrder() < n1.getOrder())
				return E2_FIRST; 
		}
		
		int function_context_order1 = call_graph.getFunctionContextOrder(n1.getFunction(), e1.getContext());
		int function_context_order2 = call_graph.getFunctionContextOrder(n2.getFunction(), e2.getContext());
		
		// different function/context: order by occurrence number
		if (function_context_order1 < function_context_order2)
			return E2_FIRST;
		else if (function_context_order2 < function_context_order1)
			return E1_FIRST;
		
//		// strategy: low priority for exit nodes
//		if (exit1 && !exit2)
//			return E2_FIRST;
//		if (exit2 && !exit1)
//			return E1_FIRST;

//
//		// strategy: low priority for entry nodes
//		if (entry1 && !entry2)
//			return E2_FIRST;
//		if (entry2 && !entry1)
//			return E1_FIRST;

//		// strategy: high priority for entry nodes
//		if (entry1 && !entry2)
//			return E1_FIRST;
//		if (entry2 && !entry1)
//			return E2_FIRST;

//		// strategy: low priority for call nodes
//		if (call1 && !call2)
//			return E2_FIRST;
//		if (call2 && !call1)
//			return E1_FIRST;
		
//		// strategy: high impact first
//		if (impact1 > impact2)
//			return E1_FIRST;
//		if (impact1 < impact2)
//			return E2_FIRST;
//		
		 // TODO: indegree? store size? number of times visited?
		
//		// strategy: low indegrees first
//		if (indegree1 < indegree2)
//			return THIS_FIRST;
//		if (indegree1 > indegree2)
//			return THIS_LAST;
		
//		// strategy: large stacks first
//		if (stacksize1 > stacksize2)
//			return THIS_FIRST;
//		if (stacksize1 < stacksize2)
//			return THIS_LAST;
//		
//		int storesize1 = original_storesize;
//		int storesize2 = p.original_storesize;
//
//		// strategy: large stores first
//		if (storesize1 > storesize2)
//			return THIS_FIRST;
//		if (storesize1 < storesize2)
//			return THIS_LAST;

//		// strategy: penalize nodes that have been visited many times
//		if (transfers1 < transfers2)
//			return THIS_FIRST;
//		if (transfers1 > transfers2)
//			return THIS_LAST;
		
//		// strategy: depth first
//		return p.serial - serial;

		// strategy: breadth first
		return serial1 - serial2;
	}
}
