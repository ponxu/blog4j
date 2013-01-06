package com.ponxu.blog4j.web;

import com.ponxu.blog4j.model.Post;

/**
 * 打开页面
 * 
 * @author xwz
 */
public class PageDetail extends BlogHandler {
	public void get(String url) {
		Post page = postService.getByUrl(url);
		if (page == null) {
			renderString("找不到页面!");
			return;
		}
		
		if (PostDetail.checkCanRead(context, page)) {
			putVal("page", page);
			renderTemplate(FTL.theme.PAGE);
		} else {
			redirect("/");
		}
	}

	@Override
	protected String cacheKey() {
		// page_page_pageid
		return "page_page_" + context.getRestParam(0);
	}

}