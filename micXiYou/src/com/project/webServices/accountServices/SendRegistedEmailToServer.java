package com.project.webServices.accountServices;

import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.project.Util.Constant;

public class SendRegistedEmailToServer implements Callable<Boolean> 
{
	private String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_ACCOUNT;
	
	private String Email;
	
	public SendRegistedEmailToServer(String Email)
	{
		this.Email = Email;
	}
	
	@Override
	public Boolean call() throws Exception
	{
		boolean isSuccess = false;
		
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_SEND_PASSWORD_TO_REGISTED_EMAIL);
		request.addProperty("registedEmail", Email);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
		
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		ht.call(Constant.NAMESPACE + Constant.METHOD_SEND_PASSWORD_TO_REGISTED_EMAIL , envelope);
		
		String str = envelope.getResponse().toString();
		if(str != null && str.length() > 0)
			isSuccess = Boolean.parseBoolean(str);
		
		return isSuccess;
	}
}
