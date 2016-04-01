package com.project.webServices.classRoomServices;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.UI.home.studyRoom.StudyRoomFragment;
import com.project.Util.Constant;

public class GetClassRoomVersion implements Callable<String>
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_CLASS_ROOM;

	public GetClassRoomVersion()
	{}
	
	@Override
	public String call() throws IOException, XmlPullParserException
	{
		String ver = "";
		
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_NEW_CLASS_ROOM_VERSION);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";

		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;

		StudyRoomFragment.isDownLoadFinish = false;
	
		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_NEW_CLASS_ROOM_VERSION , envelope);
		String count = envelope.getResponse().toString();
		
		if(count != null)
			ver = count;
		
		StudyRoomFragment.isDownLoadFinish = true;

		return ver;
	}
}
