package me.czergames.joias.listeners;

import me.czergames.joias.Main;
import me.czergames.joias.commands.MainCmd;
import me.czergames.joias.database.DBMethods;
import me.czergames.joias.obj.Joia;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerEvents implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Thread t = new Thread(() -> {
			if(!DBMethods.containsGemPlayer(e.getPlayer())) {
				DBMethods.registerGemPlayer(e.getPlayer());
			}
			ArrayList<String> a = DBMethods.getActiveGems(e.getPlayer());
			if(a.isEmpty()) return;
			Main.PJOIAS.put(e.getPlayer(), a);
		});
		t.start();
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Main.PJOIAS.remove(e.getPlayer());
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if(e.getInventory().getName() != null && e.getInventory().getName().equals(Main.c.getString("inventario.nome").replaceAll("&", "§"))) {
				e.setCancelled(true);
				for(Joia j : Main.JOIAS.values()) {
					if(e.getSlot() == j.getSlot()) {
						int status = DBMethods.getGemCount(p, j.getId());
						if(status == 0) {
							p.sendMessage("§cVocê não tem este tipo de Joia!");
							p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1f, 1f);
							return;
						}
						else if(status == 1) {
							DBMethods.activeGem(p, j.getId());
							p.playSound(p.getLocation(), Sound.ANVIL_USE, 1f, 1f);
							p.sendMessage("§aVocê ativou §f" + j.getName() + " §acom sucesso!");
							p.closeInventory();
							MainCmd.openInventory(p);
						}
						else if(status == 2) {
							DBMethods.giveGem(p, j.getId());
							p.playSound(p.getLocation(), Sound.NOTE_SNARE_DRUM, 1f, 1f);
							p.sendMessage("§aVocê desativou §f" + j.getName() + " §acom sucesso!");
							p.closeInventory();
							MainCmd.openInventory(p);
						}
						
						while(Main.PJOIAS.containsKey(p)) {
							Main.PJOIAS.remove(p);
						}
						ArrayList<String> a = DBMethods.getActiveGems(p);
						if(a.isEmpty()) return;
						Main.PJOIAS.put(p, a);
					}
				}
			}
		}
	}
}
