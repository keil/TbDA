package dk.brics.tajs.analysis.dom;

import java.io.IOException;

import org.w3c.dom.Document;

public interface HTMLParser {
	public Document build(String filename) throws IOException;
}
