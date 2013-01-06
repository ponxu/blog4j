package com.ponxu.blog4j.web;

import java.util.List;

import com.ponxu.blog4j.model.Post;

/**
 * 首页
 * 
 * @author xwz
 */
public class Index extends BlogHandler {

	public void get() {
		List<Post> posts = postService.queryPublishPostByKeywords(null,
				pageInfo);
		putVal("posts", posts);
		renderTemplate(FTL.theme.INDEX);
	}

	@Override
	protected boolean needPage() {
		return true;
	}

	@Override
	protected String cacheKey() {
		// page_index_页
		return "page_index_" + pageInfo.getCurrentIndex();
	}

}