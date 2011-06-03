package dk.brics.tajs.flowgraph.nodes;

/**
 * Interface for nodes that may read a variable.
 */
public interface IReadsVariable { // TODO: remove IReadsVariable?

	/**
	 * Returns the source variable name, or null if this node does not read a variable.
	 */
	public String getVarName();
}
