package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Displays.Label;
import me.DavidTs93.Tetris.Info.Database;
import me.DavidTs93.Tetris.Info.ScoreRecords;
import me.DavidTs93.Tetris.Info.TurnInfo;
import me.DavidTs93.Tetris.TetrisGame;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class Score extends Component {
	public static final int MAX_SCORE = 999999;
	public static final int SCORE_LENGTH = (int) Math.ceil(Math.log10(MAX_SCORE));
	public static final String FORMAT = "%" + String.format("%02d",SCORE_LENGTH) + "d";
	
	private final Database database;
	private final Label labelScoreTop;
	private final Label labelScore;
	private int score;
	
	public Score(TetrisGame game) throws SQLException, IOException,ClassNotFoundException {
		super(game);
		this.database = new Database();
		add(new Label(this,2,1).text("TOP"));
		this.labelScoreTop = new Label(this,2,3);
		add(this.labelScoreTop);
		add(new Label(this,2,6).text("SCORE"));
		this.labelScore = new Label(this,2,8);
		add(this.labelScore);
		updateTopScore();
		update();
	}
	
	private void updateTopScore() {
		try {
			List<ScoreRecords> records = database.highScores(1);
			ScoreRecords record;
			if (records.isEmpty()) record = new ScoreRecords(0);
			else record = records.get(0);
			labelScoreTop.setText(String.format(Score.FORMAT,record.score()));
		} catch (Exception e) {
			e.printStackTrace();	// FIXME
		}
	}
	
	private void update() {
		labelScore.setText(String.format(FORMAT,score));
	}
	
	public void addScore(String name) {
		try {
			database.addScore(name,score);
			updateTopScore();
		} catch (Exception e) {
			e.printStackTrace();	// FIXME
		}
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
		return 16;
	}
	
	public int endColumn() {
		return 29;
	}
}