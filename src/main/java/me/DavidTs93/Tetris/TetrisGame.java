package me.DavidTs93.Tetris;

import me.DavidTs93.Tetris.Components.Component;
import me.DavidTs93.Tetris.Components.*;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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
		setMinimumSize(new Dimension(size,size));
		setSize(new Dimension(size,size));
		this.state = State.START;
		this.board = new Board(this);
		this.components = Stream.of(new Empty(this),new Title(this),new Next(this),new Statistics(this),new Lines(this),new Level(this),new Score(this),this.board).collect(Collectors.toList());
		afterResize(size,size);
		List<Component> componentsAdd = new ArrayList<>(this.components);
		Collections.reverse(componentsAdd);
		componentsAdd.forEach(this::add);
		addComponentListener(new GameResizeAdapter());
		addKeyListener(new GameKeyAdapter());
		setFocusable(true);
		pack();
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
		private static final int ROTATE_CLOCKWISE = KeyEvent.VK_W;
		private static final int ROTATE_COUNTERCLOCKWISE = KeyEvent.VK_E;
		
		private final Set<Integer> pressedKeys = new HashSet<>();
		private final MoveActionListener moveListener = new MoveActionListener();
		private final DownActionListener downListener = new DownActionListener();
		
		private void stop() {
			moveListener.stop();
			downListener.stop();
		}
		
		private void start() {
			moveListener.start();
			downListener.start();
		}
		
		private void restart() {
			stop();
			start();
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_RIGHT) moveListener.remove(true);
			else if (key == KeyEvent.VK_LEFT) moveListener.remove(false);
			else if (key == KeyEvent.VK_DOWN) downListener.remove();
			else pressedKeys.remove(key);
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			boolean done = true;
			if (key == KeyEvent.VK_RIGHT) moveListener.add(true);
			else if (key == KeyEvent.VK_LEFT) moveListener.add(false);
			else if (key == KeyEvent.VK_DOWN) downListener.add();
			else done = false;
			if (done) return;
			if (!pressedKeys.add(key)) return;
			done = true;
			if (key == ROTATE_CLOCKWISE) update(board.update(MoveType.ROTATE_CLOCKWISE));
			else if (key == ROTATE_COUNTERCLOCKWISE) update(board.update(MoveType.ROTATE_COUNTERCLOCKWISE));
			else done = false;
			if (done) return;
			if (key == KeyEvent.VK_ESCAPE) {
				if (state == State.PLAY) {
					state = State.PAUSE;
					changeState(State.PLAY);
					stop();
				} else if (state == State.PAUSE) {
					state = State.PLAY;
					changeState(State.PAUSE);
					start();
				} else return;
				redraw();
			} else if (key == KeyEvent.VK_SPACE) {
				State oldState = state;
				if (state.isGameOver()) {
					TurnInfo info = new TurnInfo(null);
					components.forEach(component -> component.newGame(info));
					state = State.PLAY;
					changeState(oldState);
					start();
					redraw();
				} else if (state.isGameRunning()) {
					update(board.update(MoveType.DROP));
					restart();
				}
			}
		}
		
		private class MoveActionListener extends Timer implements ActionListener {
			private boolean pressedRight = false;
			private boolean pressedLeft = false;
			private Boolean moveRight = null;
			
			private MoveActionListener() {
				super(60,null);
				stop();
				setInitialDelay(0);
				addActionListener(this);
			}
			
			public void add(boolean right) {
				if (right) {
					if (pressedRight) return;
					pressedRight = true;
					moveRight = true;
				} else {
					if (pressedLeft) return;
					pressedLeft = true;
					moveRight = false;
				}
				start();
			}
			
			public void remove(boolean right) {
				if (right) {
					pressedRight = false;
					if (pressedLeft) moveRight = false;
					else {
						moveRight = null;
						moveListener.stop();
					}
				} else {
					pressedLeft = false;
					if (pressedRight) moveRight = true;
					else {
						moveRight = null;
						moveListener.stop();
					}
				}
			}
			
			public void actionPerformed(ActionEvent e) {
				if (moveRight != null) update(board.update(moveRight ? MoveType.RIGHT : MoveType.LEFT));
			}
		}
		
		private class DownActionListener extends Timer implements ActionListener {
			private boolean isPressed = false;
			
			private DownActionListener() {
				super(40,null);
				stop();
				setInitialDelay(0);
				addActionListener(this);
			}
			
			public void add() {
				if (isPressed) return;
				isPressed = true;
				start();
			}
			
			public void remove() {
				isPressed = false;
				stop();
			}
			
			public void actionPerformed(ActionEvent e) {
				if (isPressed) update(board.update(MoveType.DOWN));
			}
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