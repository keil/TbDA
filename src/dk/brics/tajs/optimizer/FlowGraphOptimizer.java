package dk.brics.tajs.optimizer;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collections;

/**
 * Optimize a flowgraph in phases.
 */
public class FlowGraphOptimizer {

    private List<OptimizationPhase> phases = Collections.newList();
    private int originalNumberOfBlocks = 0;
    private int originalNumberOfNodes = 0;

    public FlowGraphOptimizer() {
    	if (!Options.isFlowGraphOptimizationDisabled()) {
    		phases.add(new RemoveRedundantReadVariables());
    		// phases.add(new RemoveRedundantConstants()); // TODO: Current broken & disabled
    		phases.add(new RemoveReadVariableWriteVariable());
    		phases.add(new RemoveSingletonBlocks());
    	}
    }

    /**
     * Runs optimization phases.
     */
    public void optimize(FlowGraph flowGraph) {
        // Save the original "size" of the flow graph
        originalNumberOfBlocks = flowGraph.getNumberOfBlocks();
        originalNumberOfNodes = flowGraph.getNumberOfNodes();

        // Remove empty basic blocks
        RemoveEmptyBlocks removeEmptyBlocksPhase = new RemoveEmptyBlocks();
        removeEmptyBlocksPhase.optimize(flowGraph);

        // Iterate through all phases
        for (OptimizationPhase phase : phases) {
            // Run phase
            FlowGraphDecorator flowGraphDecorator = new FlowGraphDecorator(flowGraph);
            phase.optimize(flowGraph, flowGraphDecorator);
            // Remove empty basic blocks
            removeEmptyBlocksPhase.optimize(flowGraph);
        }

        // complete the flowgraph
        flowGraph.complete();

        // Attach liveness information to each basic block
        FlowGraphDecorator decorator = new FlowGraphDecorator(flowGraph);
        LivenessAnalysis livenessAnalysis = new LivenessAnalysis();
        livenessAnalysis.attachLivenessInfo(flowGraph, decorator);
    }

    @Override
    public String toString() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        writer.printf("%1$-34s %2$12s %3$12s %4$12s %5$12s\n", "Optimization phase:", "Blocks", "Nodes", "Time", "Cost");

        int totalBlocks = 0;
        int totalNodes = 0;
        int totalTime = 0;

        for (OptimizationPhase phase : phases) {
            totalBlocks = totalBlocks + phase.getNumberOfRemovedBlocks();
            totalNodes = totalNodes + phase.getNumberOfRemovedNodes();
            totalTime = totalTime + phase.getTime();
            writer.printf(
                    "%1$-34s %2$12s %3$12s %4$12s %5$12.2f\n",
                    phase.getClass().getSimpleName(),
                    phase.getNumberOfRemovedBlocks(),
                    phase.getNumberOfRemovedNodes(),
                    phase.getTime() + " ms",
                    (double) phase.getTime() / (double) ((phase.getNumberOfRemovedBlocks() + phase.getNumberOfRemovedNodes() + 1))
            );
        }
        writer.print("Total blocks: " + totalBlocks + " / " + originalNumberOfBlocks + " (" + (totalBlocks * 100) / originalNumberOfBlocks + "%), ");
        writer.print("total nodes: " + totalNodes + " / " + originalNumberOfNodes + " (" + (totalNodes * 100) / originalNumberOfNodes + "%), ");
        writer.print("total optimization time: " + totalTime + " ms");
        writer.println();
        writer.close();
        return outputStream.toString();
    }

}
