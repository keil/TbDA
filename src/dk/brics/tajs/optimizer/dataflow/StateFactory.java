package dk.brics.tajs.optimizer.dataflow;

/**
 * Interface for state factories.
 */
public interface StateFactory<T> {

    public T newState();

}
