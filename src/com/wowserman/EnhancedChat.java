package com.wowserman;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.wowserman.listeners.ChatListener;
import com.wowserman.listeners.CommandListener;
import com.wowserman.listeners.KeywordListener;
import com.wowserman.settings.Settings;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

public class EnhancedChat extends JavaPlugin {

	public static boolean isColor(ChatColor color) {
		return !isFormat(color);
	}

	public static boolean isFormat(ChatColor color) {
		return color == ChatColor.BOLD || color == ChatColor.UNDERLINE || color == ChatColor.STRIKETHROUGH
				|| color == ChatColor.ITALIC || color == ChatColor.MAGIC;
	}

	private boolean placeholders;

	public boolean isUsingPlaceholders() {
		return placeholders;
	}

	public static boolean shouldIgnore(BaseComponent component) {
						
		if (component.getClickEvent() != null || component.getHoverEvent() != null) {
			return true;
		}

		if (component.getExtra()==null) {
			return false;
		}
		
		for (BaseComponent sub:component.getExtra()) {
			if (EnhancedChat.shouldIgnore(sub))
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
		new CommandListener(this);

		// Configuration
		this.saveDefaultConfig();

		this.refreshData();
		
		this.getLogger().info("Loaded EnhancedChat successfully!");
	}
	
	public void refreshData() {
		
		this.reloadConfig();
		
		Settings.setAutoUpdate(this.getConfig().getBoolean("Plugin Settings.Plugin should Auto Update"));
		Settings.setCaseSensitive(this.getConfig().getBoolean("Plugin Settings.Keywords are Case Sensitive"));
		Settings.setDebug(this.getConfig().getBoolean("Plugin Settings.Plugin should Run in Debug Mode"));
		Settings.setIgnoreNicknames(this.getConfig().getBoolean("Plugin Settings.Players with Nicknames should be ignored"));
		Settings.setStats(this.getConfig().getBoolean("Plugin Settings.Plugin can take Annonomous Stats"));
		
		Triple<List<String>, List<String>, String> onlinePlayerKeywordSettings = Settings.read(this.getConfig().getConfigurationSection("Online Player Keyword"));
		
		Settings.getPlayerOnClick().clear();
		Settings.getPlayerOnHover().clear();
		
		Settings.getPlayerOnHover().addAll(onlinePlayerKeywordSettings.getLeft());
		Settings.getPlayerOnClick().addAll(onlinePlayerKeywordSettings.getMiddle());
		
		Triple<List<String>, List<String>, String> urlKeywordSettings = Settings.read(this.getConfig().getConfigurationSection("URL"));
		
		Settings.getURLOnHover().clear();
		
		Settings.getURLOnHover().addAll(urlKeywordSettings.getLeft());
	}
	
	public static List<String> colorify(List<String> list) {
		List<String> l = new ArrayList<String>();
		for (String string:list)
			l.add(ChatColor.translateAlternateColorCodes("&".charAt(0), string));
		return l;
	}
}
