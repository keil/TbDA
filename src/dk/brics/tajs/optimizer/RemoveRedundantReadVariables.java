package dk.brics.tajs.optimizer;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.nodes.ReadVariableNode;
import dk.brics.tajs.optimizer.dataflow.Analysis;
import dk.brics.tajs.optimizer.dataflow.ForwardAnalysis;
import dk.brics.tajs.optimizer.dataflow.ReadVariableState;
import dk.brics.tajs.optimizer.dataflow.ReadVariableTransferFunction;
import dk.brics.tajs.util.Collections;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Optimize redudant read-variables
 * <p/>
 * Example:
 * <p/>
 * node 3: constant[42.0,v3] (test/optimization/redundant_read.js:1)
 * node 4: write-variable[v3,'a'] (test/optimization/redundant_read.js:1)
 * node 5: read-variable['a',v6,v7] (test/optimization/redundant_read.js:2)
 * node 6: read-variable['a',v8,v9] (test/optimization/redundant_read.js:2)
 * node 7: +[v6,v8,v5] (test/optimization/redundant_read.js:2)
 */
public class RemoveRedundantReadVariables implements OptimizationPhase {

    private int time = 0;
    private int removedBlocks = 0;
    private int removedNodes = 0;

    @Override
    public void optimize(FlowGraph flowGraph, FlowGraphDecorator decorator) {
        long startTime = System.currentTimeMillis();
        // Iterate through all functions removing redundant read variable nodes
        for (Function function : flowGraph.getFunctions()) {

            // Variables in the global scope might be changed by other functions.
            if (function.isMain()) {
                continue;
            }

            // Analyze what variables have definitely been read at a particular program point
            ReadVariableTransferFunction transferFunction = new ReadVariableTransferFunction();
            Analysis<ReadVariableState> analysis = new ForwardAnalysis<ReadVariableState>(decorator, function);
            Map<Node, ReadVariableState> map = analysis.solve(transferFunction);

            // Remove redudant read variables
            removeRedundantReadVariables(decorator, function, map);
        }
        time = (int) (System.currentTimeMillis() - startTime);
    }

    /**
     * Remove redudant read variables nodes for the given function and map of available read variables.
     */
    private void removeRedundantReadVariables(FlowGraphDecorator decorator, Function function, Map<Node, ReadVariableState> map) {

        Set<Node> toKill = Collections.newSet();
        Set<String> declaredVariables = decorator.getDeclaredVariables(function);

        for (BasicBlock basicBlock : function.getBlocks()) {
            for (Node node : basicBlock.getNodes()) {
                if (node instanceof ReadVariableNode) {
                    ReadVariableNode target = (ReadVariableNode) node;
                    ReadVariableState state = map.get(target);

                    // No state. Do nothing. Unreachable code.
                    if (state == null) {
                        continue;
                    }

                    // If the target variable is v1 we keep the read variable node
                    if (target.getResultVar() == 1) {
                        continue;
                    }

                    // Skip arrays
                    if ("Array".equals(target.getVarName())) {
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

                    // Retrieve the list of available read variables
                    Set<ReadVariableNode> readVariables = state.get(target.getVarName());

                    // Update the set of readVariables (remove the ReadVariableNode itself + any already removed nodes)
                    Iterator<ReadVariableNode> iter = readVariables.iterator();
                    while (iter.hasNext()) {
                        ReadVariableNode readVariableNode = iter.next();
                        if (target == readVariableNode || toKill.contains(readVariableNode)) {
                            iter.remove();
                        }
                    }

                    // Redundant Read?
                    if (readVariables.size() > 0) {
                        ReadVariableNode replacement = null;
                        for (ReadVariableNode r : readVariables) {
                            if (replacement == null) {
                                replacement = r;
                            } else {
                                if (replacement.getResultVar() > r.getResultVar()) {
                                    replacement = r;
                                }
                            }
                        }

                        decorator.replaceVariable(basicBlock, target.getResultVar(), replacement.getResultVar());
                        decorator.replaceVariable(basicBlock, target.getResultBaseVar(), replacement.getResultBaseVar());

                        toKill.add(target);
                    }
                }

            }
        }

        // Were there any redundant reads?
        if (toKill.size() > 0) {
            removedNodes = removedNodes + toKill.size();
            // Remove redundant nodes
            for (BasicBlock basicBlock : function.getBlocks()) {
                Iterator<Node> iter = basicBlock.getNodes().iterator();
                while (iter.hasNext()) {
                    if (toKill.contains(iter.next())) {
                        iter.remove();
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
