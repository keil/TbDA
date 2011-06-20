package dk.brics.tajs.util;

public class Triple<T1, T2, T3> extends Pair<T1, T2> {

	/**
	 * third element
	 */
	private T3 element3;

	/**
	 * @param arg1
	 * @param arg2
	 */
	public Triple(T1 arg1, T2 arg2, T3 arg3) {
		super(arg1, arg2);
		element3 = arg3;
	}

	/**
	 * @return
	 */
	public T3 getElement3() {
		return element3;
	}

	/**
	 * @param element2
	 */
	public void setElement3(T3 element3) {
		this.element3 = element3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((element3 == null) ? 0 : element3.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
		if (element3 == null) {
			if (other.element3 != null)
				return false;
		} else if (!element3.equals(other.element3))
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

	@Override
	public String toString() {
		return "Triplet [element3=" + element3 + ", getElement1()=" + getElement1() + ", getElement2()=" + getElement2() + "]";
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