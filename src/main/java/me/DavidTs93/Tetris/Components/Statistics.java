package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Coordinates;
import me.DavidTs93.Tetris.Displays.Label;
import me.DavidTs93.Tetris.Displays.PiecePanel;
import me.DavidTs93.Tetris.TetrisGame;
import me.DavidTs93.Tetris.Tetromino;
import me.DavidTs93.Tetris.TurnInfo;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Statistics extends Component {
	private final Map<Tetromino,Statistic> statistics;
	
	public Statistics(TetrisGame game) {
		super(game);
		add(new Label(this,1,new Coordinates(1,0)).text("STATISTICS"));
		this.statistics = new HashMap<>();
		for (Tetromino tetromino : Tetromino.values()) this.statistics.put(tetromino,new Statistic(this,tetromino));
	}
	
	public void newGame(TurnInfo info) {
		statistics.values().forEach(Statistic::reset);
		statistics.get(info.piece()).increment();
	}
	
	public void update(TurnInfo info) {
		if (Boolean.TRUE.equals(info.turnOver())) statistics.get(info.piece()).increment();
		info.statistics(statistics.get(info.piece()).amount);
	}
	
	public int startRow() {
		return 6;
	}
	
	public int startColumn() {
		return 2;
	}
	
	public int endRow() {
		return 29;
	}
	
	public int endColumn() {
		return 9;
	}
	
	private static class Statistic {
		private static final String FORMAT = "%" + String.format("%02d",Level.LEVEL_LENGTH + 1) + "d";
		
		private int amount;
		private final Label label;
		
		private Statistic(Statistics parent,Tetromino tetromino) {
			parent.add(new PiecePanel(parent,(piece,rotation) -> new Coordinates(rowTetromino(piece),parent.centerColumn() - parent.startColumn() - 2)).piece(tetromino));
			this.label = new Label(parent,1,new Coordinates(rowText(tetromino),1),new Coordinates(1,parent.columns() - 2)).horizontal(SwingConstants.RIGHT);
			parent.add(this.label);
			reset();
		}
		
		private int rowTetromino(Tetromino tetromino) {
			switch (tetromino) {
				case I: return 3;
				case J: return 6;
				case L: return 9;
				case O: return 11;
				case S: return 15;
				case T: return 18;
				case Z: return 21;
				default: throw new IllegalArgumentException();
			}
		}
		
		private int rowText(Tetromino tetromino) {
			return rowTetromino(tetromino) + (tetromino == Tetromino.O ? 2 : 1);
		}
		
		private void update() {
			label.setText(String.format(FORMAT,amount));
		}
		
		public void increment() {
			amount++;
			update();
		}
		
		public void reset() {
			amount = 0;
			update();
		}
	}
}