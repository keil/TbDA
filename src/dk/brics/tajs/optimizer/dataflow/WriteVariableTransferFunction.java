package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.nodes.EnterWithNode;
import dk.brics.tajs.flowgraph.nodes.WriteVariableNode;
import dk.brics.tajs.optimizer.FlowGraphDecorator;
import dk.brics.tajs.options.Options;

import java.util.Set;

public class WriteVariableTransferFunction extends ForwardTransferFunction<WriteVariableState> implements TransferFunction<WriteVariableState> {

    public WriteVariableTransferFunction(FlowGraphDecorator flowGraph) {
        super(new WriteVariableStateFactory());
    }

    @Override
    public WriteVariableState transferEnterWithNode(EnterWithNode node, WriteVariableState state) {
        if (Options.isDebugEnabled()) {
            System.out.println("Warning: Flow Graph contains 'with'-statements. Optimization may be sub-optimal.");
        }
        return new WriteVariableState(); // Useless, but sound information.
    }

    @Override
    public WriteVariableState transferWriteVariableNode(WriteVariableNode node, WriteVariableState state) {
        WriteVariableState result = state.clone();
        result.remove(node.getVarName());
        result.add(node);
        return result;
    }

    @Override
    public WriteVariableState intersection(Set<WriteVariableState> states) {
        return WriteVariableState.intersection(states);
    }

}
