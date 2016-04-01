package com.xFire.service.impl.courseTableService;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.xFire.bean.courseTable.CourseBean;
import com.xFire.service.courseTableService.CourseTableService;
import com.xFire.util.DButil;

public class CourseTableServiceImpl implements CourseTableService, Serializable
{
	private static final long serialVersionUID = 7531883970831276504L;
	private final String TABLE_NAME = "courseTable"; 
	
	@Override
	public List<CourseBean> getCourseTableByClass(String admissionYear, String professional, String _class )
	{
		List<CourseBean> list = new ArrayList<CourseBean>();

		Connection conn = null;
		Statement statement = null;
		ResultSet rs  =null;
		
		String SQLString = "SELECT course, dayOfWeek, classOfDay, building, classRoom, teacher, isDoubleWeek FROM " 
				+ TABLE_NAME + " WHERE admissionYear = " + "\'" + admissionYear + "\'" + " AND professional = " + "\'" + 
				professional + "\'" + " AND class = " + "\'" + _class + "\'";
	
		conn = DButil.connection();
		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			
			while(rs.next())
			{
				CourseBean bean = new CourseBean();
				bean.setCourse(rs.getString("course"));
				bean.setDayOfWeek(rs.getString("dayOfWeek"));
				bean.setClassOfDay(rs.getString("classOfDay"));
				bean.setBuilding(rs.getString("building"));
				bean.setClassRoom(rs.getString("classRoom"));
				bean.setTeacher(rs.getString("teacher"));
				bean.setIsDoubleWeek(rs.getString("isDoubleWeek"));

				list.add(bean);
			}
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return list;
	}
}
