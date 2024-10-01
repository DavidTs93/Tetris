package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Coordinates;
import me.DavidTs93.Tetris.Displays.Label;
import me.DavidTs93.Tetris.TetrisGame;
import me.DavidTs93.Tetris.TurnInfo;

public class Level extends Component {
	public static final int START_LEVEL = 0;
	public static final int MAX_LEVEL = 99;
	public static final int LEVEL_LENGTH = (int) Math.ceil(Math.log10(MAX_LEVEL));
	private static final String FORMAT = "%" + String.format("%02d",LEVEL_LENGTH) + "d";
	
	private int level = 0;
	private int levelLines = 0;
	private final Label label;
	
	public Level(TetrisGame game) {
		super(game);
		this.label = new Label(this,1,new Coordinates(1,0));
		add(this.label);
		update();
	}
	
	private void update() {
		label.setText("LEVEL - " + String.format(FORMAT,level));
	}
	
	public void newGame(TurnInfo info) {
		this.level = START_LEVEL;
		this.levelLines = 0;
		update(info);
	}
	
	public void update(TurnInfo info) {
		info.levelOld(level);
		Integer removed = info.linesRemoved();
		if (removed != null && removed > 0) {
			levelLines += levelLines(removed);
			if (levelLines >= (level + 1) * 5) {
				levelLines = 0;
				level++;
				update();
			}
		}
		boolean win = level > MAX_LEVEL;
		if (win) info.win(true);
		info.levelNew(win ? MAX_LEVEL : level);
	}
	
	private int levelLines(int linesRemoved) {
		switch (linesRemoved) {
			case 4: return 8;
			case 3: return 5;
			case 2: return 3;
			default: return 1;
		}
	}
	
	public int startRow() {
		return 15;
	}
	
	public int startColumn() {
		return 22;
	}
	
	public int endRow() {
		return 17;
	}
	
	public int endColumn() {
		return 29;
	}
}