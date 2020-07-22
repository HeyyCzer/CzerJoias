package me.czergames.joias;

import me.czergames.joias.obj.Joia;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class CheckerTask {
	
	public static void runTask() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(Main.c.getStringList("mundos_desativados").contains(p.getWorld().getName())) return;
				if(Main.PJOIAS.containsKey(p) && !Main.PJOIAS.get(p).isEmpty()) {
					for(String s : Main.PJOIAS.get(p)) {
						Joia j = Main.JOIAS.get(s);
						eff(p, j);
					}
				}
			}
		}, 0, 150);
	}
	
	private static void eff(Player p, Joia j) {
		double d = p.getHealth();
		p.removePotionEffect(j.getEffectType());
		p.addPotionEffect(new PotionEffect(j.getEffectType(), 200, j.getEffectLevel(), false, false));
		p.setHealth(d);
	}
}
