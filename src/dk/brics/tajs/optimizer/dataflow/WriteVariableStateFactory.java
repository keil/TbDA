package dk.brics.tajs.optimizer.dataflow;

public class WriteVariableStateFactory implements StateFactory<WriteVariableState> {
    @Override
    public WriteVariableState newState() {
        return new WriteVariableState();
    }
}
