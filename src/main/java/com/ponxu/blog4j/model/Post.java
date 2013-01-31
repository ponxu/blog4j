package com.ponxu.blog4j.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.petebevin.markdown.MarkdownProcessor;
import com.ponxu.blog4j.service.IPostService;

/**
 * 文章
 * 
 * @author xwz
 */
public class Post implements java.io.Serializable {
	private static final long serialVersionUID = -7679909637903047354L;
	
	private int id;
	private String url = "";
	private String title = "";
	private String content = "";
	private Date addtime;
	private int top;
	private String status = IPostService.STATUS_PUBLISH;
	private String type = IPostService.TYPE_POST;
	private Set<Tag> tags = new HashSet<Tag>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
	public String getContentHtml() {
		return new MarkdownProcessor().markdown(content);
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", url=" + url + ", title=" + title
				+ ", content=" + content + ", top=" + top + ", status="
				+ status + ", type=" + type + "]";
	}
}
