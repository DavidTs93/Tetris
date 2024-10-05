package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Displays.Label;
import me.DavidTs93.Tetris.Info.State;
import me.DavidTs93.Tetris.Info.TurnInfo;
import me.DavidTs93.Tetris.TetrisGame;

public class Level extends Component {
	public static final int START_LEVEL = 0;
	public static final int MAX_LEVEL = 99;
	private static final int LEVEL_LENGTH = (int) Math.ceil(Math.log10(MAX_LEVEL));
	private static final String FORMAT = "%" + String.format("%02d",LEVEL_LENGTH) + "d";
	
	private int level = 0;
	private int levelLines = 0;
	private final Label labelLevel;
	
	public Level(TetrisGame game) {
		super(game);
		add(new Label(this,1,1).text("LEVEL"));
		this.labelLevel = new Label(this,1,3);
		add(this.labelLevel);
		update();
	}
	
	private void update() {
		labelLevel.setText(String.format(FORMAT,level));
	}
	
	@Override
	public void changeState(State oldState,State newState) {
		if (oldState.isGameOver() && newState.isGameRunning()) update();
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
		return 25;
	}
	
	public int startColumn() {
		return 22;
	}
	
	public int endRow() {
		return 29;
	}
	
	public int endColumn() {
		return 29;
	}
}