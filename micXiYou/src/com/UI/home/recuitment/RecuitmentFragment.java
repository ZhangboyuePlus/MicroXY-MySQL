package com.UI.home.recuitment;

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
import com.UI.home.courseTable.ChooseInfoFragment;
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;
import com.project.Util.MyInternet;
import com.project.Util.MyStack;
import com.project.adapter.NewsKindAdapter;
import com.project.webServices.recuitmentServices.GetRecuitment;

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

public class RecuitmentFragment extends Fragment
{
	private ListView lstvRecuitment;

//	private SimpleAdapter adapter = null;
	private NewsKindAdapter adapter = null; 
	private List<Map<String, String>> recuitmentList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
	
/*	public static final String[] MAP_KEY = {"RECUITMENT_TITLE", "RECUITMENT_READ_COUNT", 
				"RECUITMENT_TIME"};
	private final int[] MAP_NAME = {R.id.txtvSchoolNewsAdapterTitle, 
				R.id.txtvSchoolNewsAdapterReadCount, R.id.txtvSchoolNewsAdapterTime};
*/
	public static boolean isDownLoadFinish = true;
	public static boolean isNeedPlusReadCount = false;
	public static int needPlusItemPosition = -1;
	
	private MyInternet myInternet;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment(savedInstanceState);
		dealAction();
	}

	class ThreadGetRecuitment extends Thread
	{
		private int haveGotRecuitmentCount;
		
		public ThreadGetRecuitment(int haveGotRecuitmentCount)
		{
			this.haveGotRecuitmentCount = haveGotRecuitmentCount;
		}
		
		@Override
		public void run()
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<List<Map<String,String>>> getRecuitmentTitle = new GetRecuitment(haveGotRecuitmentCount);
			Future<List<Map<String, String>>> future = pool.submit(getRecuitmentTitle);
			
			try 
			{
				newList = future.get();
				Message msg = new Message();
				if(newList != null && newList.size() > 0)
				{
					msg.what = Constant.MSG_NOTIFY_RECUITMENT_ADAPTER_DATA_CHANGE;
				}
				else
				{
					msg.what = Constant.MSG_HAVE_GOT_ALL_RECUITMENT_DATA;
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
	
	private Handler handler  = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			if(msg.what == Constant.MSG_NOTIFY_RECUITMENT_ADAPTER_DATA_CHANGE)
			{
				recuitmentList.addAll(newList);
				adapter.notifyDataSetChanged();
				newList.clear();
				
				if(lstvRecuitment.getFooterViewsCount() > 0)
					lstvRecuitment.removeFooterView(MainActivity.onLoadingFooter);
				
				isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_HAVE_GOT_ALL_RECUITMENT_DATA)
			{
				lstvRecuitment.removeFooterView(MainActivity.onLoadingFooter);
				lstvRecuitment.addFooterView(MainActivity.haveGotAllDataFooter);
				isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				myInternet.showInternetIsError();
			}
		}
		
	};

	private void dealAction() 
	{
		lstvRecuitment.setOnScrollListener
		(
				new OnScrollListener()
				{
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
					{
						int lastCount = view.getLastVisiblePosition() + 1;
						if(lastCount  == totalItemCount  && isDownLoadFinish == true )
							loadRecuitment(totalItemCount);
					}

					@Override
					public void onScrollStateChanged(AbsListView arg0, int arg1) 
					{
					}
				}
		);
		
		lstvRecuitment.setOnItemClickListener
		(
				new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
					{
						if(position < lstvRecuitment.getCount() - 1)
						{
							Map<String, String> map = new HashMap<String, String>();
							map = recuitmentList.get(position);
							String title = map.get(Constant.MAP_KEY_NEWS_KIND[0]);
							String readCount = map.get(Constant.MAP_KEY_NEWS_KIND[1]);
							String time = map.get(Constant.MAP_KEY_NEWS_KIND[2]);

							Bundle bundle = new Bundle();
							//这里的position是从零开始的，而数据库的记录是从1开始的
							bundle.putInt("recuitmentPosition", position);
							bundle.putString("recuitmentTitle", title);
							bundle.putString("recuitmentReadCount", readCount);
							bundle.putString("recuitmentTime", time);

							RecuitmentContentFragment frgtRecuitmentContent = new RecuitmentContentFragment();
							frgtRecuitmentContent.setArguments(bundle);

							android.support.v4.app.FragmentTransaction transaction = 
									getActivity().getSupportFragmentManager().beginTransaction();
							transaction.add(R.id.fragmentContainer, frgtRecuitmentContent, null)
							.hide(RecuitmentFragment.this)
							.show(frgtRecuitmentContent);
							new MyStack(getActivity()).addToHomeStack(RecuitmentFragment.this);
							transaction.commit();
						}
					}
				}
		);
	}

	public void initFragment(Bundle savedInstanceState)
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_RECUITMENT_TITLE_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;

		myInternet = new MyInternet(getActivity());
		
		lstvRecuitment = (ListView) this.getActivity().findViewById(R.id.lstvRecuitment);
		adapter = new NewsKindAdapter(getActivity(), recuitmentList);

		lstvRecuitment.setDivider(null);
		lstvRecuitment.addFooterView(MainActivity.onLoadingFooter);
		lstvRecuitment.addFooterView(MainActivity.haveGotAllDataFooter);
		lstvRecuitment.setAdapter(adapter);
		lstvRecuitment.removeFooterView(MainActivity.onLoadingFooter);
		lstvRecuitment.removeFooterView(MainActivity.haveGotAllDataFooter);

		loadRecuitment(0);
	}

	public void loadRecuitment(int haveGotRecuitmentCount)
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
		{
			showRecuitment(haveGotRecuitmentCount);
		}
		else
			myInternet.showInternetIsNotAvailable();
	}
	
	public void showRecuitment(final int haveGotRecuitmentCount)
	{
		if(lstvRecuitment.getFooterViewsCount() == 0)
		{
			lstvRecuitment.addFooterView(MainActivity.onLoadingFooter);
			new ThreadGetRecuitment(haveGotRecuitmentCount).start();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_recuitment, container, false);
		
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		super.onHiddenChanged(hidden);
		
		if(hidden == false)
		{
			if(RecuitmentFragment.isNeedPlusReadCount == true && 
					RecuitmentFragment.needPlusItemPosition != -1)
			{
				Map<String, String> map = recuitmentList.get(needPlusItemPosition);
				String str = map.get(Constant.MAP_KEY_NEWS_KIND[1]);
				int newReadCount = Integer.parseInt(str.substring(5)) + 1;
				
				map.put(Constant.MAP_KEY_NEWS_KIND[1], "阅读人数:" + newReadCount);
				recuitmentList.set(needPlusItemPosition, map);
				adapter.notifyDataSetChanged();
				
				RecuitmentFragment.isNeedPlusReadCount = false;
				RecuitmentFragment.needPlusItemPosition = -1;
			}
		}
	}

	@Override
	public void onDestroy()
	{
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_HOME_ACTIVITY;
		new BackTextViewController().setBackTextViewEnable(false);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = false;
		
		super.onDestroy();
	}
}