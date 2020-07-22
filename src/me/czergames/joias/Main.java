package me.czergames.joias;

import me.czergames.joias.commands.MainCmd;
import me.czergames.joias.database.DBManager;
import me.czergames.joias.database.DBMethods;
import me.czergames.joias.listeners.PlayerEvents;
import me.czergames.joias.obj.Joia;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {
	
	public static HashMap<Player, ArrayList<String>> PJOIAS = new HashMap<>();
	public static HashMap<String, Joia> JOIAS = new HashMap<>();
	public static FileConfiguration c;
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);
		getCommand("joias").setExecutor(new MainCmd());
		
		saveDefaultConfig();
		
		c = getConfig();
		
		File db = new File(Main.getPlugin(Main.class).getDataFolder(), "database.db");
		if(!db.exists()) {
			try {
				db.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		DBManager.open();
		
		ArrayList<String> joias = new ArrayList<>();
		for(String s : c.getConfigurationSection("joias").getKeys(false)) {
			joias.add(s);
			Joia j = new Joia();
			try {
				String[] itemS = c.getString("joias." + s + ".item").split(":");
				ItemStack item = new ItemStack(Material.valueOf(itemS[0]));
				if(itemS.length == 2) {
					item.setDurability(Short.parseShort(itemS[1]));
				}
				String[] itemS2 = c.getString("joias." + s + ".item_ativado").split(":");
				ItemStack itemActivated = new ItemStack(Material.valueOf(itemS2[0]));
				if(itemS2.length == 2) {
					itemActivated.setDurability(Short.parseShort(itemS2[1]));
				}
				String name = c.getString("joias." + s + ".nome").replaceAll("&", "§");
				List<String> lore = new ArrayList<>();
				PotionEffectType effect = PotionEffectType.getByName(c.getString("joias." + s + ".efeito"));
				int effectLevel = c.getInt("joias." + s + ".nivel");
				int slot = c.getInt("joias." + s + ".slot");
				
				for(String a : c.getStringList("joias." + s + ".lore")) {
					lore.add(a.replaceAll("&", "§"));
				}
				
				j.setId(s);
				j.setItem(item);
				j.setItemActivated(itemActivated);
				j.setName(name.replaceAll("&", "§"));
				j.setLore(lore);
				j.setEffectType(effect);
				j.setEffectLevel(effectLevel);
				j.setSlot(slot);
				
				JOIAS.put(s, j);
			} catch(Exception e) {
				Bukkit.getConsoleSender().sendMessage("§cHouve algum erro na criação da Joia \"" + s + "\", verifique seus dados na config.yml!");
				e.printStackTrace();
			}
		}
		DBMethods.checkColumns(joias);
		CheckerTask.runTask();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!DBMethods.containsGemPlayer(p)) {
				DBMethods.registerGemPlayer(p);
			}
			if(!PJOIAS.containsKey(p)) {
				ArrayList<String> a = DBMethods.getActiveGems(p);
				if(a.isEmpty()) continue;
				PJOIAS.put(p, a);
			}
		}
	}
	
	@Override
	public void onDisable() {
		DBMethods.close();
	}
}
