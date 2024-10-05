package me.DavidTs93.Tetris.Parts;

import me.DavidTs93.Tetris.Info.Coordinates;
import me.DavidTs93.Tetris.TetrisGame;

import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.awt.*;

public interface TetrisPart {
	Color BACKGROUND = new Color(255,191,255);
	
	TetrisGame game();
	
	boolean isOpaque();
	
	default boolean hasBackground() {
		return isOpaque();
	}
	
	void setBackground(Color background);
	
	default Color colorBackground() {
		return BACKGROUND;
	}
	
	default void setBackground() {
		if (hasBackground()) setBackground(colorBackground());
		else setBackground(null);
	}
	
	default boolean hasBorder() {
		return true;
	}
	
	void setBorder(Border border);
	
	default void setBorder() {
		if (!hasBorder()) setBorder(null);
		else setBorder(new AbstractBorder() {
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
	
	int startRow();
	
	int startColumn();
	
	int endRow();
	
	int endColumn();
	
	default int rows() {
		return endRow() - startRow() + 1;
	}
	
	default int columns() {
		return endColumn() - startColumn() + 1;
	}
	
	default Coordinates dimensions() {
		return new Coordinates(rows(),columns());
	}
}