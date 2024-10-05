package me.DavidTs93.Tetris.Info;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ScoreRecords {
	private final int score;
	private final Set<String> names;
	
	public ScoreRecords(int score) {
		this.score = score;
		this.names = new HashSet<>();
	}
	
	public int score() {
		return score;
	}
	
	public Set<String> names() {
		return new HashSet<>(names);
	}
	
	public String namesString() {
		return names.stream().sorted().collect(Collectors.joining(", "));
	}
	
	public ScoreRecords add(String name) {
		names.add(name);
		return this;
	}
}