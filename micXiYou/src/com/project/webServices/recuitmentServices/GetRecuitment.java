package com.project.webServices.recuitmentServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.UI.home.recuitment.RecuitmentFragment;
import com.project.Util.Constant;
import com.project.bean.recuitment.RecuitmentBean;

public class GetRecuitment implements Callable<List<Map<String,String>>> 
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_RECUITMENT;

	private int haveGotNewsCount = 0;
	private int lineSize = 14;
	
	private List<Map<String,String>> recuitmentList;
	
	public GetRecuitment(int haveGotNewsCount)
	{
		this.haveGotNewsCount = haveGotNewsCount;
	}
	
	@Override
	public List<Map<String, String>> call() throws IOException, XmlPullParserException
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_RECUITMENT_TITLE);
		request.addProperty("haveGotRecuitmentCount", haveGotNewsCount);
		request.addProperty("lineSize", lineSize);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";

		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		RecuitmentFragment.isDownLoadFinish = false;
		
		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_RECUITMENT_TITLE , envelope);
		SoapObject soapObject = (SoapObject) envelope.getResponse();

		if(soapObject == null)
		{
			recuitmentList = null;
		}
		else if (soapObject.getName() == "anyType")
		{
			recuitmentList = new ArrayList<Map<String,String>>();
			for (int i = 0; i < soapObject.getPropertyCount(); i++) 
			{
				// 获取单条的数据
				SoapObject soapChilds = (SoapObject) soapObject.getProperty(i);
				// 对单个的数据进行再次遍历，把它的每行数据读取出来
				Map<String, String> map = new HashMap<String, String>();
				map.put(Constant.MAP_KEY_NEWS_KIND[0], soapChilds.getProperty("title").toString());
				map.put(Constant.MAP_KEY_NEWS_KIND[1], "阅读人数:" + soapChilds.getPropertyAsString("readCount"));
				map.put(Constant.MAP_KEY_NEWS_KIND[2], soapChilds.getPropertyAsString("time"));
				
				recuitmentList.add(map);
			}
		}
		
		RecuitmentFragment.isDownLoadFinish = true;
		
		return recuitmentList;
	}
}
