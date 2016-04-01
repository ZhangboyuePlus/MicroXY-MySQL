package com.xFire.bean.classRoom;

import java.io.Serializable;

public class ClassRoomBean implements Serializable
{
	private static final long serialVersionUID = 8721051568504252173L;
	
	private String isDoubleWeek;
	private String building;
	private String classRoom;
	private String dayOfWeek;
	private String classOfDay;
	
	public String getIsDoubleWeek() 
	{
		return isDoubleWeek;
	}
	
	public void setIsDoubleWeek(String isDoubleWeek) 
	{
		this.isDoubleWeek = isDoubleWeek;
	}
	public String getBuilding() 
	{
		return building;
	}
	
	public void setBuilding(String string) 
	{
		this.building = string;
	}
	
	public String getClassRoom() 
	{
		return classRoom;
	}
	
	public void setClassRoom(String classRoom) 
	{
		this.classRoom = classRoom;
	}
	
	public String getDayOfWeek() 
	{
		return dayOfWeek;
	}
	
	public void setDayOfWeek(String dayOfWeek) 
	{
		this.dayOfWeek = dayOfWeek;
	}
	
	public String getClassOfDay() 
	{
		return classOfDay;
	}
	
	public void setClassOfDay(String classOfDay) 
	{
		this.classOfDay = classOfDay;
	}
}