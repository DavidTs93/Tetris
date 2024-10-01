package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.*;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Component extends JPanel implements Resizeable {
	private static final Color BACKGROUND = new Color(255,191,255);
	
	private final TetrisGame game;
	private final int centerColumn;
	private final List<Resizeable> resizeables;
	
	protected Component(TetrisGame game) {
		this.game = game;
		this.centerColumn = (startColumn() + endColumn() - 1) / 2;
		this.resizeables = new ArrayList<>();
		setBackground();
		setBorder();
		setLayout(null);
	}
	
	protected void setBackground() {
		setBackground(BACKGROUND);
	}
	
	protected void setBorder() {
		setBorder(new AbstractBorder() {
			@Override
			public void paintBorder(java.awt.Component c,Graphics g,int x,int y,int width,int height) {
				// Border top + bottom
				for (int i = 0; i < columns() + 2; i++) {
					game().drawSquare(g,new Coordinates(0,i),Colors.GRAY);
					game().drawSquare(g,new Coordinates(endRow() - startRow() + 2,i),Colors.GRAY);
				}
				// Border sides
				for (int i = 0; i < rows() + 2; i++) {
					game().drawSquare(g,new Coordinates(i,0),Colors.GRAY);
					game().drawSquare(g,new Coordinates(i,endColumn() - startColumn() + 2),Colors.GRAY);
				}
			}
		});
	}
	
	public final TetrisGame game() {
		return game;
	}
	
	protected int centerColumn() {
		return centerColumn;
	}
	
	public void changeState(TetrisGame.State oldState,TetrisGame.State newState) {}
	
	@Override
	public java.awt.Component add(java.awt.Component comp) {
		if (comp instanceof Resizeable) resizeables.add((Resizeable) comp);
		return super.add(comp);
	}
	
	public void resize() {
		Coordinates start = game().indexToPositionAbsolute(new Coordinates(startRow() - 1,startColumn() - 1));
		Coordinates add = game().indexToPosition(new Coordinates(rows() + 2,columns() + 2));
		setBounds(start.column(),start.row(),add.column(),add.row());
		resizeables.forEach(Resizeable::resize);
	}
	
	public abstract void newGame(TurnInfo info);
	public abstract void update(TurnInfo info);
	public abstract int startRow();
	public abstract int startColumn();
	public abstract int endRow();
	public abstract int endColumn();
	
	public final int rows() {
		return endRow() - startRow() + 1;
	}
	
	public final int columns() {
		return endColumn() - startColumn() + 1;
	}
}