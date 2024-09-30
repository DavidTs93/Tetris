package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Coordinates;
import me.DavidTs93.Tetris.Displays.Label;
import me.DavidTs93.Tetris.TetrisGame;
import me.DavidTs93.Tetris.TurnInfo;

public class Lines extends Component {
	private static final String FORMAT = "%04d";
	
	private int lines = 0;
	private final Label label;
	
	public Lines(TetrisGame game) {
		super(game);
		this.label = new Label(this,1,new Coordinates(1,0));
		add(this.label);
		update();
	}
	
	public void newGame(TurnInfo info) {
		this.lines = 0;
	}
	
	private void update() {
		label.setText("LINES - " + String.format(FORMAT,lines));
	}
	
	public void update(TurnInfo info) {
		info.linesOld(lines);
		if (info.linesRemoved() != null) {
			lines += info.linesRemoved();
			update();
		}
		info.linesNew(lines);
	}
	
	public int startRow() {
		return 6;
	}
	
	public int startColumn() {
		return 11;
	}
	
	public int endRow() {
		return 8;
	}
	
	public int endColumn() {
		return 20;
	}
}