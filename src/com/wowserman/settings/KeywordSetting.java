package com.wowserman.settings;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents Settings for a Keyword. Note: This is not a required concept you need to implement in your plugins.
 * 
 * @author Wowserman
 *
 */
public class KeywordSetting {
	
	/// This could be a static keyword like a server name, or null or "custom" if it is dependent on a case.
	private String keyword = null; 			
	
	private String url = null, insertion = null;
	
	private List<String> description = null, commands = null;
	
	public boolean isCustom() {
		return keyword == "custom" || keyword == null || keyword.isEmpty();
	}
	
	public boolean hasURL() {
		return url != null && !url.isEmpty() && !url.equals("%none%");
	}
	
	public boolean hasDescription() {
		return description != null;
	}
	
	public boolean hasCommands() {
		return commands != null;
	}
	
	public boolean hasInsertion() {
		return insertion != null;
	}
	
	public List<String> getDescription() {
		if (hasDescription()) // Just to not deal with NPE
			return description.stream().filter(s -> s.equals("%none%")).collect(Collectors.toList());
		else return description;
	}
	
	public List<String> getCommands() {
		if (hasCommands()) // Just to not deal with NPE
			return commands.stream().filter(s -> s.equals("%none%")).collect(Collectors.toList());
		else return commands;	
	}
	
	public String getURL() {
		return url;
	}
	
	public String getInsertion() {
		return insertion;
	}
	
	public static KeywordSetting read(ConfigurationSection section) {
		return new KeywordSetting(section);
	}
	
	public void write(ConfigurationSection section) {
		if (keyword != null) 
			section.set("keyword", keyword);
		section.set("url", url);
		section.set("description", description);
		section.set("commands", commands);
	}
	
	/**
	 * Example:
	 * 
	 * Our keyword is Resident Names.
	 * 		new Keyword(null, null, description, commands);
	 * 		= is Custom
	 * 		
	 * 
	 * @param keyword
	 * @param url
	 * @param description
	 * @param commands
	 */
	public KeywordSetting(String keyword, String url, List<String> description, List<String> commands) {
		this.keyword = keyword;
		this.url = url;
		this.description = description;
		this.commands = commands;
	}

	private KeywordSetting(ConfigurationSection section) {
		if (section.contains("keyword")) {
			this.keyword = section.getString("keyword");
		} 

		if (section.contains("url")) {
			this.url = section.getString("url").length() == 0 ? null:section.getString("url");
		}

		if (section.contains("description")) {
			this.description = section.getStringList("description");
		}

		if (section.contains("commands")) {
			this.commands = section.getStringList("commands");
		}
	}

}
