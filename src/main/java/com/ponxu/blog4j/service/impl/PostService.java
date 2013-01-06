package com.ponxu.blog4j.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ponxu.blog4j.Config;
import com.ponxu.blog4j.cache.Cache;
import com.ponxu.blog4j.dao.DAO;
import com.ponxu.blog4j.model.PageInfo;
import com.ponxu.blog4j.model.Post;
import com.ponxu.blog4j.model.Tag;
import com.ponxu.blog4j.service.IPostService;
import com.ponxu.blog4j.service.ITagService;
import com.ponxu.run.db.wrap.RowWrapper;
import com.ponxu.run.db.wrap.impl.LongRowWrapper;
import com.ponxu.run.lang.BeanUtils;
import com.ponxu.run.lang.StringUtils;
import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;

/**
 * @author xwz
 */
public class PostService implements IPostService {
	private static final Log LOG = LogFactory.getLog();

	private static final RowWrapper<Post> POST_WRAPPER = new RowWrapper<Post>() {
		public Post wrap(ResultSet rs) throws SQLException {
			Post post = new Post();
			post.setId(rs.getInt("id"));
			post.setUrl(rs.getString("url"));
			post.setTitle(rs.getString("title"));
			post.setContent(rs.getString("content"));
			post.setAddtime(rs.getTimestamp("addtime"));
			post.setTop(rs.getInt("top"));
			post.setType(rs.getString("type"));
			post.setStatus(rs.getString("status"));
			return post;
		}
	};

	private ITagService tagService;

	public Post getById(int id) {
		LOG.debug("id=%d", id);
		String sql = "select * from bj_post where id=?";
		return populateTag(DAO.queryUni(POST_WRAPPER, sql, id));
	}

	public Post getByUrl(String url) {
		String sql = "select * from bj_post where url=?";
		return populateTag(DAO.queryUni(POST_WRAPPER, sql, url));
	}

	public Post getLatestDraft() {
		String sql = "select * from bj_post where status=?";
		sql += " order by " + DEFAULT_ORDER;
		return populateTag(DAO.queryUni(POST_WRAPPER, sql, STATUS_DRAFT));
	}

	public int add(Post post, int[] tagIds) {
		DAO.begin();

		// 1 添加文章
		String sql = "insert into bj_post(url,title,content,addtime,top,status,type) values(?,?,?,?,?,?,?)";
		int id = DAO.execute(sql, post.getUrl(), post.getTitle(),
				post.getContent(), new Date(), post.getTop(), post.getStatus(),
				post.getType());

		post.setId(id);

		// 2 添加标签
		updateTags(post, tagIds);

		DAO.commit();

		Cache.clear();

		return id;
	}

	public int modify(Post post, int[] tagIds) {
		DAO.begin();

		// 1 更新
		String sql = "update bj_post set url=?,title=?,content=?,top=?,status=?,type=? where id=?";
		int r = DAO.execute(sql, post.getUrl(), post.getTitle(),
				post.getContent(), post.getTop(), post.getStatus(),
				post.getType(), post.getId());

		// 2 更新标签
		updateTags(post, tagIds);

		DAO.commit();

		Cache.clear();
		return r;
	}

	public int remove(int id) {
		DAO.begin();
		tagService.removePostTag(id);
		int i = DAO.execute("delete from bj_post where id=?", id);
		DAO.commit();
		Cache.clear();
		return i;
	}

	// ===================================================================
	public List<Post> queryByTypeStatus(String type, String status,
			PageInfo page) {
		String condition = " 1=1 ";
		if (StringUtils.isNotEmpty(type)) {
			condition += " and type='" + type + "'";
		}
		if (StringUtils.isNotEmpty(status)) {
			condition += " and status='" + status + "'";
		}
		return query(condition, null, page, true);
	}

	public List<Post> queryPublishPostByKeywords(String keywords, PageInfo page) {
		String condition = PUBLISH_POST;
		if (StringUtils.isNotEmpty(keywords)) {
			condition += " and " + keywordsCondition(keywords);
		}
		return query(condition, null, page, true);
	}

	public List<Post> queryPublishPageByKeywords(String keywords, PageInfo page) {
		String condition = PUBLISH_PAGE;
		if (StringUtils.isNotEmpty(keywords)) {
			condition += " and " + keywordsCondition(keywords);
		}
		return query(condition, null, page, true);
	}

	public List<Post> queryPublishPostByTagId(int tagId, PageInfo page) {
		String condition = PUBLISH_POST;
		if (tagId > 0) {
			condition += " and exists (select tag_id from bj_post_tag pt where bj_post.id=pt.post_id and tag_id="
					+ tagId + ")";
		}
		return query(condition, null, page, true);
	}

	public List<Post> queryPublishLatestPost(int count) {
		String condition = PUBLISH_POST;
		String order = "id desc";
		PageInfo page = new PageInfo(1, count);

		return query(condition, order, page, false);
	}

	public List<Post> queryPublishRelativePost(Post post, int count) {
		String condition = PUBLISH_POST;
		String order = "rand()";
		PageInfo page = new PageInfo(1, count);

		StringBuilder tagids = new StringBuilder("(");
		int i = 0;
		for (Tag tag : post.getTags()) {
			if (i > 0)
				tagids.append(",");
			tagids.append(tag.getId());
			i++;
		}
		tagids.append(")");

		if (tagids.toString().equals("()"))
			return null;

		condition += " and id in (select post_id from bj_post_tag where tag_id in"
				+ tagids + " and post_id<>" + post.getId() + ")";
		return query(condition, order, page, false);
	}

	// ===================================================================
	/**
	 * 整合查询
	 * 
	 * @param condition
	 *            查询条件: age=20 and name like'x%'
	 * @param order
	 *            排序: 若为null, 按默认排序; id desc
	 * @param page
	 *            分页信息
	 * @param calcPage
	 *            是否计算分页信息
	 * @return 已经设置好标签的文章列表
	 */
	private List<Post> query(String condition, String order, PageInfo page,
			boolean calcPage) {
		String sql = "select id,url,title,addtime,top,status,type,left(content,"
				+ Config.sublength + ") as content from bj_post";
		String sqlCount = "select count(*) from bj_post";

		if (StringUtils.isNotEmpty(condition)) {
			sql += " where " + condition;
			sqlCount += " where " + condition;
		}
		sql += " order by " + StringUtils.ifEmpty(order, DEFAULT_ORDER);

		if (calcPage) {
			// 计算分页
			Long total = DAO.queryUni(LongRowWrapper.getInstance(), sqlCount);
			page.setTotalCount(total.intValue());
		}

		return populateTag(DAO.queryPage(POST_WRAPPER, sql,
				page.getCurrentIndex(), page.getPageSize()));
	}

	/** 生成关键词条件 */
	private String keywordsCondition(String kw) {
		StringBuilder condition = new StringBuilder("(");
		String strs[] = kw.trim().split("\\s");
		int i = 0;
		for (String keywords : strs) {
			if (i > 0)
				condition.append(" or ");

			condition.append("title like '%").append(keywords)
					.append("%' or content like '%").append(keywords)
					.append("%'");
			i++;
		}
		condition.append(")");

		return condition.toString();
	}

	/** 给文章设置标签 */
	private List<Post> populateTag(List<Post> posts) {
		final Map<Integer, Post> postsMap = new HashMap<Integer, Post>();
		StringBuilder postIds = new StringBuilder("(");
		for (int i = 0; i < posts.size(); i++) {
			if (i > 0) {
				postIds.append(",");
			}
			postIds.append(posts.get(i).getId());
			postsMap.put(posts.get(i).getId(), posts.get(i));
		}
		postIds.append(")");

		if (postIds.toString().equals("()"))
			return posts;

		String sql = "select * from bj_post_tag where post_id in " + postIds;
		DAO.queryAll(new RowWrapper<Post>() {
			public Post wrap(ResultSet rs) throws SQLException {
				int postId = rs.getInt("post_id");
				int tagId = rs.getInt("tag_id");
				postsMap.get(postId).getTags().add(tagService.getById(tagId));

				return null;
			}
		}, sql);

		return posts;
	}

	/** 给文章设置标签 */
	private Post populateTag(Post post) {
		if (post == null)
			return post;

		List<Post> posts = new ArrayList<Post>();
		posts.add(post);
		populateTag(posts);

		return post;
	}

	// ===================================================================
	// 把标签关联到文章
	private void updateTags(Post post, int[] tagIds) {
		// 1 删除多余标签
		Iterator<Tag> tit = post.getTags().iterator();
		while (tit.hasNext()) {
			Tag tag = tit.next();
			if (BeanUtils.isNot(tag.getId(), tagIds)) {
				removeTag(post.getId(), tag.getId());
				tit.remove();
			}
		}

		// 2 关联新增标签
		for (int tagId : tagIds) {
			if (!hasTag(post, tagId))
				addTag(post.getId(), tagId);
		}
	}

	// 文章是否有此标签
	private boolean hasTag(Post post, int tagId) {
		for (Tag tag : post.getTags()) {
			if (tag.getId() == tagId)
				return true;
		}
		return false;
	}

	// 给文章添加标签
	private void addTag(int postId, int tagId) {
		String sql = "insert into bj_post_tag(post_id,tag_id) values(?,?)";
		DAO.execute(sql, postId, tagId);
		tagService.addCount(tagId);
	}

	// 移除文章标签
	private void removeTag(int postId, int tagId) {
		String sql = "delete from bj_post_tag where post_id=? and tag_id=?";
		DAO.execute(sql, postId, tagId);
		tagService.reduceCount(tagId);
	}

	// ------------------------------------------------------
	public void setTagService(ITagService tagService) {
		this.tagService = tagService;
	}

}
