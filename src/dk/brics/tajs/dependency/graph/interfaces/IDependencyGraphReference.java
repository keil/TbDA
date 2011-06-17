package dk.brics.tajs.dependency.graph.interfaces;

import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.dependency.graph.DependencyNode;

public interface IDependencyGraphReference<Arg> {

	/**
	 * returns the current reference to an dependency graph
	 * 
	 * @return reference t an an dependecy node
	 */
	public DependencyGraphReference getDependencyGraphReference();

	/**
	 * set an reference to the dependency graph
	 * 
	 * @param node
	 *            current dependency node
	 */
	public Arg setDependencyGraphReference(DependencyGraphReference reference);

	/**
	 * checks if the reference is set
	 * 
	 * @return true, if an reference is set, false otherwise
	 */
	public boolean hasDependencyGraphReference();

	/**
	 * prints the current graph structure
	 * 
	 * @return print the current dependency graph node
	 */
	public String printDependencyGraphReference();

	/**
	 * return object with joined graph references
	 * 
	 * @param node
	 * @return joined object
	 */
	public Arg joinDependencyGraphReference(DependencyGraphReference reference);

	/**
	 * return object with joined graph references
	 * 
	 * @param node
	 * @return joined object
	 */
	public Arg joinDependencyGraphReference(DependencyNode node);
}
