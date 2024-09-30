package me.DavidTs93.Tetris;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public enum Tetromino {
	I(Colors.AQUA,new Shape(new boolean[][] {
			{false,false,false,false},
			{true,true,true,true},
			{false,false,false,false},
			{false,false,false,false}
	})),
	J(Colors.BLUE,new Shape(new boolean[][] {
			{true,false,false},
			{true,true,true},
			{false,false,false}
	})),
	L(Colors.ORANGE,new Shape(new boolean[][]{
			{false,false,true},
			{true,true,true},
			{false,false,false}
	})),
	O(Colors.YELLOW,new Shape(new boolean[][] {
			{false,false,false,false},
			{false,true,true,false},
			{false,true,true,false},
			{false,false,false,false}
	})),
	S(Colors.GREEN,new Shape(new boolean[][] {
			{false,true,true},
			{true,true,false},
			{false,false,false}
	})),
	T(Colors.PINK,new Shape(new boolean[][] {
			{false,true,false},
			{true,true,true},
			{false,false,false}
	})),
	Z(Colors.RED,new Shape(new boolean[][] {
			{true,true,false},
			{false,true,true},
			{false,false,false}
	}));
	
	private final Colors colors;
	private final Shape shape;
	
	Tetromino(Colors colors,Shape shape) {
		this.colors = colors;
		this.shape = shape;
	}
	
	public int width(Rotation rotation) {
		return shape.get(rotation)[0].length;
	}
	
	public int height(Rotation rotation) {
		return shape.get(rotation).length;
	}
	
	public Colors colors() {
		return colors;
	}
	
	public BufferedImage draw(TetrisGame game,Rotation rotation) {
		int width = width(rotation),height = height(rotation);
		BufferedImage image = new BufferedImage(game.squareSize() * width,game.squareSize() * height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		boolean[][] matrix = shape.get(rotation);
		for (int i = 0; i < height; i++) for (int j = 0; j < width; j++) if (matrix[i][j]) game.drawSquare(g2d,new Coordinates(i,j),colors);
		g2d.dispose();
		return image;
	}
	
	private boolean testPlacing(boolean[][] matrix,Coordinates coordinates,Object[][] board) {
		if (!testPlacingColumns(matrix,coordinates,board)) return false;
		if (!testPlacingRows(matrix,coordinates,board)) return false;
		if (!testPlacingBoard(matrix,coordinates,board)) return false;
		return true;
	}
	
	private boolean testPlacingColumns(boolean[][] matrix,Coordinates coordinates,Object[][] board) {
		int n = matrix.length;
		int column = coordinates.column();
		if (column < 0) {
			int diff = -column;
			if (diff >= n) return false;
			while (diff > 0) {
				for (boolean[] line : matrix) if (line[diff - 1]) return false;
				diff--;
			}
		}
		if (board == null) return true;
		int width = board[0].length;
		if (column + n > width) {
			int diff = column + n - width;
			if (diff >= n) return false;
			while (diff > 0) {
				for (boolean[] line : matrix) if (line[n - diff]) return false;
				diff--;
			}
		}
		return true;
	}
	
	private boolean testPlacingRows(boolean[][] matrix,Coordinates coordinates,Object[][] board) {
		int diff = coordinates.row() - board.length;
		if (diff < 0) return true;
		int n = matrix.length;
		if (diff >= n) return false;
		for (boolean b : matrix[n - diff - 1]) if (b) return false;
//		for (boolean b : matrix[matrix.length - 2]) if (b) return false;
		return true;
//		return false;
	}
	
	private boolean testPlacingBoard(boolean[][] matrix,Coordinates coordinates,Object[][] board) {
		int row = coordinates.row() - matrix.length + 1;
		for (int i = 0; i < matrix.length; i++) {
			boolean[] line = matrix[i];
			int r = row + i;
			if (r < 0 || r >= board.length) continue;
			for (int j = 0; j < line.length; j++) {
				if (matrix[i][j] && board[r][coordinates.column() + j] != null) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean testPlacing(Rotation rotation,Coordinates coordinates,Object[][] board) {
		return testPlacing(shape.get(rotation),coordinates,board);
	}
	
	public List<Coordinates> fill(Rotation rotation,Coordinates coordinates,Object[][] board) {
		List<Coordinates> fill = new ArrayList<>();
		boolean[][] matrix = shape.get(rotation);
		int n = matrix.length;
		for (int i = 0; i < n; i++) {
			boolean[] line = matrix[i];
			int r = coordinates.row() - n + i + 1;
			if (r < 0 || r >= board.length) continue;
			for (int j = 0; j < line.length; j++) if (line[j]) fill.add(new Coordinates(r,coordinates.column() + j));
		}
		return fill;
	}
	
	public static Tetromino random() {
		Tetromino[] tetrominos = values();
		return tetrominos[ThreadLocalRandom.current().nextInt(tetrominos.length)];
	}
	
	private static class Shape {
		private final Map<Rotation,boolean[][]> matrices;
		
		private Shape(boolean[][] original) {
			int size = original.length;
			if (original[0].length != size) throw new IllegalArgumentException();
			for (boolean b : original[size - 1]) if (b) throw new IllegalArgumentException();
			if (size % 2 == 0) for (boolean b : original[0]) if (b) throw new IllegalArgumentException();
			Map<Rotation,boolean[][]> matrices = new HashMap<>();
			boolean[][] matrix = original;
			for (Rotation rotation : Rotation.values()) {
				if (!matrices.isEmpty()) matrix = rotate(matrix);
				matrices.put(rotation,matrix);
			}
			this.matrices = Collections.unmodifiableMap(matrices);
		}
		
		private boolean[][] get(Rotation rotation) {
			return matrices.get(rotation);
		}
		
		private static boolean[][] rotate(boolean[][] matrix) {
			int n = matrix.length;
			boolean[][] rotated = new boolean[n][n];
			for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) rotated[j][n - 1 - i] = matrix[i][j];
			return rotated;
		}
	}
}