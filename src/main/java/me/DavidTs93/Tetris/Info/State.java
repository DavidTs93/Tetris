package me.DavidTs93.Tetris.Info;

public enum State {
	START(null,"Press SPACE to start"),
	PLAY(null,null),
	PAUSE("PAUSE",null),
	LOSE("GAME OVER","Press SPACE to restart"),
	WIN("YOU WIN!","Press SPACE to restart");
	
	private final String top;
	private final String bottom;
	
	State(String top,String bottom) {
		this.top = top == null ? "" : top;
		this.bottom = bottom == null ? "" : bottom;
	}
	
	public String top() {
		return top;
	}
	
	public String bottom() {
		return bottom;
	}
	
	public boolean isGameOver() {
		return this == START || this == LOSE || this == WIN;
	}
	
	public boolean isGameRunning() {
		return this == PLAY;
	}
}