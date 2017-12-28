package com.wowserman.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PopulateKeywordEvent extends Event {

	private final String context;

	private final String keyword;

	private String url = "", insertion = "";

	private final List<String> onClick = new ArrayList<String>(), onHover = new ArrayList<String>();

	private long id = -1;

	public boolean equalsID(long otherID) {
		return id == otherID;
	}

	public long getID() {
		return id;
	}

	public void setURL(String url) {
		this.url = fix(url);
	}

	public String getURL() {
		return url;
	}

	public boolean hasURL() {
		return url != null && url.length() != 0;
	}

	public void setInsertion(String insertion) {
		this.insertion = fix(insertion);
	}

	public String getInsertion() {
		return insertion;
	}

	public boolean hasInsertion() {
		return insertion != null && insertion.length() != 0;
	}

	public PopulateKeywordEvent(final String keyword, String context, long id) {
		this.keyword = keyword;
		this.context = context;
		this.id = id;
	}

	/**
	 * Deprecated because you need to know this returns a Clone of the Description.
	 * Do not just add to this List expecting it to work, it won't. 
	 * Use the supported methods:
	 * 
	 * @return Clone of the Description.
	 */
	@Deprecated
	public List<String> getCommands() {
		return onClick;
	}
	
	/**
	 * Deprecated because you need to know this returns a Clone of the Description.
	 * Do not just add to this List expecting it to work, it won't. 
	 * Use the supported methods:
	 * 
	 * @return Clone of the Description.
	 */
	@Deprecated
	public List<String> getDescription() {
		return new ArrayList<String>(onHover);
	}
	
	public void addDescription(String line) {
		this.onHover.add(fix(line));
	}
	
	public void setDescription(List<String> replacement) {
		this.onHover.clear();
		replacement.forEach(value -> this.onHover.add(fix(value)));
	}
	
	public void addCommand(String line) {
		this.onHover.add(fix(line));
	}
	
	public void setCommands(List<String> replacement) {
		this.onClick.clear();
		replacement.forEach(value -> this.onClick.add(fix(value)));
	}
	
	private final String fix(String line) {
		return line.replaceAll("%context%", context);
	}

	private static final HandlerList HANDLERS = new HandlerList();

	/**
	 * Context of the keyword, for example a player's name when talking about towny residents.
	 * 
	 * @return
	 */
	public String getContext() {
		return context;
	}

	/**
	 * The Keyword highlighted.
	 * 
	 * @return
	 */
	public String getKeyword() {
		return keyword;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
