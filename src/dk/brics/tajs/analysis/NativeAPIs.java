package dk.brics.tajs.analysis;

/**
 * Descriptors for the APIs implemented in the analyzer.
 */
public enum NativeAPIs {
	
	ECMA_SCRIPT_NATIVE ("ECMAScript native functions"),
	DOCUMENT_OBJECT_MODEL ("The Document Object Model");
	
	private String s;
	
	private NativeAPIs(String s) {
		this.s = s;
	}
	
	@Override
	public String toString() {
		return s;
	}
}
