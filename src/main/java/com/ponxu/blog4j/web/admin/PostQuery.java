package com.ponxu.blog4j.web.admin;

import java.util.List;

import com.ponxu.blog4j.model.Post;
import com.ponxu.blog4j.service.IPostService;
import com.ponxu.blog4j.web.FTL;
import com.ponxu.run.lang.StringUtils;

/**
 * 文章查询
 * 
 * @author xwz
 */
public class PostQuery extends AdminOAuthHandler {
	public void get() {
		String type = getParam("type");
		String status = getParam("status");
		List<Post> posts = postService
				.queryByTypeStatus(type, status, pageInfo);
		putVal("posts", posts);
		putVal("tags", tagService.queryAll());
		setListTitle(type, status);

		renderTemplate(FTL.admin.POST_LIST);
	}

	public void post() {
	}

	@Override
	protected boolean needPage() {
		return true;
	}

	private void setListTitle(String type, String status) {
		String title = "文章列表";
		if (StringUtils.isNotEmpty(type)) {
			if (type.equals(IPostService.TYPE_POST)) {
				title = "文章列表";
				pageInfo.setUrl("/admin/post-query?type=post");
			} else if (type.equals(IPostService.TYPE_PAGE)) {
				title = "页面列表";
				pageInfo.setUrl("/admin/post-query?type=page");
			}
		} else if (StringUtils.isNotEmpty(status)) {
			if (status.equals(IPostService.STATUS_DRAFT)) {
				title = "草稿箱";
				pageInfo.setUrl("/admin/post-query?status=draft");
			}
		}
		putVal("listTitle", title);
	}

}
