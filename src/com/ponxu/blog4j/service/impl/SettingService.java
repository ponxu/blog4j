package com.ponxu.blog4j.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ponxu.blog4j.cache.Cache;
import com.ponxu.blog4j.dao.DAO;
import com.ponxu.blog4j.service.ISettingService;
import com.ponxu.run.db.wrap.impl.MapRowWrapper;
import com.ponxu.run.db.wrap.impl.StringRowWrapper;
import com.ponxu.run.lang.StringUtils;

/**
 * @author xwz
 */
public class SettingService implements ISettingService {

	public String get(String name) {
		String sql = "select value from bj_setting where name=?";
		return DAO.queryUni(StringRowWrapper.getInstance(), sql, name);
	}

	public int getInt(String name) {
		String str = get(name);
		return Integer.parseInt(StringUtils.ifEmpty(str, "0"));
	}

	public void set(String name, String value, String description) {
		String v = get(name);

		if (v == null) {
			DAO.execute(
					"insert into bj_setting(name,value,description) values(?,?,?)",
					name, value, description);
		} else {
			DAO.execute("update bj_setting set value=? where name=?", value,
					name);
		}

		Cache.clear();
	}

	public List<Map<String, String>> queryAll() {
		String sql = "select * from bj_setting order by name asc";
		return DAO.queryAll(MapRowWrapper.getInstance(), sql);
	}

	public Map<String, String> asObject() {
		List<Map<String, String>> list = queryAll();
		Map<String, String> map = new HashMap<String, String>();
		for (Map<String, String> m : list) {
			map.put(m.get("name"), m.get("value"));
		}
		return map;
	}
}
