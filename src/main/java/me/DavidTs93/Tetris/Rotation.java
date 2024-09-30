package me.DavidTs93.Tetris;

public enum Rotation {
	ZERO,
	ONE,
	TWO,
	THREE;
	
	public Rotation next() {
		return switch (this) {
			case ZERO -> ONE;
			case ONE -> TWO;
			case TWO -> THREE;
			case THREE -> ZERO;
		};
	}
	
	public Rotation previous() {
		return switch (this) {
			case ZERO -> THREE;
			case ONE -> ZERO;
			case TWO -> ONE;
			case THREE -> TWO;
		};
	}
}