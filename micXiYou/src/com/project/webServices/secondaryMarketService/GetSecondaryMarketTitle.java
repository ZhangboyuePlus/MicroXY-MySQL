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

import com.UI.home.secondaryMarket.SecondaryMarketFragment;
import com.project.Util.Constant;

public class GetSecondaryMarketTitle implements Callable<List<Map<String, String>>>
{
	private int haveGotScdMktCount;
	private int lineSize = 14;
	
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_SECONDARY_MARKET;
	
	private List<Map<String,String>> secondaryMarketList;
	
	public GetSecondaryMarketTitle(int haveGotScdMktCount)
	{
		this.haveGotScdMktCount = haveGotScdMktCount;
	}

	@Override
	public List<Map<String, String>> call() throws Exception
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_SECONDARY_MARKET_TITLE);
		request.addProperty("haveGotScdMktCount", haveGotScdMktCount);
		request.addProperty("lineSize", lineSize);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";

		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		SecondaryMarketFragment.isDownLoadFinish = false;
		
		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_SECONDARY_MARKET_TITLE , envelope);
		SoapObject soapObject = (SoapObject) envelope.getResponse();

		if(soapObject == null)
		{
			secondaryMarketList = null;
		}
		else if (soapObject.getName() == "anyType")
		{
			secondaryMarketList = new ArrayList<Map<String,String>>();
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
					value = "δ��";
				else if(state.equals("2"))
					value = "����";
				else if(state.equals("3"))
					value = "δ����";
				else if(state.equals("4"))
					value = "�ѳ���";
					
				map.put(Constant.MAP_KEY_PUBLISH_KIND[3], value);
				
				secondaryMarketList.add(map);
			}
		}
		
		SecondaryMarketFragment.isDownLoadFinish = true;
		
		return secondaryMarketList;
	}

}
