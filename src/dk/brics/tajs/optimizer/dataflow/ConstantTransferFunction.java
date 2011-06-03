package dk.brics.tajs.optimizer.dataflow;

import dk.brics.tajs.flowgraph.nodes.ConstantNode;

import java.util.Set;

public class ConstantTransferFunction extends ForwardTransferFunction<ConstantState> implements TransferFunction<ConstantState> {

    public ConstantTransferFunction() {
        super(new ConstantStateFactory());
    }

    @Override
    public ConstantState transferConstantNode(ConstantNode node, ConstantState state) {
        ConstantState result = state.clone();
        result.add(node);
        return result;
    }

    @Override
    public ConstantState intersection(Set<ConstantState> states) {
        return ConstantState.intersection(states);
    }

}
