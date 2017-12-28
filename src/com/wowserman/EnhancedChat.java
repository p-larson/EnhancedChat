package com.wowserman;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.wowserman.listeners.ChatListener;
import com.wowserman.listeners.CommandListener;
import com.wowserman.listeners.KeywordListener;
import com.wowserman.settings.KeywordSetting;
import com.wowserman.settings.Settings;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * @author Wowserman
 *
 */
public class EnhancedChat extends JavaPlugin {

	public static boolean isColor(ChatColor color) {
		return !isFormat(color);
	}

	public static boolean isFormat(ChatColor color) {
		return color == ChatColor.BOLD || color == ChatColor.UNDERLINE || color == ChatColor.STRIKETHROUGH
				|| color == ChatColor.ITALIC || color == ChatColor.MAGIC;
	}

	private boolean placeholders;
	
	private KeywordSetting player, url;

	public boolean isPlaceholders() {
		return placeholders;
	}

	public KeywordSetting getPlayerSettings() {
		return player;
	}

	public KeywordSetting getUrlSettings() {
		return url;
	}

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
			this.getLogger().severe("PlaceholderAPI is missing! This Plugin cannot run without it!");
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
		
		Settings.load(this);
	}
	
	public static List<String> colorify(List<String> list) {
		List<String> l = new ArrayList<String>();
		for (String string:list)
			l.add(ChatColor.translateAlternateColorCodes("&".charAt(0), string));
		return l;
	}
}
