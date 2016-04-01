package com.project.webServices.articleFoundService;

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

import com.UI.home.ArticlesFound.ArticlesFoundFragment;
import com.project.Util.Constant;

public class GetArticleFoundTitle implements Callable<List<Map<String, String>>>
{
	private int haveGotAtcFdCount;
	private int lineSize = 14;
	
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_ARTICLE_FOUND; 
	
	private List<Map<String,String>> articleFoundList;
	
	public GetArticleFoundTitle(int haveGotAtcFdCount)
	{
		this.haveGotAtcFdCount = haveGotAtcFdCount;
	}
	
	@Override
	public List<Map<String, String>> call() throws IOException, XmlPullParserException
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_ARTICLE_FOUND_TITLE);
		request.addProperty("haveGotAtcFdCount", haveGotAtcFdCount);
		request.addProperty("lineSize", lineSize);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";

		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		ArticlesFoundFragment.isDownLoadFinish = false;
		
		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_ARTICLE_FOUND_TITLE , envelope);
		SoapObject soapObject = (SoapObject) envelope.getResponse();

		if(soapObject == null)
		{
			articleFoundList = null;
		}
		else if (soapObject.getName() == "anyType")
		{
			articleFoundList = new ArrayList<Map<String,String>>();
			for (int i = 0; i < soapObject.getPropertyCount(); i++) 
			{
				// 获取单条的数据
				SoapObject soapChilds = (SoapObject) soapObject.getProperty(i);
				// 对单个的数据进行再次遍历，把它的每行数据读取出来
				Map<String, String> map = new HashMap<String, String>();
				map.put(Constant.MAP_KEY_PUBLISH_KIND[0], soapChilds.getProperty("title").toString());
				map.put(Constant.MAP_KEY_PUBLISH_KIND[1], soapChilds.getPropertyAsString("publisher"));
				map.put(Constant.MAP_KEY_PUBLISH_KIND[2], soapChilds.getPropertyAsString("time"));
				map.put(Constant.MAP_KEY_PUBLISH_KIND[4], soapChilds.getPropertyAsString("id"));
				//state状态： ”1“代表 未认领		”2“代表已认领		”3“代表未找到		”4“代表已找到
				String state = soapChilds.getPropertyAsString("state");
				String value = "";
				if(state.equals("1"))
					value = "未认领";
				else if(state.equals("2"))
					value = "已认领";
				else if(state.equals("3"))
					value = "未找到";
				else if(state.equals("4"))
					value = "已找到";
					
				map.put(Constant.MAP_KEY_PUBLISH_KIND[3], value);
				
				articleFoundList.add(map);
			}
		}
		
		ArticlesFoundFragment.isDownLoadFinish = true;
		
		return articleFoundList;
	}

}
