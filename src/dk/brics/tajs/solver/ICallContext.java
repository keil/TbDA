package dk.brics.tajs.solver;

/**
 * Interface for analysis call contexts.
 * Must be immutable.
 */
public interface ICallContext {

	/**
	 * Checks whether this context is equal to the given object. 
	 */
	@Override
	public boolean equals(Object obj);
	
	/**
	 * Computes a hash code for this context.
	 */
	@Override
	public int hashCode();
	
	/**
	 * Returns a description of this call context.
	 */
	@Override
	public String toString();
}
