package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Displays.Label;
import me.DavidTs93.Tetris.Info.Database;
import me.DavidTs93.Tetris.Info.ScoreRecords;
import me.DavidTs93.Tetris.Info.TurnInfo;
import me.DavidTs93.Tetris.TetrisGame;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class HighScore extends Component {
	private final Database database;
	private final Label label;
	private int lastScore;
	
	public HighScore(TetrisGame game) throws SQLException,IOException,ClassNotFoundException {
		super(game);
		this.database = new Database();
		add(new Label(this,1,1).text("HIGH SCORE"));
		this.label = new Label(this,1,2);
		add(this.label);
		update();
	}
	
	public void newGame(TurnInfo info) {}
	
	public void update(TurnInfo info) {
		Integer score = info.scoreNew();
		if (score != null) lastScore = score;
	}
	
	private void update() {
		try {
			List<ScoreRecords> records = database.highScores(1);
			ScoreRecords record;
			if (records.isEmpty()) record = new ScoreRecords(0);//records.add(new ScoreRecords(100).add("D"));
			else record = records.get(0);
			label.setText(String.format(Score.FORMAT,record.score()));
		} catch (Exception e) {
			e.printStackTrace();	// FIXME
		}
	}
	
	public void addScore(String name) {
		try {
			database.addScore(name,lastScore);
			update();
		} catch (Exception e) {
			e.printStackTrace();	// FIXME
		}
	}
	
	public int startRow() {
		return 26;
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