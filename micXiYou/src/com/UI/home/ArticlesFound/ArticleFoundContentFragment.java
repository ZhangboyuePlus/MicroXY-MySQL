package com.UI.home.ArticlesFound;

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
import com.project.bean.articleFound.ArticleFoundBean;
import com.project.webServices.articleFoundService.DealThisPublishedById;
import com.project.webServices.articleFoundService.GetAtcFdContentAndTeleNumberByid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  该类是失物招领的内容页面， 大众跳转页面和我的发布跳转页面公用此类，
 *  区别在于判断isFromMyPublished = getArguments().getBoolean("isFromMyPublished")的真假；
 *  若isFromMyPublished = true, 则根据 这条发布是否已被处理来决定是否显示下面的处理按钮；
 *  若isFromMyPublished = false, 则不显示处理按钮；
 *  	即只有当　（isFromMyPublished = true && 发布未处理）时，才显示按钮 
 *  
 *  该类在调用时，需要通过bundle传递 title， publisher， time， state， articleId, itemPosition
 *  
 *  
 *  
	ArticleFound在调用Content的页面时，将所点击的Item的Index传到ContentActivity中，
	假如 ContentActivity 对 ThisPublished状态做过修改，则修改成功后将ArticleFound类中的静态值：
	isNeedModify和indexNeedModify修改为需要，ContentActivity结束后，会将ArticleFound显示，
	则在ArticleFound显示时， 根据那两个静态值做list的修改，并刷新ListView, 最后一定要将那两个静态值修改回原来的初始值。
 * @author Administrator
 */
public class ArticleFoundContentFragment extends Fragment 
{
	private boolean isNeedShowDealButton = false;
	private String dealBttnStateNum;
	private String articleId;
	private int itemPosition;
	
	private TextView txtvAtcFdTitleOfContent;
	private TextView txtvAtcFdPublisherOfContent;
	private TextView txtvAtcFdTimeOfContent;
	private TextView txtvAtcFdStateOfContent;
	private TextView txtvAtcFdTelePhNumOfContent;
	private TextView txtvAtcFdContent;
	private TextView txtvConfirmToDeal;
	
	private MyInternet myInternet;
	private ArticleFoundBean bean ;
	
	private AlertDialog ltdgDealThisPublished;
	private ProgressDialog progressDialog;
	
	public static boolean isDownLoadFinish = true;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
		
		dealAction();
	}
	
	private void dealAction() 
	{
		if(isNeedShowDealButton == true)
		{
			txtvConfirmToDeal = (TextView) getActivity().findViewById(R.id.txtvConfirmToDeal);
			txtvConfirmToDeal.setVisibility(View.VISIBLE);
			
			String temTipString = "";
			if(dealBttnStateNum.equals("1"))
			{
				txtvConfirmToDeal.setText("已有人认领");
				temTipString = "确定已经有人认领？";
			}
			else if(dealBttnStateNum.equals("3"))
			{
				txtvConfirmToDeal.setText("我已经找到");
				temTipString = "确定你已经找到？";
			}
			final String ltdgTipString = temTipString;
			
			txtvConfirmToDeal.setOnClickListener
			(
					new OnClickListener()
					{
						@Override
						public void onClick(View arg0) 
						{
							if(ltdgDealThisPublished != null)
								ltdgDealThisPublished.show();
							else
								ltdgDealThisPublished = new AlertDialog.Builder(ArticleFoundContentFragment.this.getActivity())
											.setTitle(ltdgTipString)
											.setIcon(android.R.drawable.ic_dialog_info)
											.setPositiveButton("确定", new ConfirmDealThisPublishedListener())
											.setNegativeButton("取消", new CancelDealThisPublishedListener())
											.show();
						}
					}
			);
		}
	}

	private class CancelDealThisPublishedListener implements android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			ltdgDealThisPublished.cancel();
		}
	}

	private class ConfirmDealThisPublishedListener implements android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			ltdgDealThisPublished.cancel();
			
			progressDialog.show();
			new DealThisPublishedByIdThread(Integer.parseInt(ArticleFoundContentFragment.this.articleId),
											ArticleFoundContentFragment.this.dealBttnStateNum).start();
		}
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);

			if(msg.what == Constant.MSG_HAVE_GOT_ARTICLE_FOUND_CONTENT)
			{
				String teleNumber = bean.getTelephoneNumber();
				String content = bean.getArticle();
				
				txtvAtcFdTelePhNumOfContent.setText(teleNumber);
				txtvAtcFdContent.setText(content);
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				myInternet.showInternetIsError();
			}
			else if(msg.what == Constant.MSG_SUCCESS_DEAL_THIS_PUBLISHED_BY_ID)
			{
				progressDialog.cancel();
				String newStateText = "";
				if(dealBttnStateNum.equals("1"))
					newStateText = "已认领";
				else if(dealBttnStateNum.equals("3"))
					newStateText = "已找到";
				
				txtvAtcFdStateOfContent.setText(newStateText);
				txtvConfirmToDeal.setVisibility(View.INVISIBLE);
				MyArticleFoundPublishedFragment.isNeedModifyItemState = true;
				MyArticleFoundPublishedFragment.positionOfNeedModifyItem = itemPosition;
				Toast.makeText(getActivity(), "处理成功！", Toast.LENGTH_LONG).show();
			}
		}
	};

	private class DealThisPublishedByIdThread extends Thread
	{
		private int id;
		private String stateNum;
		
		public DealThisPublishedByIdThread(int id, String stateNum)
		{
			this.id = id;
			this.stateNum = stateNum;
		}

		@Override
		public void run() 
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<Boolean> dealThisPublishedById = new DealThisPublishedById(id, stateNum);
			Future<Boolean> future = pool.submit(dealThisPublishedById);

			try 
			{
				boolean isSuccess = future.get();
				
				Message msg = new Message();
				if(isSuccess == true)
					msg.what = Constant.MSG_SUCCESS_DEAL_THIS_PUBLISHED_BY_ID;
				else 
					msg.what = Constant.MSG_SHOW_INTERNET_IS_ERROR;
				
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
	
	private class LoadContentAndTeleNumberByid extends Thread
	{
		private int id;
		
		public LoadContentAndTeleNumberByid(int id)
		{
			this.id = id;
		}

		@Override
		public void run() 
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<ArticleFoundBean> getAtcFdContentAndTeleNumberByid = new GetAtcFdContentAndTeleNumberByid(id);
			Future<ArticleFoundBean> future = pool.submit(getAtcFdContentAndTeleNumberByid);
			
			try 
			{
				bean = new ArticleFoundBean();
				bean = future.get();
				
				Message msg = new Message();
				if(bean != null)
				{
					msg.what = Constant.MSG_HAVE_GOT_ARTICLE_FOUND_CONTENT;
				}
				else
				{
					msg.what = Constant.MSG_SHOW_INTERNET_IS_ERROR;
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
	
	private void loadContentAndTeleNumber(int id) 
	{
		if(myInternet.isInternetAvailable(getActivity()) == true)
			new LoadContentAndTeleNumberByid(id).start();
		else
			myInternet.showInternetIsNotAvailable();
	}

	private String getStateNumByStateText(String stateText)
	{
		String stateNum = "";
		
		if(stateText.equals("未认领"))
			stateNum = "1";
		else if(stateText.equals("已认领"))
			stateNum = "2";
		else if(stateText.equals("未找到"))
			stateNum = "3";
		else if(stateText.equals("已找到"))
			stateNum = "4";

		return stateNum;
	}

	private void initFragment() 
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_ARTICLES_FOUND_CONTENT_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;

		txtvAtcFdTitleOfContent = (TextView) getActivity().findViewById(R.id.txtvAtcFdTitleOfContent);
		txtvAtcFdPublisherOfContent = (TextView) getActivity().findViewById(R.id.txtvAtcFdPublisherOfContent);
		txtvAtcFdTimeOfContent = (TextView) getActivity().findViewById(R.id.txtvAtcFdTimeOfContent);
		txtvAtcFdStateOfContent = (TextView) getActivity().findViewById(R.id.txtvAtcFdStateOfContent);
		txtvAtcFdTelePhNumOfContent = (TextView) getActivity().findViewById(R.id.txtvAtcFdTelePhNumOfContent);
		txtvAtcFdContent = (TextView) getActivity().findViewById(R.id.txtvAtcFdContent);
		
		myInternet = new MyInternet(getActivity());
		
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		progressDialog.setTitle("提示"); 
		progressDialog.setMessage("请稍后..."); 
		progressDialog.setIcon(android.R.drawable.ic_dialog_info); 
		progressDialog.setIndeterminate(false); 
		progressDialog.setCancelable(false); 

		Bundle bundle = this.getArguments();
		itemPosition = bundle.getInt("itemPosition");
		String title = bundle.getString("title");

		txtvAtcFdTitleOfContent.setText(title);
		txtvAtcFdPublisherOfContent.setText(bundle.getString("publisher"));
		txtvAtcFdTimeOfContent.setText(bundle.getString("time"));
		
		String stateText = bundle.getString("state");
		txtvAtcFdStateOfContent.setText(stateText);
		dealBttnStateNum = getStateNumByStateText(stateText); 

		if(bundle.getBoolean("isFromMyPublished") == true 
				&& (dealBttnStateNum.equals("1") || dealBttnStateNum.equals("3")))
			isNeedShowDealButton = true;
		
		articleId = bundle.getString("articleId");
		loadContentAndTeleNumber(Integer.parseInt(articleId));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_article_found_content, container, false);
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() 
	{
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_ARTICLES_FOUND_FRAGMENT;

		super.onDestroy();
	}
}