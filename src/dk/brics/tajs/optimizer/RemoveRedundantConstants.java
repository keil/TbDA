package dk.brics.tajs.optimizer;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.nodes.ConstantNode;
import dk.brics.tajs.optimizer.dataflow.Analysis;
import dk.brics.tajs.optimizer.dataflow.ConstantState;
import dk.brics.tajs.optimizer.dataflow.ConstantTransferFunction;
import dk.brics.tajs.optimizer.dataflow.ForwardAnalysis;
import dk.brics.tajs.util.Collections;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Remove Redundant Constant Declarations
 * <p/>
 * Example:
 * node 68: constant[null,v45] (test/sunspider/access-binary-trees.js:29)
 * node 69: constant[null,v46] (test/sunspider/access-binary-trees.js:29)
 */
/*
 * TODO: Current broken & disabled:
 * <p/>
 * <p/>
 * var foo = 0;
 * function a(offset) {
 * foo += offset;
 * foo = foo < 0 ? 1 : foo;
 * dumpValue(foo);
 * foo = foo > 2 ? 0 : foo;
 * dumpValue(foo);
 * }
 * a(+1);
 * a(-1);
 *
 * REASON: It is unsound to remove a ConstantNode that writes to temp. X if X is also written to by some other node.
 * In this case the replace operation will fail.
 */
public class RemoveRedundantConstants implements OptimizationPhase {

    private int time = 0;
    private int removedBlocks = 0;
    private int removedNodes = 0;

    @Override
    public void optimize(FlowGraph flowGraph, FlowGraphDecorator decorator) {
        long startTime = System.currentTimeMillis();
        for (Function function : flowGraph.getFunctions()) {

            ConstantTransferFunction transferFunction = new ConstantTransferFunction();
            Analysis<ConstantState> analysis = new ForwardAnalysis<ConstantState>(decorator, function);
            Map<Node, ConstantState> map = analysis.solve(transferFunction);

            removeRedundantConstants(decorator, function, map);
        }
        time = (int) (System.currentTimeMillis() - startTime);
    }

    /**
     * Remove redundant constant nodes.
     */
    private void removeRedundantConstants(FlowGraphDecorator decorator, Function function, Map<Node, ConstantState> map) {
        Set<Node> toKill = Collections.newSet();

        for (BasicBlock basicBlock : function.getBlocks()) {
            for (Node node : basicBlock.getNodes()) {
                if (node instanceof ConstantNode) {
                    ConstantNode target = (ConstantNode) node;
                    ConstantState state = map.get(target);

                    // No state. Do nothing. Unreachable code.
                    if (state == null) {
                        continue;
                    }

                    // If the target variable is v1 we keep the constant variable node
                    if (target.getResultVar() == 1) {
                        continue;
                    }

                    Set<ConstantNode> constantNodes = state.get(target);
                    Iterator<ConstantNode> iterator = constantNodes.iterator();
                    while (iterator.hasNext()) {
                        ConstantNode constantNode = iterator.next();
                        if (constantNode == target || toKill.contains(constantNode)) {
                            iterator.remove();
                        }
                    }

                    if (constantNodes.size() > 0) {
                        ConstantNode replacement = null;
                        for (ConstantNode r : constantNodes) {
                            if (replacement == null) {
                                replacement = r;
                            } else if (replacement.getResultVar() > r.getResultVar()) {
                                replacement = r;
                            }
                        }
                        decorator.replaceVariable(basicBlock, target.getResultVar(), replacement.getResultVar());
                        toKill.add(target);
                    }

                }
            }
        }

        if (toKill.size() > 0) {
            removedNodes = removedNodes + toKill.size();
            for (BasicBlock basicBlock : function.getBlocks()) {
                Iterator<Node> iterator = basicBlock.getNodes().iterator();
                while (iterator.hasNext()) {
                    Node node = iterator.next();
                    if (toKill.contains(node)) {
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
