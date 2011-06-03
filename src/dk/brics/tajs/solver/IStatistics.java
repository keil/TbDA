package dk.brics.tajs.solver;

import dk.brics.tajs.analysis.dom.DOMSpec;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Node;

/**
 * Records various information during fixpoint solving.
 */
public interface IStatistics {

	/**
	 * Registers a node transfer occurrence.
	 */
	public void registerNodeTransfer(Node n);
	
	/**
	 * Registers a block transfer occurrence.
	 */
	public void registerBlockTransfer();
	
	/**
	 * Registers new flow into a basic block for a context.
	 */
	public void registerNewFlow(BasicBlock b, ICallContext c, String diff);
	
	/**
	 * Returns the total number of node transfers.
	 */
	public int getTotalNumberOfNodeTransfers();
	
	/**
	 * Registers the given node.
	 */
	public void count(Node n);
	
	/**
	 * Registers a recovery of an unknown value.
	 */
	public void registerUnknownValueResolve();
	
	/**
	 * Registers a join operation.
	 */
	public void registerJoin();
	
	/**
	 * Register a finished iteration in the fixpoint loop
	 */
	public void registerFinishedIteration();
	
	public void registerBegin();

    /**
     * Register a DOM property.
     */
    public void registerDOMProperty(DOMSpec spec);

    /**
     * Register a DOM function.
     */
    public void registerDOMFunction(DOMSpec spec);

}
