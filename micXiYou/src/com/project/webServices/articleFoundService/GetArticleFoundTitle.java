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
				// ��ȡ����������
				SoapObject soapChilds = (SoapObject) soapObject.getProperty(i);
				// �Ե��������ݽ����ٴα�����������ÿ�����ݶ�ȡ����
				Map<String, String> map = new HashMap<String, String>();
				map.put(Constant.MAP_KEY_PUBLISH_KIND[0], soapChilds.getProperty("title").toString());
				map.put(Constant.MAP_KEY_PUBLISH_KIND[1], soapChilds.getPropertyAsString("publisher"));
				map.put(Constant.MAP_KEY_PUBLISH_KIND[2], soapChilds.getPropertyAsString("time"));
				map.put(Constant.MAP_KEY_PUBLISH_KIND[4], soapChilds.getPropertyAsString("id"));
				//state״̬�� ��1������ δ����		��2������������		��3������δ�ҵ�		��4���������ҵ�
				String state = soapChilds.getPropertyAsString("state");
				String value = "";
				if(state.equals("1"))
					value = "δ����";
				else if(state.equals("2"))
					value = "������";
				else if(state.equals("3"))
					value = "δ�ҵ�";
				else if(state.equals("4"))
					value = "���ҵ�";
					
				map.put(Constant.MAP_KEY_PUBLISH_KIND[3], value);
				
				articleFoundList.add(map);
			}
		}
		
		ArticlesFoundFragment.isDownLoadFinish = true;
		
		return articleFoundList;
	}

}
