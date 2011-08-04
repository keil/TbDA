package dk.brics.tajs.analysis.dom;

import org.jdom.Document;

import java.io.IOException;

public interface HTMLParser {
	public Document build(String filename) throws IOException;
}
