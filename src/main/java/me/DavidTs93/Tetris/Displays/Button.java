package me.DavidTs93.Tetris.Displays;

import me.DavidTs93.Tetris.Info.Coordinates;
import me.DavidTs93.Tetris.Parts.Colors;
import me.DavidTs93.Tetris.Parts.TetrisPart;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton implements Display {
	protected final TetrisPart parent;
	protected final Float squareSizeMult;
	private final Coordinates start;
	private final Coordinates dimensions;
	
	public Button(TetrisPart parent,Float squareSizeMult,Coordinates start,Coordinates dimensions) {
		this.parent = parent;
		this.squareSizeMult = squareSizeMult;
		this.start = start;
		this.dimensions = dimensions;
		setOpaque(true);
		setLayout(null);
		horizontal(CENTER);
		vertical(CENTER);
		setForeground(Colors.BLACK.body());
	}
	
	public Button horizontal(int alignment) {
		setHorizontalAlignment(alignment);
		return this;
	}
	
	public Button vertical(int alignment) {
		setVerticalAlignment(alignment);
		return this;
	}
	
	public Button text(String text) {
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
}