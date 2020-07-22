package me.czergames.joias.obj;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Joia {
	
	private String id;
	
	private ItemStack item;
	private ItemStack itemActivated;
	private int slot;
	
	private String name;
	private List<String> lore;
	private PotionEffectType effectType;
	private int effectLevel;
	
	public Joia() {}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public ItemStack getItem() {
		return item;
	}
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public ItemStack getItemActivated() {
		return itemActivated;
	}
	public void setItemActivated(ItemStack itemActivated) {
		this.itemActivated = itemActivated;
	}
	
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getLore() {
		return lore;
	}
	public void setLore(List<String> lore) {
		this.lore = lore;
	}
	
	public PotionEffectType getEffectType() {
		return effectType;
	}
	public void setEffectType(PotionEffectType effectType) {
		this.effectType = effectType;
	}
	
	public int getEffectLevel() {
		return effectLevel;
	}
	public void setEffectLevel(int effectLevel) {
		this.effectLevel = effectLevel;
	}
}
