package com.project.webServices.secondaryMarketService;

import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.UI.home.secondaryMarket.SecondaryMarketContentFragment;
import com.project.Util.Constant;
import com.project.bean.secondaryMarket.SecondaryMarketBean;

public class GetScdMktContentAndTeleNumberByid implements Callable<SecondaryMarketBean> 
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_SECONDARY_MARKET;
	private int id;
	
	public GetScdMktContentAndTeleNumberByid(int id)
	{
		this.id = id;
	}

	@Override
	public SecondaryMarketBean call() throws Exception 
	{
		SecondaryMarketBean bean = new SecondaryMarketBean();
		
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_SCDMKT_CONTENT_AND_TELENUMBER_BY_ID);
		request.addProperty("id", id);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";

		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		SecondaryMarketContentFragment.isDownLoadFinish = false;

		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_SCDMKT_CONTENT_AND_TELENUMBER_BY_ID , envelope);
		
		SoapObject soapObject = (SoapObject) envelope.getResponse();
		
		if(soapObject.getName() == "anyType")
		{
			bean.setArticle(soapObject.getPropertyAsString("article"));
			bean.setTelephoneNumber("联系电话:" + soapObject.getPropertyAsString("telephoneNumber"));
		}
		SecondaryMarketContentFragment.isDownLoadFinish = true;
		
		return bean;
	}

}
