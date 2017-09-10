package com.wowserman.api;

import java.util.ArrayList;
import java.util.List;

import com.wowserman.settings.Keyword;

public class KeywordManager {

	private static final List<Keyword> keywords = new ArrayList<Keyword>();
	
	public static boolean add(Keyword keyword) {
		return keywords.add(keyword);
	}
	
	public static String getKeyword(String text) {
		return KeywordManager.getKeyword(text, true);
	}
	
	public static String getKeyword(String text, boolean caseSensitive) {
		final Keyword keyword = keywords.stream().filter((element) -> {
			return caseSensitive ? text.contains(element.toString()):text.toLowerCase().contains(element.toString().toLowerCase());
		}).findFirst().orElseGet(null);
		
		return keyword == null ? null:keyword.toString();
	}
	
	
	public enum KeywordType {
		PLAYER, TOWN, NATION, CUSTOM;
	}
	
}
