package com.project.webServices.newsServices;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.project.Util.Constant;

public class GetNewsArticle implements Callable<String>
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_NEWS;

	private String method;
	private int newsPosition;
	
	public GetNewsArticle(String method, int newsPosition)
	{
		this.method = method;
		this.newsPosition = newsPosition;
	}
	
	@Override
	public String call() throws IOException, XmlPullParserException
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, method);
		request.addProperty("position", newsPosition);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
		
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		String article = "";

		ht.call(Constant.NAMESPACE + method, envelope);
		Object ob = envelope.getResponse();
		if(ob != null)
			article = ob.toString();

		return article;
	}

}
