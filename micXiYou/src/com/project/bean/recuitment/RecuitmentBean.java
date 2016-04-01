package com.project.bean.recuitment;

import java.io.Serializable;

public class RecuitmentBean implements Serializable
{
	private static final long serialVersionUID = -3486723647397497889L;

	private String title;
	private String article;
	private int readCount;
	private String time;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public int getReadCount() {
		return readCount;
	}
	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	

}
