package me.DavidTs93.Tetris.Displays;

import me.DavidTs93.Tetris.Info.Coordinates;
import me.DavidTs93.Tetris.Parts.Colors;
import me.DavidTs93.Tetris.Parts.TetrisPart;

import javax.swing.*;
import java.awt.*;

public class Label extends JLabel implements Display {
	protected final TetrisPart parent;
	protected final Float squareSizeMult;
	private final Coordinates start;
	private final Coordinates dimensions;
	
	public Label(TetrisPart parent,Float squareSizeMult,Coordinates start,Coordinates dimensions) {
		this.parent = parent;
		this.squareSizeMult = squareSizeMult;
		this.start = start;
		this.dimensions = dimensions;
		setOpaque(false);
		setLayout(null);
		horizontal(CENTER);
		vertical(CENTER);
		setForeground(Colors.BLACK.body());
	}
	
	public Label(TetrisPart parent,float squareSizeMult,int startRow) {
		this(parent,squareSizeMult,new Coordinates(startRow,0),new Coordinates((int) Math.ceil(squareSizeMult),parent.columns()));
	}
	
	public Label horizontal(int alignment) {
		setHorizontalAlignment(alignment);
		return this;
	}
	
	public Label vertical(int alignment) {
		setVerticalAlignment(alignment);
		return this;
	}
	
	public Label text(String text) {
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
		setFont(new Font(Font.SANS_SERIF,Font.PLAIN,sizeWithSquareSizeMult(squareSizeMult)));
	}
}