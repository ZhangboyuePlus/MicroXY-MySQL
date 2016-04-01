package com.project.webServices.recuitmentServices;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.UI.home.recuitment.RecuitmentFragment;
import com.project.Util.Constant;

public class GetRecuitmentArticleByPosition implements Callable<String>
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_RECUITMENT;
	private int position = 0;
	
	public GetRecuitmentArticleByPosition(int position)
	{
		this.position = position;
	}

	@Override
	public String call() throws IOException, XmlPullParserException
	{
		String content = "";
		
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_RECUITMENT_CONTENT_BY_POSITION);
		request.addProperty("position", position);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";

		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		RecuitmentFragment.isDownLoadFinish = false;

		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_RECUITMENT_CONTENT_BY_POSITION , envelope);
		content = envelope.getResponse().toString();
		
		RecuitmentFragment.isDownLoadFinish = true;
		
		return content;
	}
}
