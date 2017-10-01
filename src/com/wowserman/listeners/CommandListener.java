package com.wowserman.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.wowserman.EnhancedChat;
import com.wowserman.settings.Settings;

public class CommandListener implements Listener {

	private EnhancedChat plugin;
	
	public CommandListener(EnhancedChat instance) {
		Bukkit.getPluginManager().registerEvents(this, this.plugin = instance);
	}
	
	@EventHandler
	public void command(PlayerCommandPreprocessEvent event) {
		
		if (event.getMessage().contains("&&")==false)
			
			return;
		
		List<String> commands = Settings.parseCommands(event.getMessage());

		if (Settings.isDebug())
			plugin.getLogger().info(event.getPlayer().getName() + " forced to perform: " + commands.get(0));
						
		event.setMessage(commands.get(0));
		
		for (int i = 1; i < commands.size(); i++) {
			event.getPlayer().performCommand(commands.get(i));
		}
		
	}
}
