package com.UI.home.courseTable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.UI.MainActivity;
import com.UI.R;
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;
import com.project.Util.MyInternet;
import com.project.bean.courseTable.CourseBean;
import com.project.webServices.courseTableService.GetCourseTable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class CourseTableFragment extends Fragment
{
	private List<CourseBean> beanList = null;
	private TextView txtvDay[] = new TextView[5] ;
	private TextView txtvClass[] = new TextView[4];
	
	public static boolean isDownLoadFinish = true;
	private boolean haveGotAllCourseTable = false;
	
	private MyInternet myInternet;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();

		dealAction();
	}
	
	private void dealAction() 
	{
		txtvDay[0].setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						showCourseTable(0);
					}
				}
		);

		txtvDay[1].setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						showCourseTable(1);
					}
				}
		);

		txtvDay[2].setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						showCourseTable(2);
					}
				}
		);

		txtvDay[3].setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						showCourseTable(3);
					}
				}
		);

		txtvDay[4].setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						showCourseTable(4);
					}
				}
		);
	}

	private void showCourseTable(int indexOfTxtvDay)
	{
		if(beanList.size() == 20 && haveGotAllCourseTable == true)
		{
			for(int i = 0; i < 4; i++)
			{
				String courseInfo = "";
				CourseBean bean = beanList.get(indexOfTxtvDay * 4 + i);
				if(bean.getCourse() == null)
					;
				else if(bean.getCourse().contains(":"))
				{
					String courses[] = bean.getCourse().split(":");
					String buildings[] = bean.getBuilding().split(":");
					String classRooms[] = bean.getClassRoom().split(":");
					String teachers[] = bean.getTeacher().split(":");
					
					courseInfo = "(单周)" + courses[0] + "    地点：" + buildings[0] + classRooms[0] + "    老师：" + teachers[0] 
						+ "        (双周)" + courses[1] + "    地点：" + buildings[1] + classRooms[1] + "    老师：" + teachers[1];
				}
				else
				{
					String course = bean.getCourse();
					String building = bean.getBuilding();
					String classRoom = bean.getClassRoom();
					String teacher = bean.getTeacher();
					
					courseInfo = "课程：" + course + "\n地点：" + building + classRoom + "\n老师：" + teacher;
				}
				txtvClass[i].setText(courseInfo);
			}
		}
	}

	private void initFragment()
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_COURSE_TABLE_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;
		
		myInternet = new MyInternet(getActivity());
		
		int textViewId[] = {R.id.txtvCosTabMonday, R.id.txtvCosTabTuesday, R.id.txtvCosTabWednesday,
					R.id.txtvCosTabThursday, R.id.txtvCosTabFriday};
		for(int i = 0; i < 5; i++)
			txtvDay[i] = (TextView) getActivity().findViewById(textViewId[i]);
		
		int classTextViewId[] = {R.id.txtv1_2Class, R.id.txtv3_4Class, R.id.txtv5_6Class, R.id.txtv7_8Class};
		for(int i = 0; i < 4; i++)
			txtvClass[i] = (TextView) getActivity().findViewById(classTextViewId[i]);
		
		loadCourseTable();
	}

	public void loadCourseTable()
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
		{
			LoadCourseTableThread thread = new LoadCourseTableThread();
			thread.start();
		}
		else
			myInternet.showInternetIsNotAvailable();
	}

	class LoadCourseTableThread extends Thread
	{
		@Override
		public void run() 
		{
			getBeanList(); 
		}
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			if(msg.what == Constant.MSG_HAVE_GOT_ALL_COURSE_TABLE)
			{
				haveGotAllCourseTable = true;
				showCourseTable(0);
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
				myInternet.showInternetIsError();
		}
	};
	
	private void getBeanList()
	{
		Bundle bundle = getArguments();
		String year = bundle.getString("year");
		String professionalId = bundle.getString("professionalId");
		String className = bundle.getString("className");
		
		ExecutorService pool = Executors.newFixedThreadPool(1);
		Callable<List<CourseBean>> getCourseTable = new GetCourseTable(year, professionalId, className);
		Future<List<CourseBean>> future = pool.submit(getCourseTable);
		
		beanList = new ArrayList<CourseBean>();
		try 
		{
			beanList = future.get();
			isDownLoadFinish = true;
			formatBeanList();
			
			Message msg = new Message();
			msg.what = Constant.MSG_HAVE_GOT_ALL_COURSE_TABLE;
			handler.sendMessage(msg);
		} catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			myInternet.sendMsgToHandlerInternetIsError(handler);
			return;
		} catch (ExecutionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			myInternet.sendMsgToHandlerInternetIsError(handler);
			return;
		}
	}

	private void formatBeanList() 
	{
		for(int i = 0; i < beanList.size(); i++)
		{
			String course = beanList.get(i).getCourse();
			String building = beanList.get(i).getBuilding();
			String classRoom = beanList.get(i).getClassRoom();
			String teacher = beanList.get(i).getTeacher();
			
			if(course != null && course.startsWith(":") == true)
			{
				beanList.get(i).setCourse(course.substring(1));
				beanList.get(i).setBuilding(building.substring(1));
				beanList.get(i).setClassRoom(classRoom.substring(1));
				beanList.get(i).setTeacher(teacher.substring(1));
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_course_table, container, false);
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		super.onHiddenChanged(hidden);
		
		if(hidden == false)
		{
		}
	}

	@Override
	public void onDestroy()
	{
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_CHOOSE_COURSE_TABLE_INFO_FRAGMENT;

		super.onDestroy();
	}
	
}
