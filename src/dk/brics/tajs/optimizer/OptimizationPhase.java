package dk.brics.tajs.optimizer;

import dk.brics.tajs.flowgraph.FlowGraph;

/**
 * An interface for optimization phases.
 */
public interface OptimizationPhase {

    /**
     * Run this optimization phase on the given flowgraph.
     */
    public void optimize(FlowGraph flowGraph, FlowGraphDecorator flowGraphDecorator);

    /**
     * Returns the time spent performing this optimization.
     */
    public int getTime();

    /**
     * Returns the total number of removed basic blocks.
     */
    public int getNumberOfRemovedBlocks();

    /**
     * Returns the total number of removed nodes.
     */
    public int getNumberOfRemovedNodes();

}
