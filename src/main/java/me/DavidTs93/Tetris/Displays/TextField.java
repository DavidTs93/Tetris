package me.DavidTs93.Tetris.Displays;

import me.DavidTs93.Tetris.Info.Coordinates;
import me.DavidTs93.Tetris.Parts.Colors;
import me.DavidTs93.Tetris.Parts.TetrisPart;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class TextField extends JTextField implements Display {
	protected final TetrisPart parent;
	protected final Float squareSizeMult;
	private final Coordinates start;
	private final Coordinates dimensions;
	
	public TextField(TetrisPart parent,Float squareSizeMult,Coordinates start,Coordinates dimensions,Integer maxLength) {
		this.parent = parent;
		this.squareSizeMult = squareSizeMult;
		this.start = start;
		this.dimensions = dimensions;
		setDocument(new LimitedDocument(maxLength));
		setOpaque(true);
		setLayout(null);
		horizontal(LEFT);
		setForeground(Colors.BLACK.body());
		setBackground();
	}
	
	public TextField(TetrisPart parent,int squareSizeMult,int startRow,Integer maxLength) {
		this(parent,(float) squareSizeMult,new Coordinates(startRow,0),new Coordinates(squareSizeMult,parent.columns()),maxLength);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	@Override
	public boolean hasBackground() {
		return true;
	}
	
	@Override
	public Color colorBackground() {
		return Color.WHITE;
	}
	
	public TextField horizontal(int alignment) {
		setHorizontalAlignment(alignment);
		return this;
	}
	
	public TextField text(String text) {
		setText(text);
		return this;
	}
	
	public TetrisPart parent() {
		return parent;
	}
	
	@Override
	public int startRow() {
		return start.row();
	}
	
	@Override
	public int startColumn() {
		return start.column();
	}
	
	@Override
	public int rows() {
		return dimensions.row();
	}
	
	@Override
	public int columns() {
		return dimensions.column();
	}
	
	public void afterResize() {
		setFont(new Font(Font.SANS_SERIF,Font.PLAIN,squareSizeMult == null || squareSizeMult == 1 ? parent.game().squareSize() : (int) (parent.game().squareSize() * squareSizeMult + 0.5f)));
	}
	
	private static class LimitedDocument extends PlainDocument {
		private final Integer maxLength;
		
		private LimitedDocument(Integer maxLength) {
			this.maxLength = maxLength;
		}
		
		@Override
		public void insertString(int offset,String str,AttributeSet attributeSet) throws BadLocationException {
			if (str != null && (maxLength == null || getLength() + str.length() <= maxLength)) super.insertString(offset,str,attributeSet);
		}
	}
}