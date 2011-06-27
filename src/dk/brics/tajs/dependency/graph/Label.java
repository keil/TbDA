package dk.brics.tajs.dependency.graph;

public enum Label {
	NONE(""),

	// STATE
	STATE("#"), IF("if"), CALL("call"), FUNCTION("function"), RETURN("return"),

	// VALUE
	WRITE(":="), READ("="), ATTRIBUTE("."), OPERATION("?"), CREATE("!"),

	// CONTROL
	EXCEPTION("exception"), THROW("throw"), CATCH("catch"), WITH("with"), EVENT(
			"event"),

	// UNARY
	COMPLEMENT("~"), PLUS("+"), MINUS("-"), NOT("!"),

	// BINARY
	ADD("+"), SUB("-"), MUL("*"), DIV("/"), REM("%"), AND("&"), OR("|"), XOR(
			"^"), EQ("=="), NE("!="), LT("<="), GE(">="), LE("<"), GT(">"), IN(
			"in"), INSTANCEOF("instanceof"), TYPEOF("typeof"), SEQ("==="), SNE(
			"!=="), SHL("<<"), SHR(">>"), USHR(">>>");

	/**
	 * label
	 */
	private String mLabel;

	/**
	 * @param label
	 */
	private Label(String label) {
		this.mLabel = label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return this.mLabel;
	}
}