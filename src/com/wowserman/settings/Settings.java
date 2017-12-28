package com.wowserman.settings;

import java.util.ArrayList;
import java.util.List;

import com.wowserman.EnhancedChat;

public class Settings {

	private static boolean caseSensitive = false, debug = false, ignoreNicknames = false, stats = false,
			autoUpdate = false;
	
	private static KeywordSetting player, url;

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		Settings.debug = debug;
	}

	public static boolean isIgnoreNicknames() {
		return ignoreNicknames;
	}

	public static void setIgnoreNicknames(boolean ignoreNicknames) {
		Settings.ignoreNicknames = ignoreNicknames;
	}

	public static boolean isStats() {
		return stats;
	}

	public static void setStats(boolean stats) {
		Settings.stats = stats;
	}

	public static boolean isAutoUpdate() {
		return autoUpdate;
	}

	public static void setAutoUpdate(boolean autoUpdate) {
		Settings.autoUpdate = autoUpdate;
	}

	public static void setCaseSensitive(boolean caseSensitive) {
		Settings.caseSensitive = caseSensitive;
	}

	public static boolean isCaseSensitive() {
		return caseSensitive;
	}
	
	public static KeywordSetting getPlayerSettings() {
		return player;
	}

	public static KeywordSetting getUrlSettings() {
		return url;
	}
	
	public static void load(EnhancedChat plugin) {
		Settings.setAutoUpdate(plugin.getConfig().getBoolean("Plugin Settings.Plugin should Auto Update"));
		Settings.setCaseSensitive(plugin.getConfig().getBoolean("Plugin Settings.Keywords are Case Sensitive"));
		Settings.setDebug(plugin.getConfig().getBoolean("Plugin Settings.Plugin should Run in Debug Mode"));
		Settings.setIgnoreNicknames(plugin.getConfig().getBoolean("Plugin Settings.Players with Nicknames should be ignored"));
		Settings.setStats(plugin.getConfig().getBoolean("Plugin Settings.Plugin can take Annonomous Stats"));

		player = KeywordSetting.read(plugin.getConfig().getConfigurationSection("Online Player Keyword"));
		url = KeywordSetting.read(plugin.getConfig().getConfigurationSection("URL Keyword"));
	}

	public static List<String> parseCommands(String commandString) {
		List<String> commands = new ArrayList<String>();

		for (String comman : commandString.split(" && ")) {
			String command = comman;

			command = command.startsWith("/") ? command.substring(1) : command;

			commands.add(command);
		}

		return commands;
	}
}
