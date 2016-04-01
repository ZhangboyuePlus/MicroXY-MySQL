package com.project.webServices.articleFoundService;

import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.project.Util.Constant;

public class DealThisPublishedById implements Callable<Boolean>
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_ARTICLE_FOUND;
	
	private int id;
	private String stateNum;
	
	public DealThisPublishedById(int id, String stateNum)
	{
		this.id = id;
		this.stateNum = stateNum;
	}

	@Override
	public Boolean call() throws Exception 
	{
		boolean isDealSuccess = false;
		
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_DEAL_THIS_PUBLISHED_BY_ID);
		request.addProperty("id", id);
		request.addProperty("stateNum", stateNum);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";

		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		ht.call(Constant.NAMESPACE + Constant.METHOD_DEAL_THIS_PUBLISHED_BY_ID , envelope);
		
		Object object =  envelope.getResponse();
		if(object != null)
			isDealSuccess = Boolean.parseBoolean(object.toString());
		
		return isDealSuccess;
		
	}
}
