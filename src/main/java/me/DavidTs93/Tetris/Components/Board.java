package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.*;
import me.DavidTs93.Tetris.Displays.PiecePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Board extends Component implements ActionListener {
	public static final int BOARD_COLUMNS = 10;
	private static final int START_MILLISECONDS = 1000;
	private static final int MIN_MILLISECONDS = 200;
	private static final int TOLERANCE = MIN_MILLISECONDS / 10;
	private static final int DIFF_MILLISECONDS;
	private static final int START_ROW = 2;
	
	static {
		float max = START_MILLISECONDS - MIN_MILLISECONDS;
		int diff = Math.round(max / Level.MAX_LEVEL);
		int actual = diff * Level.MAX_LEVEL;
		DIFF_MILLISECONDS = actual > max + TOLERANCE ? diff - 1 : (actual < max + TOLERANCE ? diff + 1 : diff);
	}
	
	private final PiecePanel piecePanel;
	private Coordinates coordinates;
	private Colors[][] board;
	private final BoardPanel boardPanel;
	private Timer timer;
	
	public Board(TetrisGame game) {
		super(game);
		this.piecePanel = new PiecePanel(this,(tetromino,rotation) -> coordinates.subtract(START_ROW + (tetromino.height(rotation) % 2 == 0 ? 1 : 0),0));
		add(this.piecePanel);
		this.boardPanel = new BoardPanel();
		add(this.boardPanel);
	}
	
	@Override
	public void changeState(TetrisGame.State oldState,TetrisGame.State newState) {
		if (newState.isGameOver()){
			timer.stop();
			timer = null;
		} else if (oldState.isGameOver()) {
			if (newState.isGameRunning()) {	// start
				this.board = new Colors[endRow() - startRow() + 1][endColumn() - startColumn() + 1];
				piecePanel.repaint();
				setTimer(Level.START_LEVEL);
			}
		} else if (oldState.isGameRunning()) {
			if (!newState.isGameRunning()) pause();
		} else {
			if (newState.isGameRunning()) unpause();
		}
	}
	
	private void pause() {
		if (timer != null) timer.stop();
	}
	
	private void unpause() {
		if (timer != null) timer.start();
	}
	
	private Tetromino piece() {
		return piecePanel.piece();
	}
	
	private Rotation rotation() {
		return piecePanel.rotation();
	}
	
	private boolean newPiece(Tetromino piece) {
		boolean isNew = piece() == null;
		if (isNew) this.piecePanel.repaintOnUpdate(false);
		this.piecePanel.piece(piece);
		this.piecePanel.rotation(Rotation.ZERO);
		if (isNew) this.piecePanel.repaintOnUpdate(true);
		this.coordinates = new Coordinates(START_ROW,centerColumn() - startColumn() - 1);
		if (board == null || piece.testPlacing(rotation(),coordinates,board)) return true;
		this.piecePanel.piece(null);
		this.coordinates = coordinates.subtract(1,0);
		if (piece.testPlacing(rotation(),coordinates,board)) piece.fill(rotation(),coordinates,board).forEach(coordinate -> board[coordinate.row()][coordinate.column()] = piece.colors());
		return false;
	}
	
	public void newGame(TurnInfo info) {
		board = null;
		newPiece(info.piece());
	}
	
	private TurnInfo addLinesBonus(TurnInfo info,int lines,boolean drop) {
		int scoreAdd = lines;
		if (drop) scoreAdd *= 2;
		return info.scoreAdd(scoreAdd);
	}
	
	private Integer removeLines(CompletableFuture<TurnInfo> completable,TurnInfo info) {
		int height = piece().height(rotation());
		List<Integer> removed = new ArrayList<>();
		completable.thenRun(() -> removeRemovedLines(removed));
		for (int row = coordinates.row() - height + 1; row <= coordinates.row(); row++) {
			if (row >= board.length) continue;
			boolean hasEmpty = false;
			for (Colors colors : board[row]) if (colors == null) {
				hasEmpty = true;
				break;
			}
			if (hasEmpty) continue;
			removed.add(row);
		}
		Timer t = new Timer(40,null);
		t.addActionListener(new ActionListener() {
			private int i = centerColumn() - startColumn();
			
			@Override
			public void actionPerformed(ActionEvent e) {
				t.stop();
				if (i < 0 || removed.isEmpty()) {
					if (i == -1) i--;
					else {
						completable.complete(info);
						unpause();
						return;
					}
				} else {
					removed.forEach(row -> board[row][i] = null);
					removed.forEach(row -> board[row][BOARD_COLUMNS - i - 1] = null);
					i--;
					boardPanel.repaint();
				}
				t.start();
			}
		});
		t.start();
		piecePanel.piece(null);
		t.setInitialDelay(t.getDelay());
		return removed.isEmpty() ? null : removed.size();
	}
	
	private void removeRemovedLines(List<Integer> removed) {
		List<Colors[]> list = new ArrayList<>(Arrays.asList(board));
		for (int row : removed) {
			int len = list.remove(row).length;
			list.addFirst(new Colors[len]);
		}
		board = list.toArray(Colors[][]::new);
	}
	
	private CompletableFuture<TurnInfo> down(boolean button) {
		TurnInfo info = new TurnInfo(piece());
		Coordinates newCoordinates = coordinates.add(1,0);
		if (piece().testPlacing(rotation(),newCoordinates,board)) {
			coordinates = newCoordinates;
			if (button) {
				addLinesBonus(info,1,false);
				timer.restart();
			}
			return CompletableFuture.completedFuture(info);
		} else {
			if (button) return CompletableFuture.completedFuture(null);
			pause();
			info.turnOver(true);
			piece().fill(rotation(),coordinates,board).forEach(coordinate -> board[coordinate.row()][coordinate.column()] = piece().colors());
//			boardPanel.repaint();
			CompletableFuture<TurnInfo> completable = new CompletableFuture<>();
			info.linesRemoved(removeLines(completable,info));
			return completable;
		}
	}
	
	private CompletableFuture<TurnInfo> rotate(boolean next) {
		Rotation newRotation = next ? rotation().next() : rotation().previous();
		Coordinates newCoordinates = coordinates;
		if (piece() == Tetromino.I && coordinates.row() == START_ROW && (newRotation == Rotation.ONE || newRotation == Rotation.THREE)) newCoordinates = newCoordinates.add(1,0);
		TurnInfo info = null;
		if (piece().testPlacing(newRotation,newCoordinates,board)) {
			coordinates = newCoordinates;
			piecePanel.rotation(newRotation);
			info = new TurnInfo(piece());
		}
		return CompletableFuture.completedFuture(info);
	}
	
	private CompletableFuture<TurnInfo> move(boolean right) {
		Coordinates newCoordinates = right ? coordinates.add(0,1) : coordinates.subtract(0,1);
		TurnInfo info = null;
		if (piece().testPlacing(rotation(),newCoordinates,board)) {
			coordinates = newCoordinates;
			info = new TurnInfo(piece());
		}
		return CompletableFuture.completedFuture(info);
	}
	
	private CompletableFuture<TurnInfo> drop() {
		int lines = 0;
		do {
			lines++;
		} while (piece().testPlacing(rotation(),coordinates.add(lines,0),board));
		lines--;
		TurnInfo info = null;
		if (lines > 0) {
			coordinates = coordinates.add(lines,0);
			timer.restart();
			info = addLinesBonus(new TurnInfo(piece()),lines,true);
		}
		return CompletableFuture.completedFuture(info);
	}
	
	private void setTimer(Integer level) {
		if (level == null) return;
		if (timer != null) timer.stop();
		timer = new Timer(START_MILLISECONDS - DIFF_MILLISECONDS * level,this);
		timer.start();
	}
	
	public void update(TurnInfo info) {
		if (!Boolean.TRUE.equals(info.turnOver())) return;
		if (Boolean.TRUE.equals(info.levelAdd())) setTimer(info.levelNew());
		if (!newPiece(info.piece())) info.lose(true);
	}
	
	public CompletableFuture<TurnInfo> update(MoveType moveType) {
		return piece() == null || game().state() != TetrisGame.State.PLAY ? CompletableFuture.completedFuture(null) : switch (moveType) {
			case ROTATE_CLOCKWISE -> rotate(true);
			case ROTATE_COUNTERCLOCKWISE -> rotate(false);
			case RIGHT -> move(true);
			case LEFT -> move(false);
			case DROP -> drop();
			case DOWN -> down(true);
		};
	}
	
	public int startRow() {
		return 10;
	}
	
	public int startColumn() {
		return 11;
	}
	
	public int endRow() {
		return 29;
	}
	
	public int endColumn() {
		return 20;
	}
	
	public void actionPerformed(ActionEvent e) {
		game().update(down(false));
	}
	
	private class BoardPanel extends JPanel implements Resizeable {
		private BoardPanel() {
			setOpaque(false);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			BufferedImage image = new BufferedImage(game().squareSize() * columns(),game().squareSize() * rows(),BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = image.createGraphics();
			drawBoard(g2d);
			g2d.dispose();
			Coordinates coordinates = game().indexToPosition(new Coordinates(0,0));
			g.drawImage(image,coordinates.column(),coordinates.row(),this);
		}
		
		protected void drawBoard(Graphics2D g2d) {
			if (board != null) for (int i = 0; i < board.length; i++) {
				Colors[] line = board[i];
				for (int j = 0; j < line.length; j++) {
					Colors colors = line[j];
					if (colors != null) game().drawSquare(g2d,new Coordinates(i,j),colors);
				}
			}
		}
		
		public void resize() {
			Coordinates start = game().indexToPosition(new Coordinates(1,1));
			Coordinates add = game().indexToPosition(new Coordinates(rows(),columns()));
			setBounds(start.column(),start.row(),add.column(),add.row());
			repaint();
		}
	}
}