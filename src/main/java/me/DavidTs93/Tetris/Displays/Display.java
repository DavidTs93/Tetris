package me.DavidTs93.Tetris.Displays;

import me.DavidTs93.Tetris.Info.Coordinates;
import me.DavidTs93.Tetris.Parts.TetrisPart;
import me.DavidTs93.Tetris.Parts.TetrisPartResizeable;
import me.DavidTs93.Tetris.TetrisGame;

public interface Display extends TetrisPartResizeable {
	TetrisPart parent();
	
	default TetrisGame game() {
		return parent().game();
	}
	
	@Override
	default Coordinates resizeStart() {
		return parent().hasBorder() ? TetrisPartResizeable.super.resizeStart().add(1,1) : TetrisPartResizeable.super.resizeStart();
	}
	
	@Override
	default boolean hasBorder() {
		return false;
	}
	
	@Override
	default int rows() {
		return parent().rows();
	}
	
	@Override
	default int columns() {
		return parent().columns();
	}
	
	default int startRow() {
		return 0;
	}
	
	default int startColumn() {
		return 0;
	}
	
	default int endRow() {
		return startRow() + rows() - 1;
	}
	
	default int endColumn() {
		return startColumn() + columns() - 1;
	}
}