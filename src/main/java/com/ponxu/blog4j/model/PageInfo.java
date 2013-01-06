package com.ponxu.blog4j.model;

/**
 * 分页信息(页码从1开始)
 * 
 * @author xwz
 */
public class PageInfo {
	private int currentIndex;
	private int totalCount;
	private int pageSize;
	private int pageCount;
	private String url = "";

	public PageInfo(int currentIndex, int pageSize) {
		this.currentIndex = currentIndex;
		this.pageSize = pageSize;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		this.pageCount = (int) Math.ceil((double) totalCount / pageSize);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getNextIndex() {
		return currentIndex < pageCount ? currentIndex + 1 : pageCount;
	}

	public int getPreIndex() {
		return currentIndex > 1 ? currentIndex - 1 : 1;
	}

	public String getRightParamUrl() {
		if (url.indexOf("?") > -1)
			return url + "&page=";
		else
			return url + "?page=";
	}

	public String getNextUrl() {
		return getRightParamUrl() + getNextIndex();
	}

	public String getPreUrl() {
		return getRightParamUrl() + getPreIndex();
	}

}
