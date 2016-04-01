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
import com.UI.home.ArticlesFound.ArticlesFoundFragment;
import com.UI.home.ArticlesFound.IWantPublishArticleFoundFragment;
import com.UI.home.ArticlesFound.MyArticleFoundPublishedFragment;
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;
import com.project.Util.MyInternet;
import com.project.Util.MyStack;
import com.project.adapter.PublishKindAdapter;
import com.project.bean.secondaryMarket.SecondaryMarketBean;
import com.project.webServices.secondaryMarketService.GetSecondaryMarketTitle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class SecondaryMarketFragment extends Fragment
{
	public static boolean isDownLoadFinish = true;
	private MyInternet myInternet;
	private ListView lstvSecondaryMarket;
	
	private TextView txtvMyTradeInScdMkt;
	private TextView txtvIWantTradeInScdMkt;
	
//	private SimpleAdapter adapter;
	private PublishKindAdapter adapter;
	private List<Map<String, String>> secondaryMarketList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
	
	public static boolean isNeedAddItem = false;
	public static SecondaryMarketBean bean = new SecondaryMarketBean();

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
		
		dealAction();
	}

	private void dealAction()
	{
		lstvSecondaryMarket.setOnScrollListener
		(
				new OnScrollListener()
				{
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
					{
						int lastCount = view.getLastVisiblePosition() + 1;
						if(lastCount  == totalItemCount  && isDownLoadFinish == true )
							loadSecondaryMarket(totalItemCount);
					}

					@Override
					public void onScrollStateChanged(AbsListView arg0, int arg1) 
					{
					}
				}
		);
		
		lstvSecondaryMarket.setOnItemClickListener
		(
				new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
					{
						if(position < lstvSecondaryMarket.getCount() - 1)
						{
							Map<String, String> map = new HashMap<String, String>();
							map = secondaryMarketList.get(position);
							String title = map.get(Constant.MAP_KEY_PUBLISH_KIND[0]);
							String publisher = map.get(Constant.MAP_KEY_PUBLISH_KIND[1]);
							String time = map.get(Constant.MAP_KEY_PUBLISH_KIND[2]);
							String state = map.get(Constant.MAP_KEY_PUBLISH_KIND[3]);
							String articleId = map.get(Constant.MAP_KEY_PUBLISH_KIND[4]);

							Bundle bundle = new Bundle();
							bundle.putBoolean("isFromMyTrade", false);
							bundle.putString("articleId", articleId);
							bundle.putString("title", title);
							bundle.putString("publisher", publisher);
							bundle.putString("time", time);
							bundle.putString("state", state);

							SecondaryMarketContentFragment frgtSecondaryMarketContent = new SecondaryMarketContentFragment();
							frgtSecondaryMarketContent.setArguments(bundle);

							android.support.v4.app.FragmentTransaction transaction = 
									getActivity().getSupportFragmentManager().beginTransaction();
							transaction.add(R.id.fragmentContainer, frgtSecondaryMarketContent, null)
							.hide(SecondaryMarketFragment.this)
							.show(frgtSecondaryMarketContent);
							new MyStack(getActivity()).addToHomeStack(SecondaryMarketFragment.this);
							transaction.commit();
						}
					}
				}
		);
		
		txtvMyTradeInScdMkt.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View arg0) 
				{
					MySecondaryMarketTradeFragment frgtMySecondaryMarketTrade = new MySecondaryMarketTradeFragment();
					android.support.v4.app.FragmentTransaction transaction = 
							getActivity().getSupportFragmentManager().beginTransaction();
					transaction.add(R.id.fragmentContainer, frgtMySecondaryMarketTrade, null)
					.hide(SecondaryMarketFragment.this)
					.show(frgtMySecondaryMarketTrade);
					new MyStack(getActivity()).addToHomeStack(SecondaryMarketFragment.this);
					transaction.commit();
				}
			}
		);

		txtvIWantTradeInScdMkt.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View arg0) 
				{
					IWantPublishTradeFragment frgtIWantPublishTrade = new IWantPublishTradeFragment();
					android.support.v4.app.FragmentTransaction transaction = 
							getActivity().getSupportFragmentManager().beginTransaction();
					transaction.add(R.id.fragmentContainer, frgtIWantPublishTrade, null)
					.hide(SecondaryMarketFragment.this)
					.show(frgtIWantPublishTrade);
					new MyStack(getActivity()).addToHomeStack(SecondaryMarketFragment.this);
					transaction.commit();
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
			
			if(msg.what == Constant.MSG_NOTIFY_SECONDARY_MARKET_ADAPTER_DATA_CHANGE)
			{
				secondaryMarketList.addAll(newList);
				adapter.notifyDataSetChanged();
				newList.clear();
				
				if(lstvSecondaryMarket.getFooterViewsCount() > 0)
					lstvSecondaryMarket.removeFooterView(MainActivity.onLoadingFooter);
				
				isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_HAVE_GOT_ALL_SECONDARY_MARKET_DATA)
			{
				lstvSecondaryMarket.removeFooterView(MainActivity.onLoadingFooter);
				lstvSecondaryMarket.addFooterView(MainActivity.haveGotAllDataFooter);
				isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{		
				lstvSecondaryMarket.removeFooterView(MainActivity.onLoadingFooter);
				myInternet.showInternetIsError();
			}
		}
	};

	private void initFragment() 
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_SECONDARY_MARKET_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;

		myInternet = new MyInternet(getActivity());
		
		txtvMyTradeInScdMkt = (TextView) getActivity().findViewById(R.id.txtvMyTradeInScdMkt);
		txtvIWantTradeInScdMkt = (TextView) getActivity().findViewById(R.id.txtvIWantTradeInScdMkt);
		lstvSecondaryMarket = (ListView) getActivity().findViewById(R.id.lstvSecondaryMarket);
		adapter = new PublishKindAdapter(getActivity(), secondaryMarketList);

		lstvSecondaryMarket.setDivider(null);
		lstvSecondaryMarket.addFooterView(MainActivity.onLoadingFooter);
		lstvSecondaryMarket.addFooterView(MainActivity.haveGotAllDataFooter);
		lstvSecondaryMarket.setAdapter(adapter);
		lstvSecondaryMarket.removeFooterView(MainActivity.onLoadingFooter);
		lstvSecondaryMarket.removeFooterView(MainActivity.haveGotAllDataFooter);

		loadSecondaryMarket(0);
	}

	private void loadSecondaryMarket(int haveGotScdMktCount) 
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
		{
			if(lstvSecondaryMarket.getFooterViewsCount() == 0)
			{
				lstvSecondaryMarket.addFooterView(MainActivity.onLoadingFooter);
				new ThreadGetSecondaryMarket(haveGotScdMktCount).start();
			}
		}
		else
			myInternet.showInternetIsNotAvailable();
	}

	private class ThreadGetSecondaryMarket extends Thread
	{
		private int haveGotScdMktCount;
		
		public ThreadGetSecondaryMarket(int haveGotScdMktCount)
		{
			this.haveGotScdMktCount = haveGotScdMktCount;
		}
		
		@Override
		public void run()
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<List<Map<String,String>>> getSecondaryMarketTitle = new GetSecondaryMarketTitle(haveGotScdMktCount);
			Future<List<Map<String, String>>> future = pool.submit(getSecondaryMarketTitle);
			
			try 
			{
				newList = future.get();
				Message msg = new Message();
				if(newList != null && newList.size() > 0)
				{
					msg.what = Constant.MSG_NOTIFY_SECONDARY_MARKET_ADAPTER_DATA_CHANGE;
				}
				else
				{
					msg.what = Constant.MSG_HAVE_GOT_ALL_SECONDARY_MARKET_DATA;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.fragment_secondary_market, container, false);
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		super.onHiddenChanged(hidden);
		
		if(hidden == false && isNeedAddItem == true && bean != null)
		{
			addNewItemToListView();
		}
	}

	private void addNewItemToListView() 
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constant.MAP_KEY_PUBLISH_KIND[0], bean.getTitle());
		map.put(Constant.MAP_KEY_PUBLISH_KIND[1], bean.getPublisher());
		map.put(Constant.MAP_KEY_PUBLISH_KIND[2], bean.getTime());
		map.put(Constant.MAP_KEY_PUBLISH_KIND[4], bean.getId() + "");
		//state状态： ”1“代表 未买到		”2“代表已买到		”3“代表未售出		”4“代表已售出
		String state = bean.getState();
		String value = "";
		if(state.equals("1"))
			value = "未买到";
		else if(state.equals("2"))
			value = "已买到";
		else if(state.equals("3"))
			value = "未售出";
		else if(state.equals("4"))
			value = "已售出";
			
		map.put(Constant.MAP_KEY_PUBLISH_KIND[3], value);
		secondaryMarketList.add(0, map);
		adapter.notifyDataSetChanged();
		
		isNeedAddItem = false;
		bean = null;
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
