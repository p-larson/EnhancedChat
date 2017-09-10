package com.wowserman;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.wowserman.listeners.ChatListener;
import com.wowserman.listeners.KeywordListener;

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
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") == false) {
			this.getLogger().severe("PlaceholderAPI	 is missing! This Plugin cannot run without it!");
			this.setEnabled(false);
			return;
		}
		
		if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib") == false) {
			this.getLogger().severe("ProtocolLib is missing! This Plugin cannot run without it!");
			this.setEnabled(false);
			return;
		}
		
		new ChatListener(this);
		new KeywordListener(this);
	}
}
