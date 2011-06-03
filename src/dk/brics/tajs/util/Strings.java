package dk.brics.tajs.util;

/**
 * Miscellaneous string operations.
 */
public class Strings {

	private Strings() {}
	
	/**
	 * Escapes special characters in the given string.
	 * Special characters are all Unicode chars except 0x20-0x7e but including \, ", {, and }.
	 */
	public static String escape(String s) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '"':
				b.append("\\\"");
				break;
			case '\\':
				b.append("\\\\");
				break;
			case '\b':
				b.append("\\b");
				break;
			case '\t':
				b.append("\\t");
				break;
			case '\n':
				b.append("\\n");
				break;
			case '\r':
				b.append("\\r");
				break;
			case '\f':
				b.append("\\f");
				break;
			case '<':
				b.append("\\<");
				break;
			case '>':
				b.append("\\>");
				break;
			case '{':
				b.append("\\{");
				break;
			case '}':
				b.append("\\}");
				break;
			default:
				if (c >= 0x20 && c <= 0x7e)
					b.append(c);
				else {
					b.append("\\u");
					String t = Integer.toHexString(c & 0xffff);
					for (int j = 0; j + t.length() < 4; j++)
						b.append('0');
					b.append(t);
				}
			}
		}
		return b.toString();
	}
	
	/**
	 * Checks whether the given string is a valid array index.
	 */
	public static boolean isArrayIndex(String s) {
		if (s.length() == 0)
			return false;
		long val = 0L;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c < '0' || c > '9')
				return false;
			val = 10*val + Character.digit(c, 10);
			if (val > 2 * (long)Integer.MAX_VALUE)
				return false;
		}
		return true;
	}
}
