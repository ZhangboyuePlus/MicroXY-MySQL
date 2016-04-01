package com.project.Util;

import com.UI.MainActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class MyStack 
{
	private FragmentActivity frgtActivity;
	
	public MyStack(FragmentActivity activity)
	{
		frgtActivity = new FragmentActivity();
		this.frgtActivity = activity;
	}

	public void addToHomeStack(Fragment fragment)
	{
		MainActivity.ryltHomeFragment.add(fragment);
		MainActivity.stacktopIndex_HomeFragment ++;
	}
	
	public void addToNewsStack(Fragment fragment)
	{
		MainActivity.ryltNewsFragment.add(fragment);
		MainActivity.stacktopIndex_NewsFragment ++;
	}

	public void addToXYFoodStack(Fragment fragment)
	{
		MainActivity.ryltXYFoodFragment.add(fragment);
		MainActivity.stacktopIndex_XYFoodFragment ++;
	}
	
	public void addToMyInfoStack(Fragment fragment)
	{
		MainActivity.ryltMyInfoFragment.add(fragment);
		MainActivity.stacktopIndex_MyInfoFragment ++;
	}
	
	public void popHomeStack()
	{
		int index = MainActivity.stacktopIndex_HomeFragment;
		if(index >= 0)
		{
			//�õ�ջ��fragment
			Fragment fragment = new Fragment();
			fragment = MainActivity.ryltHomeFragment.get(index);
			
			//��ʾջ��fragment��������ǰ��fragment������activity�Ƴ�
			frgtActivity.getSupportFragmentManager().beginTransaction()
				.show(fragment)
				.remove(MainActivity.frgtCurrentHome).commit();

			//�ǰ��ջ��fragment�����ո���ʾ��fragment���Ϊ��ǰ��ʾ��fragment
			MainActivity.frgtCurrentHome = fragment;
			
			//��ջ��fragment��ջ���Ƴ�����ջ��ָ��  -1
			MainActivity.ryltHomeFragment.remove(index);
			MainActivity.stacktopIndex_HomeFragment --;
		}
	}

	public void popNewsStack()
	{
		int index = MainActivity.stacktopIndex_NewsFragment;
		if(index >= 0)
		{
			Fragment fragment = new Fragment();
			fragment = MainActivity.ryltNewsFragment.get(index);
			
			frgtActivity.getSupportFragmentManager().beginTransaction()
				.show(fragment)
				.remove(MainActivity.frgtCurrentNews).commit();
			
			MainActivity.frgtCurrentNews = fragment;
			
			MainActivity.ryltNewsFragment.remove(index);
			MainActivity.stacktopIndex_NewsFragment --;
		}
	}

	public void popXYFoodStack()
	{
		int index = MainActivity.stacktopIndex_XYFoodFragment;
		if(index >= 0)
		{
			Fragment fragment = new Fragment();
			fragment = MainActivity.ryltXYFoodFragment.get(index);
			
			frgtActivity.getSupportFragmentManager().beginTransaction()
				.show(fragment)
				.remove(MainActivity.frgtCurrentXYFood).commit();
			
			MainActivity.frgtCurrentXYFood = fragment;
			
			MainActivity.ryltXYFoodFragment.remove(index);
			MainActivity.stacktopIndex_XYFoodFragment --;
		}
	}
	
	public void popMyInfoStack()
	{
		int index = MainActivity.stacktopIndex_MyInfoFragment;
		
		Log.i("tag", "index = " + index);
		
		if(index >= 0)
		{
			Fragment fragment = new Fragment();
			fragment = MainActivity.ryltMyInfoFragment.get(index);
			
			frgtActivity.getSupportFragmentManager().beginTransaction()
				.show(fragment)
				.remove(MainActivity.frgtCurrentMyInfo).commit();
			
			MainActivity.frgtCurrentMyInfo = fragment;
			
			MainActivity.ryltMyInfoFragment.remove(index);
			MainActivity.stacktopIndex_MyInfoFragment --;
		}
	}

}
