package com.ponxu.blog4j.web.admin;

import com.ponxu.blog4j.model.Post;
import com.ponxu.blog4j.web.FTL;

/**
 * 文章编辑
 * 
 * @author xwz
 */
public class PostEdit extends AdminOAuthHandler {
	public void get(int id) {
		Post post = preparePost(id);
		if (post == null) {
			renderString("找不到文章!");
			return;
		}

		putVal("post", post);
		putVal("tags", tagService.queryAll());
		renderTemplate(FTL.admin.POST_EDIT);
	}

	public void post(int id) {
		Post post = preparePost(id);
		post.setTitle(getParam("title"));
		post.setUrl(getParam("url"));
		post.setContent(getParam("content"));
		post.setTop(getIntParam("top", 0));
		post.setStatus(getParam("status"));
		post.setType(getParam("type"));
		int[] tagIds = getIntParams("tagid");

		if (id == 0) {
			id = postService.add(post, tagIds);
		} else {
			postService.modify(post, tagIds);
		}

		renderString(String.valueOf(id));
	}

	private Post preparePost(int id) {
		Post post = null;
		if (id > 0) {
			post = postService.getById(id);
		} else {
			post = postService.getLatestDraft();
			if (post == null)
				post = new Post();
		}
		return post;
	}
}
