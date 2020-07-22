package me.czergames.joias.database;

import me.czergames.joias.Main;

import java.io.File;
import java.sql.*;

public class DBManager {
	
	public static Connection con;
	
	public static void open() {
		if (isOpen()) return;
		
		File f = new File(Main.getPlugin(Main.class).getDataFolder(), "database.db");
		try {
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:" + f;
			
			con = DriverManager.getConnection(url);
			createTable();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void close() {
		if (isClosed()) return;
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isOpen() {
		return con != null;
	}
	public static boolean isClosed() {
		return con == null;
	}
	
	private static void createTable() {
		PreparedStatement stm;
		try {
			stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS `joias`(`player` VARCHAR(20))");
			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
