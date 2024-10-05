package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Displays.Label;
import me.DavidTs93.Tetris.Info.State;
import me.DavidTs93.Tetris.Info.TurnInfo;
import me.DavidTs93.Tetris.TetrisGame;

public class Title extends Component {
	private final Label top;
	private final Label bottom;
	
	public Title(TetrisGame game) {
		super(game);
		this.top = new Label(this,2,0);
		this.bottom = new Label(this,1,2);
		setOpaque(false);
		add(this.top);
		add(this.bottom);
		update(game.state(),true);
	}
	
	@Override
	public boolean hasBorder() {
		return false;
	}
	
	@Override
	public boolean hasBackground() {
		return false;
	}
	
	@Override
	public void changeState(State oldState,State newState) {
		update(newState,!game().inputPaused());
	}
	
	private void update(State state,boolean displayBottom) {
		top.setText(state.top());
		if (displayBottom) bottom.setText(state.bottom());
		repaint();
	}
	
	public void newGame(TurnInfo info) {}
	
	public void update(TurnInfo info) {}
	
	public int startRow() {
		return 1;
	}
	
	public int startColumn() {
		return 2;
	}
	
	public int endRow() {
		return 4;
	}
	
	public int endColumn() {
		return 29;
	}
}