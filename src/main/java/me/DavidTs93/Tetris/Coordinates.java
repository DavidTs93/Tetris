package me.DavidTs93.Tetris;

public record Coordinates(int row,int column) {
	public Coordinates add(int row,int column) {
		return new Coordinates(row() + row,column() + column);
	}
	
	public Coordinates subtract(int row,int column) {
		return new Coordinates(row() - row,column() - column);
	}
	
	public boolean equals(Object obj) {
		return (obj instanceof Coordinates coordinates) && coordinates.row() == row() && coordinates.column() == column();
	}
	
	public String toString() {
		return "Coordinates{column=" + column + ", row=" + row + "}";
	}
}