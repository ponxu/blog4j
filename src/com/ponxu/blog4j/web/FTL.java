package com.ponxu.blog4j.web;

import com.ponxu.blog4j.Config;

/**
 * 模板路径
 * 
 * @author xwz
 */
public interface FTL {
	interface theme {
		/** 首页 */
		String INDEX = Config.theme + "/index.ftl";
		/** 文章 */
		String POST = Config.theme + "/post.ftl";
		/** 页面 */
		String PAGE = Config.theme + "/page.ftl";
		/** 搜索结果 */
		String SEARCH = Config.theme + "/search.ftl";
	}

	interface admin {
		/** 登陆 */
		String LOGIN = "admin/login.ftl";
		/** 编辑 */
		String POST_EDIT = "admin/post-edit.ftl";
		/** 列表 */
		String POST_LIST = "admin/post-list.ftl";
		/** 标签 */
		String TAG_LIST = "admin/tag-list.ftl";
		/** 设置 */
		String SETING = "admin/setting.ftl";
	}

}