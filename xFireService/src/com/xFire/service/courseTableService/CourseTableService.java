package com.xFire.service.courseTableService;

import java.util.List;

import com.xFire.bean.courseTable.CourseBean;

public interface CourseTableService 
{
	public List<CourseBean> getCourseTableByClass(String admissionYear, String professional, String _class);
}
