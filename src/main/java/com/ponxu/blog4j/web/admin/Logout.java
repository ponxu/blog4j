package com.ponxu.blog4j.web.admin;

import com.ponxu.blog4j.web.BlogHandler;

/**
 * 登录
 * 
 * @author xwz
 */
public class Logout extends BlogHandler {
	public void get() {
		removeCookie("loginname");
		removeCookie("loginpassword");
		request().getSession().invalidate();

		redirect("/");
	}
}