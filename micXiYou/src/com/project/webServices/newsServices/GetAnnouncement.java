package com.project.webServices.newsServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Handler;

import com.UI.micxiyou.news.SchoolAnnouncementFragment;
import com.UI.micxiyou.news.SchoolNewsFragment;
import com.project.Util.Constant;
import com.project.bean.news.SchoolAnnouncementBean;
import com.project.bean.news.SchoolNewsBean;

public class GetAnnouncement implements Callable<List<Map<String,String>>>
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_NEWS;

	private int haveGotAnnouncementCount = 0;
	private int lineSize = 14;
	
	private List<Map<String,String>> announcementList = null;
	
	public GetAnnouncement(int haveGotAnnouncementCount)
	{
		this.haveGotAnnouncementCount = haveGotAnnouncementCount;
	}
	
	@Override
	public List<Map<String, String>> call() throws IOException, XmlPullParserException
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_SCHOOL_ANNOUNCEMENT);
		request.addProperty("haveGotAnnouncementCount", haveGotAnnouncementCount);
		request.addProperty("lineSize", lineSize);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
		
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		SchoolAnnouncementFragment.isDownLoadFinish = false;

		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_SCHOOL_ANNOUNCEMENT , envelope);
		SoapObject soapObject = (SoapObject) envelope.getResponse();

		if(soapObject == null)
		{
			announcementList = null;
		}
		else if(soapObject.getName() == "anyType")
		{
			List<SchoolAnnouncementBean> announcementBeanList = new ArrayList<SchoolAnnouncementBean>();
			// 遍历Web Service获得的集合
			for (int i = 0; i < soapObject.getPropertyCount(); i++) 
			{
				SchoolAnnouncementBean schoolAnnouncementBean = new SchoolAnnouncementBean();
				// 获取单条的数据
				SoapObject soapChilds = (SoapObject) soapObject.getProperty(i);
				// 对单个的数据进行再次遍历，把它的每行数据读取出来
				schoolAnnouncementBean.setTitle(soapChilds.getProperty("title").toString());
				schoolAnnouncementBean.setReadCount(Integer.parseInt(soapChilds.getProperty("readCount").toString()));
				schoolAnnouncementBean.setTime(soapChilds.getPropertyAsString("time"));
				announcementBeanList.add(schoolAnnouncementBean);
			}

			announcementList = new ArrayList<Map<String,String>>();
			for(int i = 0; i < announcementBeanList.size(); i++)
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put(Constant.MAP_KEY_NEWS_KIND[0], announcementBeanList.get(i).getTitle());
				map.put(Constant.MAP_KEY_NEWS_KIND[1], "阅读人数:" + announcementBeanList.get(i).getReadCount());
				map.put(Constant.MAP_KEY_NEWS_KIND[2], announcementBeanList.get(i).getTime());
			
				announcementList.add(map);
			}
		}
		
		SchoolAnnouncementFragment.isDownLoadFinish = true;
		return announcementList;
	}
}