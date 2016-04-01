package com.UI.micxiyou.myInfo;

import com.UI.MainActivity;
import com.UI.R;
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class DevelopMemberFragment extends Fragment
{

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
	}

	private void initFragment() 
	{
		MainActivity.frgtCurrentMyInfo = this;
		Constant.CURRENT_MY_INFO_PAGE_STATE = Constant.PAGE_STATE_DEVELOP_MEMBER;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_MY_INFO_FRAGMENT] = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.fragment_develop_member, container, false);
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() 
	{
		Constant.CURRENT_MY_INFO_PAGE_STATE = Constant.PAGE_STATE_MY_INFO_ACTIVITY;
		new BackTextViewController().setBackTextViewEnable(false);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_MY_INFO_FRAGMENT] = false;
		
		super.onDestroy();
	}
	
}
