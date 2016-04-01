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
import com.project.webServices.newsServices.GetNews;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SchoolNewsFragment extends Fragment
{
	private ListView lstvSchNews;
//	private SimpleAdapter adapter = null;
	private NewsKindAdapter adapter = null;
	private List<Map<String,String>> newsList = new ArrayList<Map<String, String>>();
	private List<Map<String,String>> newList  = new ArrayList<Map<String, String>>();

	public static boolean isDownLoadFinish = true;
	private boolean isFragmentFirstShow = true;
	
	private MyInternet myInternet;

//	public static boolean isNeedPlusReadCount = false;
//	public static int indexOfItemNeedPlusReadCount = -1;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

//		Log.i("tag", "news fragment init");
		
		initFragment(savedInstanceState);
		dealAction();
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			if(msg.what == Constant.MSG_NOTIFY_SCHOOL_NEWS_ADAPTER_DATA_CHANGE)
			{
				newsList.addAll(newList);
				adapter.notifyDataSetChanged();
				newList.clear();

				if(lstvSchNews.getFooterViewsCount() > 0)
					lstvSchNews.removeFooterView(MainActivity.onLoadingFooter);
				
				SchoolNewsFragment.isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_HAVE_GOT_ALL_SCHOOL_NEWS_DATA)
			{
				lstvSchNews.removeFooterView(MainActivity.onLoadingFooter);
				lstvSchNews.addFooterView(MainActivity.haveGotAllDataFooter);
				SchoolNewsFragment.isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				myInternet.showInternetIsError();
			}
		}
	};
	
	public void showNews(int haveGotNewsCount)
	{
		if(lstvSchNews.getFooterViewsCount() == 0)
		{
			lstvSchNews.addFooterView(MainActivity.onLoadingFooter);
			new ThreadGetNews(haveGotNewsCount).start();
		}
	}
	
	private class ThreadGetNews extends Thread
	{
		private int haveGotNewsCount;
		
		public ThreadGetNews(int haveGotNewsCount)
		{
			this.haveGotNewsCount = haveGotNewsCount;
		}

		@Override
		public void run() 
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<List<Map<String,String>>> getNews = new GetNews(haveGotNewsCount);
			Future<List<Map<String, String>>> future = pool.submit(getNews);

			try 
			{
				newList = future.get();
				Message msg = new Message();
				if(newList != null && newList.size() > 0)
					msg.what = Constant.MSG_NOTIFY_SCHOOL_NEWS_ADAPTER_DATA_CHANGE;
				else
					msg.what = Constant.MSG_HAVE_GOT_ALL_SCHOOL_NEWS_DATA;
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

	private void dealAction() 
	{
		lstvSchNews.setOnScrollListener
		(
				new OnScrollListener()
				{
					@Override //onScroll会在该第一次初始化后调用一次
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
					{
						int lastNewsCount = view.getLastVisiblePosition() + 1;
						if(lastNewsCount == totalItemCount && isDownLoadFinish == true 
										&& isFragmentFirstShow == false)
						{
							loadNews(totalItemCount);
						}
					}

					@Override
					public void onScrollStateChanged(AbsListView arg0, int arg1) 
					{
					}
				}
		);
		
		lstvSchNews.setOnItemClickListener
		(
				new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
					{
						//为防止用户点击footView而出现下标越界，做如下判断，  position是从0开始的，而count是从1开始的
						if(position < lstvSchNews.getCount() - 1)
						{
							plusReadCountByIndex(position);//*************************************
							
							Map<String, String> map = new HashMap<String, String>();
							map = newsList.get(position);
							String title = map.get(Constant.MAP_KEY_NEWS_KIND[0]);
							String readCount = map.get(Constant.MAP_KEY_NEWS_KIND[1]);
							String time = map.get(Constant.MAP_KEY_NEWS_KIND[2]);
							
							Bundle bundle = new Bundle();
							//这里的position是从零开始的，而数据库的记录是从1开始的， 这里将item位置即下标传到article页面，最后传到服务器，
							//服务器端数据库中的记录号 pos = 记录总数 - position
							bundle.putInt("newsPosition", position);
							bundle.putString("newsTitle", title);
							bundle.putString("newsReadCount", readCount);
							bundle.putString("newsTime", time);
							bundle.putString("method", Constant.METHOD_GET_SCHOOL_NEWS_CONTENT_BY_POSITION);

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
		
		lstvSchNews = (ListView) this.getActivity().findViewById(R.id.lstvSchNews);
		lstvSchNews.setDivider(null);
		adapter = new NewsKindAdapter(getActivity(), newsList);
		
		lstvSchNews.addFooterView(MainActivity.onLoadingFooter);
		lstvSchNews.addFooterView(MainActivity.haveGotAllDataFooter);
		lstvSchNews.setAdapter(adapter);
		lstvSchNews.removeFooterView(MainActivity.onLoadingFooter);
		lstvSchNews.removeFooterView(MainActivity.haveGotAllDataFooter);
	}

	public void loadNews(int haveGotNewsCount)
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
		{
			showNews(haveGotNewsCount);
		}
		else
			myInternet.showInternetIsNotAvailable();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_school_news, container, false);
		
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		super.onHiddenChanged(hidden);
		if(hidden == false)
		{
//			Log.i("tag", "news fragment show");
			
			MainActivity.frgtCurrentNews = this;
			Constant.CURRENT_NEWS_PAGE_STATE = Constant.PAGE_STATE_NEWS_TITLE_FRAGMENT;

			if(isFragmentFirstShow == true)
			{
//				Log.i("tag", "news load");
				loadNews(0);
				isFragmentFirstShow = false;
			}
			
/*			if(SchoolNewsFragment.isNeedPlusReadCount = true && SchoolNewsFragment.indexOfItemNeedPlusReadCount != -1)
			{
				Map<String, String> map =  newsList.get(indexOfItemNeedPlusReadCount);
				String str = map.get(SchoolNewsFragment.MAP_KEY[1]);
				
				int newReadCount = Integer.parseInt(str.substring(5)) + 1;
				
				map.put(SchoolNewsFragment.MAP_KEY[1], "阅读人数:" + newReadCount);
				newsList.set(indexOfItemNeedPlusReadCount, map);
				adapter.notifyDataSetChanged();
				
				SchoolNewsFragment.isNeedPlusReadCount = false;
				SchoolNewsFragment.indexOfItemNeedPlusReadCount = -1;
			}
*/		}
	}
	
	private void plusReadCountByIndex(int index)
	{
		Map<String, String> map =  newsList.get(index);
		String str = map.get(Constant.MAP_KEY_NEWS_KIND[1]);
		
		int newReadCount = Integer.parseInt(str.substring(5)) + 1;
		
		map.put(Constant.MAP_KEY_NEWS_KIND[1], "阅读人数:" + newReadCount);
		newsList.set(index, map);
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
	}
}