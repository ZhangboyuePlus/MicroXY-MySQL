package com.project.webServices.LoginOrRegisterServices;

import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.project.Util.Constant;

public class RegisterNewAccount implements Callable<String>
{
	private String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_LOGIN_OR_REGISTER;
	
	private String userName;
	private String Email;
	private String passWord;
	
	public RegisterNewAccount(String userName, String Email, String passWord)
	{
		this.userName = userName;
		this.Email = Email;
		this.passWord = passWord;
	}
	
	@Override
	public String call() throws Exception
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_REGISTER_NEW_ACCOUNT);
		request.addProperty("userName", userName);
		request.addProperty("Email", Email);
		request.addProperty("passWord", passWord);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
		
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		ht.call(Constant.NAMESPACE + Constant.METHOD_REGISTER_NEW_ACCOUNT , envelope);
		
		String str = envelope.getResponse().toString();

		return str;
	}
}
