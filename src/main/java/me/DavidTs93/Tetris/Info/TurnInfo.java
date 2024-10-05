package me.DavidTs93.Tetris.Info;

import me.DavidTs93.Tetris.Parts.Tetromino;

public final class TurnInfo {
	private Tetromino piece;
	private Boolean lose;
	private Boolean turnOver;
	private Boolean win;
	private Integer levelOld;
	private Integer levelNew;
	private Integer linesOld;
	private Integer linesRemoved;
	private Integer linesNew;
	private Integer statistics;
	private Integer scoreOld;
	private Integer scoreAdd;
	private Integer scoreNew;
	
	public TurnInfo(Tetromino piece) {
		this.piece = piece;
	}
	
	public Tetromino piece() {
		return piece;
	}
	
	public TurnInfo piece(Tetromino piece) {
		this.piece = piece;
		return this;
	}
	
	public Boolean lose() {
		return lose;
	}
	
	public TurnInfo lose(Boolean lose) {
		this.lose = lose;
		return this;
	}
	
	public Boolean win() {
		return win;
	}
	
	public TurnInfo win(Boolean win) {
		this.win = win;
		return this;
	}
	
	public Boolean turnOver() {
		return turnOver;
	}
	
	public TurnInfo turnOver(Boolean turnOver) {
		this.turnOver = turnOver;
		return this;
	}
	
	public Integer levelOld() {
		return levelOld;
	}
	
	public TurnInfo levelOld(Integer levelOld) {
		this.levelOld = levelOld;
		return this;
	}
	
	public Boolean levelAdd() {
		return levelOld() == null || levelNew() == null ? null : levelNew() > levelOld();
	}
	
	public Integer levelNew() {
		return levelNew;
	}
	
	public TurnInfo levelNew(Integer levelNew) {
		this.levelNew = levelNew;
		return this;
	}
	
	public Integer linesRemoved() {
		return linesRemoved;
	}
	
	public TurnInfo linesRemoved(Integer linesRemoved) {
		this.linesRemoved = linesRemoved;
		return this;
	}
	
	public Integer linesNew() {
		return linesNew;
	}
	
	public TurnInfo linesNew(Integer linesNew) {
		this.linesNew = linesNew;
		return this;
	}
	
	public Integer linesOld() {
		return linesOld;
	}
	
	public TurnInfo linesOld(Integer linesOld) {
		this.linesOld = linesOld;
		return this;
	}
	
	public Integer statistics() {
		return statistics;
	}
	
	public TurnInfo statistics(Integer statistics) {
		this.statistics = statistics;
		return this;
	}
	
	public Integer scoreOld() {
		return scoreOld;
	}
	
	public TurnInfo scoreOld(Integer score) {
		this.scoreOld = score;
		return this;
	}
	
	public Integer scoreAdd() {
		if (scoreOld() != null && scoreNew() != null) return scoreNew() - scoreOld();
		return scoreAdd;
	}
	
	public TurnInfo scoreAdd(Integer scoreAdd) {
		this.scoreAdd = scoreAdd;
		return this;
	}
	
	public Integer scoreNew() {
		return scoreNew;
	}
	
	public TurnInfo scoreNew(Integer scoreNew) {
		this.scoreNew = scoreNew;
		return this;
	}
}