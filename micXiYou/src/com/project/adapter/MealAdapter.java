package com.project.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.UI.R;
import com.project.Util.Constant;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MealAdapter extends BaseAdapter
{
	private Context context;
	private List<Map<String,String>> dataList = new ArrayList<Map<String, String>>();
	private LayoutInflater layoutInflater;

	public MealAdapter(Context context, List<Map<String,String>> dataList)
	{
		this.context = context;
		this.dataList = dataList;
		layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() 
	{
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ItemView itemView = null;
		if(convertView == null)
		{
			convertView = layoutInflater.inflate(R.layout.adapter_meal, null);
			itemView = new ItemView();
			itemView.txtvMealName = 
					(TextView) convertView.findViewById(R.id.txtvMealName) ;
			itemView.txtvMealPrice = 
					(TextView) convertView.findViewById(R.id.txtvMealPrice);
			convertView.setTag(itemView);
		}
		else
		{
			itemView = (ItemView) convertView.getTag();
		}
		
		itemView.txtvMealName.setText
				(dataList.get(position).get(Constant.MAP_KEY_MEAL[0]));
		itemView.txtvMealPrice.setText
				(dataList.get(position).get(Constant.MAP_KEY_MEAL[1]));

		//…Ë÷√—’…´ ***************************************************************************
		if(position % 2 == 0)
			convertView.setBackgroundColor(Color.parseColor("#EDEDED"));
		else
			convertView.setBackgroundColor(Color.parseColor("#F6F6F6"));
		
		return convertView;
	}

	private class ItemView
	{
		public TextView txtvMealName;
		public TextView txtvMealPrice;
	}
}
