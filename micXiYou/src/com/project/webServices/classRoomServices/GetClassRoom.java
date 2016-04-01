package com.project.webServices.classRoomServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.UI.home.studyRoom.StudyRoomFragment;
import com.project.Util.Constant;
import com.project.bean.classRoom.ClassRoomBean;

public class GetClassRoom implements Callable<List<ClassRoomBean>> 
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_CLASS_ROOM;
	
	public GetClassRoom()
	{}
	
	@Override
	public List<ClassRoomBean> call() throws IOException, XmlPullParserException
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_CLASS_ROOM);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
		
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		StudyRoomFragment.isDownLoadFinish = false;
		List<ClassRoomBean> newsBeanList = new ArrayList<ClassRoomBean>();

		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_CLASS_ROOM , envelope);
		SoapObject soapObject = (SoapObject) envelope.getResponse();
		// 如果获取的是个集合，就对它进行下面的操作
		if (soapObject.getName() == "anyType")
		{
			// 遍历Web Service获得的集合
			for (int i = 0; i < soapObject.getPropertyCount(); i++) 
			{
				ClassRoomBean classRoomBean = new ClassRoomBean();
				// 获取单条的数据
				SoapObject soapChilds = (SoapObject) soapObject.getProperty(i);
				// 对单个的数据进行再次遍历，把它的每行数据读取出来
				classRoomBean.setDayOfWeek(soapChilds.getProperty("dayOfWeek").toString());
				classRoomBean.setClassOfDay(soapChilds.getProperty("classOfDay").toString());
				classRoomBean.setBuilding(soapChilds.getProperty("building").toString());
				classRoomBean.setClassRoom(soapChilds.getProperty("classRoom").toString());
				classRoomBean.setIsDoubleWeek(soapChilds.getProperty("isDoubleWeek").toString());
				newsBeanList.add(classRoomBean);
			}
		}
		
		return newsBeanList;
	}
}
