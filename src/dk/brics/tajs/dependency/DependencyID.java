package dk.brics.tajs.dependency;

import java.util.HashMap;

public class DependencyID {
	
	
	static private HashMap<DependencyID, DependencyObject> cache = new HashMap<DependencyID, DependencyObject>();
	
	static public boolean cacheContains(DependencyID id) {
		System.out.println("@ CACHE contains " + id);
		return cache.containsKey(id);
	}
	
	static public DependencyObject cacheGet(DependencyID id) {
		System.out.println("@ GET CACHE of " + id);
		return cache.get(id);
	}
	
	static public void cacheSet(DependencyID id, DependencyObject dependencyObject) {
		System.out.println("@ SET CACHE with " + id + "  as " + dependencyObject);
		cache.put(id, dependencyObject);
	}
	

	private String id;

	/**
	 * @param id
	 */
	public DependencyID(String id) {
		super();
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/* (non-Javadoc)
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
		DependencyID other = (DependencyID) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ("#"+this.id);
	}	
}