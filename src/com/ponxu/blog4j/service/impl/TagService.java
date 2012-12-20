package com.ponxu.blog4j.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.ponxu.blog4j.cache.Cache;
import com.ponxu.blog4j.dao.DAO;
import com.ponxu.blog4j.model.Tag;
import com.ponxu.blog4j.service.ITagService;
import com.ponxu.run.db.wrap.RowWrapper;

/**
 * 标签操作业务
 * 
 * @author xwz
 */
public class TagService implements ITagService {

	private static final RowWrapper<Tag> TAG_WRAPPER = new RowWrapper<Tag>() {
		public Tag wrap(ResultSet rs) throws SQLException {
			Tag tag = new Tag();
			tag.setId(rs.getInt("id"));
			tag.setName(rs.getString("name"));
			tag.setPostCount(rs.getInt("post_count"));
			tag.setSort(rs.getInt("sort"));
			return tag;
		}
	};

	public Tag getById(int id) {
		String sql = "select * from bj_tag where id=?";
		return DAO.queryUni(TAG_WRAPPER, sql, id);
	}

	public List<Tag> queryAll() {
		String sql = "select * from bj_tag order by sort desc, id asc";
		List<Tag> tags = DAO.queryAll(TAG_WRAPPER, sql);
		return tags;
	}

	public int add(Tag tag) {
		String sql = "insert into bj_tag(name) values(?)";
		int id = DAO.execute(sql, tag.getName());

		Cache.clear();
		return id;
	}

	public void modify(Tag tag) {
		String sql = "update bj_tag set name=?,sort=? where id=?";
		DAO.execute(sql, tag.getName(), tag.getSort(), tag.getId());
		Cache.clear();
	}

	public void remove(int id) {
		DAO.begin();
		DAO.execute("delete from bj_post_tag where tag_id=?", id);
		DAO.execute("delete from bj_tag where id=?", id);
		DAO.commit();
		Cache.clear();
	}

	public void removePostTag(int postid) {
		DAO.execute("update bj_tag set post_count=post_count-1"
				+ " where post_count>0"
				+ " and exists (select tag_id from bj_post_tag"
				+ " where tag_id=bj_tag.id and post_id=?)", postid);
		DAO.execute("delete from bj_post_tag where post_id=?", postid);
	}

	public void addCount(int id) {
		String sql = "update bj_tag set post_count=post_count+1 where id=?";
		DAO.execute(sql, id);
	}

	public void reduceCount(int id) {
		String sql = "update bj_tag set post_count=post_count-1 where post_count>0 and id=?";
		DAO.execute(sql, id);
	}
}
