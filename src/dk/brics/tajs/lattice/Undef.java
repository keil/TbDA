package dk.brics.tajs.lattice;

/**
 * 'Undefined' facet for abstract values.
 */
public interface Undef {

	/**
	 * Returns true if this value is maybe undefined.
	 */
	public boolean isMaybeUndef(); 
	
	/**
	 * Returns true if this value is definitely not undefined.
	 */
	public boolean isNotUndef(); 
	
	/**
	 * Returns true if this value is maybe some other than undefined.
	 */
	public boolean isMaybeOtherThanUndef();
	
	/**
	 * Constructs a value as the join of this value and maybe undefined.
	 */
	public Value joinUndef();

	/**
	 * Constructs a value as a copy of this value but definitely not undefined.
	 */
	public Value restrictToNotUndef();
	
	/**
	 * Constructs a value as a copy of this value but only considering its undefined facet.
	 */
	public Value restrictToUndef();
}
