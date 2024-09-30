package me.DavidTs93.Tetris.Displays;

import me.DavidTs93.Tetris.Colors;
import me.DavidTs93.Tetris.Components.Component;
import me.DavidTs93.Tetris.Coordinates;
import me.DavidTs93.Tetris.Resizeable;

import javax.swing.*;
import java.awt.*;

public class Label extends JLabel implements Resizeable {
	protected final Component parent;
	protected final int squareSizeMult;
	private final Coordinates start;
	private final Coordinates add;
	
	public Label(Component parent,int squareSizeMult,Coordinates start,Coordinates add) {
		this.parent = parent;
		this.squareSizeMult = squareSizeMult;
		this.start = start;
		this.add = add;
		setOpaque(false);
		setLayout(null);
		horizontal(CENTER);
		vertical(CENTER);
		setForeground(Colors.BLACK.body());
	}
	
	public Label(Component parent,int squareSizeMult,Coordinates start) {
		this(parent,squareSizeMult,start,new Coordinates(squareSizeMult,parent.columns()));
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
	
	public void resize() {
		Coordinates start = parent.game().indexToPosition(this.start.add(1,1));
		Coordinates add = parent.game().indexToPosition(this.add);
		setBounds(start.column(),start.row(),add.column(),add.row());
		setFont(new Font(Font.SANS_SERIF,Font.PLAIN,parent.game().squareSize() * squareSizeMult));
	}
}