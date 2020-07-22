package me.czergames.joias.database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class DBMethods extends DBManager {
	
	public static ArrayList<String> joiaTypes = new ArrayList<>();
	
	public static void checkColumns(ArrayList<String> joias) {
		try {
			ArrayList<String> columns = new ArrayList<>();
			
			PreparedStatement stm;
			stm = con.prepareStatement("SELECT * FROM joias");
			ResultSet rs = stm.executeQuery();
			
			ResultSetMetaData md = rs.getMetaData();
			for (int i = 1; i <= md.getColumnCount(); i++) {
				String cname = md.getColumnName(i);
				if(joias.contains(cname)) {
					columns.add(cname);
					joiaTypes.add(cname);
				}
			}
			
			for(String cname : joias) {
				if(!columns.contains(cname)) {
					createColumn(cname);
					joiaTypes.add(cname);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void createColumn(String cname) {
		try {
			PreparedStatement stm;
			stm = con.prepareStatement("ALTER TABLE `joias` ADD " + cname + " INT DEFAULT 0");
			stm.executeUpdate();
			
			Bukkit.getConsoleSender().sendMessage("§a[Database] §fColuna §a\"" + cname + "\"§f criada com sucesso!");
		}catch(Exception e) {
			Bukkit.getConsoleSender().sendMessage("§c[Database] §fHouve um erro ao criar a coluna §c\"" + cname + "\"§f!");
			e.printStackTrace();
		}
	}
	
	public static boolean containsGemPlayer(Player p) {
		try {
			PreparedStatement stm;
			stm = con.prepareStatement("SELECT * FROM joias WHERE player = ?");
			stm.setString(1, p.getName().toLowerCase());
			
			ResultSet rs = stm.executeQuery();
			return rs.next();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static void registerGemPlayer(Player p) {
		try {
			PreparedStatement stm;
			stm = con.prepareStatement("INSERT INTO joias (player) VALUES (?)");
			stm.setString(1, p.getName().toLowerCase());
			stm.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static int getGemCount(Player p, String type) {
		try {
			PreparedStatement stm;
			stm = con.prepareStatement("SELECT * FROM joias WHERE player = ?");
			stm.setString(1, p.getName().toLowerCase());
			ResultSet rs = stm.executeQuery();
			if(rs.next()) {
				return rs.getInt(type);
			}
			return -1;
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	public static ArrayList<String> getActiveGems(Player p) {
		try {
			ArrayList<String> lista = new ArrayList<>();
			PreparedStatement stm;
			stm = con.prepareStatement("SELECT * FROM joias WHERE player = ?");
			stm.setString(1, p.getName().toLowerCase());
			ResultSet rs = stm.executeQuery();
			if(rs.next()) {
				for(String j : joiaTypes) {
					if(rs.getInt(j) == 2) {
						lista.add(j);
					}
				}
			}
			return lista;
		}catch(Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	public static void giveGem(Player p, String type) {
		try {
			PreparedStatement stm;
			stm = con.prepareStatement("UPDATE joias SET " + type + " = ? WHERE player = ?");
			stm.setInt(1, 1);
			stm.setString(2, p.getName().toLowerCase());
			stm.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void activeGem(Player p, String type) {
		try {
			PreparedStatement stm;
			stm = con.prepareStatement("UPDATE joias SET " + type + " = ? WHERE player = ?");
			stm.setInt(1, 2);
			stm.setString(2, p.getName().toLowerCase());
			stm.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void removeGem(Player p, String type) {
		try {
			PreparedStatement stm;
			stm = con.prepareStatement("UPDATE joias SET " + type + " = ? WHERE player = ?");
			stm.setInt(1, 0);
			stm.setString(2, p.getName().toLowerCase());
			stm.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
