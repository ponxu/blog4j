package com.ponxu.blog4j.cache;

import com.ponxu.blog4j.Config;
import com.ponxu.run.lang.StringUtils;
import com.sina.sae.memcached.SaeMemcache;

/**
 * 缓存管理(优先Memeche)
 * 
 * @author xwz
 */
public class Cache {
	private static SaeMemcache memcache;

	static {
		try {
			memcache = new SaeMemcache();
		} catch (Exception e) {
			memcache = null;
		}
	}

	public static Object get(String key) {
		if (Config.cache && StringUtils.isNotEmpty(key)) {
			if (memcache != null) {
				memcache.init();
				return memcache.get(key);
			} else {
				return RAMCache.get(key);
			}
		}
		return null;
	}

	public static void put(String key, Object value) {
		if (Config.cache && StringUtils.isNotEmpty(key)) {
			if (memcache != null) {
				memcache.init();
				memcache.set(key, value);
			} else {
				RAMCache.put(key, value);
			}
		}
	}

	public static void clear() {
		if (memcache != null) {
			memcache.init();
			memcache.flushAll();
		} else {
			RAMCache.clear();
		}
	}
}
