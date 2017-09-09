package com.wowserman.settings;

import java.util.ArrayList;
import java.util.List;

public class Keyword {

	private String name, onClick;
	private List<String> onHover = new ArrayList<String>();
	
	
	public String getName() {
		return this.toString();
	}
	
	
	public Keyword(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return SettingsManager.CASE_SENSITIVE ? name:name.toLowerCase();
	}
}
