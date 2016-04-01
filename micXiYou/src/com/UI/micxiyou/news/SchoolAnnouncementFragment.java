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
import com.project.webServices.newsServices.GetAnnouncement;

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

public class SchoolAnnouncementFragment extends Fragment
{
	private ListView lstvSchAnnouncement;
//	private  SimpleAdapter adapter = null;
	private NewsKindAdapter adapter = null; 
	private  List<Map<String,String>> announcementList = new ArrayList<Map<String, String>>();
	private  List<Map<String,String>> newList  = new ArrayList<Map<String, String>>();

//	public static final String[] MAP_KEY = {"SCHOOL_ANNOUNCEMENT_TITLE", "SCHOOL_ANNOUNCEMENT_READ_COUNT", 
//					"SCHOOL_ANNOUNCEMENT_TIME"};
//	private final int[] MAP_NAME = {R.id.txtvSchoolNewsAdapterTitle, 
//			R.id.txtvSchoolNewsAdapterReadCount, R.id.txtvSchoolNewsAdapterTime};

	public static boolean isDownLoadFinish = true;
	private boolean isFragmentFirstShow = true;
	
	private MyInternet myInternet;
	
/*	public static boolean isNeedPlusReadCount = false;
	public static int indexOfItemNeedPlusReadCount = -1;
*/
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
//		Log.i("tag", "school announcement init");
		
		initFragment(savedInstanceState);
		dealAction();
	}
	
	private class ThreadGetAnnouncement extends Thread
	{
		private int haveGotAnnouncementCount;

		public ThreadGetAnnouncement(int haveGotAnnouncementCount)
		{
			this.haveGotAnnouncementCount = haveGotAnnouncementCount;
		}
		
		@Override
		public void run() 
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<List<Map<String,String>>> getAnnouncement = new GetAnnouncement(haveGotAnnouncementCount);
			Future<List<Map<String, String>>> future = pool.submit(getAnnouncement);
			
			try 
			{
				newList = future.get();
				Message msg = new Message();
				if(newList != null && newList.size() > 0)
				{
					msg.what = Constant.MSG_NOTIFY_SCHOOL_ANNOUNCEMENT_ADAPTER_DATA_CHANGE;
				}
				else
				{
					msg.what = Constant.MSG_HAVE_GOT_ALL_SCHOOL_ANNOUNCEMENT_DATA;
				}
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
			
			if(msg.what == Constant.MSG_NOTIFY_SCHOOL_ANNOUNCEMENT_ADAPTER_DATA_CHANGE)
			{
				announcementList.addAll(newList);
				adapter.notifyDataSetChanged();
				newList.clear();

				if(lstvSchAnnouncement.getFooterViewsCount() > 0)
					lstvSchAnnouncement.removeFooterView(MainActivity.onLoadingFooter);
				
				SchoolAnnouncementFragment.isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_HAVE_GOT_ALL_SCHOOL_ANNOUNCEMENT_DATA)
			{
				lstvSchAnnouncement.removeFooterView(MainActivity.onLoadingFooter);
				lstvSchAnnouncement.addFooterView(MainActivity.haveGotAllDataFooter);
				isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				myInternet.showInternetIsError();
			}

		}
	};
	
	public void showAnnouncement(int haveGotAnnouncementCount) 
	{
		if(lstvSchAnnouncement.getFooterViewsCount() == 0)
		{
			lstvSchAnnouncement.addFooterView(MainActivity.onLoadingFooter);
			new ThreadGetAnnouncement(haveGotAnnouncementCount).start();
		}
	}
	
	private void dealAction() 
	{
		lstvSchAnnouncement.setOnScrollListener
		(
				new OnScrollListener()
				{
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
					{
						int lastAnnouncementCount = view.getLastVisiblePosition() + 1;
						if(lastAnnouncementCount == totalItemCount && isDownLoadFinish == true 
										&& isFragmentFirstShow == false)
							loadAnnouncement(totalItemCount);
					}

					@Override
					public void onScrollStateChanged(AbsListView arg0, int arg1) 
					{
					}
				}
		);

		lstvSchAnnouncement.setOnItemClickListener
		(
				new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
					{
						if(position < lstvSchAnnouncement.getCount() - 1)
						{
							plusReadCountByIndex(position);
							
							Map<String, String> map = new HashMap<String, String>();
							map = announcementList.get(position);
							String title = map.get(Constant.MAP_KEY_NEWS_KIND[0]);
							String readCount = map.get(Constant.MAP_KEY_NEWS_KIND[1]);
							String time = map.get(Constant.MAP_KEY_NEWS_KIND[2]);
							
							SchoolNewsContentFragment frgtNewsContent = new SchoolNewsContentFragment();
							Bundle bundle = new Bundle();
							//这里的position是从零开始的，而数据库的记录是从1开始的
							bundle.putInt("newsPosition", position);
							bundle.putString("newsTitle", title);
							bundle.putString("newsReadCount", readCount);
							bundle.putString("newsTime", time);
							bundle.putString("method", Constant.METHOD_GET_SCHOOL_ANNOUNCEMENT_CONTENT_BY_POSITION);
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
		Constant.CURRENT_NEWS_PAGE_STATE = Constant.PAGE_STATE_NEWS_TITLE_FRAGMENT;
		MainActivity.frgtCurrentNews = this;
		
		myInternet = new MyInternet(getActivity());
		
		lstvSchAnnouncement = (ListView) this.getActivity().findViewById(R.id.lstvSchAnnouncement);
		adapter = new NewsKindAdapter(getActivity(), announcementList);
		
		lstvSchAnnouncement.setDivider(null);
		lstvSchAnnouncement.addFooterView(MainActivity.onLoadingFooter);
		lstvSchAnnouncement.addFooterView(MainActivity.haveGotAllDataFooter);
		lstvSchAnnouncement.setAdapter(adapter);
		lstvSchAnnouncement.removeFooterView(MainActivity.onLoadingFooter);
		lstvSchAnnouncement.removeFooterView(MainActivity.haveGotAllDataFooter);
	}

	public void loadAnnouncement(int haveGotAnnouncementCount) 
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
		{
			showAnnouncement(haveGotAnnouncementCount);
		}
		else
			myInternet.showInternetIsNotAvailable();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_school_announcement, container, false);
		
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		super.onHiddenChanged(hidden);
		if(hidden == false)
		{
			Constant.CURRENT_NEWS_PAGE_STATE = Constant.PAGE_STATE_NEWS_TITLE_FRAGMENT;
			MainActivity.frgtCurrentNews = this;
			
//			Log.i("tag", "AnnouncementFragment show");
			
			if(isFragmentFirstShow == true)
			{
				loadAnnouncement(0);
				isFragmentFirstShow = false;
			}
			
/*			if(SchoolAnnouncementFragment.isNeedPlusReadCount = true && SchoolAnnouncementFragment.indexOfItemNeedPlusReadCount != -1)
			{
				Map<String, String> map =  announcementList.get(indexOfItemNeedPlusReadCount);
				String str = map.get(SchoolAnnouncementFragment.MAP_KEY[1]);
				
				int newReadCount = Integer.parseInt(str.substring(5)) + 1;
				
				map.put(SchoolAnnouncementFragment.MAP_KEY[1], "阅读人数:" + newReadCount);
				announcementList.set(indexOfItemNeedPlusReadCount, map);
				adapter.notifyDataSetChanged();
				
				SchoolAnnouncementFragment.isNeedPlusReadCount = false;
				SchoolAnnouncementFragment.indexOfItemNeedPlusReadCount = -1;
			}
*/
		}
	}

	private void plusReadCountByIndex(int index)
	{
		Map<String, String> map =  announcementList.get(index);
		String str = map.get(Constant.MAP_KEY_NEWS_KIND[1]);
		
		int newReadCount = Integer.parseInt(str.substring(5)) + 1;
		
		map.put(Constant.MAP_KEY_NEWS_KIND[1], "阅读人数:" + newReadCount);
		announcementList.set(index, map);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
	}
}