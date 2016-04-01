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

import com.UI.micxiyou.xyFood.MealFragment;
import com.project.Util.Constant;

public class GetMealByRestaurantId implements Callable<List<Map<String, String>>> 
{
	private String id = "";
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_MEAL_SERVICE;
	
	public GetMealByRestaurantId(String id)
	{
		this.id = id;
	}

	@Override
	public List<Map<String, String>> call() throws IOException, XmlPullParserException
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_MEAL_BY_RESTAURANT_ID);
		request.addProperty("id", id);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
		
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		MealFragment.isDownLoadFinish = false;

		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
		
		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_MEAL_BY_RESTAURANT_ID , envelope);
		SoapObject soapObject = (SoapObject) envelope.getResponse();
		
		if(soapObject == null)
		{
			newList = null;
		}
		else if(soapObject.getName() == "anyType")
		{
			for (int i = 0; i < soapObject.getPropertyCount(); i++) 
			{
				SoapObject soapChilds = (SoapObject) soapObject.getProperty(i);
				Map<String, String> map = new HashMap<String, String>();
				
				map.put(Constant.MAP_KEY_MEAL[0], soapChilds.getPropertyAsString("name"));
				map.put(Constant.MAP_KEY_MEAL[1], soapChilds.getPropertyAsString("price"));
				
				newList.add(map);
			}
		}

		MealFragment.isDownLoadFinish = true;
		
		return newList;
	}
}
