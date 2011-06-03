package dk.brics.tajs.dependency;

import java.util.HashMap;

import dk.brics.tajs.flowgraph.SourceLocation;

public class DependencyObject implements Comparable<DependencyObject> {

	/**
	 * Counter, to number trace labels
	 */
	private static int i = 0;

	/**
	 * HashMap to cache DependencyObjects
	 */
	private static HashMap<SourceLocation, DependencyObject> cache = new HashMap<SourceLocation, DependencyObject>();

	/**
	 * Returns and increments the counter
	 * 
	 * @return next trace number
	 */
	private static int nextNumber() {
		return DependencyObject.i++;
	}

	/**
	 * Create an DependencyObject
	 * 
	 * @param sourceLocation
	 * @return DependencyObject
	 */
	public static DependencyObject getDependencyObject(
			SourceLocation sourceLocation) {
		if (!DependencyObject.cache.containsKey(sourceLocation))
			DependencyObject.cache.put(sourceLocation, new DependencyObject(
					sourceLocation));

		return DependencyObject.cache.get(sourceLocation);
	}

	/**
	 * Create an InitialState DependencyObject
	 * 
	 * @param sourceLocation
	 * @return DependencyObject
	 */
	public static DependencyObject getInitialStateDependencyObject(String label) {
		SourceLocation sourceLocation = new SourceLocation(0, "<INITIALSTATE> "
				+ label);
		return DependencyObject.getDependencyObject(sourceLocation);
	}

	/**
	 * Return DependenyCache
	 * 
	 * @return DependenyCache
	 */
	public static HashMap<SourceLocation, DependencyObject> getCache() {
		return cache;
	}

	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////

	private String mTrace;
	private SourceLocation mSourceLocation;

	/**
	 * @param sourceLocation
	 */
	private DependencyObject(SourceLocation sourceLocation) {
		super();
		mSourceLocation = sourceLocation;
		mTrace = "t" + DependencyObject.nextNumber();
	}

	/**
	 * @return trace
	 */
	public String getTrace() {
		return mTrace;
	}

	/**
	 * @return sourceLocation
	 */
	public SourceLocation getSourceLocation() {
		return mSourceLocation;
	}

	/**
	 * @return new Dependency
	 */
	public Dependency getDependency() {
		return new Dependency(this);
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DependencyObject other = (DependencyObject) obj;
		if (mSourceLocation == null) {
			if (other.mSourceLocation != null)
				return false;
		} else if (!mSourceLocation.equals(other.mSourceLocation))
			return false;
		if (mTrace == null) {
			if (other.mTrace != null)
				return false;
		} else if (!mTrace.equals(other.mTrace))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mSourceLocation == null) ? 0 : mSourceLocation.hashCode());
		result = prime * result + ((mTrace == null) ? 0 : mTrace.hashCode());
		return result;
	}

	@Override
	public int compareTo(DependencyObject o) {
		Integer i = Integer.parseInt(getTrace().substring(1));
		Integer j = Integer.parseInt(o.getTrace().substring(1));
		return i.compareTo(j);
	}

	@Override
	public String toString() {
		return mTrace;
	}
}