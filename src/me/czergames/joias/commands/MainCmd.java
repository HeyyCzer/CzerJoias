package me.czergames.joias.commands;

import me.czergames.joias.Main;
import me.czergames.joias.database.DBMethods;
import me.czergames.joias.obj.Joia;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MainCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length != 3) { // joias dar <player> <tipo>
				if(args.length == 0) {
					openInventory(p);
					return true;
				}
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("ajuda") || args[0].equalsIgnoreCase("help")) {
						p.sendMessage("§f");
						p.sendMessage("§a/joias §7- Abre menu para ativação das Joias");
						p.sendMessage("§a/joias ajuda §7- Mostra esta mensagem");
						if (p.hasPermission(Main.c.getString("permissao_admin"))) {
							p.sendMessage("§a/joias dar <jogador> <tipo> §7- Adiciona determinado tipo de joia ao jogador.");
							p.sendMessage("§a/joias remover <jogador> <tipo> §7- Retira determinado tipo de joia do jogador.");
						}
						p.sendMessage("§f");
						return true;
					}
				}
				p.sendMessage("§cUso incorreto do comando! Utilize §f/joias ajuda §cpara mais informações.");
			}else {
				if(!p.hasPermission(Main.c.getString("permissao_admin"))) {
					p.sendMessage("§cVocê não tem permissão para utilizar este comando!");
					return true;
				}
				if(args[0].equalsIgnoreCase("dar") || args[0].equalsIgnoreCase("give")) {
					Player alvo;
					try {
						alvo = Bukkit.getPlayer(args[1]);
					}catch(Exception e) {
						p.sendMessage("§cO jogador alvo não existe ou não foi encontrado!");
						return true;
					}
					if(!Main.JOIAS.containsKey(args[2])) {
						p.sendMessage("§cO tipo de Joia não foi encontrado! Escreva de forma idêntica as joias da configuração.");
						return true;
					}
					if(DBMethods.getGemCount(alvo, args[2]) == 1) {
						p.sendMessage("§cO jogador §f" + alvo.getName() + " §cjá possui este tipo de Joia!");
						return true;
					}
					DBMethods.giveGem(alvo, args[2]);
					p.sendMessage("§aO jogador " + alvo.getName() + " agora possui §f" + Main.JOIAS.get(args[2]).getName());
				}
				else if(args[0].equalsIgnoreCase("remover") || args[0].equalsIgnoreCase("rem") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("retirar")) {
					Player alvo;
					try {
						alvo = Bukkit.getPlayer(args[1]);
					}catch(Exception e) {
						p.sendMessage("§cO jogador alvo não existe ou não foi encontrado!");
						return true;
					}
					if(!Main.JOIAS.containsKey(args[2])) {
						p.sendMessage("§cO tipo de Joia não foi encontrado! Escreva de forma idêntica as joias da configuração.");
						return true;
					}
					if(DBMethods.getGemCount(alvo, args[2]) == 0) {
						p.sendMessage("§cO jogador §f" + alvo.getName() + " §cnão possui este tipo de Joia!");
						return true;
					}
					DBMethods.removeGem(alvo, args[2]);
					p.sendMessage("§aVocê retirou §f" + Main.JOIAS.get(args[2]).getName() + " §ado jogador §f" + alvo.getName() + " §acom sucesso!");
				}
				else {
					p.sendMessage("§cUso incorreto do comando! Utilize §f/joias ajuda §cpara mais informações.");
				}
			}
		}
		else {
			if(args[0].equalsIgnoreCase("dar") || args[0].equalsIgnoreCase("give")) {
				Player alvo;
				try {
					alvo = Bukkit.getPlayer(args[1]);
				}catch(Exception e) {
					sender.sendMessage("§cO jogador alvo não existe ou não foi encontrado!");
					return true;
				}
				if(!Main.JOIAS.containsKey(args[2])) {
					sender.sendMessage("§cO tipo de Joia não foi encontrado! Escreva de forma idêntica as joias da configuração.");
					return true;
				}
				if(DBMethods.getGemCount(alvo, args[2]) == 1) {
					sender.sendMessage("§cO jogador §f" + alvo.getName() + " §cjá possui este tipo de Joia!");
					return true;
				}
				DBMethods.giveGem(alvo, args[2]);
				sender.sendMessage("§aO jogador " + alvo.getName() + " agora possui §f" + Main.JOIAS.get(args[2]).getName());
			}
			else if(args[0].equalsIgnoreCase("remover") || args[0].equalsIgnoreCase("rem") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("retirar")) {
				Player alvo;
				try {
					alvo = Bukkit.getPlayer(args[1]);
				}catch(Exception e) {
					sender.sendMessage("§cO jogador alvo não existe ou não foi encontrado!");
					return true;
				}
				if(!Main.JOIAS.containsKey(args[2])) {
					sender.sendMessage("§cO tipo de Joia não foi encontrado! Escreva de forma idêntica as joias da configuração.");
					return true;
				}
				if(DBMethods.getGemCount(alvo, args[2]) == 0) {
					sender.sendMessage("§cO jogador §f" + alvo.getName() + " §cnão possui este tipo de Joia!");
					return true;
				}
				DBMethods.removeGem(alvo, args[2]);
				sender.sendMessage("§aVocê retirou §f" + Main.JOIAS.get(args[2]).getName() + " §ado jogador §f" + alvo.getName() + " §acom sucesso!");
			}
			else {
				sender.sendMessage("§cUso incorreto do comando! Utilize §f/joias ajuda §cpara mais informações.");
			}
		}
		return false;
	}
	
	public static void openInventory(Player p) {
		Inventory inv = Bukkit.createInventory(null, Main.c.getInt("inventario.linhas")*9, Main.c.getString("inventario.nome").replaceAll("&", "§"));
		
		for(Joia j : Main.JOIAS.values()) {
			ItemStack item;
			int gem = DBMethods.getGemCount(p, j.getId());
			if(gem == 2) {
				item = j.getItemActivated();
			}else {
				item = j.getItem();
			}
			
			ItemMeta itemM = item.getItemMeta();
			itemM.setDisplayName(j.getName());
			
			List<String> lore = new ArrayList<>(j.getLore());
			lore.add("§f");
			if(gem == 0) {
				lore.add("§cVocê não tem este tipo de Joia!");
			}
			else if(gem == 1) {
				lore.add("§eClique para ativar!");
			}
			else if(gem == 2) {
				lore.add("§eClique para desativar!");
			}
			
			itemM.setLore(lore);
			item.setItemMeta(itemM);
			
			inv.setItem(j.getSlot(), item);
		}
		
		p.openInventory(inv);
	}
}
