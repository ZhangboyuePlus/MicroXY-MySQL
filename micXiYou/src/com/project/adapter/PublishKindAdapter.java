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

public class PublishKindAdapter extends BaseAdapter
{
	private Context context;
	private List<Map<String,String>> dataList = new ArrayList<Map<String, String>>();
	private LayoutInflater layoutInflater;

	public PublishKindAdapter(Context context, List<Map<String,String>> dataList)
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
			convertView = layoutInflater.inflate(R.layout.adapter_publish_kind, null);
			itemView = new ItemView();
			itemView.txtvPubKindTitle = 
					(TextView) convertView.findViewById(R.id.txtvPubKindTitle) ;
			itemView.txtvPubKindPublisher = 
					(TextView) convertView.findViewById(R.id.txtvPubKindPublisher);
			itemView.txtvPubKindTime = 
					(TextView) convertView.findViewById(R.id.txtvPubKindTime);
			itemView.txtvPubKindState = 
					(TextView) convertView.findViewById(R.id.txtvPubKindState);
			convertView.setTag(itemView);
		}
		else
		{
			itemView = (ItemView) convertView.getTag();
		}
		
		itemView.txtvPubKindTitle.setText
				(dataList.get(position).get(Constant.MAP_KEY_PUBLISH_KIND[0]));
		itemView.txtvPubKindPublisher.setText
				(dataList.get(position).get(Constant.MAP_KEY_PUBLISH_KIND[1]));
		itemView.txtvPubKindTime.setText
				(dataList.get(position).get(Constant.MAP_KEY_PUBLISH_KIND[2]));
		itemView.txtvPubKindState.setText
				(dataList.get(position).get(Constant.MAP_KEY_PUBLISH_KIND[3]));

		//…Ë÷√—’…´ ***************************************************************************
		if(position % 2 == 0)
			convertView.setBackgroundColor(Color.parseColor("#EDEDED"));
		else
			convertView.setBackgroundColor(Color.parseColor("#F6F6F6"));
		
		return convertView;
	}
	
	private class ItemView
	{
		private TextView txtvPubKindTitle;
		private TextView txtvPubKindPublisher;
		private TextView txtvPubKindTime;
		private TextView txtvPubKindState;

	}

}
