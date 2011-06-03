package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.optimizer.FlowGraphDecorator;
import dk.brics.tajs.util.Collections;

import java.util.Map;
import java.util.Set;

/**
 * Template class for forward transfer functions. (i.e. for situations where the state depends on the past)
 */
public abstract class ForwardTransferFunction<T> extends AbstractTransferFunction<T> implements TransferFunction<T> {

    private StateFactory<T> factory;

    protected ForwardTransferFunction(StateFactory<T> factory) {
        this.factory = factory;
    }

    public abstract T intersection(Set<T> states);

    /**
     * Join function for *FORWARD* *MUST* transfer functions.
     * (i.e. looks at predecessors nodes and uses intersection)
     * <p/>
     * NOTE: If you need a forward *MAY* analysis split this class into two!
     */
    @Override
    public T join(FlowGraphDecorator flowGraph, Map<Node, T> states, Node node) {
        // Case 1: The first Node in the BasicBlock
        if (node == node.getBlock().getFirstNode()) {
            // Case 1.1: The first Node in the whole Function
            if (node == node.getBlock().getFunction().getEntry().getFirstNode()) {
                return factory.newState();
            }
            // Case 1.2: Not the first Node in the whole Function
            else {
                Set<T> predecessorStates = Collections.newSet();
                for (BasicBlock basicBlock : flowGraph.getPredecessorBlocks(node.getBlock())) {
                    Node predecessorNode = basicBlock.getLastNode();
                    predecessorStates.add(states.get(predecessorNode));
                }
                return intersection(predecessorStates);
            }
        }
        // Case 2: NOT the first Node in BasicBlock
        else {
            return states.get(flowGraph.getPredecessorNode(node));
        }
    }

}
