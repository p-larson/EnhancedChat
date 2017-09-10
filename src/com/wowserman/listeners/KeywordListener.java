package com.wowserman.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.wowserman.EnhancedChat;
import com.wowserman.api.KeywordManager;
import com.wowserman.api.PopulateKeywordEvent;
import com.wowserman.api.SearchForKeywordEvent;

import me.clip.placeholderapi.PlaceholderAPI;

public class KeywordListener implements Listener {

	private static final long PLAYER_KEYWORD_ID = KeywordManager.createID("player-name");
	
	@EventHandler
	public void search(SearchForKeywordEvent event) {
		
		for (Player player:Bukkit.getOnlinePlayers()) {
			
			if (event.containsCustomKeyword(player.getName())) 
				
				event.setKeyword(player.getName());
			
			else if (event.containsCustomKeyword(player.getDisplayName()))
				
				event.setKeyword(player.getDisplayName());
			
			else return;
			
			event.setID(PLAYER_KEYWORD_ID);
			
			event.setContext(player);
			
			return;
		}
	}
	
	@EventHandler
	public void populate(PopulateKeywordEvent event) {
				
		if (event.equalsID(PLAYER_KEYWORD_ID) == false)
			return;
		
		final String[] description = new String[] {
				event.getContext().getName(),
				ChatColor.DARK_GREEN + "Bank: " + ChatColor.GREEN + PlaceholderAPI.setPlaceholders(event.getContext(), "%vault_eco_balance_formatted%")
		};
		
		event.getDescription().addAll(Arrays.asList(description));
	}
	
	public KeywordListener(EnhancedChat instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
}
