package com.ponxu.blog4j.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.ponxu.blog4j.cache.Cache;
import com.ponxu.blog4j.cache.RAMCache;
import com.ponxu.blog4j.dao.DAO;
import com.ponxu.blog4j.model.Tag;
import com.ponxu.blog4j.service.ITagService;
import com.ponxu.run.db.wrap.RowWrapper;

/**
 * 
 * @author xwz
 */
@SuppressWarnings("unchecked")
public class TagService implements ITagService {
	private static final String CACHE_KEY_PRE = "model_tag_";
	private static final String CACHE_KEY_ALL = CACHE_KEY_PRE + "all";

	public Tag getById(int id) {
		queryAll();
		return (Tag) RAMCache.get(CACHE_KEY_PRE + id);
	}

	public List<Tag> queryAll() {
		// 读取缓存
		List<Tag> tags = (List<Tag>) RAMCache.get(CACHE_KEY_ALL);

		if (tags == null) {
			// 查询, 并缓存
			String sql = "select * from bj_tag order by sort desc, id asc";
			tags = DAO.queryAll(new RowWrapper<Tag>() {
				public Tag wrap(ResultSet rs) throws SQLException {
					Tag tag = new Tag();
					tag.setId(rs.getInt("id"));
					tag.setName(rs.getString("name"));
					tag.setPostCount(rs.getInt("post_count"));
					tag.setSort(rs.getInt("sort"));
					RAMCache.put(CACHE_KEY_PRE + tag.getId(), tag);
					return tag;
				}
			}, sql);
			RAMCache.put(CACHE_KEY_ALL, tags);
		}

		return tags;
	}

	public int add(Tag tag) {
		Cache.clear();
		clear();
		String sql = "insert into bj_tag(name) values(?)";
		return DAO.execute(sql, tag.getName());
	}

	public void modify(Tag tag) {
		Cache.clear();
		clear();
		String sql = "update bj_tag set name=?,sort=? where id=?";
		DAO.execute(sql, tag.getName(), tag.getSort(), tag.getId());
	}

	public void remove(int id) {
		Cache.clear();
		clear();
		DAO.begin();
		DAO.execute("delete from bj_post_tag where tag_id=?", id);
		DAO.execute("delete from bj_tag where id=?", id);
		DAO.commit();
	}

	public void removePostTag(int postid) {
		clear();
		DAO.execute("update bj_tag set post_count=post_count-1"
				+ " where post_count>0"
				+ " and exists (select tag_id from bj_post_tag"
				+ " where tag_id=bj_tag.id and post_id=?)", postid);
		DAO.execute("delete from bj_post_tag where post_id=?", postid);
	}

	public void addCount(int id) {
		clear();
		String sql = "update bj_tag set post_count=post_count+1 where id=?";
		DAO.execute(sql, id);
	}

	public void reduceCount(int id) {
		clear();
		String sql = "update bj_tag set post_count=post_count-1 where post_count>0 and id=?";
		DAO.execute(sql, id);
	}

	/** 清理缓存 */
	private void clear() {
		RAMCache.clearByKeyPrefix(CACHE_KEY_PRE);
	}
}
