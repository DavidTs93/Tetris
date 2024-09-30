package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.TetrisGame;
import me.DavidTs93.Tetris.TurnInfo;

public class Empty extends Component {
	public Empty(TetrisGame game) {
		super(game);
	}
	
	@Override
	protected void setBackground() {}
	
	@Override
	protected void setBorder() {}
	
	public void newGame(TurnInfo info) {}
	
	public void update(TurnInfo info) {}
	
	public int startRow() {
		return 0;
	}
	
	public int startColumn() {
		return 0;
	}
	
	public int endRow() {
		return 0;
	}
	
	public int endColumn() {
		return 0;
	}
}