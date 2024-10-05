package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Displays.Label;
import me.DavidTs93.Tetris.Info.TurnInfo;
import me.DavidTs93.Tetris.TetrisGame;

import java.util.Objects;

public class Score extends Component {
	public static final int MAX_SCORE = 999999;
	public static final int SCORE_LENGTH = (int) Math.ceil(Math.log10(MAX_SCORE));
	public static final String FORMAT = "%" + String.format("%02d",SCORE_LENGTH) + "d";
	
	private int score;
	private final Label label;
	
	public Score(TetrisGame game) {
		super(game);
		add(new Label(this,2,1).text("SCORE"));
		this.label = new Label(this,2,5);
		add(this.label);
		update();
	}
	
	private void update() {
		label.setText(String.format(FORMAT,score));
	}
	
	public void newGame(TurnInfo info) {
		this.score = 0;
		update();
	}
	
	public void update(TurnInfo info) {
		info.scoreOld(score);
		Integer scoreAdd = info.scoreAdd();
		boolean update = false;
		if (scoreAdd != null) {
			score += scoreAdd;
			update = true;
		}
		info.scoreAdd(null);
		Integer removed = info.linesRemoved();
		if (removed != null && removed > 0) {
			score += (Objects.requireNonNull(info.levelOld()) + 1) * scoreLines(removed);
			update = true;
		}
		if (update) update();
		boolean win = score > MAX_SCORE;
		if (win) info.win(true);
		info.scoreNew(win ? MAX_SCORE : score);
	}
	
	private int scoreLines(int linesRemoved) {
		switch (linesRemoved) {
			case 4: return 1200;
			case 3: return 300;
			case 2: return 100;
			default: return 40;
		}
	}
	
	public int startRow() {
		return 6;
	}
	
	public int startColumn() {
		return 22;
	}
	
	public int endRow() {
		return 13;
	}
	
	public int endColumn() {
		return 29;
	}
}