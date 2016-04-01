package com.project.webServices.restaurantService;

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

import com.UI.micxiyou.xyFood.RestaurantFragment;
import com.project.Util.Constant;

public class GetRestaurantByLocation implements Callable<List<Map<String,String>>>
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_MEAL_SERVICE;
	private String isInXRY;
	
	public GetRestaurantByLocation(String isInXRY)
	{
		this.isInXRY = isInXRY;
	}

	@Override
	public List<Map<String,String>> call() throws IOException, XmlPullParserException
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_RESTAURANT_BY_LOCATION);
		request.addProperty("isInXRY", isInXRY);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
		
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
		RestaurantFragment.isDownLoadFinish = false;

		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_RESTAURANT_BY_LOCATION , envelope);
		SoapObject soapObject = (SoapObject) envelope.getResponse();
		
		if(soapObject == null)
		{
			mapList = null;
		}
		else if(soapObject.getName() == "anyType")
		{
			for (int i = 0; i < soapObject.getPropertyCount(); i++) 
			{
				SoapObject soapChilds = (SoapObject) soapObject.getProperty(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put(Constant.MAP_KEY_RESTAURANT[0], 
						soapChilds.getPropertyAsString("id") + " " + soapChilds.getPropertyAsString("name"));
				map.put(Constant.MAP_KEY_RESTAURANT[1], "¶©²Íµç»°£º" + soapChilds.getPropertyAsString("telephoneNumber"));
				
				mapList.add(map);
			}
		}
	
		RestaurantFragment.isDownLoadFinish = true;
		
		return mapList;
	}
	
	
}
