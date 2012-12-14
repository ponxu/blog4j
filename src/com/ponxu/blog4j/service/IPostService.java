package com.ponxu.blog4j.service;

import java.util.List;

import com.ponxu.blog4j.model.PageInfo;
import com.ponxu.blog4j.model.Post;

/**
 * 文章
 * 
 * @author xwz
 */
public interface IPostService {
	// 状态
	public static final String STATUS_DRAFT = "draft";
	public static final String STATUS_PUBLISH = "publish";
	public static final String STATUS_PRIVATE = "private";
	public static final String STATUS_PASSWORD = "password";

	// 类型
	public static final String TYPE_POST = "post";
	public static final String TYPE_PAGE = "page";

	// 排序
	public static final String DEFAULT_ORDER = " top desc, id desc";
	// 发布的文章
	public static final String PUBLISH_POST = " type='" + TYPE_POST
			+ "' and status='" + STATUS_PUBLISH + "'";
	// 发布的页面
	public static final String PUBLISH_PAGE = " type='" + TYPE_PAGE
			+ "' and status='" + STATUS_PUBLISH + "'";

	// =========================================================================

	/** 根据ID获取 */
	public Post getById(int id);

	/** 根据URL获取 */
	public Post getByUrl(String url);

	/** 最近的一篇草稿 */
	public Post getLatestDraft();

	/** 添加 */
	public int add(Post post, int[] tagIds);

	/** 修改 */
	public int modify(Post post, int[] tagIds);

	/** 删除 */
	public int remove(int id);

	/** 根据状态/类型查询 */
	public List<Post> queryByTypeStatus(String type, String status, PageInfo page);

	/** 根据关键词查询发布的文章 */
	public List<Post> queryPublishPostByKeywords(String keywords, PageInfo page);

	/** 根据标签查询发布的文章 */
	public List<Post> queryPublishPostByTagId(int tagId, PageInfo page);
	
	/** 最新发布文章 */
	public List<Post> queryPublishLatestPost(int count);
	
	/** 相关文章 */
	public List<Post> queryPublishRelativePost(Post post, int count);

	/** 发布的页面 */
	public List<Post> queryPublishPageByKeywords(String keywords, PageInfo page);

}
