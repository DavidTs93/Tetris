package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Coordinates;
import me.DavidTs93.Tetris.Displays.Label;
import me.DavidTs93.Tetris.Displays.PiecePanel;
import me.DavidTs93.Tetris.TetrisGame;
import me.DavidTs93.Tetris.Tetromino;
import me.DavidTs93.Tetris.TurnInfo;

public class Next extends Component {
	private final PiecePanel piecePanel;
	
	public Next(TetrisGame game) {
		super(game);
		add(new Label(this,1,new Coordinates(1,0)).text("NEXT"));
		this.piecePanel = new PiecePanel(this,(tetromino,rotation) -> new Coordinates(endRow() - startColumn() + (tetromino.height(rotation) % 2 == 0 ? 0 : 1),centerColumn() - startColumn() - (tetromino.width(rotation) % 2 == 0 ? 1 : 0))).noDisplayOnStart(true);
		add(this.piecePanel);
	}
	
	public void newGame(TurnInfo info) {
		this.piecePanel.piece(null);
		info.piece(newPiece(false));
	}
	
	private Tetromino piece() {
		return piecePanel.piece();
	}
	
	private Tetromino newPiece(boolean repaint) {
		if (piece() == null) {
			this.piecePanel.repaintOnUpdate(false);
			setRandom();
			this.piecePanel.repaintOnUpdate(true);
		}
		Tetromino prev = piece();
		setRandom();
		if (repaint) piecePanel.repaint();
		return prev;
	}
	
	private void setRandom() {
		this.piecePanel.piece(Tetromino.random());
	}
	
	@Override
	public void changeState(TetrisGame.State oldState,TetrisGame.State newState) {
		if (oldState.isGameOver() && newState.isGameRunning()) piecePanel.repaint();
	}
	
	public void update(TurnInfo info) {
		if (info.piece() == null || Boolean.TRUE.equals(info.turnOver())) info.piece(newPiece(true));
	}
	
	public int startRow() {
		return 19;
	}
	
	public int startColumn() {
		return 22;
	}
	
	public int endRow() {
		return 24;
	}
	
	public int endColumn() {
		return 29;
	}
}