package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.Node;

import java.util.Map;
import java.util.Set;

/**
 * Interface for simple data flow analyses on flow graphs.
 */
public interface Analysis<T> {

    public Map<Node, T> solve(TransferFunction<T> transferFunction);

    public Set<Node> depends(Node node);

}
