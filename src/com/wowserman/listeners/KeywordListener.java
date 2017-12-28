package com.wowserman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.wowserman.EnhancedChat;
import com.wowserman.api.KeywordManager;
import com.wowserman.api.PopulateKeywordEvent;
import com.wowserman.api.SearchForKeywordEvent;
import com.wowserman.settings.Settings;

import me.clip.placeholderapi.PlaceholderAPI;

public class KeywordListener implements Listener {
	
	private static final long PLAYER_KEYWORD_ID = KeywordManager.createID("player-name");
	public static final long URL_ID = KeywordManager.createID("url");
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void search(SearchForKeywordEvent event) {
		
		for (String word:event.getWords()) {
			if (word.toLowerCase().startsWith("https://") || word.toLowerCase().startsWith("http://")) {
				
				event.setID(URL_ID);
				
				event.setKeyword(word);
				
				return;
			}
		}
		
		for (Player player:Bukkit.getOnlinePlayers()) {
			
			if (event.containsCustomKeyword(player.getName())) 
				
				event.setKeyword(player.getName());
			
			else if (event.containsCustomKeyword(player.getDisplayName()))
				
				event.setKeyword(player.getDisplayName());
			
			else continue;
			
			event.setID(PLAYER_KEYWORD_ID);
			
			event.setContext(player.getName());
			
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void populate(PopulateKeywordEvent event) {
					
		if (event.equalsID(URL_ID)) {
			
			event.setURL(event.getKeyword());
			
			event.setDescription(PlaceholderAPI.setPlaceholders(null, Settings.getUrlSettings().getDescription()));
			
			
		}
		
		if (event.equalsID(PLAYER_KEYWORD_ID) == false)
			return;
		
		Player context = Bukkit.getPlayer(event.getContext());
		
		event.setDescription(PlaceholderAPI.setPlaceholders(context, Settings.getPlayerSettings().getDescription()));
		event.setCommands(PlaceholderAPI.setPlaceholders(context, Settings.getPlayerSettings().getCommands()));
	}
	
	public KeywordListener(EnhancedChat instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
}
