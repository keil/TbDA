package dk.brics.tajs.lattice;

/**
 * 'Null' facet for abstract values.
 */
public interface Null {

	/**
	 * Returns true if this value is maybe null.
	 */
	public boolean isMaybeNull();

	/**
	 * Returns true if this value is definitely not null.
	 */
	public boolean isNotNull(); 
	
	/**
	 * Returns true if this value is maybe some other than null.
	 */
	public boolean isMaybeOtherThanNull();
	
	/**
	 * Constructs a value as the join of this value and maybe null.
	 */
	public Value joinNull();

	/**
	 * Constructs a value as a copy of this value but definitely not null.
	 */
	public Value restrictToNotNull();
	
	/**
	 * Constructs a value as a copy of this value but only considering its null facet.
	 */
	public Value restrictToNull();
}