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

import android.util.Log;

import com.UI.micxiyou.news.SchoolNewsFragment;
import com.project.Util.Constant;
import com.project.bean.news.SchoolNewsBean;

public class GetNews implements Callable<List<Map<String,String>>>
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_NEWS;

	private int haveGotNewsCount = 0;
	private int lineSize = 14;
	
	private List<Map<String,String>> newsList = null;
	
	public GetNews(int haveGotNewsCount)
	{
		this.haveGotNewsCount = haveGotNewsCount;
	}
	
	@Override
	public List<Map<String, String>> call() throws IOException, XmlPullParserException
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_SCHOOL_NEWS);
		request.addProperty("haveGotNewsCount", haveGotNewsCount);
		request.addProperty("lineSize", lineSize);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
		
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		SchoolNewsFragment.isDownLoadFinish = false;

		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_SCHOOL_NEWS , envelope);
		SoapObject soapObject = (SoapObject) envelope.getResponse();
		
		if(soapObject == null)
		{
			newsList = null;
		}
		else if(soapObject.getName() == "anyType") // 如果获取的是个集合，就对它进行下面的操作
		{
			List<SchoolNewsBean> newsBeanList = new ArrayList<SchoolNewsBean>();
			// 遍历Web Service获得的集合
			for (int i = 0; i < soapObject.getPropertyCount(); i++) 
			{
				SchoolNewsBean schoolNewsBean = new SchoolNewsBean();
				// 获取单条的数据
				SoapObject soapChilds = (SoapObject) soapObject.getProperty(i);
				// 对单个的数据进行再次遍历，把它的每行数据读取出来
				schoolNewsBean.setTitle(soapChilds.getProperty("title").toString());
				schoolNewsBean.setReadCount(Integer.parseInt(soapChilds.getProperty("readCount").toString()));
				schoolNewsBean.setTime(soapChilds.getPropertyAsString("time"));
				
				newsBeanList.add(schoolNewsBean);
			}

			newsList = new ArrayList<Map<String,String>>();
			for(int i = 0; i < newsBeanList.size(); i++)
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put(Constant.MAP_KEY_NEWS_KIND[0], newsBeanList.get(i).getTitle());
				map.put(Constant.MAP_KEY_NEWS_KIND[1], "阅读人数:" + newsBeanList.get(i).getReadCount());
				map.put(Constant.MAP_KEY_NEWS_KIND[2], newsBeanList.get(i).getTime());
			
				newsList.add(map);
			}
		}
		SchoolNewsFragment.isDownLoadFinish = true;
		
		return newsList;
	}
}