package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Coordinates;
import me.DavidTs93.Tetris.Displays.Label;
import me.DavidTs93.Tetris.TetrisGame;
import me.DavidTs93.Tetris.TurnInfo;

public class Title extends Component {
	private final Label top;
	private final Label bottom;
	
	public Title(TetrisGame game) {
		super(game);
		this.top = new Label(this,2,new Coordinates(0,0));
		this.bottom = new Label(this,1,new Coordinates(2,0));
		setOpaque(false);
		add(this.top);
		add(this.bottom);
		update(game.state());
	}
	
	@Override
	protected void setBackground() {}
	
	@Override
	protected void setBorder() {}
	
	@Override
	public void changeState(TetrisGame.State oldState,TetrisGame.State newState) {
		update(newState);
	}
	
	private void update(TetrisGame.State state) {
		top.setText(state.top());
		bottom.setText(state.bottom());
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