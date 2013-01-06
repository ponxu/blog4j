package com.ponxu.blog4j.web;

import com.ponxu.blog4j.model.Post;
import com.ponxu.run.web.WebContext;

/**
 * 打开文章
 * 
 * @author xwz
 */
public class PostDetail extends BlogHandler {
	public void get(int id) {
		Post post = postService.getById(id);
		if (post == null) {
			renderString("找不到文章!");
			return;
		}
		
		if (checkCanRead(context, post)) {
			putVal("post", post);
			// 相关文章
			putVal("relativePosts",
					postService.queryPublishRelativePost(post, 5));
			renderTemplate(FTL.theme.POST);
		} else {
			redirect("/");
		}
	}

	public static boolean checkCanRead(WebContext context, Post p) {
		return true;
	}

	@Override
	protected String cacheKey() {
		// page_post_postid
		return "page_post_" + context.getRestParam(0);
	}

}