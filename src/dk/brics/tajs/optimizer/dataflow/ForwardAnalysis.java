package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.Node;
import dk.brics.tajs.optimizer.FlowGraphDecorator;
import dk.brics.tajs.util.Collections;

import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Solver for forward dataflow analyses.
 */
public class ForwardAnalysis<T> implements Analysis<T> {

    private FlowGraphDecorator flowGraph;
    private Function function;

    private Map<Node, T> states = Collections.newMap();

    public ForwardAnalysis(FlowGraphDecorator flowGraph, Function function) {
        this.flowGraph = flowGraph;
        this.function = function;
    }

    /**
     * Solve the constraint system using the working list algorithm.
     * <p/>
     * Note: 1) Initially only the entry block is added to the queue.
     * Note: 2) null is used as the initial state. (bottom element)
     */
    @Override
    public Map<Node, T> solve(TransferFunction<T> transferFunction) {
        Queue<Node> queue = Collections.newQueue();
        queue.add(function.getEntry().getFirstNode());
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            T currentState = states.get(node);
            T newState = transferFunction.transfer(flowGraph, states, node);
            states.put(node, newState);
            if (!newState.equals(currentState)) {
                queue.addAll(depends(node));
            }
        }
        return states;
    }

    @Override
    public Set<Node> depends(Node node) {
        return flowGraph.getAllSuccessorNodes(node);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Forward Analysis:\n");
        sb.append("Function: ");
        sb.append(function.getName());
        sb.append(":\n");
        for (BasicBlock basicBlock : function.getBlocks()) {
            for (Node node : basicBlock.getNodes()) {
                sb.append("\tnode: ");
                sb.append(node.getIndex());
                sb.append(" {");
                sb.append(states.get(node));
                sb.append("}\n");
            }
        }
        return sb.toString();
    }

}
