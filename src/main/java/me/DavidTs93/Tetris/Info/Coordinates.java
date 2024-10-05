package me.DavidTs93.Tetris.Info;

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
	
	public Coordinates add(Coordinates coordinates) {
		return add(coordinates.row(),coordinates.column());
	}
	
	public Coordinates subtract(int row,int column) {
		return new Coordinates(row() - row,column() - column);
	}
	
	public Coordinates subtract(Coordinates coordinates) {
		return subtract(coordinates.row(),coordinates.column());
	}
	
	public Coordinates row(int row) {
		return new Coordinates(row,column);
	}
	
	public Coordinates column(int column) {
		return new Coordinates(row,column);
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Coordinates)) return false;
		Coordinates coordinates = (Coordinates) obj;
		return coordinates.row() == row() && coordinates.column() == column();
	}
	
	public String toString() {
		return "Coordinates{row=" + row + ", column=" + column + "}";
	}
}