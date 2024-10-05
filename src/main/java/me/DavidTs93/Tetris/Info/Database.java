package me.DavidTs93.Tetris.Info;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Database {
	private static final String TABLE = "high_scores";
	
	private final String poolName;
	private final File file;
	private HikariDataSource hikari;
	
	public Database() throws ClassNotFoundException,IOException,SQLException {
		Class.forName("org.sqlite.JDBC");
		this.poolName = "Tetris";
		this.file = new File(FileSystemView.getFileSystemView().getDefaultDirectory(),"My Games/Tetris/high_scores.db");
		start();
	}
	
	private void start() throws IOException,SQLException {
		prepareDatabase();
		connect();
		createTable();
	}
	
	private void prepareDatabase() throws IOException {
		if (file.exists()) return;
		file.getParentFile().mkdirs();
		if (!file.createNewFile()) throw new IOException("Couldn't create database file!");
	}
	
	private HikariDataSource createHikari() {
		String url = "jdbc:sqlite:" + file;
		HikariConfig hikariConfig = new HikariConfig();
		if (poolName != null) hikariConfig.setPoolName(poolName);
		hikariConfig.setJdbcUrl(url);
		hikariConfig.addDataSourceProperty("cachePrepStmts","true");
		hikariConfig.addDataSourceProperty("prepStmtCacheSize","250");
		hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit","2048");
		hikariConfig.addDataSourceProperty("useServerPrepStmts","true");
		hikariConfig.addDataSourceProperty("useLocalSessionState","true");
		hikariConfig.addDataSourceProperty("rewriteBatchedStatements","true");
		hikariConfig.addDataSourceProperty("cacheResultSetMetadata","true");
		hikariConfig.addDataSourceProperty("cacheServerConfiguration","true");
		hikariConfig.addDataSourceProperty("elideSetAutoCommits","true");
		hikariConfig.addDataSourceProperty("maintainTimeStats","false");
		hikariConfig.setMaximumPoolSize(4);
		return new HikariDataSource(hikariConfig);
	}
	
	public List<ScoreRecords> highScores(int n) throws SQLException {
		try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE + " WHERE Score IN (SELECT DISTINCT Score FROM " + TABLE + " ORDER BY Score DESC LIMIT ?) ORDER BY Score DESC;")) {
			statement.setInt(1,n);
			try (ResultSet results = statement.executeQuery()) {
				List<ScoreRecords> records = new ArrayList<>();
				ScoreRecords scoreRecords = null;
				while (results.next()) {
					int score = results.getInt(2);
					if (score <= 0) break;
					if (scoreRecords == null || scoreRecords.score() != score) {
						scoreRecords = new ScoreRecords(score);
						records.add(scoreRecords);
					}
					scoreRecords.add(results.getString(1));
				}
				return records;
			}
		}
	}
	
	public void addScore(String name,int score) throws SQLException {
		if (score > 0) try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLE + " (Name,Score) VALUES(?,?);")) {
			statement.setString(1,name);
			statement.setInt(2,score);
			if (statement.executeUpdate() <= 0) throw new SQLException("Couldn't add new record");
		}
	}
	
	private void connect() {
		this.hikari = createHikari();
	}
	
	private Connection getConnection() throws SQLException {
		if (hikari == null || hikari.isClosed()) connect();
		return hikari.getConnection();
	}
	
	private void createTable() throws SQLException {
		try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
			DatabaseMetaData data = connection.getMetaData();
			LinkedHashMap<String,String> columns = new LinkedHashMap<>();
			columns.put("Name","VARCHAR(64) NOT NULL");
			columns.put("Score","INT NOT NULL");
			statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE + " (" + columns.entrySet().stream().map(entry -> entry.getKey() + " " + entry.getValue()).collect(Collectors.joining(",")) + ");");
			for (Map.Entry<String,String> entry : columns.entrySet()) if (!data.getColumns(null,null,TABLE,entry.getKey()).next()) statement.execute("ALTER TABLE " + TABLE + " ADD " + entry.getKey() + " " + entry.getValue() + ";");
		}
	}
}