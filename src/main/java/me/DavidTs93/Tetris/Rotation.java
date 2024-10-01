package me.DavidTs93.Tetris;

public enum Rotation {
	ZERO,
	ONE,
	TWO,
	THREE;
	
	public Rotation next() {
		switch (this) {
			case ZERO: return ONE;
			case ONE: return TWO;
			case TWO: return THREE;
			case THREE: return ZERO;
			default: throw new IllegalArgumentException();
		}
	}
	
	public Rotation previous() {
		switch (this) {
			case ZERO: return THREE;
			case ONE: return ZERO;
			case TWO: return ONE;
			case THREE: return TWO;
			default: throw new IllegalArgumentException();
		}
	}
}