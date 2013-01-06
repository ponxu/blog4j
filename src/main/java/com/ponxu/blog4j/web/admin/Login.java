package com.ponxu.blog4j.web.admin;

import com.ponxu.blog4j.web.BlogHandler;
import com.ponxu.blog4j.web.FTL;
import com.ponxu.run.lang.StringUtils;

/**
 * 登录
 * 
 * @author xwz
 */
public class Login extends BlogHandler {
	public void get() {
		if (!checkLoginFromCookie()) {
			// 未登录: 显示登录页面
			toLogin();
		} else {
			// 已经登录: 转到后台首页
			toIndex();
		}
	}

	public void post() {
		String username = StringUtils.md5(getParam("username"));
		String password = StringUtils.md5(getParam("password"));

		if (checkLogin(username, password)) {
			// request().getSession().setAttribute("loginname", username);
			// ??? SAE cookie路径有问题
			setCookie("loginname", username);
			setCookie("loginpassword", password);
			toIndex();
		} else {
			toLogin();
		}
	}

	private void toIndex() {
		redirect("/admin/post-edit/0");
	}

	private void toLogin() {
		renderTemplate(FTL.admin.LOGIN);
	}
}
