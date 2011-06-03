package dk.brics.tajs.optimizer.dataflow;

public class ReadVariableStateFactory implements StateFactory<ReadVariableState> {
    @Override
    public ReadVariableState newState() {
        return new ReadVariableState();
    }
}
