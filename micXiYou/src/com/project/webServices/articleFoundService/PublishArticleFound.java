package com.project.webServices.articleFoundService;

import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.project.Util.Constant;
import com.project.bean.articleFound.ArticleFoundBean;

public class PublishArticleFound implements Callable<Integer> 
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_ARTICLE_FOUND;
	
	private ArticleFoundBean bean = new ArticleFoundBean();
	
	public PublishArticleFound(ArticleFoundBean bean)
	{
		this.bean = bean;
	}
	
	@Override
	public Integer call() throws Exception 
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_PUBLISH_ARTICLE_FOUND);
//		request.addProperty("bean", bean);
		
/*		Log.i("tag", "info = " + bean.getArticle() + "		kind=#" + bean.getKind() + "#		publisher=#"
				+ bean.getPublisher() + "#		" + bean.getPublisherEmail() + "		state=#" + bean.getState()
				+ "#		" + bean.getTelephoneNumber() + "		" + bean.getTime() 
				+ "		"  + bean.getTitle());
*/		
		request.addProperty("title", bean.getTitle());
		request.addProperty("publisher", bean.getPublisher());
		request.addProperty("time", bean.getTime());
		request.addProperty("state", bean.getState());
		request.addProperty("kind", bean.getKind());
		request.addProperty("article", bean.getArticle());
		request.addProperty("telephoneNumber", bean.getTelephoneNumber());
		request.addProperty("publisherEmail", bean.getPublisherEmail());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
//		envelope.addMapping("http://www.zby.com/ArticleFoundBeanNameSpace", "ArticleFoundBean", ArticleFoundBean.class);
	
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		ht.call(Constant.NAMESPACE + Constant.METHOD_PUBLISH_ARTICLE_FOUND , envelope);
		
		int newId = -1;
		if(envelope.getResponse() != null)
			newId = Integer.parseInt(envelope.getResponse().toString());

		return newId;
	}
}
