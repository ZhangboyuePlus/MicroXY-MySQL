package com.UI.micxiyou.news;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.UI.MainActivity;
import com.UI.R;
import com.UI.micxiyou.NewsActivity;
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;
import com.project.Util.MyInternet;
import com.project.webServices.newsServices.GetNewsArticle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SchoolNewsContentFragment extends Fragment
{
	private TextView txtvNewsArticleTitle;
	private TextView txtvNewsArticleReadCount;
	private TextView txtvNewsArticleTime;
	private static TextView txtvNewsArticle;

	private MyInternet myInternet;
	
	private static final int MSG_WHAT_HAVE_GOT_NEWS_ARTICLE = 110;
	private static final int MSG_WHAT_ERROR_GOT_NEWS_ARTICLE = 120;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			if(msg.what == MSG_WHAT_HAVE_GOT_NEWS_ARTICLE)
			{
				Bundle bundle = msg.getData();
/*				String method = bundle.getString("method");
				int position = bundle.getInt("position");
				sendMsgToListFragmentPlusReadCount(position, method);
*/
				String article = bundle.getString("article"); 
				
				txtvNewsArticle.setText(article);
			}
			else if(msg.what == MSG_WHAT_ERROR_GOT_NEWS_ARTICLE || 
					msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				myInternet.showInternetIsError();
			}
		}
	};

/*	protected void sendMsgToListFragmentPlusReadCount(int position, String method) 
	{
		if(method.equals(Constant.METHOD_GET_SCHOOL_NEWS_CONTENT_BY_POSITION))
		{
			SchoolNewsFragment.isNeedPlusReadCount = true;
			SchoolNewsFragment.indexOfItemNeedPlusReadCount = position;
			
			NewsActivity.isNeedShowNewsFragment = true;
			NewsActivity.indexOfNeedShowNewsFragment = Constant.INDEX_SCHOOL_NEWS_FRAGMENT;
		}
		else if(method.equals(Constant.METHOD_GET_SCHOOL_INFO_CONTENT_BY_POSITION))
		{
			SchoolInfoFragment.isNeedPlusReadCount = true;
			SchoolInfoFragment.indexOfItemNeedPlusReadCount = position;
		
			NewsActivity.isNeedShowNewsFragment = true;
			NewsActivity.indexOfNeedShowNewsFragment = Constant.INDEX_SCHOOL_INFO_FRAGMENT;
		}
		else if(method.equals(Constant.METHOD_GET_SCHOOL_ANNOUNCEMENT_CONTENT_BY_POSITION))
		{
			SchoolAnnouncementFragment.isNeedPlusReadCount = true;
			SchoolAnnouncementFragment.indexOfItemNeedPlusReadCount = position;
			
			NewsActivity.isNeedShowNewsFragment = true;
			NewsActivity.indexOfNeedShowNewsFragment = Constant.INDEX_SCHOOL_ANNOUNCEMENT_FRAGMENT;
		}
	}
*/	
	public class GetNewsContent extends Thread
	{
		private int newsPosition;
		private String method;
		
		public GetNewsContent(int position, String method)
		{
			this.newsPosition = position;
			this.method = method;
		}

		@Override
		public void run()
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<String> getNewsArticle = new GetNewsArticle(method, newsPosition);
			Future<String> future = pool.submit(getNewsArticle);
			
			try
			{
				String article = future.get();
				Message msg = new Message();
				if(article != null && article.equals("") == false)
				{
					msg.what = MSG_WHAT_HAVE_GOT_NEWS_ARTICLE;
					
					Bundle bundle = new Bundle();
					bundle.putInt("position", newsPosition);
					bundle.putString("method", method);
					bundle.putString("article", article);
					msg.setData(bundle);
				}
				else
				{
					msg.what = MSG_WHAT_ERROR_GOT_NEWS_ARTICLE;
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

	private void getNewsContentByPosition(int newsPosition, String method) 
	{
		if(myInternet.isInternetAvailable(getActivity()))
		{
			GetNewsContent get = new GetNewsContent(newsPosition, method);
			get.start();
		}
		else
			myInternet.showInternetIsNotAvailable();
	}

	private void initFragment() 
	{
		MainActivity.frgtCurrentNews = this;
		Constant.CURRENT_NEWS_PAGE_STATE = Constant.PAGE_STATE_SCHOOL_NEWS_CONTENT_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_NEWS_FRAGMENT] = true;
		
		myInternet = new MyInternet(getActivity());
		
		txtvNewsArticleTitle = (TextView) getActivity().findViewById(R.id.txtvNewsArticleTitle);
		txtvNewsArticleReadCount = (TextView) getActivity().findViewById(R.id.txtvNewsArticleReadCount);
		txtvNewsArticleTime = (TextView) getActivity().findViewById(R.id.txtvNewsArticleTime);
		txtvNewsArticle = (TextView) getActivity().findViewById(R.id.txtvNewsArticle);

		Bundle bundle = this.getArguments();
		
		txtvNewsArticleTitle.setText(bundle.getString("newsTitle"));
		txtvNewsArticleReadCount.setText(bundle.getString("newsReadCount"));
		txtvNewsArticleTime.setText(bundle.getString("newsTime"));

		String method = bundle.getString("method");
		
		int newsPosition = bundle.getInt("newsPosition");
		getNewsContentByPosition(newsPosition, method);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.fragment_school_news_content, null);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden)
	{
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() 
	{
		Constant.CURRENT_NEWS_PAGE_STATE = Constant.PAGE_STATE_NEWS_TITLE_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(false);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_NEWS_FRAGMENT] = false;

		super.onDestroy();
	}
}
