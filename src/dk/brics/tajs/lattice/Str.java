package dk.brics.tajs.lattice;

/**
 * 'String' facet for abstract values.
 */
public interface Str {

	/**
	 * Returns true if this value is maybe any string.
	 */
	public boolean isMaybeAnyStr();

	/**
	 * Returns true if value is maybe a singleton string.
	 */
	public boolean isMaybeSingleStr();

	/**
	 * Returns true if this value is maybe any UInt string.
	 */
	public boolean isMaybeStrUInt();

	/**
	 * Returns true if this value is maybe any non-UInt string.
	 */
	public boolean isMaybeStrNotUInt();
	
	/**
	 * Returns true if this value is maybe any UInt string but not a non-UInt string.
	 */
	public boolean isMaybeStrOnlyUInt();
	
	/**
	 * Returns true if this value may be a non-string.
	 */
	public boolean isMaybeOtherThanStr();

	/**
	 * Returns true if this value is maybe a non-singleton string.
	 */
	public boolean isMaybeFuzzyStr();

	/**
     * Returns true if this value is maybe any JSON string.
     */
    public boolean isMaybeJSONStr();

	/**
	 * Returns the singleton string value, or null if definitely not a singleton string.
	 */
	public String getStr();

	/**
	 * Returns true if this value is definitely not a string.
	 */
	public boolean isNotStr();

	/**
	 * Constructs a value as the join of this value and maybe any string.
	 */
	public Value joinAnyStr();

	/**
	 * Constructs a value as the join of this value and maybe any UInt string.
	 */
	public Value joinAnyStrUInt();

	/**
	 * Constructs a value as the join of this value and the given concrete string.
	 */
	public Value joinStr(String v);
	
	/**
	 * Constructs a value from this value where only the string facet is considered.
	 */
	public Value restrictToStr();
}
