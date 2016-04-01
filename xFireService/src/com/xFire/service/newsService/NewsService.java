package com.xFire.service.newsService;

import java.util.List;

import com.xFire.bean.news.*;

public interface NewsService 
{
	public List<SchoolNewsBean> getSchoolNews(int haveGotNewsCount, int lineSize);
	public List<SchoolAnnouncementBean> getSchoolAnnouncement(int haveGotAnnouncementCount, int lineSize);
	public List<SchoolInfoBean> getSchoolInfo(int haveGotInfoCount, int lineSize); 
	
	public String getSchoolNewsContentByPosition(int position);
	public String getSchoolAnnouncementContentByPosition(int position);
	public String getSchoolInfoContentByPosition(int position);
}
