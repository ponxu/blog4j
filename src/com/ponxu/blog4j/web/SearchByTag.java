package com.ponxu.blog4j.web;

import java.util.List;

import com.ponxu.blog4j.model.Post;

/**
 * 某标签下文章搜索
 * 
 * @author xwz
 */
public class SearchByTag extends BlogHandler {
	public void get(int tagId) {
		List<Post> posts = postService.queryPublishPostByTagId(tagId, pageInfo);
		putVal("posts", posts);

		pageInfo.setUrl("/tag/" + tagId);
		renderTemplate(FTL.theme.SEARCH);
	}

	@Override
	protected boolean needPage() {
		return true;
	}

	@Override
	protected String cacheKey() {
		// page_tag_tagid_页
		return "page_tag_" + context.getRestParam(0) + "_"
				+ pageInfo.getCurrentIndex();
	}

}