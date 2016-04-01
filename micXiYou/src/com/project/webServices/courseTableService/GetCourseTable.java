package com.project.webServices.courseTableService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.UI.home.courseTable.CourseTableFragment;
import com.project.Util.Constant;
import com.project.bean.courseTable.CourseBean;

public class GetCourseTable implements Callable<List<CourseBean>>
{
	private final String WSDL = Constant.WSDL_WITHOUT_SERVICE_NAME + Constant.SERVICE_COURSE_TABLE; 
	
	private String year;
	private String professionalId;
	private String className;
	
	public GetCourseTable(String year, String professionalId, String className)
	{
		this.year = year;
		this.professionalId = professionalId;
		this.className = className;
	}

	@Override
	public List<CourseBean> call() throws IOException, XmlPullParserException
	{
		SoapObject request = new SoapObject(Constant.NAMESPACE, Constant.METHOD_GET_COURSE_TABLE_BY_CLASS);
		request.addProperty("admissionYear", year);
		request.addProperty("professional", professionalId);
		request.addProperty("_class", className);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.encodingStyle = "UTF-8";
		
		HttpTransportSE ht = new HttpTransportSE(WSDL, Constant.HTTP_REQUEST_TIME_OUT);
		ht.debug = true;
		
		CourseTableFragment.isDownLoadFinish = false;
		List<CourseBean> tempBeanList = new ArrayList<CourseBean>();
		List<CourseBean> beanList = new ArrayList<CourseBean>();
		
		ht.call(Constant.NAMESPACE + Constant.METHOD_GET_COURSE_TABLE_BY_CLASS , envelope);
		SoapObject soapObject = (SoapObject) envelope.getResponse();

		// 如果获取的是个集合，就对它进行下面的操作
		if (soapObject.getName() == "anyType")
		{
			// 遍历Web Service获得的集合
			for (int i = 0; i < soapObject.getPropertyCount(); i++) 
			{
				CourseBean courseBean = new CourseBean();
				// 获取单条的数据
				SoapObject soapChilds = (SoapObject) soapObject.getProperty(i);
				// 对单个的数据进行再次遍历，把它的每行数据读取出来
				courseBean.setDayOfWeek(soapChilds.getPropertyAsString("dayOfWeek"));
				courseBean.setClassOfDay(soapChilds.getPropertyAsString("classOfDay"));
				courseBean.setBuilding(soapChilds.getPropertyAsString("building"));
				courseBean.setClassRoom(soapChilds.getPropertyAsString("classRoom"));
				courseBean.setTeacher(soapChilds.getPropertyAsString("teacher"));
				courseBean.setCourse(soapChilds.getPropertyAsString("course"));
				courseBean.setIsDoubleWeek(soapChilds.getPropertyAsString("isDoubleWeek"));
			
				tempBeanList.add(courseBean);
			}
			
			beanList = sortCourseBeanList(tempBeanList);
		}
		
		return beanList;
	}

	// (周数 - 1) * 5 + 第几节课   - 1	= CourseBean 的下标
	private List<CourseBean> sortCourseBeanList(List<CourseBean> tempBeanList)
	{
		CourseBean[] bean = new CourseBean[20];
		char flag[] = new char[20];
		for(int i = 0; i < 20; i++)
		{
			bean[i] = new CourseBean();
			flag[i] = '0';
		}
		
		for(int i = 0; i < tempBeanList.size(); i++)
		{
			int index = (Integer.parseInt(tempBeanList.get(i).getDayOfWeek()) - 1 )*5 
					+ (Integer.parseInt(tempBeanList.get(i).getClassOfDay())) - 1;
			
			if(flag[i] == '0')
			{
				if(tempBeanList.get(i).getIsDoubleWeek().equals("0"))
					bean[index] = tempBeanList.get(i);
				else if(tempBeanList.get(i).getIsDoubleWeek().equals("1"))
				{
					bean[index] = tempBeanList.get(i);
					bean[index].setCourse(":" + tempBeanList.get(i).getCourse());
					bean[index].setTeacher(":" + tempBeanList.get(i).getTeacher());
					bean[index].setBuilding(":" + tempBeanList.get(i).getBuilding());
					bean[index].setClassRoom(":" + tempBeanList.get(i).getClassRoom());
				}
				flag[i] = '1';
			}
			else if(flag[i] == '1')
			{
				if(tempBeanList.get(i).getIsDoubleWeek().equals("0"))
					bean[index].setCourse(tempBeanList.get(i).getCourse() + ":" + bean[index].getCourse());
				else if(tempBeanList.get(i).getIsDoubleWeek().equals("1"))
				{
					bean[index].setCourse(bean[index].getCourse() + ":" + tempBeanList.get(i).getCourse());
					bean[index].setTeacher(bean[index].getTeacher() + ":" + tempBeanList.get(i).getTeacher());
					bean[index].setBuilding(bean[index].getBuilding() + ":" + tempBeanList.get(i).getBuilding());
					bean[index].setClassRoom(bean[index].getClassRoom() + ":" + tempBeanList.get(i).getClassRoom());
				}
				flag[i] = '2';
			}
		}
		
		List<CourseBean> beanList = new ArrayList<CourseBean>();
		for(int i = 0; i < 20; i++)
			beanList.add(bean[i]);
		
		return beanList;
	}
}