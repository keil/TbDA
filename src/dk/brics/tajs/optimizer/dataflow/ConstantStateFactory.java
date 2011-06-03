package dk.brics.tajs.optimizer.dataflow;

public class ConstantStateFactory implements StateFactory<ConstantState> {
    @Override
    public ConstantState newState() {
        return new ConstantState();
    }
}
