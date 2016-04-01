package com.project.Util;

import com.UI.R;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class MyInternet
{
	private Context context;
	
	public MyInternet(Context context)
	{
		this.context = context;
	}

	public boolean isInternetAvailable(Activity activity)
	{
		boolean isAvailable = true;

		ConnectivityManager manager = (
				ConnectivityManager) activity.getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netWorkInfo = manager.getActiveNetworkInfo();
		
		if(netWorkInfo == null || netWorkInfo.isAvailable() == false)
			isAvailable = false;
		
		return isAvailable;
	}
	
	public boolean isInternetAvailable(FragmentActivity activity)
	{
		boolean isAvailable = true;
		
		ConnectivityManager manager = (ConnectivityManager) activity.getApplicationContext()
							.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo netWorkInfo = manager.getActiveNetworkInfo();
		
		if(netWorkInfo == null || netWorkInfo.isAvailable() == false)
			isAvailable = false;

		return isAvailable;
	}
	
	public void sendMsgToHandlerInternetIsError(Handler handler)
	{
		Message msg = new Message();
		msg.what = Constant.MSG_SHOW_INTERNET_IS_ERROR;
		handler.sendMessage(msg);
	}

	public void sendMsgHandlerInternetIsNotAvailable(Handler handler)
	{
		Message msg = new Message();
		msg.what = Constant.MSG_SHOW_INTERNET_IS_ERROR;
		handler.sendMessage(msg);
	}
	
	public void showInternetIsError()
	{
		Toast.makeText(context, R.string.the_internet_is_error, Toast.LENGTH_LONG).show();
	}
	
	public void showInternetIsNotAvailable()
	{
		Toast.makeText(context, R.string.the_internet_is_not_avaliable, Toast.LENGTH_LONG).show();
	}
	
	
}
