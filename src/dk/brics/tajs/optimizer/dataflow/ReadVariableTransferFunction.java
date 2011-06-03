package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.nodes.*;
import dk.brics.tajs.options.Options;

import java.util.Set;

public class ReadVariableTransferFunction extends ForwardTransferFunction<ReadVariableState> implements TransferFunction<ReadVariableState> {

    public ReadVariableTransferFunction() {
        super(new ReadVariableStateFactory());
    }

    @Override
    public ReadVariableState transferEnterWithNode(EnterWithNode node, ReadVariableState state) {
        if (Options.isDebugEnabled()) {
            System.out.println("Warning: Flow Graph contains 'with'-statements. Optimization may be sub-optimal.");
        }
        return new ReadVariableState(); // Useless, but sound information.
    }

    @Override
    public ReadVariableState transferLeaveWithNode(LeaveWithNode node, ReadVariableState state) {
        return new ReadVariableState();
    }

    @Override
    public ReadVariableState transferReadVariableNode(ReadVariableNode node, ReadVariableState state) {
        ReadVariableState result = state.clone();
        result.add(node);
        return result;
    }

    @Override
    public ReadVariableState transferWriteVariableNode(WriteVariableNode node, ReadVariableState state) {
        ReadVariableState result = state.clone();
        result.remove(node.getVarName());
        return result;
    }

    @Override
    public ReadVariableState intersection(Set<ReadVariableState> states) {
        return ReadVariableState.intersection(states);
    }

}
