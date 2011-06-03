package dk.brics.tajs.dependency.graph;

public enum Label {
	NONE(""),

	STATE("#"), IF("if"), CALL("call"), FUNCTION("function"), RETURN("return"),

	WRITE(":="), READ("="), ATTRIBUTE("."), OPERATION("?"),

	EXCEPTION("exception"), THROW("throw"), CATCH("catch"), WITH("with"), EVENT(
			"event"),

	COMPLEMENT("~"), PLUS("+"), MINUS("-"), NOT("!"),

	ADD("+"), SUB("-"), MUL("*"), DIV("/"), REM("%"), AND("&"), OR("|"), XOR(
			"^"), EQ("=="), NE("!="), LT("<="), GE(">="), LE("<"), GT(">"), IN(
			"in"), INSTANCEOF("instanceof"), TYPEOF("typeof"), SEQ("==="), SNE(
			"!=="), SHL("<<"), SHR(">>"), USHR(">>>");

	private String mLabel;

	private Label(String label) {
		this.mLabel = label;
	}

	public String toString() {
		return this.mLabel;
	}
}