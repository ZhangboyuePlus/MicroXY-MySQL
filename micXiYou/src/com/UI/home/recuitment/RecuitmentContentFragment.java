package com.UI.home.recuitment;

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
import com.project.webServices.recuitmentServices.GetRecuitmentArticleByPosition;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecuitmentContentFragment extends Fragment 
{
	private TextView txtvRecuitmentArticleTitle;
	private TextView txtvRecuitmentArticleReadCount;
	private TextView txtvRecuitmentArticleTime;
	private TextView txtvRecuitmentArticle;

	private String recuitmentArticle;

	private MyInternet myInternet;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);

		initFragment();
	}

	private void initFragment()
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_RECUITMENT_CONTENT_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;

		myInternet = new MyInternet(getActivity());
		
		txtvRecuitmentArticleTitle = (TextView) getActivity().findViewById(R.id.txtvRecuitmentArticleTitle);
		txtvRecuitmentArticleReadCount = (TextView) getActivity().findViewById(R.id.txtvRecuitmentArticleReadCount);
		txtvRecuitmentArticleTime = (TextView) getActivity().findViewById(R.id.txtvRecuitmentArticleTime);
		txtvRecuitmentArticle = (TextView) getActivity().findViewById(R.id.txtvRecuitmentArticle);
		
		Bundle bundle = this.getArguments();
		
		txtvRecuitmentArticleTitle.setText(bundle.getString("recuitmentTitle"));
		txtvRecuitmentArticleReadCount.setText(bundle.getString("recuitmentReadCount"));
		txtvRecuitmentArticleTime.setText(bundle.getString("recuitmentTime"));
		int recuitmentPosition = bundle.getInt("recuitmentPosition");

		loadRecuitmentContent(recuitmentPosition);
	}

	private void loadRecuitmentContent(int recuitmentPosition) 
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
			getRecuitmentArticleByPosition(recuitmentPosition);
		else
			myInternet.showInternetIsNotAvailable();
	}

	private void getRecuitmentArticleByPosition(int recuitmentPosition) 
	{
		new LoadRecuitmentArticleByPos(recuitmentPosition).start();
	}

	public Handler handler  = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			if(msg.what == Constant.MSG_HAVE_GOT_RECUITMENT_ARTICLE)
			{
				txtvRecuitmentArticle.setText(recuitmentArticle);
				
				RecuitmentFragment.isNeedPlusReadCount = true;
				RecuitmentFragment.needPlusItemPosition = msg.getData().getInt("position");
			}
			else if(msg.what == Constant.MSG_ERROR_GOT_RECUITMENT_ARTICLE
					|| msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				myInternet.showInternetIsError();
			}
		}
	};
	
	class LoadRecuitmentArticleByPos extends Thread
	{
		private int position;
		
		public LoadRecuitmentArticleByPos(int position)
		{
			this.position = position;
		}

		@Override
		public void run()
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<String> getRecuitmentContent = new GetRecuitmentArticleByPosition(position);
			Future<String> future = pool.submit(getRecuitmentContent);
			
			try 
			{
				recuitmentArticle = future.get();
				
				Message msg = new Message();
				if(recuitmentArticle != null && recuitmentArticle.equals("") == false)
				{
					msg.what = Constant.MSG_HAVE_GOT_RECUITMENT_ARTICLE;
					
					Bundle bundle = new Bundle();
					bundle.putInt("position", position);
					msg.setData(bundle);
				}
				else
				{
					msg.what = Constant. MSG_ERROR_GOT_RECUITMENT_ARTICLE;
				}
				handler.sendMessage(msg);
			} catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				myInternet.showInternetIsError();
				return;
			} catch (ExecutionException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				myInternet.showInternetIsError();
				return;
			}
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.fragment_recuitment_content,container , false);
	}

	@Override
	public void onHiddenChanged(boolean hidden)
	{
		super.onHiddenChanged(hidden);
		
		if(hidden == false)
		{
		}
	}

	@Override
	public void onDestroy() 
	{
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_RECUITMENT_TITLE_FRAGMENT;

		super.onDestroy();
	}
}