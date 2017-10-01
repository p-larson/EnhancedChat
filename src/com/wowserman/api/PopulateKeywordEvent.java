package com.wowserman.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PopulateKeywordEvent extends Event {

	private final String context;
		
	private final String keyword;
	
	private boolean completed = false;
	
	private String url = "";

	private final List<String> onClick = new ArrayList<String>(), onHover = new ArrayList<String>();

	private long id = -1;

	public boolean equalsID(long otherID) {
		return id == otherID;
	}
	
	public long getID() {
		return id;
	}
	
	public void setURL(String url) {
		this.url = url;
	}
	
	public String getURL() {
		return url;
	}
	
	public boolean hasURL() {
		return url!=null && url.length()!=0;
	}

	public PopulateKeywordEvent(final String keyword, String context, long id) {
		this.keyword = keyword;
		this.context = context;
		this.id = id;
	}

	public List<String> getCommands() {
		return onClick;
	}

	public List<String> getDescription() {
		return onHover;
	}

	private static final HandlerList HANDLERS = new HandlerList();

	/**
	 * String that is in the Context.
	 * 
	 * @return
	 */
	public String getContext() {
		return context;
	}

	/**
	 * The Keyword in Context.
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

	public boolean isComplete() {
		return completed;
	}

	public void complete() {
		this.completed = true;
	}

}
