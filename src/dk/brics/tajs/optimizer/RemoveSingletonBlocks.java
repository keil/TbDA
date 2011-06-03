package dk.brics.tajs.optimizer;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.flowgraph.nodes.*;
import dk.brics.tajs.util.Collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Removes basic blocks with a single node and a single predecessor/successor.
 */
public class RemoveSingletonBlocks implements OptimizationPhase {

    private int time = 0;
    private int removedBlocks = 0;
    private int removedNodes = 0;

    // TODO: Currently only collapses blocks with a single node.

    @Override
    public void optimize(FlowGraph flowGraph, FlowGraphDecorator flowGraphDecorator) {
        long startTime = System.currentTimeMillis();
        for (Function function : flowGraph.getFunctions()) {
            removeSingletonBlocks(flowGraphDecorator, function);
        }
        time = (int) (System.currentTimeMillis() - startTime);
    }

    private void removeSingletonBlocks(FlowGraphDecorator flowGraphDecorator, Function function) {
        Iterator<BasicBlock> iterator = function.getBlocks().iterator();
        while (iterator.hasNext()) {
            BasicBlock basicBlock = iterator.next();

            // Does the basic block contain a single node?
            if (basicBlock.getFirstNode() != basicBlock.getLastNode()) {
                continue;
            }

            // Can the basic block exit by an exception?
            if (basicBlock.canThrowExceptions()) {
                continue;
            }

            // Must the node have its own basic block?
            if (requiresOwnBlock(basicBlock)) {
                continue;
            }

            Node node = basicBlock.getSingleNode();

            // Case 1: The basic block is the first block in the function
            if (basicBlock.isEntry()) {
                if (basicBlock.getSuccessors().size() == 1) {
                    BasicBlock successorBlock = basicBlock.getSingleSuccessor();
                    if (!requiresOwnBlock(successorBlock)) {
                        successorBlock.getNodes().add(0, node);
                        node.setBlock(successorBlock);

                        function.setEntry(successorBlock);
                        iterator.remove();
                        removedBlocks++;
                        continue;
                    }
                }
            }

            // Case 2: The basic block is NOT the first block in the function
            Collection<BasicBlock> predecessorBlocks = getPredecessorBlocks(basicBlock);
            Collection<BasicBlock> successorBlocks = getSuccessorBlocks(basicBlock);
            if (predecessorBlocks.size() != 1 || successorBlocks.size() != 1) {
                continue;
            }

            BasicBlock predecessorBlock = predecessorBlocks.iterator().next();
            BasicBlock successorBlock = successorBlocks.iterator().next();

            if (!requiresOwnBlock(predecessorBlock)) {
                if (getSuccessorBlocks(predecessorBlock).size() == 1) {
                    // Push up
                    predecessorBlock.getNodes().add(node);
                    node.setBlock(predecessorBlock);

                    predecessorBlock.getSuccessors().remove(basicBlock);
                    predecessorBlock.getSuccessors().add(successorBlock);
                    iterator.remove();
                    removedBlocks++;
                    continue;
                }
            }

            if (!requiresOwnBlock(successorBlock)) {
                if (getPredecessorBlocks(successorBlock).size() == 1) {
                    // Push down
                    successorBlock.getNodes().add(0, node);
                    node.setBlock(successorBlock);

                    predecessorBlock.getSuccessors().remove(basicBlock);
                    predecessorBlock.getSuccessors().add(successorBlock);
                    iterator.remove();
                    removedBlocks++;
                    continue;
                }
            }
        }

    }

    /**
     * Returns the set of predecessor blocks for the given block.
     * (Note: Must be recomputed every time, because we change the flow graph.)
     */
    private Collection<BasicBlock> getPredecessorBlocks(BasicBlock basicBlock) {
        Set<BasicBlock> result = Collections.newSet();
        for (BasicBlock bs : basicBlock.getFunction().getBlocks()) {
            if (bs.getSuccessors().contains(basicBlock)) {
                result.add(bs);
            }
        }
        return result;
    }

    /**
     * Returns the set of successor blocks for the given block.
     */
    private Collection<BasicBlock> getSuccessorBlocks(BasicBlock basicBlock) {
        return basicBlock.getSuccessors();
    }

    /**
     * Returns true iff the basic block contains exactly one node and it requires its own block.
     */
    private boolean requiresOwnBlock(BasicBlock basicBlock) {
        Node node = basicBlock.getFirstNode();
        return basicBlock.getFirstNode() == basicBlock.getLastNode()
                && (node instanceof CallNode
                || node instanceof DeclareFunctionNode
                || node instanceof ExceptionalReturnNode
                || node instanceof EventDispatcherNode
                || node instanceof ReturnNode
                || node instanceof EnterWithNode
                || node instanceof LeaveWithNode
                || node instanceof ContextDependencyPushNode
                || node instanceof ContextDependencyPopNode);
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
