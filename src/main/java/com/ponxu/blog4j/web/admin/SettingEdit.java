package com.ponxu.blog4j.web.admin;

import com.ponxu.blog4j.web.FTL;
import com.ponxu.run.lang.BeanUtils;
import com.ponxu.run.lang.StringUtils;

/**
 * 设置
 * 
 * @author xwz
 */
public class SettingEdit extends AdminOAuthHandler {
	public void get() {
		putVal("settings", settingService.queryAll());
		renderTemplate(FTL.admin.SETING);
	}

	public void post() {
		String name = getParam("name");
		String value = getParam("value");
		String description = getParam("description");
		
		String old = settingService.get(name);

		if (StringUtils.equalsIgnoreCase(name, "loginpassword")) {
			// 传过来的和原密码不一样, 那么: md5
			if (!StringUtils.equalsIgnoreCase(old, value)) {
				value = StringUtils.md5(value);
			}
		}

		// 有改动
		if (BeanUtils.isNot(value, old))
			settingService.set(name, value, description);

		renderString(String.valueOf(1));
	}
}