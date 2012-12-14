package com.ponxu.blog4j.web.admin;

import com.ponxu.blog4j.model.Tag;
import com.ponxu.blog4j.web.FTL;

/**
 * 标签编辑
 * 
 * @author xwz
 */
public class TagEdit extends AdminOAuthHandler {
	public void get() {
		putVal("tags", tagService.queryAll());
		renderTemplate(FTL.admin.TAG_LIST);
	}

	public void post() {
		int id = getIntParam("id");
		Tag tag = prepareTag(id);
		tag.setName(getParam("name"));
		tag.setSort(getIntParam("sort", tag.getSort()));

		if (id == 0) {
			id = tagService.add(tag);
		} else {
			tagService.modify(tag);
		}

		renderString(String.valueOf(id));
	}

	private Tag prepareTag(int id) {
		Tag tag = null;
		tag = new Tag();
		tag.setId(id);
		return tag;
	}
}