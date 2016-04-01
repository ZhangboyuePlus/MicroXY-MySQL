package com.UI.micxiyou;

import com.UI.MainActivity;
import com.UI.R;
import com.project.Util.Constant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsActivity extends Fragment
{
	public static boolean isFragmentFirstShow = true;
	
/*	public static boolean isNeedShowNewsFragment = false;
	public static int indexOfNeedShowNewsFragment = -1;
*/	
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
//		Log.i("tag", "news activity init!");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_news, container, false);
		
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden)
	{
		super.onHiddenChanged(hidden);
		if(hidden == false)
		{
			Log.i("tag", "newsActivity show");
			
			Constant.CURRENT_MAIN_PAGE_STATE = Constant.PAGE_STATE_NEWS_ACTIVITY;
			if(NewsActivity.isFragmentFirstShow == true)
				NewsActivity.isFragmentFirstShow = false;
			
/*
			if(NewsActivity.isNeedShowNewsFragment == true && NewsActivity.indexOfNeedShowNewsFragment != -1)
			{
//***********************************************				
				//TODO
				getActivity().getSupportFragmentManager().beginTransaction()
				.hide(MainActivity.newsFragments[indexOfNeedShowNewsFragment])
				.show(MainActivity.newsFragments[indexOfNeedShowNewsFragment]).commit();

				NewsActivity.isNeedShowNewsFragment = false;
				NewsActivity.indexOfNeedShowNewsFragment = -1;
			}
*/
		}
	}

	public void onDestroy() 
	{
		super.onDestroy();
	}
}
