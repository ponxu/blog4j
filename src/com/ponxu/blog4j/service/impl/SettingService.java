package com.ponxu.blog4j.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ponxu.blog4j.cache.Cache;
import com.ponxu.blog4j.cache.RAMCache;
import com.ponxu.blog4j.dao.DAO;
import com.ponxu.blog4j.service.ISettingService;
import com.ponxu.run.db.wrap.RowWrapper;
import com.ponxu.run.db.wrap.impl.MapRowWrapper;
import com.ponxu.run.lang.StringUtils;

/**
 * @author xwz
 */
@SuppressWarnings("unchecked")
public class SettingService implements ISettingService {
	private static final String CACHE_KEY_PRE = "model_setting_";
	private static final String CACHE_KEY_ALL = CACHE_KEY_PRE + "all";

	private Map<String, String> map;

	public String get(String name) {
		asObject();
		return map.get(name);
	}

	public int getInt(String name) {
		String str = get(name);
		return Integer.parseInt(StringUtils.ifEmpty(str, "0"));
	}

	public void set(String name, String value, String description) {
		String v = get(name);
		clear();

		if (v == null) {
			DAO.execute("insert into bj_setting(name,value,description) values(?,?,?)", name, value, description);
		} else {
			DAO.execute("update bj_setting set value=? where name=?", value, name);
		}
		
		Cache.clear();
	}

	public List<Map<String, String>> queryAll() {
		asObject();
		return (List<Map<String, String>>) RAMCache.get(CACHE_KEY_ALL);
	}

	public Map<String, String> asObject() {
		List<Map<String, String>> list = (List<Map<String, String>>) RAMCache.get(CACHE_KEY_ALL);
		if (list == null) {
			String sql = "select * from bj_setting";
			map = new HashMap<String, String>();
			list = DAO.queryAll(new RowWrapper<Map<String, String>>() {
				public Map<String, String> wrap(ResultSet rs) throws SQLException {
					Map<String, String> row = MapRowWrapper.getInstance().wrap(rs);
					RAMCache.put(CACHE_KEY_PRE + row.get("name"), row.get("value"));
					map.put(row.get("name"), row.get("value"));
					return row;
				}
			}, sql);
			RAMCache.put(CACHE_KEY_ALL, list);
		}
		return map;
	}

	private void clear() {
		RAMCache.clearByKeyPrefix(CACHE_KEY_PRE);
		map = null;
	}

}
