package com.project.Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.project.bean.news.SchoolAnnouncementBean;
import com.project.bean.news.SchoolInfoBean;
import com.project.bean.news.SchoolNewsBean;

import android.util.Xml;

/*
 * 没有用过
 */
public class NewsXmlPullUtil
{
/*	private FileInputStream in;
	private String elementName;
	
	public NewsXmlPullUtil(FileInputStream in)
	{
		this.in = in;
	}

	public List<SchoolNewsBean> getSchoolNews()
	{
		List<SchoolNewsBean> lstSchNews = null; 
		SchoolNewsBean schNewsBean = null;

		XmlPullParser parser = Xml.newPullParser();
		try
		{
			parser.setInput(in, "UTF-8");
			
			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT)
			{
				elementName = parser.getName();
				if(eventType == XmlPullParser.START_DOCUMENT)
					lstSchNews = new ArrayList<SchoolNewsBean>();
				else if(eventType == XmlPullParser.START_TAG)
				{
					if("schoolNews".equals(elementName))
						schNewsBean = new SchoolNewsBean();
					else if("newsTitle".equals(elementName))
						schNewsBean.setNewsTitle(parser.nextText());
					else if("newsContent".equals(elementName))
						schNewsBean.setNewsContent(parser.nextText());
				}
				else if(eventType == XmlPullParser.END_TAG && "schoolNews".equals(elementName))
				{
					lstSchNews.add(schNewsBean);
					schNewsBean = null;
				}
				eventType = parser.next(); 
			}
		} catch (XmlPullParserException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return lstSchNews;
	}
	
	public List<SchoolAnnouncementBean> getSchoolAnnouncement()
	{
		List<SchoolAnnouncementBean> lstSchAnnouncement = null; 
		SchoolAnnouncementBean schAnnounceBean = null;

		XmlPullParser parser = Xml.newPullParser();
		try
		{
			parser.setInput(in, "UTF-8");
			
			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT)
			{
				elementName = parser.getName();
				if(eventType == XmlPullParser.START_DOCUMENT)
					lstSchAnnouncement = new ArrayList<SchoolAnnouncementBean>();
				else if(eventType == XmlPullParser.START_TAG)
				{
					if("schoolAnnouncement".equals(elementName))
						schAnnounceBean = new SchoolAnnouncementBean();
					else if("announcementTitle".equals(elementName))
						schAnnounceBean.setAnnouncementTitle(parser.nextText());
					else if("announcementContent".equals(elementName))
						schAnnounceBean.setAnnouncementContent(parser.nextText());
				}
				else if(eventType == XmlPullParser.END_TAG && "schoolAnnouncement".equals(elementName))
				{
					lstSchAnnouncement.add(schAnnounceBean);
					schAnnounceBean = null;
				}
				eventType = parser.next(); 
			}
		} catch (XmlPullParserException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return lstSchAnnouncement;
	}
	
	public List<SchoolInfoBean> getSchoolInfo()
	{
		List<SchoolInfoBean> lstSchInfo = null; 
		SchoolInfoBean schInfoBean = null;

		XmlPullParser parser = Xml.newPullParser();
		try
		{
			parser.setInput(in, "UTF-8");
			
			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT)
			{
				elementName = parser.getName();
				if(eventType == XmlPullParser.START_DOCUMENT)
					lstSchInfo = new ArrayList<SchoolInfoBean>();
				else if(eventType == XmlPullParser.START_TAG)
				{
					if("schoolInfo".equals(elementName))
						schInfoBean = new SchoolInfoBean();
					else if("infoTitle".equals(elementName))
						schInfoBean.setInfoTitle(parser.nextText());
					else if("infoContent".equals(elementName))
						schInfoBean.setInfoContent(parser.nextText());
				}
				else if(eventType == XmlPullParser.END_TAG && "schoolInfo".equals(elementName))
				{
					lstSchInfo.add(schInfoBean);
					schInfoBean = null;
				}
				eventType = parser.next(); 
			}
		} catch (XmlPullParserException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return lstSchInfo;
	}
*/
}
		
