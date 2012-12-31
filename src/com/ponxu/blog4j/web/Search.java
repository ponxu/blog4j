package com.ponxu.blog4j.web;

import java.util.List;

import com.ponxu.blog4j.model.Post;

/**
 * 搜索
 * 
 * @author xwz
 */
public class Search extends BlogHandler {
	public void get() {
		String s = getParam("s");
		s = s.replaceAll("['\\(\\)%<>=]", ""); // 防止SQL出错, 来至OSC @Fzxs
		
		List<Post> posts = postService.queryPublishPostByKeywords(s, pageInfo);
		putVal("posts", posts);

		pageInfo.setUrl("/search?s=" + s);
		renderTemplate(FTL.theme.SEARCH);
	}

	@Override
	protected boolean needPage() {
		return true;
	}
}