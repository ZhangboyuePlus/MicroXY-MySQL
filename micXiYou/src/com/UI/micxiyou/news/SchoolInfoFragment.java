package com.UI.micxiyou.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.UI.MainActivity;
import com.UI.R;
import com.project.Util.Constant;
import com.project.Util.MyInternet;
import com.project.Util.MyStack;
import com.project.adapter.NewsKindAdapter;
import com.project.webServices.newsServices.GetInfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class SchoolInfoFragment extends Fragment
{
	private  ListView lstvSchInfo;
//	public  SimpleAdapter adapter = null;
	private NewsKindAdapter adapter = null;
	private  List<Map<String,String>> infoList = new ArrayList<Map<String, String>>();
	private  List<Map<String, String>> newList = new ArrayList<Map<String, String>>();

/*	public static final String[] MAP_KEY = {"SCHOOL_INFO_TITLE", "SCHOOL_INFO_READ_COUNT", 
					"SCHOOL_INFO_TIME"};
	private final int[] MAP_NAME = {R.id.txtvSchoolNewsAdapterTitle, 
			R.id.txtvSchoolNewsAdapterReadCount, R.id.txtvSchoolNewsAdapterTime};
*/	
	public static boolean isDownLoadFinish = true;
	private int fragmentShowCount = 0;
	
	private MyInternet myInternet;
	
/*	public static boolean isNeedPlusReadCount = false;
	public static int indexOfItemNeedPlusReadCount = -1;
*/
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
//		Log.i("tag", "init shchool info");
		
		initFragment(savedInstanceState);
		dealAction();
	}
	
	public void showInfo(final int haveGotInfoCount) 
	{
		if(lstvSchInfo.getFooterViewsCount() == 0)
		{
			lstvSchInfo.addFooterView(MainActivity.onLoadingFooter);
			new ThreadGetInfo(haveGotInfoCount).start();
		}
	}

	private class ThreadGetInfo extends Thread
	{
		private int haveGotInfoCount;
		
		public ThreadGetInfo(int haveGotInfoCount)
		{
			this.haveGotInfoCount = haveGotInfoCount;
		}
		
		@Override
		public void run() 
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<List<Map<String,String>>> getInfo = new GetInfo(haveGotInfoCount);
			Future<List<Map<String, String>>> future = pool.submit(getInfo);
			
			try 
			{
				newList = future.get();

				Message msg = new Message();
				if(newList != null && newList.size() > 0)
					msg.what = Constant.MSG_NOTIFY_SCHOOL_INFO_ADAPTER_DATA_CHANGE;
				else
					msg.what = Constant.MSG_HAVE_GOT_ALL_SCHOOL_INFO_DATA;
				handler.sendMessage(msg);
			} catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				myInternet.sendMsgToHandlerInternetIsError(handler);
				return;
			} catch (ExecutionException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				myInternet.sendMsgToHandlerInternetIsError(handler);
				return;
			}
		}
	}
		
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			if(msg.what == Constant.MSG_NOTIFY_SCHOOL_INFO_ADAPTER_DATA_CHANGE)
			{
				infoList.addAll(newList);
				adapter.notifyDataSetChanged();
				newList.clear();

				if(lstvSchInfo.getFooterViewsCount() > 0)
					lstvSchInfo.removeFooterView(MainActivity.onLoadingFooter);
				
				SchoolInfoFragment.isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_HAVE_GOT_ALL_SCHOOL_INFO_DATA)
			{
				lstvSchInfo.removeFooterView(MainActivity.onLoadingFooter);
				lstvSchInfo.addFooterView(MainActivity.haveGotAllDataFooter);
				SchoolInfoFragment.isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				myInternet.showInternetIsError();
			}
			
		}
	};
	
	private void dealAction() 
	{
		lstvSchInfo.setOnScrollListener
		(
				new OnScrollListener()
				{
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
					{
						int lastInfoCount = view.getLastVisiblePosition() + 1;
						if(lastInfoCount  == totalItemCount && isDownLoadFinish == true && fragmentShowCount > 2)
						{
							loadInfo(totalItemCount );
						}
					}

					@Override
					public void onScrollStateChanged(AbsListView arg0, int arg1) 
					{
					}
				}
		);
		
		lstvSchInfo.setOnItemClickListener
		(
				new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
					{
						if(position < lstvSchInfo.getCount() - 1)
						{
							plusReadCountByIndex(position);
							
							Map<String, String> map = new HashMap<String, String>();
							map = infoList.get(position);
							String title = map.get(Constant.MAP_KEY_NEWS_KIND[0]);
							String readCount = map.get(Constant.MAP_KEY_NEWS_KIND[1]);
							String time = map.get(Constant.MAP_KEY_NEWS_KIND[2]);

							Bundle bundle = new Bundle();
							//这里的position是从零开始的，而数据库的记录是从1开始的
							bundle.putInt("newsPosition", position);
							bundle.putString("newsTitle", title);
							bundle.putString("newsReadCount", readCount);
							bundle.putString("newsTime", time);
							bundle.putString("method", Constant.METHOD_GET_SCHOOL_INFO_CONTENT_BY_POSITION);

							SchoolNewsContentFragment frgtNewsContent = new SchoolNewsContentFragment();
							frgtNewsContent.setArguments(bundle);
							android.support.v4.app.FragmentTransaction transaction = 
									getActivity().getSupportFragmentManager().beginTransaction();
							transaction.add(R.id.fragmentContainer, frgtNewsContent, null)
							.hide(MainActivity.mainFragments[Constant.INDEX_NEWS_FRAGMENT])
							.show(frgtNewsContent);
							new MyStack(getActivity()).addToNewsStack(MainActivity.mainFragments[Constant.INDEX_NEWS_FRAGMENT]);
							transaction.commit();
						}
					}
				}
		);
	}

	public void initFragment(Bundle savedInstanceState) 
	{
		MainActivity.frgtCurrentNews = this;
		Constant.CURRENT_NEWS_PAGE_STATE = Constant.PAGE_STATE_NEWS_TITLE_FRAGMENT;

		myInternet = new MyInternet(getActivity());
		
		lstvSchInfo = (ListView) this.getActivity().findViewById(R.id.lstvSchInfo);
		adapter = new NewsKindAdapter(getActivity(), infoList);

		lstvSchInfo.setDivider(null);
		lstvSchInfo.addFooterView(MainActivity.onLoadingFooter);
		lstvSchInfo.addFooterView(MainActivity.haveGotAllDataFooter);
		lstvSchInfo.setAdapter(adapter);
		lstvSchInfo.removeFooterView(MainActivity.onLoadingFooter);
		lstvSchInfo.removeFooterView(MainActivity.haveGotAllDataFooter);
	}

	public void loadInfo(int haveGotNewsCount)
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
		{
			showInfo(haveGotNewsCount);
		}
		else
			myInternet.showInternetIsNotAvailable();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_school_info, container, false);

		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		super.onHiddenChanged(hidden);
		if(hidden == false)
		{
//			Log.i("tag", "school info show ");
			
			fragmentShowCount ++;
			MainActivity.frgtCurrentNews = this;
			Constant.CURRENT_NEWS_PAGE_STATE = Constant.PAGE_STATE_NEWS_TITLE_FRAGMENT;

			if(fragmentShowCount == 2)
			{
				loadInfo(0);
			}
			
/*			if(SchoolInfoFragment.isNeedPlusReadCount = true && SchoolInfoFragment.indexOfItemNeedPlusReadCount != -1)
			{
				Map<String, String> map =  infoList.get(indexOfItemNeedPlusReadCount);
				String str = map.get(SchoolInfoFragment.MAP_KEY[1]);
				
				int newReadCount = Integer.parseInt(str.substring(5)) + 1;
				
				map.put(SchoolInfoFragment.MAP_KEY[1], "阅读人数:" + newReadCount);
				infoList.set(indexOfItemNeedPlusReadCount, map);
				adapter.notifyDataSetChanged();
				
				SchoolInfoFragment.isNeedPlusReadCount = false;
				SchoolInfoFragment.indexOfItemNeedPlusReadCount = -1;
			}
*/		}
	}
	
	private void plusReadCountByIndex(int index)
	{
		Map<String, String> map =  infoList.get(index);
		String str = map.get(Constant.MAP_KEY_NEWS_KIND[1]);
		
		int newReadCount = Integer.parseInt(str.substring(5)) + 1;
		
		map.put(Constant.MAP_KEY_NEWS_KIND[1], "阅读人数:" + newReadCount);
		infoList.set(index, map);
		adapter.notifyDataSetChanged();
	}

	public void onDestroy() 
	{
		super.onDestroy();
	}
}