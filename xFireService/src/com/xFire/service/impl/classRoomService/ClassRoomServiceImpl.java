package com.xFire.service.impl.classRoomService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.xFire.bean.classRoom.ClassRoomBean;
import com.xFire.service.classRoomService.ClassRoomService;
import com.xFire.util.DButil;

public class ClassRoomServiceImpl implements ClassRoomService
{
	private final String TABLE_NAME_CLASS_ROOM = "studyRoom";
	
	private final String TABLE_NAME_VERSION = "version";
	private final String FEILD_NAME_VERSION = "ver";
	private final String FEILD_NAME_OWNER_OF_VERSION = "ownerOfVersion";
	private final String RECORD_FEILD_NAME_STUDY_ROOM = "studyRoom";
	
	@Override
	public String getNewClassRoomVersion() 
	{
		String version = "";
		Connection conn = null;
		Statement statement = null;
		ResultSet rs  =null;
		
		String SQLString = "SELECT " + FEILD_NAME_VERSION + " FROM " + TABLE_NAME_VERSION + " WHERE " + 
				FEILD_NAME_OWNER_OF_VERSION	+ " = \'" + RECORD_FEILD_NAME_STUDY_ROOM + "\'";
		
		conn = DButil.connection();
		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			
			if(rs.next())
				version = rs.getString(FEILD_NAME_VERSION);
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return version;
	}

	@Override
	public List<ClassRoomBean> getClassRoom() 
	{
		List<ClassRoomBean> beanList = new ArrayList<ClassRoomBean>();
		
		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		
		String SQLString = "SELECT * FROM " + TABLE_NAME_CLASS_ROOM ;
		conn = DButil.connection();
		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			
			while(rs.next())
			{
				ClassRoomBean bean = new ClassRoomBean();
				bean.setIsDoubleWeek(rs.getString("isDoubleWeek"));
				bean.setBuilding(rs.getString("building"));
				bean.setClassRoom(rs.getString("classRoom"));
				bean.setDayOfWeek(rs.getString("dayOfWeek"));
				bean.setClassOfDay(rs.getString("classOfDay"));
				
				beanList.add(bean);
			}
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return beanList;
	}

}
