package me.DavidTs93.Tetris.Parts;

import me.DavidTs93.Tetris.Info.Coordinates;

public interface TetrisPartResizeable extends TetrisPart,Resizeable {
	void afterResize();
	
	void setBounds(int x,int y,int width,int height);
	
	default Coordinates resizeStart() {
		Coordinates start = new Coordinates(startRow(),startColumn());
		return hasBorder() ? start.subtract(1,1) : start;
	}
	
	default Coordinates resizeDimensions() {
		Coordinates dimensions = new Coordinates(rows(),columns());
		return hasBorder() ? dimensions.add(2,2) : dimensions;
	}
	
	default void resize() {
		Coordinates start = game().indexToPosition(resizeStart());
		Coordinates dimensions = game().indexToPosition(resizeDimensions());
		setBounds(start.column(),start.row(),dimensions.column(),dimensions.row());
		afterResize();
	}
}
