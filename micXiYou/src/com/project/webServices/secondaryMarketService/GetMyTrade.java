package com.project.webServices.secondaryMarketService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.UI.home.secondaryMarket.MySecondaryMarketTradeFragment;
import com.project.Util.Constant;

public class GetMyTrade implements Callable<List<Map<String,String>>>
{
	private int haveGotScdMktCount;
	private String registedEmail;
	private int lineSize = 14;
	
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_SECONDARY_MARKET;

	private List<Map<String,String>> tradeList;
	
	public GetMyTrade(int haveGotScdMktCount, String registedEmail)
	{
		this.haveGotScdMktCount = haveGotScdMktCount;
		this.registedEmail = registedEmail;
	}

	@Override
	public List<Map<String, String>> call() throws Exception 
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_MY_SECONDARY_MARKET_PUBLISHED_TITLE);
		request.addProperty("haveGotScdMktCount", haveGotScdMktCount);
		request.addProperty("lineSize", lineSize);
		request.addProperty("registedEmail", registedEmail);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";

		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		MySecondaryMarketTradeFragment.isDownLoadFinish = false;
		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_MY_SECONDARY_MARKET_PUBLISHED_TITLE , envelope);
		SoapObject soapObject = (SoapObject) envelope.getResponse();

		if(soapObject == null)
		{
			tradeList = null;
		}
		else if (soapObject.getName() == "anyType")
		{
			tradeList = new ArrayList<Map<String,String>>();
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
					value = "未买到";
				else if(state.equals("2"))
					value = "已买到";
				else if(state.equals("3"))
					value = "未售出";
				else if(state.equals("4"))
					value = "已售出";
					
				map.put(Constant.MAP_KEY_PUBLISH_KIND[3], value);
				
				tradeList.add(map);
			}
		}
		MySecondaryMarketTradeFragment.isDownLoadFinish = true;
		
		return tradeList;
	}

}
