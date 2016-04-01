package com.UI.home.studyRoom;

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
import com.project.bean.classRoom.ClassRoomBean;
import com.project.database.DBClassRoomOperator;
import com.project.database.DBVersionOperator;
import com.project.webServices.classRoomServices.GetClassRoom;
import com.project.webServices.classRoomServices.GetClassRoomVersion;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class StudyRoomFragment extends Fragment
{
	private ToggleButton tgbtIsDoubleWeek = null;
	private Spinner spnnDayOfWeek = null;
	private Spinner spnnClassOfDay = null;
	
	private ArrayAdapter<CharSequence> rdptDayOfWeek = null;
	private ArrayAdapter<CharSequence> rdptClassOfDay = null;
	
	public static String classRoomVersion = "";
	private String nativeClassRoomVersion = "";
	public static boolean isDownLoadFinish = true; 
	
	private String ownerOfVersion = "studyRoom";
	
	private ListView lstvABuilding = null;
	private ListView lstvBBuilding = null;
	private ListView lstvCBuilding = null;
	
	private ArrayAdapter<String> A_adapter = null;
	private ArrayAdapter<String> B_adapter = null;
	private ArrayAdapter<String> C_adapter = null;
	
	private List<String> A_List = new ArrayList<String>();
	private List<String> B_List = new ArrayList<String>();
	private List<String> C_List = new ArrayList<String>();
	
	private boolean isOneSpinnerHadSelectedBySystem = false;
	
	private MyInternet myInternet;
	
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
		dealAction();
	}

	private void dealAction() 
	{
		tgbtIsDoubleWeek.setOnCheckedChangeListener
		(
				new OnCheckedChangeListener()
				{
					/**
					 * 当开关为开状态时，arg = true， 否则为 false；
					 */
					@Override
					public void onCheckedChanged(CompoundButton button, boolean arg) 
					{
						reFreshListView();
					}

				}
		);

		spnnDayOfWeek.setOnItemSelectedListener
		(
				new OnItemSelectedListener()
				{
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
					{
						if(isOneSpinnerHadSelectedBySystem == true)
							reFreshListView();
						else
							isOneSpinnerHadSelectedBySystem = true;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) 
					{}
				}
		);
		
		spnnClassOfDay.setOnItemSelectedListener
		(
				new OnItemSelectedListener()
				{
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
					{
						if(isOneSpinnerHadSelectedBySystem == true)
							reFreshListView();
						else
							isOneSpinnerHadSelectedBySystem = true;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) 
					{}
				}
		);
	}
	
	private void reFreshListView() 
	{
		String isDoubleWeek = tgbtIsDoubleWeek.isChecked() ? "1" : "0" ;
		String dayOfWeek =  spnnDayOfWeek.getSelectedItemPosition() + 1 + "";
		String classOfDay = spnnClassOfDay.getSelectedItemPosition() + 1 + "";
		
		DBClassRoomOperator operator = new DBClassRoomOperator(getActivity());
		A_List.clear();
		A_List.addAll(operator.selectClassRoomNoClose(isDoubleWeek, "A", dayOfWeek, classOfDay));
		A_adapter.notifyDataSetChanged();
		
		B_List.clear();
		B_List.addAll(operator.selectClassRoomNoClose(isDoubleWeek, "B", dayOfWeek, classOfDay));
		B_adapter.notifyDataSetChanged();

		C_List.clear();
		C_List.addAll(operator.selectClassRoomNoClose(isDoubleWeek, "C", dayOfWeek, classOfDay));
		C_adapter.notifyDataSetChanged();
		
		operator.closeDB();
	}

	//系统会自己将spinner的选项默认选为第一个选项，下标为0 ；
	private void initFragment()
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_STUDY_ROOM_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;

		myInternet = new MyInternet(getActivity());
		
		tgbtIsDoubleWeek = (ToggleButton) getActivity().findViewById(R.id.tgbtIsDoubleWeek);
		spnnDayOfWeek = (Spinner) getActivity().findViewById(R.id.spnnDayOfWeek);
		spnnClassOfDay = (Spinner) getActivity().findViewById(R.id.spnnClassOfDay);
		
		rdptDayOfWeek = ArrayAdapter.createFromResource(this.getActivity(), R.array.dayOfWeek, 
							android.R.layout.simple_spinner_item);
		rdptDayOfWeek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnnDayOfWeek.setAdapter(rdptDayOfWeek);

		rdptClassOfDay = ArrayAdapter.createFromResource(getActivity(), R.array.classOfDay, 
							android.R.layout.simple_spinner_item);
		rdptClassOfDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnnClassOfDay.setAdapter(rdptClassOfDay);
		
		lstvABuilding = (ListView) getActivity().findViewById(R.id.lstvABuilding);
		lstvBBuilding = (ListView) getActivity().findViewById(R.id.lstvBBuilding);
		lstvCBuilding = (ListView) getActivity().findViewById(R.id.lstvCBuilding);
		
		A_adapter = new ArrayAdapter<String>(getActivity(), R.layout.adapter_my_simple_list_item, A_List);
		B_adapter = new ArrayAdapter<String>(getActivity(), R.layout.adapter_my_simple_list_item, B_List);
		C_adapter = new ArrayAdapter<String>(getActivity(), R.layout.adapter_my_simple_list_item, C_List);

		lstvABuilding.setAdapter(A_adapter);
		lstvBBuilding.setAdapter(B_adapter);
		lstvCBuilding.setAdapter(C_adapter);

		initDataBase();
	}

	private void initDataBase()
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
		{
			new InitDataBaseThread().start();
		}
		else
			myInternet.showInternetIsNotAvailable();
	}

	class InitDataBaseThread extends Thread
	{
		@Override
		public void run()
		{
			classRoomVersion = getServiceClassRoomVersion();
			DBVersionOperator verOperator = new DBVersionOperator(getActivity());
			if(verOperator.isVersionExist(ownerOfVersion))
			{
				nativeClassRoomVersion = getNativeClassRoomVersion();
				
				if(classRoomVersion.equals(nativeClassRoomVersion) == false)
				{
					List<ClassRoomBean> roomBeanList = getNewClassRoom();
					wirteClassRoomToDB(roomBeanList);
					updateClassRoomVersion(classRoomVersion);
				}
			}
			else
			{
				List<ClassRoomBean> roomBeanList = getNewClassRoom();
				wirteClassRoomToDB(roomBeanList);
				verOperator.insertVersion(classRoomVersion, ownerOfVersion);
			}
		}
	}
	
	private void wirteClassRoomToDB(List<ClassRoomBean> roomBeanList)
	{
		DBClassRoomOperator operator = new DBClassRoomOperator(getActivity());
		for(int i = 0; i < roomBeanList.size(); i++)
		{
			String isDoubleWeek = roomBeanList.get(i).getIsDoubleWeek();
			String building = roomBeanList.get(i).getBuilding();
			String classRoom = roomBeanList.get(i).getClassRoom();
			String dayOfWeek = roomBeanList.get(i).getDayOfWeek();
			String classOfDay = roomBeanList.get(i).getClassOfDay();
			operator.insertClassRoomNoClose(isDoubleWeek, building, classRoom, dayOfWeek, classOfDay);
		}
		operator.closeDB();
	}

	private void updateClassRoomVersion(String ver)
	{
		DBVersionOperator operator = new DBVersionOperator(getActivity());
		operator.updateVersion(ver, ownerOfVersion);
	}

	private List<ClassRoomBean> getNewClassRoom()
	{
		List<ClassRoomBean> roomBeanList = new ArrayList<ClassRoomBean>();
		ExecutorService pool = Executors.newFixedThreadPool(2);
		Callable<List<ClassRoomBean>> getClassRoom = new GetClassRoom();
		Future<List<ClassRoomBean>> future = pool.submit(getClassRoom);
		
		try 
		{
			roomBeanList = future.get();
		} catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			myInternet.sendMsgToHandlerInternetIsError(handler);
			return roomBeanList;
		} catch (ExecutionException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			myInternet.sendMsgToHandlerInternetIsError(handler);
			return roomBeanList;
		}
		
		return roomBeanList;
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				myInternet.showInternetIsError();
			}
		}
	};
	
	private String getServiceClassRoomVersion()
	{
		ExecutorService pool = Executors.newFixedThreadPool(1);
		Callable<String> getVersion = new GetClassRoomVersion();
		Future<String> future = pool.submit(getVersion);
		
		String ver = "";
		try 
		{
			 ver = future.get();
		} catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			myInternet.sendMsgToHandlerInternetIsError(handler);
			return ver;
		} catch (ExecutionException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			myInternet.sendMsgToHandlerInternetIsError(handler);
			return ver;
		}
		
		return ver;
	}

	private String getNativeClassRoomVersion() 
	{
		DBVersionOperator operator = new DBVersionOperator(getActivity());
		
		return operator.getVersion(ownerOfVersion);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_study_room, container, false);

		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		super.onHiddenChanged(hidden);
		
		if(hidden == false)
		{
		}
	}

	public void onDestroy() 
	{
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_HOME_ACTIVITY;
		new BackTextViewController().setBackTextViewEnable(false);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = false;

		super.onDestroy();
	}
}