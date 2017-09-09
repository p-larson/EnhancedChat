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
	
	public static boolean hasVariable(Player player, String string) {
		return PlaceholderAPI.setPlaceholders(player, string);
	}
	
	public static String getVariableNahme(Player player, String text) {
		for (String placeholder:PlaceHolderAPI.)
	}
	
	@SuppressWarnings("deprecation")
	public static String getVariableName(String string) {

		final String copy = string.startsWith(String.valueOf(ChatColor.COLOR_CHAR)) ? string.substring(2) : string;

		if (copy.length() != "SERVER".length() && copy.toLowerCase().contains("SERVER".toLowerCase()))
			return "SERVER";

		for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
			for (String author : plugin.getDescription().getAuthors())
				if (copy.length() != author.length() && copy.toLowerCase().contains(author.toLowerCase()))
					return author;

		for (Resident resident : towny.getTownyUniverse().getResidents())
			if (copy.length() != resident.getName().length()
					&& copy.toLowerCase().contains(resident.getName().toLowerCase()))
				return resident.getName();

		for (Town town : towny.getTownyUniverse().getTowns())
			if (copy.length() != town.getName().length() && copy.toLowerCase().contains(town.getName().toLowerCase()))
				return town.getName();

		for (Nation nation : towny.getTownyUniverse().getNations())
			if (copy.length() != nation.getName().length()
					&& copy.toLowerCase().contains(nation.getName().toLowerCase()))
				return nation.getName();

		return null;
	}

	public static List<String> getValueOfVariable(String variable) {

		if (variable == null)
			return null;

		List<String> value = new ArrayList<String>();

		if (variable.toLowerCase().contains("SERVER".toLowerCase())) {

			value.add("Server");
			value.add(Bukkit.getBukkitVersion());
			value.add(Bukkit.getIp());
			value.add(Bukkit.getOnlinePlayers().size() + " Online Players");
			value.add(Bukkit.getWorlds().size() + " Loaded Worlds");

			return value;
		}

		for (Town town : TownyUniverse.getDataSource().getTowns()) {

			if (variable.toLowerCase().contains(town.getName().toLowerCase())) {

				try {
					value.add(ChatColor.AQUA + town.getName() + (town.hasNation()
							? ChatColor.WHITE + ", " + ChatColor.GOLD + town.getNation().getName() + ChatColor.WHITE
							: ChatColor.WHITE));
					value.add(town.getResidents().size() + " Residents.");
					value.add("HomeBlock: " + town.getHomeBlock().getX() + ", " + town.getHomeBlock().getZ());
				} catch (Exception e) {
					value.add("Could not Load");
					value.add("Information to Town.");
				}

				return value;
			}
		}

		for (Nation nation : TownyUniverse.getDataSource().getNations()) {

			if (variable.toLowerCase().contains(nation.getName().toLowerCase())) {

				try {
					value.add(ChatColor.GOLD + nation.getName() + ChatColor.WHITE);
					value.add(nation.getResidents().size() + " Residents.");
					value.add("Capital: " + nation.getCapital().getName());
				} catch (Exception e) {
					value.add("Could not Load");
					value.add("Information to Town.");
				}

				return value;
			}
		}

		for (Resident resident : TownyUniverse.getDataSource().getResidents())
			if (variable.toLowerCase().contains(resident.getName().toLowerCase())) {

				if (value.size() == 0) {
					value.add(resident.getName());
				}

				try {

					if (resident.hasTown())
						value.add(ChatColor.AQUA + resident.getTown().getName()
								+ (resident.getTown().hasNation()
										? ChatColor.WHITE + ", " + ChatColor.GOLD
												+ resident.getTown().getNation().getName() + ChatColor.WHITE
										: ChatColor.WHITE));
					value.add(ChatColor.GREEN + "$" + resident.getHoldingBalance() + ChatColor.WHITE);
				} catch (Exception e) {
					
				}
				
				value.add("");
			}
		
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			for (String author : plugin.getDescription().getAuthors()) {
				if (variable.toLowerCase().contains(author.toLowerCase())) {

					if (value.size() == 0) {
						value.add(author);
						value.add("");
					}
					value.add("Developer of " + plugin.getName());
				}
			}
		}

		if (value.size() != 0)
			return value;

		return null;
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
