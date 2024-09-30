package me.DavidTs93.Tetris;

import me.DavidTs93.Tetris.Components.Component;
import me.DavidTs93.Tetris.Components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class TetrisGame extends JFrame implements ActionListener {
	private static final int SIDE_COLUMNS = 10;
	private static final int TOTAL = Board.BOARD_COLUMNS + 2 + (SIDE_COLUMNS * 2);
	
	private static final int SIZE_RATIO = 10;
	
	private int unit,squareSize,width,height,offsetX,offsetY;
	private final Board board;
	private final List<Component> components;
	private State state;
	
	public TetrisGame() {
		super("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		int maxWidth = dm.getWidth(),maxHeight = dm.getHeight();
		int size = Math.min(maxWidth * 7 / 10,maxHeight * 7 / 10);
		setPreferredSize(new Dimension(size,size));
		this.state = State.START;
		this.board = new Board(this);
		this.components = Stream.of(new Empty(this),new Title(this),new Next(this),new Statistics(this),new Lines(this),new Level(this),new Score(this),this.board).toList();
		afterResize(size,size);
		this.components.reversed().forEach(this::add);
		addComponentListener(new GameResizeAdapter());
		addKeyListener(new GameKeyAdapter());
		setFocusable(true);
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	public void update(CompletableFuture<TurnInfo> completable) {
		completable.thenAccept(TetrisGame.this::update);
	}
	
	private void update(TurnInfo info) {
		if (info == null) return;
		this.components.forEach(component -> component.update(info));
		State oldState = state;
		if (Boolean.TRUE.equals(info.lose())) this.state = State.LOSE;
		else if (Boolean.TRUE.equals(info.win())) this.state = State.WIN;
		if (state.isGameOver()) changeState(oldState);
		redraw();
	}
	
	public void drawSquare(Graphics g,Coordinates coordinates,Colors color) {
		Coordinates pos = indexToPosition(coordinates);
		Square.drawSquare(g,pos.column(),pos.row(),this.unit,this.squareSize,color);
	}
	
	private void afterResize(int width,int height) {
		this.unit = Math.max(Math.min(width / (TOTAL * SIZE_RATIO),height / (TOTAL * SIZE_RATIO)),1);
		this.squareSize = unit() * SIZE_RATIO;
		this.width = squareSize() * TetrisGame.TOTAL;
		this.height = squareSize() * TetrisGame.TOTAL;
		this.offsetX = (width - width()) / 2;
		this.offsetY = (height - height()) / 2;
		setSize(width,height);
		setBounds(0,0,width,height);
		components.forEach(Resizeable::resize);
		repaint();
	}
	
	private void redraw() {
		afterResize(getWidth(),getHeight());
	}
	
	public State state() {
		return state;
	}
	
	public int unit() {
		return this.unit;
	}
	
	public int squareSize() {
		return this.squareSize;
	}
	
	public int width() {
		return this.width;
	}
	
	public int height() {
		return this.height;
	}
	
	public int indexToPosition(int idx) {
		return idx * this.squareSize;
	}
	
	public int indexToPositionOffsetX(int idx) {
		return idx * this.squareSize + this.offsetX;
	}
	
	public int indexToPositionOffsetY(int idx) {
		return idx * this.squareSize + this.offsetY;
	}
	
	public Coordinates indexToPosition(Coordinates coordinates) {
		return new Coordinates(indexToPosition(coordinates.row()),indexToPosition(coordinates.column()));
	}
	
	public Coordinates indexToPositionAbsolute(Coordinates coordinates) {
		return new Coordinates(indexToPositionOffsetY(coordinates.row()),indexToPositionOffsetX(coordinates.column()));
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	private void changeState(State oldState) {
		components.forEach(component -> component.changeState(oldState,state));
	}
	
	private class GameKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			MoveType moveType = null;
			if (key == KeyEvent.VK_LEFT) moveType = MoveType.LEFT;
			else if (key == KeyEvent.VK_RIGHT) moveType = MoveType.RIGHT;
			else if (key == KeyEvent.VK_UP) moveType = MoveType.ROTATE_CLOCKWISE;
			else if (key == KeyEvent.VK_DOWN) moveType = MoveType.DOWN;
			else if (key == KeyEvent.VK_ESCAPE) {
				if (state == State.PLAY) {
					state = State.PAUSE;
					changeState(State.PLAY);
				} else if (state == State.PAUSE) {
					state = State.PLAY;
					changeState(State.PAUSE);
				} else return;
				redraw();
			} else if (key == KeyEvent.VK_SPACE) {
				State oldState = state;
				if (state.isGameOver()) {
					TurnInfo info = new TurnInfo(null);
					components.forEach(component -> component.newGame(info));
					state = State.PLAY;
					changeState(oldState);
					redraw();
					return;
				}
				if (state.isGameRunning()) moveType = MoveType.DROP;
			}
			if (moveType != null) update(board.update(moveType));
		}
	}
	
	private class GameResizeAdapter extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			afterResize(getWidth(),getHeight());
		}
	}
	
	public enum State {
		START(null,"Press SPACE to start"),
		PLAY(null,null),
		PAUSE("PAUSE",null),
		LOSE("GAME OVER","Press SPACE to restart"),
		WIN("YOU WIN!","Press SPACE to restart");
		
		private final String top;
		private final String bottom;
		
		State(String top,String bottom) {
			this.top = top == null ? "" : top;
			this.bottom = bottom == null ? "" : bottom;
		}
		
		public String top() {
			return top;
		}
		
		public String bottom() {
			return bottom;
		}
		
		public boolean isGameOver() {
			return this == START || this == LOSE || this == WIN;
		}
		
		public boolean isGameRunning() {
			return this == PLAY;
		}
	}
}