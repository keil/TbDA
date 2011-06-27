package dk.brics.tajs.dependency.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class DependencyGraphReference implements Cloneable, Iterable<DependencyNode> {

	/**
	 * TODO: remove
	 */
	public final static DependencyGraphReference NONE = new DependencyGraphReference();
	
	/**
	 * list of referenced nodes
	 */
	private LinkedHashSet<DependencyNode> mReference;

	/**
	 * 
	 */
	public DependencyGraphReference() {
		mReference = new LinkedHashSet<DependencyNode>();
	}

	/**
	 * @param node
	 */
	public DependencyGraphReference(DependencyNode node) {
		mReference = new LinkedHashSet<DependencyNode>();
		join(node);
	}

	/**
	 * @param reference
	 */
	public DependencyGraphReference(DependencyGraphReference reference) {
		mReference = new LinkedHashSet<DependencyNode>();
		join(reference);
	}

	/**
	 * @param node
	 */
	public void join(DependencyNode node) {
		mReference.add(node);
	}

	/**
	 * @param reference
	 */
	public void join(DependencyGraphReference reference) {
		mReference.addAll(reference.getReference());
	}

	/**
	 * @param reference
	 * @return
	 */
	public DependencyGraphReference merge(DependencyGraphReference reference) {
		DependencyGraphReference newReference = new DependencyGraphReference();
		newReference.join(this);
		newReference.join(reference);
		return newReference;
	}

	/**
	 * @return
	 */
	public Collection<DependencyNode> getReference() {
		return new HashSet<DependencyNode>(mReference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mReference == null) ? 0 : mReference.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DependencyGraphReference other = (DependencyGraphReference) obj;
		if (mReference == null) {
			if (other.mReference != null)
				return false;
		} else if (!mReference.equals(other.mReference))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		DependencyGraphReference reference = new DependencyGraphReference();
		reference.join(this);
		return reference;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		for (DependencyNode node : mReference)
			buffer.append(" " + node.getDependency() + " #" + node + " ");

		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<DependencyNode> iterator() {
		return mReference.iterator();
	}
}