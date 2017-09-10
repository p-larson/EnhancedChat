package com.wowserman.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.wowserman.settings.SettingsManager;

public class SearchForKeywordEvent extends Event implements Cancellable {

	private final Player context;
	
	private final String message;
	
	private String keyword = null;

    public SearchForKeywordEvent(final String message, Player context) {
    		this.message = message;
    		this.context = context;
    }
    
    public boolean containsCustomKeyword(String keyword) {
    		return SettingsManager.CASE_SENSITIVE ? message.contains(keyword):message.toLowerCase().contains(keyword.toLowerCase());
    }

    private static final HandlerList HANDLERS = new HandlerList();
    
    /**
     * 
     * 
     * @return Player that is in the Context.
     */
    public Player getContext() {
    		return context;
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
    
}
