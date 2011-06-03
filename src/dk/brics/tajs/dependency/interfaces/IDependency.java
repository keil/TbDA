package dk.brics.tajs.dependency.interfaces;

import dk.brics.tajs.dependency.Dependency;

/**
 * interface to handle dependencies
 * 
 * @author Matthias Keil
 * @param <Arg>
 *            generic type
 */
public interface IDependency<Arg> {

	/**
	 * Gets the current dependency
	 */
	public boolean hasDependency();

	/**
	 * Gets the current dependency
	 */
	public Dependency getDependency();

	/**
	 * Joins an dependency to the current one
	 * 
	 * @return
	 */
	public Arg joinDependency(Dependency dependency);

	/**
	 * Sets the current dependency
	 * 
	 * @return
	 */
	public Arg setDependency(Dependency dependency);
	
	/**
	 * Remove the dependency
	 */
	public void removeDependency();

	/**
	 * Prints the dependency
	 */
	public String printDependency();

}