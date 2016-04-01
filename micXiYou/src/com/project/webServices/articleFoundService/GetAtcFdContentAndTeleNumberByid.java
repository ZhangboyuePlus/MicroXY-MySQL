package com.project.webServices.articleFoundService;

import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.UI.home.ArticlesFound.ArticleFoundContentFragment;
import com.project.Util.Constant;
import com.project.bean.articleFound.ArticleFoundBean;

public class GetAtcFdContentAndTeleNumberByid implements Callable<ArticleFoundBean> 
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_ARTICLE_FOUND;
	private int id;
	
	public GetAtcFdContentAndTeleNumberByid(int id)
	{
		this.id = id;
	}
	
	@Override
	public ArticleFoundBean call() throws Exception 
	{
		ArticleFoundBean bean = new ArticleFoundBean();
		
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_ATCFD_CONTENT_AND_TELENUMBER_BY_ID);
		request.addProperty("id", id);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";

		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		ArticleFoundContentFragment.isDownLoadFinish = false;

		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_ATCFD_CONTENT_AND_TELENUMBER_BY_ID , envelope);
		
		SoapObject soapObject = (SoapObject) envelope.getResponse();
		
		if(soapObject.getName() == "anyType")
		{
			bean.setArticle(soapObject.getPropertyAsString("article"));
			bean.setTelephoneNumber("联系电话:" + soapObject.getPropertyAsString("telephoneNumber"));
		}
		ArticleFoundContentFragment.isDownLoadFinish = true;
		
		return bean;
		
	}
}
