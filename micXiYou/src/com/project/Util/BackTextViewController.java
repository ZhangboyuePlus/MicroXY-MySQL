package com.project.Util;

import android.view.View;

import com.UI.MainActivity;
import com.UI.R;

public class BackTextViewController
{
	public BackTextViewController()
	{}
	
	public void setBackTextViewEnable(boolean flag)
	{
		if(flag == true)
		{
//			MainActivity.mgbtBack.setText(R.string.back);
			MainActivity.mgbtBack.setVisibility(View.VISIBLE);
			MainActivity.mgbtBack.setEnabled(true);
		}
		else
		{
//			MainActivity.mgbtBack.setText("");
			MainActivity.mgbtBack.setVisibility(View.INVISIBLE);
			MainActivity.mgbtBack.setEnabled(false);
		}
	}
}
