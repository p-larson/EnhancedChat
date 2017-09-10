package com.wowserman;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class EnhancedChat extends JavaPlugin {

	public static boolean isColor(ChatColor color) {
		return !isFormat(color);
	}
	
	public static boolean isFormat(ChatColor color) {
		return color==ChatColor.BOLD || color==ChatColor.UNDERLINE || color==ChatColor.STRIKETHROUGH || color==ChatColor.ITALIC || color==ChatColor.MAGIC;
	}
	
	private boolean placeholders;
	
	public boolean isUsingPlaceholders() {
		return placeholders;
	}
	
	public static boolean shouldIgnore(TextComponent component) {
		
		if (component.getClickEvent() != null || component.getHoverEvent() != null)
			return true;
		
		List<TextComponent> components = new ArrayList<TextComponent>();
		
		for (BaseComponent base:component.getExtra())
			components.add(new TextComponent(base));
		
		for (TextComponent subComponent:components) {
			if (subComponent.getClickEvent() != null || subComponent.getHoverEvent() != null)
				return true;
		}
		
		return false;
	}	
	
	@Override
	public void onEnable() {
		placeholders = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
	}
}
