package dk.brics.tajs.dependency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Dependency implements Iterable<DependencyObject>, Cloneable {

	/**
	 * set of dependencies
	 */
	private Set<DependencyObject> mDependency = new HashSet<DependencyObject>();

	/**
	 * 
	 */
	public Dependency() {
		super();
	}

	/**
	 * @param dependency
	 */
	public Dependency(Collection<DependencyObject> dependency) {
//		super();
		mDependency.addAll(dependency);
	}

	/**
	 * @param dependencyObject
	 */
	public Dependency(DependencyObject dependencyObject) {
		mDependency.add(dependencyObject);
	}

	/**
	 * @param dependency
	 */
	public Dependency(Dependency dependency) {
//		super();
		mDependency.addAll(dependency.toCollection());
	}

	/**
	 * @param dependency
	 */
	public void join(Dependency dependency) {
		Iterator<DependencyObject> iterator = dependency.iterator();
		while (iterator.hasNext()) {
			DependencyObject dependencyObject = iterator.next();
			join(dependencyObject);
		}
	}

	/**
	 * @param dependencyObject
	 */
	public void join(DependencyObject dependencyObject) {
		if (!mDependency.contains(dependencyObject))
			mDependency.add(dependencyObject);
	}

	/**
	 * @param arg
	 * @return merged Dependency
	 */
	public Dependency merge(DependencyObject arg) {
		Dependency dependency = new Dependency();
		dependency.join(this);
		dependency.join(arg);
		return dependency;
	}

	/**
	 * @param arg
	 * @return merged Dependency
	 */
	public Dependency merge(Dependency arg) {
		Dependency dependency = new Dependency();
		dependency.join(this);
		dependency.join(arg);
		return dependency;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mDependency == null) ? 0 : mDependency.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dependency other = (Dependency) obj;
		if (mDependency == null) {
			if (other.mDependency != null)
				return false;
		} else if (!mDependency.equals(other.mDependency))
			return false;
		return true;
	}

	@Override
	public String toString() {
		ArrayList<DependencyObject> dependencyList = new ArrayList<DependencyObject>(
				mDependency);
		Collections.sort(dependencyList);

		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		Iterator<DependencyObject> iterator = dependencyList.iterator();
		while (iterator.hasNext()) {
			DependencyObject dependencyObject = iterator.next();
			buffer.append(dependencyObject.toString());

			if (iterator.hasNext())
				buffer.append(", ");

		}
		buffer.append("}");
		return buffer.toString();
	}

	/**
	 * @return size of assigned dependency objects
	 */
	public int size() {
		return mDependency.size();
	}

	/**
	 * @return true, if no dependencies are set, otherwise false
	 */
	public boolean isEmpty() {
		return mDependency.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<DependencyObject> iterator() {
		return mDependency.iterator();
	}

	/**
	 * @return dependency objects as array
	 */
	public Object[] toArray() {
		return mDependency.toArray();
	}

	/**
	 * @return
	 */
	public Collection<DependencyObject> toCollection() {
		return new HashSet<DependencyObject>(mDependency);
	}

	/**
	 * @param o
	 * @return true, if the dependency contains current object, otherwise false
	 */
	public boolean contains(DependencyObject o) {
		return mDependency.contains(o);
	}
}