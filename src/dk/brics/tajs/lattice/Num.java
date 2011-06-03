package dk.brics.tajs.lattice;

/**
 * 'Number' facet for abstract values.
 */
public interface Num {

	/**
	 * Returns true if this value is maybe any number.
	 */
	public boolean isMaybeAnyNum();
	
	/**
	 * Returns true if this value is maybe a singleton number, excluding NaN and +/-Inf.
	 */
	public boolean isMaybeSingleNum();

	/**
	 * Returns true if this value is maybe a non-singleton number or NaN or +/-Inf.
	 */
	public boolean isMaybeFuzzyNum();

	/**
	 * Returns true if this value is maybe NaN.
	 */
	public boolean isMaybeNaN();
	
	/**
	 * Returns true if this value is definitely NaN.
	 */
	public boolean isNaN();
	
	/**
	 * Returns true if this value is maybe infinite.
	 */
	public boolean isMaybeInf();
	
	/**
	 * Returns true if this value is maybe any UInt number.
	 */
	public boolean isMaybeNumUInt();
	
	/**
	 * Returns true if this value is maybe any non-UInt number.
	 */
	public boolean isMaybeNumNotUInt();
	
	/**
	 * Returns the singleton number value, or null if definitely not a singleton number.
	 */
	public Double getNum();

	/**
	 * Returns true if this value is definitely not a number.
	 */
	public boolean isNotNum(); 
	
	/**
	 * Returns true if this value is maybe a non-number.
	 */
	public boolean isMaybeOtherThanNum();
	
	/**
	 * Returns true if this value is maybe not an UInt-number.
	 */
	public boolean isMaybeOtherThanNumUInt();
	
	/**
	 * Constructs a value as the join of this value and maybe any number.
	 */
	public Value joinAnyNum();
	
	/**
	 * Constructs a value as the join of this value and maybe any UInt number.
	 */
	public Value joinAnyNumUInt();
	
	/**
	 * Constructs a value as the join of this value and maybe any non-UInt number.
	 */
	public Value joinAnyNumNotUInt();

	/**
	 * Constructs a value as the join of this value and the given concrete number.
	 */
	public Value joinNum(double v);

	/**
	 * Constructs a value as the join of this value and NaN.
	 */
	public Value joinNumNaN();

	/**
	 * Constructs a value as the join of this value and +/-Inf.
	 */
	public Value joinNumInf();

	/**
	 * Constructs a value as a copy of this value but definitely not NaN.
	 */
	public Value restrictToNotNaN();
	
	/**
	 * Constructs a value from this value where only the number facet is considered.
	 */
	public Value restrictToNum();
	
	/**
	 * Constructs a value from this value but definitely not a number.
	 */
	public Value restrictToNotNum();
}
