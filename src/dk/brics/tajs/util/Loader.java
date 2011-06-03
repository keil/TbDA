package dk.brics.tajs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * String loader.
 */
public class Loader {

	private Loader() {}
	
	/**
	 * Returns the contents of the given resource (file path or URL) as a string.
	 * @param u file path or URL
	 * @param charset character encoding (if null, use system default)
	 * @return contents
	 * @throws IOException
	 */
	public static String getString(String u, String charset) throws IOException {
		BufferedReader r = null;
		if (charset == null)
			charset = Charset.defaultCharset().name();
		try {
			InputStream in;
			try {
				in = new URL(u).openStream();
			} catch (MalformedURLException e) {
				in = new FileInputStream(u);
			}
			r = new BufferedReader(new InputStreamReader(in, charset));
			StringBuilder b = new StringBuilder();
			boolean done = false;
			while (!done) {
				int c = r.read();
				if (c == -1)
					done = true;
				else
					b.append((char)c);
			}
			return b.toString();
		} finally {
			if (r != null)
				r.close();
		}
	}
	
	/**
	 * Resolves relative paths/URLs.
	 * @param base base path/URL
	 * @param u path/URL to resolve (relative or absolute)
	 * @return absolute URL
	 * @throws MalformedURLException 
	 */
	public static String resolveRelative(String base, String u) throws MalformedURLException {
		String abs;
		try {
			abs = new URL(new URL(base), u).toString();
		} catch (MalformedURLException e) {
			abs = new URL(new URL(new File(base).toURI().toString()), u).toString();
		}
		if (abs.startsWith("file:/"))
			abs = abs.substring(6);
		return abs;
	}
}
