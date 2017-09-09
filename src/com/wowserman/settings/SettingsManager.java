package com.wowserman.settings;

import org.bukkit.configuration.file.FileConfiguration;

import com.wowserman.EnhancedChat;

public class SettingsManager {

	private static FileConfiguration config;
	
	public static boolean CASE_SENSITIVE = false;
	
	public void write(Keyword keyword) {
		
	}
	
	private static SettingsManager instance;
	
	public static SettingsManager getInstance() {
		return instance;
	}
	
	public static boolean isInitialized() {
		return instance == null;
	}
	
	public static void load(EnhancedChat plugin) {
		config = plugin.getConfig();
	}
	
	private SettingsManager() {
		
	}
}
