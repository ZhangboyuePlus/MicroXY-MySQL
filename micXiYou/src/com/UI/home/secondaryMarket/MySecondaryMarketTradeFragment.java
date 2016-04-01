package com.UI.home.secondaryMarket;

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
import com.UI.home.ArticlesFound.ArticleFoundContentFragment;
import com.UI.home.ArticlesFound.MyArticleFoundPublishedFragment;
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;
import com.project.Util.MyAccountSharedPreferences;
import com.project.Util.MyInternet;
import com.project.Util.MyStack;
import com.project.adapter.PublishKindAdapter;
import com.project.webServices.articleFoundService.GetMyArticleFoundPublishedTitle;
import com.project.webServices.secondaryMarketService.GetMyTrade;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class MySecondaryMarketTradeFragment extends Fragment 
{
	public static boolean isDownLoadFinish = true;
	private MyInternet myInternet;
	
	private ListView lsvwMyTradeSecondaryMarket;
//	private SimpleAdapter adapter;
	private PublishKindAdapter adapter;
	private List<Map<String, String>> myTradeList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
	
	public static boolean isNeedModifyItemState = false;
	public static int positionOfNeedModifyItem = -1;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
		
		dealAction();
	}

	private void dealAction()
	{
		lsvwMyTradeSecondaryMarket.setOnScrollListener
		(
				new OnScrollListener()
				{
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
					{
						int lastCount = view.getLastVisiblePosition() + 1;
						if(lastCount  == totalItemCount  && isDownLoadFinish == true )
							loadMyTrade(totalItemCount);
					}

					@Override
					public void onScrollStateChanged(AbsListView arg0, int arg1)  
					{
					}
				}
		);

		lsvwMyTradeSecondaryMarket.setOnItemClickListener
		(
				new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
					{
						if(position < lsvwMyTradeSecondaryMarket.getCount() - 1)
						{
							Map<String, String> map = new HashMap<String, String>();
							map = myTradeList.get(position);
							String title = map.get(Constant.MAP_KEY_PUBLISH_KIND[0]);
							String publisher = map.get(Constant.MAP_KEY_PUBLISH_KIND[1]);
							String time = map.get(Constant.MAP_KEY_PUBLISH_KIND[2]);
							String state = map.get(Constant.MAP_KEY_PUBLISH_KIND[3]);
							String articleId = map.get(Constant.MAP_KEY_PUBLISH_KIND[4]);

							Bundle bundle = new Bundle();
							bundle.putBoolean("isFromMyTrade", true);
							bundle.putString("articleId", articleId);
							bundle.putString("title", title);
							bundle.putString("publisher", publisher);
							bundle.putString("time", time);
							bundle.putString("state", state);
							bundle.putInt("itemPosition", position);

							SecondaryMarketContentFragment frgtSecondaryMarketContent = new SecondaryMarketContentFragment();
							frgtSecondaryMarketContent.setArguments(bundle);

							android.support.v4.app.FragmentTransaction transaction = 
									getActivity().getSupportFragmentManager().beginTransaction();
							transaction.add(R.id.fragmentContainer, frgtSecondaryMarketContent, null)
							.hide(MySecondaryMarketTradeFragment.this)
							.show(frgtSecondaryMarketContent);
							new MyStack(getActivity()).addToHomeStack(MySecondaryMarketTradeFragment.this);
							transaction.commit();
						}
					}
				}
		);
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			
			if(msg.what == Constant.MSG_NOTIFY_MY_TRADE_ADAPTER_DATA_CHANGE)
			{
				myTradeList.addAll(newList);
				adapter.notifyDataSetChanged();
				newList.clear();
				
				if(lsvwMyTradeSecondaryMarket.getFooterViewsCount() > 0)
					lsvwMyTradeSecondaryMarket.removeFooterView(MainActivity.onLoadingFooter);
				
				isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_HAVE_GOT_ALL_MY_TRADE_DATA)
			{
				lsvwMyTradeSecondaryMarket.removeFooterView(MainActivity.onLoadingFooter);
				lsvwMyTradeSecondaryMarket.addFooterView(MainActivity.haveGotAllDataFooter);
				isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				lsvwMyTradeSecondaryMarket.removeFooterView(MainActivity.onLoadingFooter);
				myInternet.showInternetIsError();
			}
		}
	};
	
	private class TheadGetMyTrade extends Thread
	{
		private int haveGotScdMktCount;
		private String registedEmail;
		
		public TheadGetMyTrade(int haveGotScdMktCount, String registedEmail)
		{
			this.haveGotScdMktCount = haveGotScdMktCount;
			this.registedEmail = registedEmail;
		}
		
		@Override
		public void run()
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<List<Map<String,String>>> getMyTrade = new GetMyTrade(haveGotScdMktCount, registedEmail);
			Future<List<Map<String, String>>> future = pool.submit(getMyTrade);
			
			try 
			{
				newList = future.get();
				Message msg = new Message();
				if(newList != null && newList.size() > 0)
					msg.what = Constant.MSG_NOTIFY_MY_TRADE_ADAPTER_DATA_CHANGE;
				else
					msg.what = Constant.MSG_HAVE_GOT_ALL_MY_TRADE_DATA;

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

	private void initFragment()
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_MY_TRADE_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;
		
		myInternet = new MyInternet(getActivity());
		
		lsvwMyTradeSecondaryMarket = (ListView) getActivity().findViewById(R.id.lsvwMyTradeSecondaryMarket);
		adapter = new PublishKindAdapter(getActivity(), myTradeList);

		lsvwMyTradeSecondaryMarket.setDivider(null);
		lsvwMyTradeSecondaryMarket.addFooterView(MainActivity.onLoadingFooter);
		lsvwMyTradeSecondaryMarket.addFooterView(MainActivity.haveGotAllDataFooter);
		lsvwMyTradeSecondaryMarket.setAdapter(adapter);
		lsvwMyTradeSecondaryMarket.removeFooterView(MainActivity.onLoadingFooter);
		lsvwMyTradeSecondaryMarket.removeFooterView(MainActivity.haveGotAllDataFooter);

		loadMyTrade(0);
	}

	private void loadMyTrade(int haveGotScdMktCount) 
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
		{
			if(lsvwMyTradeSecondaryMarket.getFooterViewsCount() == 0)
			{
				lsvwMyTradeSecondaryMarket.addFooterView(MainActivity.onLoadingFooter);
				
				String Email = new MyAccountSharedPreferences(getActivity()).getAccountEmail();
				new TheadGetMyTrade(haveGotScdMktCount, Email).start();
			}
		}
		else
			myInternet.showInternetIsNotAvailable();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_my_secondary_market_trade, container, false);
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		super.onHiddenChanged(hidden);
		
		if(hidden == false && isNeedModifyItemState == true && positionOfNeedModifyItem >=0) 
		{
			Map<String, String> map = myTradeList.get(positionOfNeedModifyItem);
			String state = map.get(Constant.MAP_KEY_PUBLISH_KIND[3]);
			
			if(state.equals("未买到"))
				state = "已买到";
			else if(state.equals("未售出"))
				state = "已售出";
			
			map.put(Constant.MAP_KEY_PUBLISH_KIND[3], state);
			myTradeList.set(positionOfNeedModifyItem, map);
			adapter.notifyDataSetChanged();

			isNeedModifyItemState = false;
			positionOfNeedModifyItem = -1;
		}

	}

	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
