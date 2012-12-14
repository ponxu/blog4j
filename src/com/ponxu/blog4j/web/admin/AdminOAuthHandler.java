package com.ponxu.blog4j.web.admin;

import com.ponxu.blog4j.web.BlogHandler;

/**
 * 需要验证的handler
 * 
 * @author xwz
 */
public class AdminOAuthHandler extends BlogHandler {
	@Override
	protected boolean oauth() {
		return checkLoginFromCookie();
	}
}