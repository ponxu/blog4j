package com.ponxu.blog4j.model;

/**
 * 标签
 * 
 * @author xwz
 */
public class Tag implements java.io.Serializable {
	private static final long serialVersionUID = 3998059389560376840L;
	private int id;
	private String name;
	private int sort;
	private int postCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getPostCount() {
		return postCount;
	}

	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}
	
}
