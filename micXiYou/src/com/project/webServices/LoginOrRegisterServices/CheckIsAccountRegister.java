package com.project.webServices.LoginOrRegisterServices;

import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.project.Util.Constant;

public class CheckIsAccountRegister implements Callable<String>
{
	private String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_LOGIN_OR_REGISTER;
	
	private String Email;
	private String passWord;
	
	public CheckIsAccountRegister(String Email, String passWord)
	{
		this.Email = Email;
		this.passWord = passWord;
	}
	
	@Override
	public String call() throws Exception
	{
		String info = "";
		
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_IS_ACCOUNT_REGISTERED);
		request.addProperty("Email", Email);
		request.addProperty("passWord", passWord);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
		
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		ht.call(Constant.NAMESPACE + Constant.METHOD_IS_ACCOUNT_REGISTERED , envelope);
		
		if(envelope.getResponse() != null)
			info = envelope.getResponse().toString();
		
		return info;
	}
	
}
