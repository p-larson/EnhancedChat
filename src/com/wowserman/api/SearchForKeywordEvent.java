package com.wowserman.api;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.wowserman.settings.Settings;

public class SearchForKeywordEvent extends Event implements Cancellable {

	private String context;

	private final String message;

	private String keyword = null;

	private long id = -1;

	public SearchForKeywordEvent(final String message, String context) {
		this.message = message;
		this.context = context;
	}

	public boolean containsCustomKeyword(String keyword) {

		if (Settings.isCaseSensitive())
			return message.contains(keyword);
		else
			return message.toLowerCase().contains(keyword.toLowerCase());
	}

	public boolean isOnlyKeyword() {
		return this.hasFoundKeyword() && keyword.length() == message.length();
	}

	private static final HandlerList HANDLERS = new HandlerList();

	/**
	 * 
	 * 
	 * @return String that is in the Context.
	 */
	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * Can be null, see {@link #hasFoundKeyword()}.
	 * 
	 * @return the Keyword
	 */
	public String getFoundKeyword() {
		return keyword;
	}

	/**
	 * @return true if Keyword is not null.
	 */
	public boolean hasFoundKeyword() {
		return keyword != null;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
		this.setCancelled(true);
	}

	public String[] getWords() {
		return message.split(" ");
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	private boolean cancelled = false;

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}

	public void setID(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

}
