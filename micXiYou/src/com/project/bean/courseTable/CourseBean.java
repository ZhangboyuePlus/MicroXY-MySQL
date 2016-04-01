package com.project.bean.courseTable;

import java.io.Serializable;

public class CourseBean implements Serializable
{
	private static final long serialVersionUID = -2069137337922460495L;
	
	private String grade;
	private String professional;
	private String _class;
	private String isDoubleWeek;
	private String course;
	private String dayOfWeek;
	private String classOfDay;
	private String building;
	private String classRoom;
	private String teacher;

	public String getGrade() 
	{
		return grade;
	}
	
	public void setGrade(String grade) 
	{
		this.grade = grade;
	}
	
	public String getProfessional() 
	{
		return professional;
	}
	
	public void setProfessional(String professional) 
	{
		this.professional = professional;
	}
	
	public String get_class() 
	{
		return _class;
	}
	
	public void set_class(String _class) 
	{
		this._class = _class;
	}
	
	public String getIsDoubleWeek() 
	{
		return isDoubleWeek;
	}
	
	public void setIsDoubleWeek(String isDoubleWeek) 
	{
		this.isDoubleWeek = isDoubleWeek;
	}
	
	public String getCourse() 
	{
		return course;
	}
	
	public void setCourse(String course) 
	{
		this.course = course;
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
	
	public String getBuilding() 
	{
		return building;
	}
	
	public void setBuilding(String building) 
	{
		this.building = building;
	}
	
	public String getClassRoom() 
	{
		return classRoom;
	}
	
	public void setClassRoom(String classRoom) 
	{
		this.classRoom = classRoom;
	}

	public String getTeacher() 
	{
		return teacher;
	}

	public void setTeacher(String teacher) 
	{
		this.teacher = teacher;
	}
	
}
