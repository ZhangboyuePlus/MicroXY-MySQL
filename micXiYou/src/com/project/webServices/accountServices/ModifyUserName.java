package com.project.webServices.accountServices;

import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.project.Util.Constant;

public class ModifyUserName implements Callable<Boolean> 
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_ACCOUNT; 
	
	private String Email;
	private String newUserName;
	
	public ModifyUserName(String Email, String newUserName)
	{
		this.Email = Email;
		this.newUserName = newUserName;
	}
	
	@Override
	public Boolean call() throws Exception 
	{
		boolean ok = false;
		
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_MODIFY_USER_NAME);
		request.addProperty("Email", Email);
		request.addProperty("newUserName", newUserName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
		
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		ht.call(Constant.NAMESPACE + Constant.METHOD_MODIFY_USER_NAME , envelope);
		
		String str = envelope.getResponse().toString();
		if(str != null && str.length() > 0)
			ok = Boolean.parseBoolean(str);

		return ok;
	}
}
