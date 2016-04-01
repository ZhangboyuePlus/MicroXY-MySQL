package com.project.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyAccountSharedPreferences
{
	private Context context;
	
	public MyAccountSharedPreferences(Context context)
	{
		this.context = context;
	}
	
	public void saveAccount(String Email,String passWord, String userName)
	{
        SharedPreferences sharedPre=context.getSharedPreferences(
        				Constant.SHARED_PREFERNECES_FILE_NAME_ACCOUNT, Context.MODE_PRIVATE);
        Editor editor=sharedPre.edit();
        editor.putBoolean("isAccountExist", true);
        editor.putString("Email", Email);
        editor.putString("passWord", passWord);
        editor.putString("userName", userName);
        editor.commit();
    }
	
	public String getAccountEmail()
	{
		SharedPreferences sharedPre= context.getSharedPreferences(
				Constant.SHARED_PREFERNECES_FILE_NAME_ACCOUNT, Context.MODE_PRIVATE);
		
		return sharedPre.getString("Email", "");
	}
	
	public String getAccountPassWord()
	{
		SharedPreferences sharedPre= context.getSharedPreferences(
				Constant.SHARED_PREFERNECES_FILE_NAME_ACCOUNT, Context.MODE_PRIVATE);
		
		return sharedPre.getString("passWord", "");
	}

	public String[] getAccount()
	{
		SharedPreferences sharedPre= context.getSharedPreferences(
						Constant.SHARED_PREFERNECES_FILE_NAME_ACCOUNT, Context.MODE_PRIVATE);
	    String[] accountInfo = new String[3];
	    accountInfo[0] = sharedPre.getString("userName", "");
	    accountInfo[1] = sharedPre.getString("Email", "");
	    accountInfo[2] = sharedPre.getString("passWord", "");
	    
	    return accountInfo;
	}
	
	public boolean isExistAccount()
	{
		boolean isExist = false;
		
		SharedPreferences sharedPre= context.getSharedPreferences(
				Constant.SHARED_PREFERNECES_FILE_NAME_ACCOUNT, Context.MODE_PRIVATE);
		if(sharedPre.contains("isAccountExist") == true 
					&& sharedPre.getBoolean("isAccountExist", false) == true)
			isExist = true;
		
		return isExist;
	}
	
	public void deleteAccount()
	{
		SharedPreferences sharedPre = context.getSharedPreferences(
						Constant.SHARED_PREFERNECES_FILE_NAME_ACCOUNT, Context.MODE_PRIVATE);
		Editor editor = sharedPre.edit();
		editor.putBoolean("isAccountExist", false);
		editor.remove("Email");
		editor.remove("passWord");
		editor.remove("userName");
		editor.commit();
	}
	
	public boolean modifyUserName(String newUserName)
	{
		boolean ok = false;
		
		SharedPreferences sharedPre= context.getSharedPreferences(
				Constant.SHARED_PREFERNECES_FILE_NAME_ACCOUNT, Context.MODE_PRIVATE);
		if(sharedPre.contains("isAccountExist") == true)
		{
			Editor editor = sharedPre.edit();
			editor.putString("userName", newUserName);
			editor.commit();
			
			ok = true; 
		}
		
		return ok;
	}
	
	public boolean modifyPassWord(String newPassWord)
	{
		boolean ok = false;
		
		SharedPreferences sharedPre= context.getSharedPreferences(
				Constant.SHARED_PREFERNECES_FILE_NAME_ACCOUNT, Context.MODE_PRIVATE);
		if(sharedPre.contains("isAccountExist") == true)
		{
			Editor editor = sharedPre.edit();
			editor.putString("passWord", newPassWord);
			editor.commit();
			
			ok = true; 
		}
		
		return ok;
	}
}
