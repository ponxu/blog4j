package com.ponxu.blog4j.service;

import java.util.List;

import com.ponxu.blog4j.model.Tag;

/**
 * 标签
 * 
 * @author xwz
 */
public interface ITagService {
	/** 根据ID获取标签 */
	public Tag getById(int id);

	/** 所有标签 */
	public List<Tag> queryAll();

	/** 添加标签 */
	public int add(Tag tag);

	/** 修改标签 */
	public void modify(Tag tag);

	/** 删除标签 */
	public void remove(int id);

	/** 文章数量-1 */
	public void addCount(int id);

	/** 文章数量-1 */
	public void reduceCount(int id);

	/** 移除文章的标签 */
	public void removePostTag(int postid);
}
