package com.ponxu.blog4j.web.admin;

/**
 * 文章删除
 * 
 * @author xwz
 */
public class PostDelete extends AdminOAuthHandler {
	public void get(int id) {
		int i = postService.remove(id);
		renderString(String.valueOf(i));
	}
}