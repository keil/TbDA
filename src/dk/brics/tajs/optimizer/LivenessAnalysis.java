package dk.brics.tajs.optimizer;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.nodes.IfNode;
import dk.brics.tajs.optimizer.dataflow.BackwardAnalysis;
import dk.brics.tajs.optimizer.dataflow.LiveVariableState;
import dk.brics.tajs.optimizer.dataflow.LiveVariableTransferFunction;
import dk.brics.tajs.optimizer.dataflow.TransferFunction;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Determines which variables *MAY* be live *AFTER* each basic block.
 */
public class LivenessAnalysis {

    public void attachLivenessInfo(FlowGraph flowGraph, FlowGraphDecorator flowGraphDecorator) {

        for (Function function : flowGraph.getFunctions()) {
            // Perform liveness analysis
            TransferFunction<LiveVariableState> transferFunction = new LiveVariableTransferFunction();
            BackwardAnalysis<LiveVariableState> analysis = new BackwardAnalysis<LiveVariableState>(flowGraphDecorator, function);
            Map<Node, LiveVariableState> states = analysis.solve(transferFunction);

            // Attach live variables to each basic block
            for (BasicBlock basicBlock : function.getBlocks()) {
                LiveVariableState state = transferFunction.join(flowGraphDecorator, states, basicBlock.getLastNode());

                Set<Integer> liveVariables = state.getLiveVariables();

                // NOTE: The EdgeTransfer function may use the conditional variable contained in an if node.
                // We check if the last node of the basic block is an if node, and if it is, we add
                // the conditional variable to the set of live variables.
                if (basicBlock.getLastNode() instanceof IfNode) {
                    IfNode ifNode = (IfNode) basicBlock.getLastNode();
                    liveVariables.add(ifNode.getConditionVar());
                }

                int[] result = new int[liveVariables.size()];
                Iterator<Integer> iterator = liveVariables.iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    result[i] = iterator.next();
                    i++;
                }
                basicBlock.setLiveVariables(result);
            }
        }

    }

}
