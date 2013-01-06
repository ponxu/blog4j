package com.ponxu.blog4j.service;

import java.util.List;
import java.util.Map;

/**
 * 设置
 * 
 * @author xwz
 */
public interface ISettingService {
	public String get(String name);

	public int getInt(String name);

	public void set(String name, String value, String description);

	/** 所有设置项 */
	public Map<String, String> asObject();

	public List<Map<String, String>> queryAll();
}