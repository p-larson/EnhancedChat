package com.wowserman.api;

import java.util.HashMap;

public class KeywordManager {

	private static final HashMap<String, Long> ids = new HashMap<String, Long>();
	
	/**
	 * Create a Unique ID for a Keyword Catagory
	 * 
	 * @param keywordCatagoryName
	 * @return
	 */
	public static long createID(String keywordCatagoryName) {
		if (ids.containsKey(keywordCatagoryName))
			return ids.get(keywordCatagoryName);
		else {
			final long id = ids.size() + 1;
			ids.put(keywordCatagoryName, id);
			return id;
		}
	}
}
