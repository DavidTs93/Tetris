package me.DavidTs93.Tetris.Displays;

import me.DavidTs93.Tetris.Components.Component;
import me.DavidTs93.Tetris.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.function.BiFunction;

public class PiecePanel extends JPanel implements Resizeable {
	protected final Component parent;
	protected final BiFunction<Tetromino,Rotation,Coordinates> coordinatesSupplier;
	private Tetromino piece;
	private BufferedImage pieceImage;
	private Rotation rotation = Rotation.ZERO;
	private boolean noDisplayOnStart = false;
	private boolean repaintOnUpdate = false;
	
	public PiecePanel(Component parent,BiFunction<Tetromino,Rotation,Coordinates> coordinatesSupplier) {
		this.parent = parent;
		this.coordinatesSupplier = coordinatesSupplier;
		setOpaque(false);
		setLayout(null);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (piece == null || (noDisplayOnStart && parent.game().state() == TetrisGame.State.START)) return;
		super.paintComponent(g);
		Coordinates coords = coordinatesSupplier.apply(piece,rotation);/*.add(1,1)*/
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
		if (!Objects.equals(this.piece,piece)) {
			this.piece = piece;
			if (repaintOnUpdate) redraw();
		}
		return this;
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
		if (!Objects.equals(this.rotation,rotation)) {
			this.rotation = rotation;
			if (repaintOnUpdate) redraw();
		}
		return this;
	}
	
	public boolean repaintOnUpdate() {
		return repaintOnUpdate;
	}
	
	public void repaintOnUpdate(boolean repaintOnUpdate) {
		this.repaintOnUpdate = repaintOnUpdate;
	}
	
	public void resize() {
		Coordinates start = parent.game().indexToPosition(new Coordinates(1,1));
		Coordinates add = parent.game().indexToPosition(new Coordinates(parent.rows(),parent.columns()));
		setBounds(start.column(),start.row(),add.column(),add.row());
		redraw();
	}
}