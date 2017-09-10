package com.wowserman.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PopulateKeywordEvent extends Event {

	private final Player context;

	private final String keyword;

	private final List<String> onClick = new ArrayList<String>(), onHover = new ArrayList<String>();

	private long id = -1;

	public boolean equalsID(long otherID) {
		return id == otherID;
	}
	
	public long getID() {
		return id;
	}

	public PopulateKeywordEvent(final String keyword, Player context, long id) {
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
	 * Player that is in the Context.
	 * 
	 * @return
	 */
	public Player getContext() {
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

}
