package dk.brics.tajs.gui;

import java.io.Writer;

import javax.swing.JTextArea;

public class TextAreaWriter extends Writer {

	private final JTextArea textarea;

	public TextAreaWriter(final JTextArea textArea) {
		this.textarea = textArea;
	}

	@Override
	public void flush() {}

	@Override
	public void close() {}

	@Override
	public void write(char[] cbuf, int off, int len) {
		textarea.append(new String(cbuf, off, len));
		textarea.setCaretPosition(textarea.getText().length());
	}
}