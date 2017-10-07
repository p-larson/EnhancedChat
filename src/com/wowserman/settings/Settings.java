package com.wowserman.settings;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.configuration.ConfigurationSection;

public class Settings {

	private static boolean caseSensitive = false, debug = false, ignoreNicknames = false, stats = false,
			autoUpdate = false;

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

	private static List<String> playerOnClick = new ArrayList<String>(), playerOnHover = new ArrayList<String>(),
			urlOnHover = new ArrayList<String>();

	public static List<String> getPlayerOnClick() {
		return playerOnClick;
	}

	public static List<String> getPlayerOnHover() {
		return playerOnHover;
	}

	public static List<String> getURLOnHover() {
		return urlOnHover;
	}

	/**
	 * @param section
	 *            The Section of a Configuration File that is being read.
	 * @return Triple of the Description, Commands, and URL.
	 */
	public static Triple<List<String>, List<String>, String> read(ConfigurationSection section) {

		List<String> description = new ArrayList<String>(), commands = new ArrayList<String>();
		String url = "";

		description = section.getStringList("description") != null ? section.getStringList("description") : description;
		commands = section.getStringList("commands") != null ? section.getStringList("commands") : commands;

		url = section.getString("url", url);

		return Triple.of(description, commands, url);
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
