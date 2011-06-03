package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.optimizer.FlowGraphDecorator;
import dk.brics.tajs.util.Collections;

import java.util.Map;
import java.util.Set;

/**
 * Template class for backward transfer functions. (i.e. for situations where the state depends on the future.)
 */
public abstract class BackwardTransferFunction<T> extends AbstractTransferFunction<T> implements TransferFunction<T> {

    private StateFactory<T> factory;

    protected BackwardTransferFunction(StateFactory<T> factory) {
        this.factory = factory;
    }

    /**
     * Union a set of abstract states.
     */
    public abstract T union(Set<T> states);

    /**
     * Join function for *BACKWARD* *MAY* transfer functions.
     * (i.e. looks at successor nodes and uses union)
     * <p/>
     * NOTE: If you need a backward *MUST* analysis then split this class into two!
     */
    @Override
    public T join(FlowGraphDecorator flowGraph, Map<Node, T> states, Node node) {
        Set<Node> successorNodes = flowGraph.getAllSuccessorNodes(node);
        if (successorNodes.size() == 1) {
            return states.get(successorNodes.iterator().next());
        }

        Set<T> successorStates = Collections.newSet();
        for (Node successorNode : flowGraph.getAllSuccessorNodes(node)) {
            successorStates.add(states.get(successorNode));
        }
        return union(successorStates);
    }

}

