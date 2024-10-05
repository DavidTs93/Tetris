package me.DavidTs93.Tetris.Components;

import me.DavidTs93.Tetris.Displays.Label;
import me.DavidTs93.Tetris.Displays.PiecePanel;
import me.DavidTs93.Tetris.Info.Coordinates;
import me.DavidTs93.Tetris.Info.State;
import me.DavidTs93.Tetris.Info.TurnInfo;
import me.DavidTs93.Tetris.Parts.Tetromino;
import me.DavidTs93.Tetris.TetrisGame;

public class Next extends Component {
	private final PiecePanel piecePanel;
	private final int calcRow;
	
	public Next(TetrisGame game) {
		super(game);
		this.calcRow = (startRow() + endRow() - 1) / 2 - startRow();
		add(new Label(this,1,1).text("NEXT"));
		this.piecePanel = new PiecePanel(this,(tetromino,rotation) -> new Coordinates(calcRow + (tetromino.height(rotation) % 2 == 0 ? 0 : 1),centerColumn() - startColumn() - (tetromino.width(rotation) % 2 == 0 ? 1 : 0))).noDisplayOnStart(true);
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
	public void changeState(State oldState,State newState) {
		if (oldState.isGameOver() && newState.isGameRunning()) piecePanel.redraw();
	}
	
	public void update(TurnInfo info) {
		if (info.piece() == null || Boolean.TRUE.equals(info.turnOver())) info.piece(newPiece(true));
		piecePanel.redraw();
	}
	
	public int startRow() {
		return 18;
	}
	
	public int startColumn() {
		return 22;
	}
	
	public int endRow() {
		return 23;
	}
	
	public int endColumn() {
		return 29;
	}
}