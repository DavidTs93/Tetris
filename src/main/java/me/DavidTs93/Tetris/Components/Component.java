package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Info.State;
import me.DavidTs93.Tetris.Info.TurnInfo;
import me.DavidTs93.Tetris.Parts.Resizeable;
import me.DavidTs93.Tetris.Parts.TetrisPartResizeable;
import me.DavidTs93.Tetris.TetrisGame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Component extends JPanel implements TetrisPartResizeable {
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
	
	public final TetrisGame game() {
		return game;
	}
	
	protected int centerColumn() {
		return centerColumn;
	}
	
	public void changeState(State oldState,State newState) {}
	
	@Override
	public java.awt.Component add(java.awt.Component comp) {
		if (comp instanceof Resizeable) resizeables.add((Resizeable) comp);
		return super.add(comp);
	}
	
	public void afterResize() {
		resizeables.forEach(Resizeable::resize);
	}
	
	public abstract void newGame(TurnInfo info);
	public abstract void update(TurnInfo info);
}