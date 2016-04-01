package com.xFire.service.secondaryMarketService;

import java.util.List;

import com.xFire.bean.secondaryMarket.SecondaryMarketBean;

public interface SecondaryMarketService 
{
	public List<SecondaryMarketBean> getSecondaryMarketTitle(int haveGotScdMktCount, int lineSize);
	
	public List<SecondaryMarketBean> getMySecondaryMarketPublishedTitle(int haveGotScdMktCount,
							int lineSize, String registedEmail);

	public SecondaryMarketBean getScdMktContentAndTeleNumberByid(int id);
	
	public boolean dealThisTradeById(int id, String stateNum);
	
//	public boolean publishArticleFound(ArticleFoundBean bean);
	
	public int publishSecondaryMarket(String title, String publisher, String time, String state,
			String kind, String article, String telephoneNumber, String publisherEmail);
}
