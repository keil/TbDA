package dk.brics.tajs.util;

public class Pair<T1, T2> {

	/**
	 * first element
	 */
	private T1 element1;
	/**
	 * second element
	 */
	private T2 element2;

	/**
	 * @param arg1
	 * @param arg2
	 */
	public Pair(T1 arg1, T2 arg2) {
		element1 = arg1;
		element2 = arg2;
	}

	/**
	 * @return
	 */
	public T1 getElement1() {
		return element1;
	}

	/**
	 * @param element1
	 */
	public void setElement1(T1 element1) {
		this.element1 = element1;
	}

	/**
	 * @return
	 */
	public T2 getElement2() {
		return element2;
	}

	/**
	 * @param element2
	 */
	public void setElement2(T2 element2) {
		this.element2 = element2;
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
		result = prime * result + ((element1 == null) ? 0 : element1.hashCode());
		result = prime * result + ((element2 == null) ? 0 : element2.hashCode());
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
		Pair<?, ?> other = (Pair<?, ?>) obj;
		if (element1 == null) {
			if (other.element1 != null)
				return false;
		} else if (!element1.equals(other.element1))
			return false;
		if (element2 == null) {
			if (other.element2 != null)
				return false;
		} else if (!element2.equals(other.element2))
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
		return super.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Pair [element1=" + element1 + ", element2=" + element2 + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}