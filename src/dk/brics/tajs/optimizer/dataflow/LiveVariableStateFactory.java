package dk.brics.tajs.optimizer.dataflow;

public class LiveVariableStateFactory implements StateFactory<LiveVariableState> {
    @Override
    public LiveVariableState newState() {
        return new LiveVariableState();
    }
}
