package com.xFire.service.articlesFoundService;

import java.util.List;

import com.xFire.bean.articleFound.ArticleFoundBean;

public interface ArticlesFoundService 
{
	public List<ArticleFoundBean> getArticleFoundTitle(int haveGotAtcFdCount, int lineSize);
	
	public List<ArticleFoundBean> getMyArticleFoundPublishedTitle(int haveGotAtcFdCount,
							int lineSize, String registedEmail);

	public ArticleFoundBean getAtcFdContentAndTeleNumberByid(int id);
	
	public boolean dealThisPublishedById(int id, String stateNum);
	
//	public boolean publishArticleFound(ArticleFoundBean bean);
	
	public int publishArticleFound(String title, String publisher, String time, String state,
			String kind, String article, String telephoneNumber, String publisherEmail);
}
