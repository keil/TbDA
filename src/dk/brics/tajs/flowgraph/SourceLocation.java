package dk.brics.tajs.flowgraph;

/**
 * Source location.
 */
public class SourceLocation implements Comparable<SourceLocation> { // TODO: can we get column numbers from rhino?

	private int linenumber;
	
	private String filename;
	
	/**
	 * Constructs a new source location.
	 */
	public SourceLocation(int linenumber, String filename) {
		this.linenumber = linenumber;
		this.filename = filename;
	}
	
	/**
	 * Returns the source file name.
	 */
	public String getFileName() {
		return filename;
	}

	/**
	 * Returns the source line number.
	 */
	public int getLineNumber() {
		return linenumber;
	}

	/**
	 * Returns a string description of the source information associated with this node.
	 */
	@Override
	public String toString() {
		return filename + (linenumber > 0 ? ":" + linenumber : "");
	}
	
	/**
	 * Returns a hash code for this object.
	 */
	@Override
	public int hashCode() {
		return filename.hashCode() * 31 + linenumber * 3;
	}

	/**
	 * Checks whether this and the given object represent the same source location.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SourceLocation other = (SourceLocation) obj;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (linenumber != other.linenumber)
			return false;
		return true;
	}

	/**
	 * Compares source locations by file name and line number.
	 */
	@Override
	public int compareTo(SourceLocation e) {
		int c = filename.compareTo(e.filename);
		if (c != 0)
			return c;
		return linenumber - e.linenumber;
	}
}
