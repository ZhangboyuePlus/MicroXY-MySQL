package com.project.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.UI.R;
import com.UI.micxiyou.news.SchoolNewsFragment;
import com.project.Util.Constant;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsKindAdapter extends BaseAdapter
{
	private Context context;
	private List<Map<String,String>> dataList = new ArrayList<Map<String, String>>();
	private LayoutInflater layoutInflater;

	public NewsKindAdapter(Context context, List<Map<String,String>> dataList)
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
			convertView = layoutInflater.inflate(R.layout.adapter_news_kind, null);
			itemView = new ItemView();
			itemView.txtvNewsKindAdapterTitle = 
					(TextView) convertView.findViewById(R.id.txtvNewsKindAdapterTitle) ;
			itemView.txtvNewsKindAdapterReadCount = 
					(TextView) convertView.findViewById(R.id.txtvNewsKindAdapterReadCount);
			itemView.txtvNewsKindAdapterTime = 
					(TextView) convertView.findViewById(R.id.txtvNewsKindAdapterTime);
			convertView.setTag(itemView);
		}
		else
		{
			itemView = (ItemView) convertView.getTag();
		}
		
		itemView.txtvNewsKindAdapterTitle.setText
				(dataList.get(position).get(Constant.MAP_KEY_NEWS_KIND[0]));
		itemView.txtvNewsKindAdapterReadCount.setText
				(dataList.get(position).get(Constant.MAP_KEY_NEWS_KIND[1]));
		itemView.txtvNewsKindAdapterTime.setText
				(dataList.get(position).get(Constant.MAP_KEY_NEWS_KIND[2]));

		//…Ë÷√—’…´ ***************************************************************************
		if(position % 2 == 0)
			convertView.setBackgroundColor(Color.parseColor("#EDEDED"));
		else
			convertView.setBackgroundColor(Color.parseColor("#F6F6F6"));
		
		return convertView;
	}

	private class ItemView
	{
		public TextView txtvNewsKindAdapterTitle;
		public TextView txtvNewsKindAdapterReadCount;
		public TextView txtvNewsKindAdapterTime;
	}
}
