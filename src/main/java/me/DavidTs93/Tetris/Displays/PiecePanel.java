package me.DavidTs93.Tetris.Displays;

import me.DavidTs93.Tetris.Info.Coordinates;
import me.DavidTs93.Tetris.Info.Rotation;
import me.DavidTs93.Tetris.Info.State;
import me.DavidTs93.Tetris.Parts.TetrisPart;
import me.DavidTs93.Tetris.Parts.Tetromino;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.function.BiFunction;

public class PiecePanel extends JPanel implements Display {
	protected final TetrisPart parent;
	protected final BiFunction<Tetromino,Rotation,Coordinates> coordinatesSupplier;
	private Tetromino piece;
	private BufferedImage pieceImage;
	private Rotation rotation = Rotation.ZERO;
	private boolean noDisplayOnStart = false;
	private boolean repaintOnUpdate = false;
	
	public PiecePanel(TetrisPart parent,BiFunction<Tetromino,Rotation,Coordinates> coordinatesSupplier) {
		this.parent = parent;
		this.coordinatesSupplier = coordinatesSupplier;
		setOpaque(false);
		setLayout(null);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (piece == null || (noDisplayOnStart && parent.game().state() == State.START)) return;
		super.paintComponent(g);
		Coordinates coords = coordinatesSupplier.apply(piece,rotation);
		Coordinates coordinates = parent.game().indexToPosition(coords);
		g.drawImage(pieceImage,coordinates.column(),coordinates.row(),this);
	}
	
	public PiecePanel redraw() {
		this.pieceImage = piece == null ? null : piece.draw(parent.game(),rotation);
		if (repaintOnUpdate) repaint();
		return this;
	}
	
	public Tetromino piece() {
		return piece;
	}
	
	public PiecePanel piece(Tetromino piece) {
		if (Objects.equals(this.piece,piece)) return this;
		this.piece = piece;
		return repaintOnUpdate ? redraw() : this;
	}
	
	public boolean noDisplayOnStart() {
		return noDisplayOnStart;
	}
	
	public PiecePanel noDisplayOnStart(boolean noDisplayOnStart) {
		this.noDisplayOnStart = noDisplayOnStart;
		return this;
	}
	
	public Rotation rotation() {
		return rotation;
	}
	
	public PiecePanel rotation(Rotation rotation) {
		if (Objects.equals(this.rotation,rotation)) return this;
		this.rotation = rotation;
		return repaintOnUpdate ? redraw() : this;
	}
	
	public boolean repaintOnUpdate() {
		return repaintOnUpdate;
	}
	
	public void repaintOnUpdate(boolean repaintOnUpdate) {
		this.repaintOnUpdate = repaintOnUpdate;
	}
	
	public TetrisPart parent() {
		return parent;
	}
	
	public void afterResize() {
		redraw();
	}
}