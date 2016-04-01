package com.UI.home.ArticlesFound;

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
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;
import com.project.Util.MyInternet;
import com.project.Util.MyStack;
import com.project.adapter.PublishKindAdapter;
import com.project.bean.articleFound.ArticleFoundBean;
import com.project.webServices.articleFoundService.GetArticleFoundTitle;

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
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ArticlesFoundFragment extends Fragment
{
	public static boolean isDownLoadFinish = true;
	private MyInternet myInternet;
	private ListView lstvArticleFound;
	
	private TextView txtvMyPublishInArticleFound;
	private TextView txtvIWantPublishInArticleFound;
	
//	private SimpleAdapter adapter;
	private PublishKindAdapter adapter;
	private List<Map<String, String>> articleFoundList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
	
	public static boolean isNeedAddItem = false;
	public static ArticleFoundBean bean = new ArticleFoundBean();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
		dealAction();
	}
	
	private void dealAction() 
	{
		lstvArticleFound.setOnScrollListener
		(
				new OnScrollListener()
				{
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
					{
						int lastCount = view.getLastVisiblePosition() + 1;
						if(lastCount  == totalItemCount  && isDownLoadFinish == true )
							loadArticleFound(totalItemCount);
					}

					@Override
					public void onScrollStateChanged(AbsListView arg0, int arg1) 
					{
					}
				}
		);
		
		lstvArticleFound.setOnItemClickListener
		(
				new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
					{
						if(position < lstvArticleFound.getCount() - 1)
						{
							Map<String, String> map = new HashMap<String, String>();
							map = articleFoundList.get(position);
							String title = map.get(Constant.MAP_KEY_PUBLISH_KIND[0]);
							String publisher = map.get(Constant.MAP_KEY_PUBLISH_KIND[1]);
							String time = map.get(Constant.MAP_KEY_PUBLISH_KIND[2]);
							String state = map.get(Constant.MAP_KEY_PUBLISH_KIND[3]);
							String articleId = map.get(Constant.MAP_KEY_PUBLISH_KIND[4]);

							Bundle bundle = new Bundle();
							bundle.putBoolean("isFromMyPublished", false);
							bundle.putString("articleId", articleId);
							bundle.putString("title", title);
							bundle.putString("publisher", publisher);
							bundle.putString("time", time);
							bundle.putString("state", state);

							ArticleFoundContentFragment frgtArticleFoundContent = new ArticleFoundContentFragment();
							frgtArticleFoundContent.setArguments(bundle);

							android.support.v4.app.FragmentTransaction transaction = 
									getActivity().getSupportFragmentManager().beginTransaction();
							transaction.add(R.id.fragmentContainer, frgtArticleFoundContent, null)
							.hide(ArticlesFoundFragment.this)
							.show(frgtArticleFoundContent);
							new MyStack(getActivity()).addToHomeStack(ArticlesFoundFragment.this);
							transaction.commit();
						}
					}
				}
		);
		
		txtvMyPublishInArticleFound.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						MyArticleFoundPublishedFragment frgtMyArticleFoundPublished = new MyArticleFoundPublishedFragment();
						android.support.v4.app.FragmentTransaction transaction = 
								getActivity().getSupportFragmentManager().beginTransaction();
						transaction.add(R.id.fragmentContainer, frgtMyArticleFoundPublished, null)
						.hide(ArticlesFoundFragment.this)
						.show(frgtMyArticleFoundPublished);
						new MyStack(getActivity()).addToHomeStack(ArticlesFoundFragment.this);
						transaction.commit();
					}
				}
		);
		
		txtvIWantPublishInArticleFound.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						IWantPublishArticleFoundFragment frgtIWantPublishArticleFoundFragment = new IWantPublishArticleFoundFragment();
						android.support.v4.app.FragmentTransaction transaction = 
								getActivity().getSupportFragmentManager().beginTransaction();
						transaction.add(R.id.fragmentContainer, frgtIWantPublishArticleFoundFragment, null)
						.hide(ArticlesFoundFragment.this)
						.show(frgtIWantPublishArticleFoundFragment);
						new MyStack(getActivity()).addToHomeStack(ArticlesFoundFragment.this);
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
			
			if(msg.what == Constant.MSG_NOTIFY_ARTICLE_FOUND_ADAPTER_DATA_CHANGE)
			{
				articleFoundList.addAll(newList);
				adapter.notifyDataSetChanged();
				newList.clear();
				
				if(lstvArticleFound.getFooterViewsCount() > 0)
					lstvArticleFound.removeFooterView(MainActivity.onLoadingFooter);
				
				isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_HAVE_GOT_ALL_ARTICLE_FOUND_DATA)
			{
				lstvArticleFound.removeFooterView(MainActivity.onLoadingFooter);
				lstvArticleFound.addFooterView(MainActivity.haveGotAllDataFooter);
				isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{		
				lstvArticleFound.removeFooterView(MainActivity.onLoadingFooter);
				myInternet.showInternetIsError();
			}
		}
	};
	
	private void initFragment()
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_ARTICLES_FOUND_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;

		myInternet = new MyInternet(getActivity());
		
		txtvMyPublishInArticleFound = (TextView) getActivity().findViewById(R.id.txtvMyPublishInArticleFound);
		txtvIWantPublishInArticleFound = (TextView) getActivity().findViewById(R.id.txtvIWantPublishInArticleFound);
		lstvArticleFound = (ListView) getActivity().findViewById(R.id.lstvArticleFound);
		adapter = new PublishKindAdapter(getActivity(), articleFoundList);

		lstvArticleFound.setDivider(null);
		lstvArticleFound.addFooterView(MainActivity.onLoadingFooter);
		lstvArticleFound.addFooterView(MainActivity.haveGotAllDataFooter);
		lstvArticleFound.setAdapter(adapter);
		lstvArticleFound.removeFooterView(MainActivity.onLoadingFooter);
		lstvArticleFound.removeFooterView(MainActivity.haveGotAllDataFooter);

		loadArticleFound(0);
}

	private void loadArticleFound(int haveGotAtcFdCount) 
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
		{
			if(lstvArticleFound.getFooterViewsCount() == 0)
			{
				lstvArticleFound.addFooterView(MainActivity.onLoadingFooter);
				new ThreadGetArticleFound(haveGotAtcFdCount).start();
			}
		}
		else
			myInternet.showInternetIsNotAvailable();
	}
	
	private class ThreadGetArticleFound extends Thread
	{
		private int haveGotAtcFdCount;
		
		public ThreadGetArticleFound(int haveGotAtcFdCount)
		{
			this.haveGotAtcFdCount = haveGotAtcFdCount;
		}
		
		@Override
		public void run()
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<List<Map<String,String>>> getArticleFoundTitle = new GetArticleFoundTitle(haveGotAtcFdCount);
			Future<List<Map<String, String>>> future = pool.submit(getArticleFoundTitle);
			
			try 
			{
				newList = future.get();
				Message msg = new Message();
				if(newList != null && newList.size() > 0)
				{
					msg.what = Constant.MSG_NOTIFY_ARTICLE_FOUND_ADAPTER_DATA_CHANGE;
				}
				else
				{
					msg.what = Constant.MSG_HAVE_GOT_ALL_ARTICLE_FOUND_DATA;
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
		// TODO Auto-generated method stub
		return  inflater.inflate(R.layout.fragment_articles_found, container, false);
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		// TODO Auto-generated method stub
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
		//state状态： ”1“代表 未认领		”2“代表已认领		”3“代表未找到		”4“代表已找到
		String state = bean.getState();
		String value = "";
		if(state.equals("1"))
			value = "未认领";
		else if(state.equals("2"))
			value = "已认领";
		else if(state.equals("3"))
			value = "未找到";
		else if(state.equals("4"))
			value = "已找到";
			
		map.put(Constant.MAP_KEY_PUBLISH_KIND[3], value);
		articleFoundList.add(0, map);
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
