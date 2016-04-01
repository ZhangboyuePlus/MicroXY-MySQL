package com.UI.micxiyou;

import com.UI.MainActivity;
import com.UI.R;
import com.UI.home.ArticlesFound.ArticlesFoundFragment;
import com.UI.home.XYMap.XYMapFragment;
import com.UI.home.courseTable.ChooseInfoFragment;
import com.UI.home.recuitment.RecuitmentFragment;
import com.UI.home.secondaryMarket.SecondaryMarketFragment;
import com.UI.home.studyRoom.StudyRoomFragment;
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;
import com.project.Util.MyStack;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class HomeActivity extends Fragment
{
	private TextView txtvCourseTable = null;
	private TextView txtvStudyRoom = null;
	private TextView txtvXYMap = null;
	private TextView txtvRecuitment = null;
	private TextView txtvArticlesFound = null;
	private TextView txtvSecondaryMarket  =null;
	
	private boolean isFragmentFirstShow = true;
	
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);

		MainActivity.frgtCurrentHome  = this;
		new BackTextViewController().setBackTextViewEnable(false);
	}

	private void dealAction() 
	{
		txtvStudyRoom.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						StudyRoomFragment frgtStudyRoom = new StudyRoomFragment();
						android.support.v4.app.FragmentTransaction transaction = 
								getActivity().getSupportFragmentManager().beginTransaction();
						transaction.replace(R.id.fragment_home, frgtStudyRoom);
						new MyStack(getActivity()).addToHomeStack(HomeActivity.this);
						transaction.commit();
					}
				}
		);

		txtvRecuitment.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						RecuitmentFragment frgtRecuitment = new RecuitmentFragment();
						android.support.v4.app.FragmentTransaction transaction = 
								getActivity().getSupportFragmentManager().beginTransaction();
						transaction.replace(R.id.fragment_home, frgtRecuitment);
						new MyStack(getActivity()).addToHomeStack(HomeActivity.this);
						transaction.commit();
					}
				}
		);
		
		txtvCourseTable.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						ChooseInfoFragment frgtChooseInfo = new ChooseInfoFragment();
						android.support.v4.app.FragmentTransaction transaction = 
								getActivity().getSupportFragmentManager().beginTransaction();
						//这里fragment替换后，homeFragment虽然没有加到系统的返回栈，但并不会被destroy，
						//可能是因为homeFragment是在布局文件中定义的缘故
						transaction.replace(R.id.fragment_home, frgtChooseInfo);
						new MyStack(getActivity()).addToHomeStack(HomeActivity.this);
						transaction.commit();
					}
				}
		);
		
		txtvXYMap.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						XYMapFragment frgtXYMap = new XYMapFragment();
						android.support.v4.app.FragmentTransaction transaction = 
								getActivity().getSupportFragmentManager().beginTransaction();
						transaction.replace(R.id.fragment_home, frgtXYMap);
						new MyStack(getActivity()).addToHomeStack(HomeActivity.this);
						transaction.commit();
					}
				}
		);
		
		txtvArticlesFound.setOnClickListener
		(
				new OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						ArticlesFoundFragment frgtArticleFound = new ArticlesFoundFragment();
						android.support.v4.app.FragmentTransaction transaction = 
								getActivity().getSupportFragmentManager().beginTransaction();
						transaction.replace(R.id.fragment_home, frgtArticleFound);
						new MyStack(getActivity()).addToHomeStack(HomeActivity.this);
						transaction.commit();
					}
				}
		);
		
		txtvSecondaryMarket.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						SecondaryMarketFragment frgtSecondaryMarket = new SecondaryMarketFragment();
						android.support.v4.app.FragmentTransaction transaction = 
								getActivity().getSupportFragmentManager().beginTransaction();
						transaction.replace(R.id.fragment_home, frgtSecondaryMarket);
						new MyStack(getActivity()).addToHomeStack(HomeActivity.this);
						transaction.commit();
					}
				}
		);
	}

	private void initFragment()
	{
		txtvCourseTable = (TextView) getActivity().findViewById(R.id.txtvCourseTable);
		txtvStudyRoom = (TextView) getActivity().findViewById(R.id.txtvStudyRoom);
		txtvXYMap = (TextView) getActivity().findViewById(R.id.txtvXYMap);
		txtvRecuitment = (TextView) getActivity().findViewById(R.id.txtvRecuitment);
		txtvArticlesFound = (TextView) getActivity().findViewById(R.id.txtvArticlesFound);
		txtvSecondaryMarket = (TextView) getActivity().findViewById(R.id.txtvSecondaryMarket);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		super.onHiddenChanged(hidden);
		if(hidden == false)
		{
			Constant.CURRENT_MAIN_PAGE_STATE = Constant.PAGE_STATE_HOME_ACTIVITY;

			if(isFragmentFirstShow == true)
			{
				initFragment();
				dealAction();

				isFragmentFirstShow = false;
			}
		}
	}

	public void onDestroy() 
	{
		super.onDestroy();
	}
}
