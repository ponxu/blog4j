package com.ponxu.blog4j.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于内存的缓存
 * 
 * @author xwz
 */
public class RAMCache {
	private static final Map<String, Object> CACHE = new HashMap<String, Object>();

	public static Object get(String key) {
		return CACHE.get(key);
	}

	public static void put(String key, Object value) {
		CACHE.put(key, value);
	}

	public static void clear() {
		CACHE.clear();
	}

	public static void clearByKeyPrefix(String prefix) {
		List<String> clearKeys = new ArrayList<String>();
		for (String key : CACHE.keySet()) {
			if (key.startsWith(prefix))
				clearKeys.add(key);
		}

		for (String key : clearKeys)
			CACHE.remove(key);
	}
}
