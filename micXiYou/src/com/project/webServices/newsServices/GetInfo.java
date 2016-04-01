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

import com.UI.micxiyou.news.SchoolInfoFragment;
import com.UI.micxiyou.news.SchoolNewsFragment;
import com.project.Util.Constant;
import com.project.bean.news.SchoolInfoBean;

public class GetInfo implements Callable<List<Map<String,String>>>
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_NEWS;

	private int haveGotInfoCount = 0;
	private int lineSize = 14;
	
	private List<Map<String,String>> infoList = null;
	
	public GetInfo(int haveGotInfoCount)
	{
		this.haveGotInfoCount = haveGotInfoCount;
	}
	
	@Override
	public List<Map<String, String>> call() throws IOException, XmlPullParserException
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_SCHOOL_INFO);
		request.addProperty("haveGotInfoCount", haveGotInfoCount);
		request.addProperty("lineSize", lineSize);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
		
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		SchoolInfoFragment.isDownLoadFinish = false;

		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_SCHOOL_INFO , envelope);
		SoapObject soapObject = (SoapObject) envelope.getResponse();
		
		if(soapObject == null)
		{
			infoList = null;
		}
		else if (soapObject.getName() == "anyType")
		{
			List<SchoolInfoBean> infoBeanList = new ArrayList<SchoolInfoBean>();
			// 遍历Web Service获得的集合
			for (int i = 0; i < soapObject.getPropertyCount(); i++) 
			{
				SchoolInfoBean schoolInfoBean = new SchoolInfoBean();
				// 获取单条的数据
				SoapObject soapChilds = (SoapObject) soapObject.getProperty(i);
				// 对单个的数据进行再次遍历，把它的每行数据读取出来
				schoolInfoBean.setTitle(soapChilds.getProperty("title").toString());
				schoolInfoBean.setReadCount(Integer.parseInt(soapChilds.getPropertyAsString("readCount")));
				schoolInfoBean.setTime(soapChilds.getPropertyAsString("time"));
				
				infoBeanList.add(schoolInfoBean);
			}

			infoList = new ArrayList<Map<String,String>>();
			for(int i = 0; i < infoBeanList.size(); i++)
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put(Constant.MAP_KEY_NEWS_KIND[0], infoBeanList.get(i).getTitle());
				map.put(Constant.MAP_KEY_NEWS_KIND[1], "阅读人数:" + infoBeanList.get(i).getReadCount());
				map.put(Constant.MAP_KEY_NEWS_KIND[2], infoBeanList.get(i).getTime());
			
				infoList.add(map);
			}
		}
		SchoolInfoFragment.isDownLoadFinish = true;
		
		return infoList;
	}
}