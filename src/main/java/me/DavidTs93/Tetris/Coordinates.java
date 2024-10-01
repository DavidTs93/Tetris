package me.DavidTs93.Tetris;

public class Coordinates {
	private final int row;
	private final int column;
	
	public Coordinates(int row,int column) {
		this.row = row;
		this.column = column;
	}
	
	public int row() {
		return row;
	}
	
	public int column() {
		return column;
	}
	
	public Coordinates add(int row,int column) {
		return new Coordinates(row() + row,column() + column);
	}
	
	public Coordinates subtract(int row,int column) {
		return new Coordinates(row() - row,column() - column);
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Coordinates)) return false;
		Coordinates coordinates = (Coordinates) obj;
		return coordinates.row() == row() && coordinates.column() == column();
	}
	
	public String toString() {
		return "Coordinates{column=" + column + ", row=" + row + "}";
	}
}