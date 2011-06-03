package dk.brics.tajs.lattice;

/**
 * 'Boolean' facet for abstract values.
 */
public interface Bool {

	/**
	 * Returns true if this value is maybe any boolean.
	 */
	public boolean isMaybeAnyBool();

	/**
	 * Returns true if this value is maybe true.
	 */
	public boolean isMaybeTrueButNotFalse();

	/**
	 * Returns true if this value is maybe false.
	 */
	public boolean isMaybeFalseButNotTrue();
	
	/**
	 * Returns true if this value is maybe true.
	 */
	public boolean isMaybeTrue();

	/**
	 * Returns true if this value is maybe false.
	 */
	public boolean isMaybeFalse();

	/**
	 * Returns true if this value is definitely not a boolean.
	 */
	public boolean isNotBool(); 
	
	/**
	 * Returns true if this value is maybe a non-boolean.
	 */
	public boolean isMaybeOtherThanBool();
	
	/**
	 * Constructs a value as the join of this value and maybe any boolean.
	 */
	public Value joinAnyBool();

	/**
	 * Constructs a value as the join of this value and the given concrete boolean value.
	 */
	public Value joinBool(boolean x); 
}
