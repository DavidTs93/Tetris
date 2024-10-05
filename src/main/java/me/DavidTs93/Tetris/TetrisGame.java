package me.DavidTs93.Tetris;

import me.DavidTs93.Tetris.Components.Component;
import me.DavidTs93.Tetris.Components.*;
import me.DavidTs93.Tetris.Displays.InputPanel;
import me.DavidTs93.Tetris.Info.Coordinates;
import me.DavidTs93.Tetris.Info.State;
import me.DavidTs93.Tetris.Info.TurnInfo;
import me.DavidTs93.Tetris.Parts.*;

import javax.swing.Timer;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TetrisGame extends JLayeredPane implements ActionListener,TetrisPart,Resizeable {
	private static final int SIDE_COLUMNS = 10;
	private static final int TOTAL = Board.BOARD_COLUMNS + 2 + (SIDE_COLUMNS * 2);
	private static final int SIZE_RATIO = 10;
	
	private int unit,squareSize;
	private State state;
	private final Board board;
	private final Score score;
	private final List<Component> components;
	private final InputPanel nameInput;
	private final List<Resizeable> resizeables;
	private final GameKeyAdapter keysListener;
	private boolean inputPaused;
	
	public TetrisGame() throws SQLException,IOException,ClassNotFoundException {
		this.state = State.START;
		this.board = new Board(this);
		this.score = new Score(this);
		this.components = Stream.of(new Empty(this),new Title(this),new Next(this),new Statistics(this),new Lines(this),new Level(this),this.score,this.board).collect(Collectors.toList());
		this.nameInput = new InputPanel(this,this::nameSubmit,this::nameSubmit,InputPanel.defaultStart(new Coordinates(6,2),new Coordinates(24,28),InputPanel.DEFAULT_TOTAL_DIMENSIONS),InputPanel.DEFAULT_TOTAL_DIMENSIONS);
		this.resizeables = new ArrayList<>();
		List<Component> componentsAdd = new ArrayList<>(this.components);
		Collections.reverse(componentsAdd);
		componentsAdd.forEach(this::add);
		this.keysListener = new GameKeyAdapter();
		setFocusable(true);
	}
	
	@Override
	public java.awt.Component add(java.awt.Component comp) {
		if (comp instanceof Resizeable) resizeables.add((Resizeable) comp);
		return super.add(comp);
	}
	
	public void resize() {
		resizeables.forEach(Resizeable::resize);
		repaint();
	}
	
	public void update(CompletableFuture<TurnInfo> completable) {
		completable.thenAccept(TetrisGame.this::update);
	}
	
	public boolean inputPaused() {
		return inputPaused;
	}
	
	private void nameSubmit(String name) {
		score.addScore(name);
		remove(nameInput);
		unpauseInput();
		changeState(state);
		repaint();
	}
	
	private void update(TurnInfo info) {
		if (info == null) return;
		this.components.forEach(component -> component.update(info));
		State oldState = state;
		if (Boolean.TRUE.equals(info.lose())) this.state = State.LOSE;
		else if (Boolean.TRUE.equals(info.win())) this.state = State.WIN;
		if (state.isGameOver()) {
			pauseInput();
			changeState(oldState);
			add(nameInput,JLayeredPane.POPUP_LAYER,0);
			nameInput.resize();
		}
	}
	
	public void drawSquare(Graphics g,Coordinates coordinates,Colors color) {
		Coordinates pos = indexToPosition(coordinates);
		Square.drawSquare(g,pos.column(),pos.row(),this.unit,this.squareSize,color);
	}
	
	private void afterResize(int width,int height) {
		this.unit = Math.max(Math.min(width / (TOTAL * SIZE_RATIO),height / (TOTAL * SIZE_RATIO)),1);
		this.squareSize = unit() * SIZE_RATIO;
		int actualSize = squareSize() * TetrisGame.TOTAL;
		int offsetX = (width - actualSize) / 2;
		int offsetY = (height - actualSize) / 2;
		setBounds(offsetX,offsetY,actualSize,actualSize);
		resize();
	}
	
	private void pauseInput() {
		inputPaused = true;
		keysListener.stop();
	}
	
	private void unpauseInput() {
		inputPaused = false;
		keysListener.start();
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
	
	public int indexToPosition(int idx) {
		return idx * this.squareSize;
	}
	
	public Coordinates indexToPosition(Coordinates coordinates) {
		return new Coordinates(indexToPosition(coordinates.row()),indexToPosition(coordinates.column()));
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	private void changeState(State oldState) {
		components.forEach(component -> component.changeState(oldState,state));
	}
	
	public TetrisGame game() {
		return this;
	}
	
	public int startRow() {
		return 0;
	}
	
	public int startColumn() {
		return 0;
	}
	
	public int endRow() {
		return TOTAL - 1;
	}
	
	public int endColumn() {
		return TOTAL - 1;
	}
	
	@Override
	public boolean hasBorder() {
		return false;
	}
	
	public void setBorder(Border border) {}
	
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
			if (inputPaused) return;
			if (key == ROTATE_CLOCKWISE) update(board.update(MoveType.ROTATE_CLOCKWISE));
			else if (key == ROTATE_COUNTERCLOCKWISE) update(board.update(MoveType.ROTATE_COUNTERCLOCKWISE));
			else if (key == KeyEvent.VK_ESCAPE) {
				if (state == State.PLAY) {
					state = State.PAUSE;
					changeState(State.PLAY);
					stop();
				} else if (state == State.PAUSE) {
					state = State.PLAY;
					changeState(State.PAUSE);
					start();
				}
			} else if (key == KeyEvent.VK_SPACE) {
				State oldState = state;
				if (state.isGameOver()) {
					TurnInfo info = new TurnInfo(null);
					components.forEach(component -> component.newGame(info));
					state = State.PLAY;
					changeState(oldState);
					start();
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
			
			@Override
			public void start() {
				if (moveRight != null && !inputPaused) super.start();
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
			
			@Override
			public void start() {
				if (isPressed && !inputPaused) super.start();
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
	
	private static class MainFrame extends JFrame {
		private final TetrisGame game;
		
		private MainFrame() throws SQLException,IOException,ClassNotFoundException {
			super("Tetris");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(null);
			DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
			int maxWidth = dm.getWidth(),maxHeight = dm.getHeight();
			int size = Math.min(maxWidth * 7 / 10,maxHeight * 7 / 10);
			setPreferredSize(new Dimension(size,size));
			setSize(new Dimension(size,size));
			this.game = new TetrisGame();
			add(this.game);
			this.game.afterResize(size,size);
			addComponentListener(new GameResizeAdapter());
			addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					game.keysListener.keyReleased(e);
				}

				@Override
				public void keyPressed(KeyEvent e) {
					game.keysListener.keyPressed(e);
				}
			});
			setFocusable(true);
			pack();
			setVisible(true);
		}
		
		private class GameResizeAdapter extends ComponentAdapter {
			@Override
			public void componentResized(ComponentEvent e) {
				game.afterResize(getWidth(),getHeight());
			}
		}
	}
	
	public static void main(String[] args) throws SQLException,IOException,ClassNotFoundException {
		new MainFrame();
	}
}