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
import com.project.Util.MyAccountSharedPreferences;
import com.project.Util.MyInternet;
import com.project.Util.MyStack;
import com.project.adapter.PublishKindAdapter;
import com.project.webServices.articleFoundService.GetMyArticleFoundPublishedTitle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyArticleFoundPublishedFragment extends Fragment
{
	public static boolean isDownLoadFinish = true;
	private MyInternet myInternet;
	
	private ListView lsvwMyPublishedArticleFound;
//	private SimpleAdapter adapter;
	private PublishKindAdapter adapter;
	private List<Map<String, String>> myArticleFoundList = new ArrayList<Map<String, String>>();
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
		lsvwMyPublishedArticleFound.setOnScrollListener
		(
				new OnScrollListener()
				{
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
					{
						int lastCount = view.getLastVisiblePosition() + 1;
						if(lastCount  == totalItemCount  && isDownLoadFinish == true )
							loadMyArticleFoundPublished(totalItemCount);
					}

					@Override
					public void onScrollStateChanged(AbsListView arg0, int arg1)  
					{
					}
				}
		);
		
		lsvwMyPublishedArticleFound.setOnItemClickListener
		(
				new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
					{
						if(position < lsvwMyPublishedArticleFound.getCount() - 1)
						{
							Map<String, String> map = new HashMap<String, String>();
							map = myArticleFoundList.get(position);
							String title = map.get(Constant.MAP_KEY_PUBLISH_KIND[0]);
							String publisher = map.get(Constant.MAP_KEY_PUBLISH_KIND[1]);
							String time = map.get(Constant.MAP_KEY_PUBLISH_KIND[2]);
							String state = map.get(Constant.MAP_KEY_PUBLISH_KIND[3]);
							String articleId = map.get(Constant.MAP_KEY_PUBLISH_KIND[4]);

							Bundle bundle = new Bundle();
							bundle.putBoolean("isFromMyPublished", true);
							bundle.putString("articleId", articleId);
							bundle.putString("title", title);
							bundle.putString("publisher", publisher);
							bundle.putString("time", time);
							bundle.putString("state", state);
							bundle.putInt("itemPosition", position);
							

							ArticleFoundContentFragment frgtArticleFoundContent = new ArticleFoundContentFragment();
							frgtArticleFoundContent.setArguments(bundle);

							android.support.v4.app.FragmentTransaction transaction = 
									getActivity().getSupportFragmentManager().beginTransaction();
							transaction.add(R.id.fragmentContainer, frgtArticleFoundContent, null)
							.hide(MyArticleFoundPublishedFragment.this)
							.show(frgtArticleFoundContent);
							new MyStack(getActivity()).addToHomeStack(MyArticleFoundPublishedFragment.this);
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
			
			if(msg.what == Constant.MSG_NOTIFY_MY_ARTICLE_FOUND_PUBLISHED_ADAPTER_DATA_CHANGE)
			{
				myArticleFoundList.addAll(newList);
				adapter.notifyDataSetChanged();
				newList.clear();
				
				if(lsvwMyPublishedArticleFound.getFooterViewsCount() > 0)
					lsvwMyPublishedArticleFound.removeFooterView(MainActivity.onLoadingFooter);
				
				isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_HAVE_GOT_ALL_MY_ARTICLE_FOUND_PUBLISHED_DATA)
			{
				lsvwMyPublishedArticleFound.removeFooterView(MainActivity.onLoadingFooter);
				lsvwMyPublishedArticleFound.addFooterView(MainActivity.haveGotAllDataFooter);
				isDownLoadFinish = true;
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				lsvwMyPublishedArticleFound.removeFooterView(MainActivity.onLoadingFooter);
				myInternet.showInternetIsError();
			}
		}
	};
	
	private class TheadGetMyArticleFoundPublished extends Thread
	{
		private int haveGotAtcFdCount;
		private String registedEmail;
		
		public TheadGetMyArticleFoundPublished(int haveGotAtcFdCount, String registedEmail)
		{
			this.haveGotAtcFdCount = haveGotAtcFdCount;
			this.registedEmail = registedEmail;
		}
		
		@Override
		public void run()
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<List<Map<String,String>>> getMyArticleFoundPublishedTitle = 
					new GetMyArticleFoundPublishedTitle(haveGotAtcFdCount, registedEmail);
			Future<List<Map<String, String>>> future = pool.submit(getMyArticleFoundPublishedTitle);
			
			try 
			{
				newList = future.get();
				Message msg = new Message();
				if(newList != null && newList.size() > 0)
					msg.what = Constant.MSG_NOTIFY_MY_ARTICLE_FOUND_PUBLISHED_ADAPTER_DATA_CHANGE;
				else
					msg.what = Constant.MSG_HAVE_GOT_ALL_MY_ARTICLE_FOUND_PUBLISHED_DATA;

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
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_MY_ARTICLE_FOUND_PUBLISHED_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;
		
		myInternet = new MyInternet(getActivity());
		
		lsvwMyPublishedArticleFound = (ListView) getActivity().findViewById(R.id.lsvwMyPublishedArticleFound);
		adapter = new PublishKindAdapter(getActivity(), myArticleFoundList);

		lsvwMyPublishedArticleFound.setDivider(null);
		lsvwMyPublishedArticleFound.addFooterView(MainActivity.onLoadingFooter);
		lsvwMyPublishedArticleFound.addFooterView(MainActivity.haveGotAllDataFooter);
		lsvwMyPublishedArticleFound.setAdapter(adapter);
		lsvwMyPublishedArticleFound.removeFooterView(MainActivity.onLoadingFooter);
		lsvwMyPublishedArticleFound.removeFooterView(MainActivity.haveGotAllDataFooter);

		loadMyArticleFoundPublished(0);
	}
	
	private void loadMyArticleFoundPublished(int haveGotAtcFdCount) 
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
		{
			if(lsvwMyPublishedArticleFound.getFooterViewsCount() == 0)
			{
				lsvwMyPublishedArticleFound.addFooterView(MainActivity.onLoadingFooter);
				
				String Email = new MyAccountSharedPreferences(getActivity()).getAccountEmail();
				new TheadGetMyArticleFoundPublished(haveGotAtcFdCount, Email).start();
			}
		}
		else
			myInternet.showInternetIsNotAvailable();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.fragment_my_article_found_published, 
					container, false);
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		super.onHiddenChanged(hidden);
		
		if(hidden == false && isNeedModifyItemState == true && positionOfNeedModifyItem >=0) 
		{
			Map<String, String> map = myArticleFoundList.get(positionOfNeedModifyItem);
			String state = map.get(Constant.MAP_KEY_PUBLISH_KIND[3]);
			
			if(state.equals("未找到"))
				state = "已找到";
			else if(state.equals("未认领"))
				state = "已认领";
			
			map.put(Constant.MAP_KEY_PUBLISH_KIND[3], state);
			myArticleFoundList.set(positionOfNeedModifyItem, map);
			adapter.notifyDataSetChanged();

			isNeedModifyItemState = false;
			positionOfNeedModifyItem = -1;
		}
	}

	@Override
	public void onDestroy() 
	{
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_ARTICLES_FOUND_FRAGMENT;
		
		super.onDestroy();
	}
}
