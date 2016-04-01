package com.project.database;

import java.util.ArrayList;
import java.util.List;

import com.project.Util.Constant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBClassRoomOperator 
{
	private SQLiteDatabase db  =null;
	
	public DBClassRoomOperator(Context context)
	{
		DBHelper helper = new DBHelper(context, Constant.NAME_DB, Constant.BDVersion);
		db = helper.getWritableDatabase();
	}
	
	public void insertClassRoom(String isDoubleWeek, String building, String classRoom, String dayOfWeek, String classOfDay)
	{
		String SQLString = "INSERT INTPO " + Constant.TABLE_NAME_STUDY_ROOM + 
						   " (isDoubleWeek, building, classRoom, dayOfWeek, classOfDay) VALUES " + 
						   "(\'" + isDoubleWeek + "\', \'" + building + "\', \'" + classRoom + "\', \'" +
						   dayOfWeek + "\', \'" + classOfDay + "\')";
		db.execSQL(SQLString);
		db.close();
 	}
	
	public void insertClassRoomNoClose(String isDoubleWeek, String building, String classRoom, String dayOfWeek, String classOfDay)
	{
		String SQLString = "INSERT INTO " + Constant.TABLE_NAME_STUDY_ROOM + 
						   " (isDoubleWeek, building, classRoom, dayOfWeek, classOfDay) VALUES " + 
						   "(\'" + isDoubleWeek + "\', \'" + building + "\', \'" + classRoom + "\', \'" +
						   dayOfWeek + "\', \'" + classOfDay + "\')";
		db.execSQL(SQLString);
 	}
	
	public void closeDB()
	{
		db.close();
	}
	
	public List<String> selectClassRoom(String isDoubleWeek, String building, String dayOfWeek, String classOfDay)
	{
		List<String> beanList = new ArrayList<String>();
		String classRoom = "";
		String SQLString = "SELECT classRoom FROM " + Constant.TABLE_NAME_STUDY_ROOM + " WHERE isDoubleWeek = " + 
							"\'" + isDoubleWeek + "\' AND dayOfWeek = \'" + dayOfWeek + "\' AND classOfDay = \'"
							+ classOfDay + "\'¡¡AND building = \'" + building + "\' ORDER BY classRoom ASC";
		
		Cursor cursor = db.rawQuery(SQLString, null);
		for(cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext())
		{
			classRoom = cursor.getString(0);
			beanList.add(classRoom);
		}
		db.close();
		
		return beanList;
	}
	
	public List<String> selectClassRoomNoClose(String isDoubleWeek, String building, String dayOfWeek, String classOfDay)
	{
		List<String> beanList = new ArrayList<String>();
		String classRoom = "";
		String SQLString = "SELECT classRoom FROM " + Constant.TABLE_NAME_STUDY_ROOM + " WHERE isDoubleWeek = \'" 
							+ isDoubleWeek + "\' AND dayOfWeek = \'" + dayOfWeek + "\' AND classOfDay = \'"
							+ classOfDay + "\' AND building = \'" + building + "\' ORDER BY classRoom ASC";
		
		Cursor cursor = db.rawQuery(SQLString, null);
		for(cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext())
		{
			classRoom = cursor.getString(0);
			beanList.add(classRoom);
		}
		
		return beanList;
	}

}
