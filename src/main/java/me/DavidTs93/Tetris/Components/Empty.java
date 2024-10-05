package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Info.TurnInfo;
import me.DavidTs93.Tetris.TetrisGame;

public class Empty extends Component {
	public Empty(TetrisGame game) {
		super(game);
	}
	
	@Override
	public boolean hasBorder() {
		return false;
	}
	
	@Override
	public boolean hasBackground() {
		return false;
	}
	
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