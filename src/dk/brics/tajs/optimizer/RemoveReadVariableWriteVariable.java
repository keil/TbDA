package dk.brics.tajs.optimizer;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.nodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.nodes.WriteVariableNode;
import dk.brics.tajs.optimizer.dataflow.*;
import dk.brics.tajs.util.Collections;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Optimize a WriteVariableNode followed by a ReadVariableNode for the same variable.
 * <p/>
 * Examples:
 * node 129: write-variable[v81,'check'] (test/sunspider/access-binary-trees.js:48)
 * node 130: read-variable['check',v95,v96] (test/sunspider/access-binary-trees.js:49)
 */
public class RemoveReadVariableWriteVariable implements OptimizationPhase {

    private int time = 0;
    private int removedBlocks = 0;
    private int removedNodes = 0;

    @Override
    public void optimize(FlowGraph flowGraph, FlowGraphDecorator decorator) {
        long startTime = System.currentTimeMillis();
        for (Function function : flowGraph.getFunctions()) {

            // Variables in the global scope might be changed by other functions.
            if (function.isMain()) {
                continue;
            }

            // Analyse available write variables
            WriteVariableTransferFunction writeVariableTransfer = new WriteVariableTransferFunction(decorator);
            Analysis<WriteVariableState> writeVariableAnalysis = new ForwardAnalysis<WriteVariableState>(decorator, function);
            Map<Node, WriteVariableState> writeVariableMap = writeVariableAnalysis.solve(writeVariableTransfer);

            // Analyse live variables (used to determine what read variable nodes can safely be killed)
            TransferFunction<LiveVariableState> liveVariableTransfer = new LiveVariableTransferFunction();
            Analysis<LiveVariableState> liveVariableAnalysis = new BackwardAnalysis<LiveVariableState>(decorator, function);
            Map<Node, LiveVariableState> liveVariableMap = liveVariableAnalysis.solve(liveVariableTransfer);

            removeRedundantReadVariables(decorator, function, writeVariableMap, liveVariableMap, liveVariableTransfer);
        }
        time = (int) (System.currentTimeMillis() - startTime);
    }

    private void removeRedundantReadVariables(FlowGraphDecorator decorator, Function function,
                                              Map<Node, WriteVariableState> writeVariableMap,
                                              Map<Node, LiveVariableState> liveVariableMap,
                                              TransferFunction<LiveVariableState> liveVariableTransfer) {

        Set<Node> toKill = Collections.newSet();
        Set<String> declaredVariables = decorator.getDeclaredVariables(function);

        for (BasicBlock basicBlock : function.getBlocks()) {
            for (Node node : basicBlock.getNodes()) {
                if (node instanceof ReadVariableNode) {
                    ReadVariableNode target = (ReadVariableNode) node;
                    WriteVariableState state = writeVariableMap.get(target);

                    // No state. Do nothing. Unreachable code.
                    if (state == null) {
                        continue;
                    }

                    // If the target variable is v1 we keep the read variable node
                    if (target.getResultVar() == 1) {
                        continue;
                    }

                    // Skip arrays
                    if (target.getVarName().equals("Array")) {
                        continue;
                    }

                    // Skip variables that are not declared
                    if (!declaredVariables.contains(target.getVarName())) {
                        continue;
                    }

                    // Avoid variables (possibly) overwritten by inner functions
                    if (decorator.getInnerVariablesWritten(function).contains(target.getVarName())) {
                        continue;
                    }

                    WriteVariableNode replacement = state.get(target.getVarName());
                    if (replacement != null) {
                        // Kill ReadVariable, unless the activation object variable is live (determined by liveness analysis)
                        // Note: We use the join-function because we want the state immediately before the node
                        LiveVariableState liveState = liveVariableTransfer.join(decorator, liveVariableMap, node);
                        if (!liveState.isAlive(target.getResultBaseVar())) {
                            decorator.replaceVariable(basicBlock, target.getResultVar(), replacement.getValueVar());
                            toKill.add(target);
                        }
                    }
                }
            }
        }

        if (toKill.size() > 0) {
            removedNodes = removedNodes + toKill.size();

            for (BasicBlock basicBlock : function.getBlocks()) {
                Iterator<Node> iterator = basicBlock.getNodes().iterator();
                while (iterator.hasNext()) {
                    if (toKill.contains(iterator.next())) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public int getNumberOfRemovedBlocks() {
        return removedBlocks;
    }

    @Override
    public int getNumberOfRemovedNodes() {
        return removedNodes;
    }

}
