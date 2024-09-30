package me.DavidTs93.Tetris;

import java.awt.*;

public enum Colors {
	RED(new Color(255,0,0),
			new Color(255,127,127),
			new Color(191,0,0)),
	GREEN(new Color(0,255,0),
			new Color(127,255,127),
			new Color(0,191,0)),
	BLUE(new Color(0,0,255),
			new Color(127,127,255)
			,new Color(0,0,191)),
	YELLOW(new Color(255,255,0),
			new Color(255,255,127),
			new Color(191,191,0)),
	PINK(new Color(255,0,255),
			new Color(255,127,255),
			new Color(191,0,191)),
	AQUA(new Color(0,255,255),
			new Color(127,255,255),
			new Color(0,191,191)),
	ORANGE(new Color(255,127,0),
			new Color(255,191,127),
			new Color(191,127,0)),
	GRAY(new Color(127,127,127),
			new Color(191,191,191),
			new Color(63,63,63)),
	BLACK(new Color(0,0,0),new Color(0,0,0),new Color(0,0,0)),
	;
	
	private final Color body;
	private final Color top;
	private final Color bottom;
	
	Colors(Color body,Color top,Color bottom) {
		this.body = body;
		this.top = top;
		this.bottom = bottom;
	}
	
	public final Color body() {
		return body;
	}
	
	public final Color top() {
		return top;
	}
	
	public final Color bottom() {
		return bottom;
	}
}