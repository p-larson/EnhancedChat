package com.wowserman.settings;

import java.util.ArrayList;
import java.util.List;

public class KeywordManager {

	private static final List<String> keywords = new ArrayList<String>();
	
	public static String getKeyword(String text) {
		return KeywordManager.getKeyword(text, true);
	}
	
	public static String getKeyword(String text, boolean caseSensitive) {
		return keywords.stream().filter((keyword) -> {
			return caseSensitive ? text.contains(keyword):text.toLowerCase().contains(keyword.toLowerCase());
		}).findFirst().orElseGet(null);
	}
	
	
}
