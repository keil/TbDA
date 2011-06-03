package dk.brics.tajs.gui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JTextArea;

public class LineNumberedTextArea extends JTextArea {

	public LineNumberedTextArea() {
		super();
		setOpaque(false);
		setFont(new Font(Font.MONOSPACED, Font.PLAIN, getFont().getSize()));
	}

	@Override
	public Insets getInsets() {
		return getInsets(new Insets(0,0,0,0));
	}

	@Override
	public Insets getInsets(Insets insets) {
		insets = super.getInsets(insets);
		insets.left += lineNumberWidth();
		return insets;
	}

	private int lineNumberWidth() {
		int lineCount = Math.max(getRows(), getLineCount() + 1);
		return getFontMetrics(getFont()).stringWidth(lineCount + " ");
	}

	@Override
	public void paintComponent(Graphics g) {
		Insets insets = getInsets();

		Rectangle clip = g.getClipBounds();

		g.setColor(getBackground());
		g.fillRect(clip.x, clip.y, clip.width, clip.height);

		if (clip.x < insets.left) {
			FontMetrics fm = g.getFontMetrics();
			int fontHeight = fm.getHeight();

			int y = fm.getAscent() + insets.top;

			int startingLineNumber = ((clip.y + insets.top) / fontHeight) + 1;

			if (y < clip.y)
				y = startingLineNumber * fontHeight - (fontHeight - fm.getAscent() - 1) + 1;

			int yend = y + clip.height + fontHeight;

			g.setColor(getForeground());
			g.setFont(g.getFont().deriveFont(Font.BOLD));
			int length = ("" + Math.max(getRows(), getLineCount() + 1)).length();
			int lines = getLineCount();
			while (y < yend && startingLineNumber <= lines) {
				String label = padLabel(startingLineNumber, length);
				g.drawString(label, insets.left - fm.stringWidth(label), y);
				y += fontHeight;
				startingLineNumber++;
			}
		}
		super.paintComponent(g);
	}

	private String padLabel(int lineNumber, int length) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(lineNumber);
		for (int count = (length - buffer.length()); count > 0; count--)
			buffer.insert(0, ' ');
		buffer.append(' ');
		return buffer.toString();
	}
}
