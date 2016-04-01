package com.UI.home.secondaryMarket;

import java.text.SimpleDateFormat;
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
import com.project.bean.secondaryMarket.SecondaryMarketBean;
import com.project.webServices.secondaryMarketService.PublishTrade;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class IWantPublishTradeFragment extends Fragment
{
	private RadioButton rdbtISale;	//数据库中 kind字段， 当为售出， kind = “1”
	private RadioButton rdbtIBuy;		//		       	当为买入， kind = “0”
	private RadioButton rdbtFemaleTradeScdMkt;
	private RadioButton rdbtMaleTradeScdMkt;
	
	private EditText dttxTitleTradeScdMkt;
	private EditText dttxArticleTradeScdMkt;
	private EditText dttxPhoneNumberTradeScdMkt;
	private EditText dttxLastNameTradeScdMkt;
	
	private Button bttnConfirmTradeScdMkt;
	
	private AlertDialog ltdgPublishTrade;
	private ProgressDialog progressDialog;
	private MyInternet myInternet;
	
	private SecondaryMarketBean bean;
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment(); 

		dealAction();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.fragment_i_want_publish_trade, container, false);
	}

	private void dealAction()
	{
		bttnConfirmTradeScdMkt.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						if(checkInputIsValid() == true)
						{
							if(ltdgPublishTrade != null)
								ltdgPublishTrade.show();
							else
								ltdgPublishTrade = new AlertDialog.Builder(IWantPublishTradeFragment.this.getActivity())
											.setTitle("确定发布信息？")
											.setIcon(android.R.drawable.ic_dialog_info)
											.setPositiveButton("确定", new ConfirmPublishListener())
											.setNegativeButton("取消", new CancelPublishListener())
											.show();
						}
						
					}
				}
		);	
	}
	private class ConfirmPublishListener implements android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			ltdgPublishTrade.cancel();

			if(myInternet.isInternetAvailable(getActivity()) == true)
			{
				progressDialog.show();
				
				bean = new SecondaryMarketBean();
				bean.setTitle(dttxTitleTradeScdMkt.getText().toString());
				bean.setArticle(dttxArticleTradeScdMkt.getText().toString());
				bean.setTelephoneNumber(dttxPhoneNumberTradeScdMkt.getText().toString());
				
//				String date = DateFormat.getDateInstance().format(new Date());
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");     
				String date = sDateFormat.format(new java.util.Date());  
				bean.setTime(date);
				
				MyAccountSharedPreferences shared = new MyAccountSharedPreferences(getActivity());
				String publisherEmail = shared.getAccountEmail();
				bean.setPublisherEmail(publisherEmail);
				
				String lastName = dttxLastNameTradeScdMkt.getText().toString();
				String publisher = "";
				if(rdbtFemaleTradeScdMkt.isChecked() == true)
					publisher = lastName + "女士";
				else if(rdbtMaleTradeScdMkt.isChecked() == true)
					publisher = lastName + "先生";
				bean.setPublisher(publisher);
				
				String kind = "";
				String state = "";
				//state状态： ”1“代表 未买到		”2“代表已买到		”3“代表未售出		”4“代表已售出				
				if(rdbtISale.isChecked() == true)
				{
					kind = "1";
					state = "3";
				}
				else if(rdbtIBuy.isChecked() == true)
				{
					kind = "0";
					state = "1";
				}
				bean.setKind(kind);
				bean.setState(state);
				
				PublishSecondaryMarketThread thread = new PublishSecondaryMarketThread(bean);
				thread.start();
			}
			else
				myInternet.showInternetIsNotAvailable();
		}
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			
			progressDialog.cancel();
			if(msg.what == Constant.MSG_SUCCESS_PUBLISH_TRADE)
			{
				//TODO 给ArticleFound界面发送消息，提示刷新界面或者添加刚发布的信息
				int newId = msg.getData().getInt("newId");
				bean.setId(newId);
				SecondaryMarketFragment.bean = bean;
				SecondaryMarketFragment.isNeedAddItem = true;
				
				Toast.makeText(getActivity(), "发布成功！", Toast.LENGTH_LONG).show();
			}
			else if(msg.what == Constant.MSG_FAIL_PUBLISH_TRADE)
				Toast.makeText(getActivity(), "发布失败！", Toast.LENGTH_LONG).show();
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
				myInternet.showInternetIsError();

		}
	};

	private class PublishSecondaryMarketThread extends Thread
	{
		private SecondaryMarketBean bean = new SecondaryMarketBean();
		
		public PublishSecondaryMarketThread(SecondaryMarketBean bean)
		{
			this.bean = bean;
		}

		@Override
		public void run()
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<Integer> publishTrade = new PublishTrade(bean);
			Future<Integer> future = pool.submit(publishTrade);

			try 
			{
				int newId = future.get();
				Message msg = new Message();

				if(newId != -1)
				{
					Bundle bundle = new Bundle();
					bundle.putInt("newId", newId);
					msg.setData(bundle);
					msg.what = Constant.MSG_SUCCESS_PUBLISH_TRADE;
				}
				else
					msg.what = Constant.MSG_FAIL_PUBLISH_TRADE;
				
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
	
	private class CancelPublishListener implements  android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			ltdgPublishTrade.cancel();
		}
	}

	protected boolean checkInputIsValid()
	{
		boolean isValid = true;
		
		String title = dttxTitleTradeScdMkt.getText().toString();
		String article = dttxArticleTradeScdMkt.getText().toString();
		String phoneNumber = dttxPhoneNumberTradeScdMkt.getText().toString();
		String lastName = dttxLastNameTradeScdMkt.getText().toString();
		
		Log.i("tag", "lastName = #" + lastName + "#");
		Log.i("tag", "length =" + lastName.length());
		
		if(rdbtISale.isChecked() || rdbtIBuy.isChecked())
		{
			if(isValid == true && title != null && title.length() > 0 && title.length() < 20)
			{
				if(isValid == true && article != null && article.length() > 0 && article.length() < 200)
				{
					if(isValid == true && phoneNumber != null && phoneNumber.matches("[0-9]{7,11}"))
					{
						if(isValid == true && lastName != null && lastName.length() > 0 && lastName.length() < 2)
						{
							if(isValid == true && rdbtFemaleTradeScdMkt.isChecked() || rdbtMaleTradeScdMkt.isChecked())
								;
							else
							{
								isValid = false;
								Toast.makeText(getActivity(), "请选择性别！", Toast.LENGTH_LONG).show();
							}
						}
						else
						{
							isValid = false;
							Toast.makeText(getActivity(), "姓氏有误！应在2个汉字以内！", Toast.LENGTH_LONG).show();
						}
					}
					else
					{
						isValid = false;
						Toast.makeText(getActivity(), "电话号码有误！应在7-11个数字之间！", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					isValid = false;
					Toast.makeText(getActivity(), "输入的正文应在200个汉字以内！", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				isValid = false;
				Toast.makeText(getActivity(), "输入的标题应在20个汉字以内！", Toast.LENGTH_LONG).show();
			}
		}
		else 
		{
			isValid = false;
			Toast.makeText(getActivity(), "请选择交易类型！", Toast.LENGTH_LONG).show();
		}
		
		return isValid;
	}

	private void initFragment() 
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_I_WANT_PUBLISH_TRADE_FARGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;
		
		myInternet = new MyInternet(getActivity());

		rdbtISale = (RadioButton) getActivity().findViewById(R.id.rdbtISale);
		rdbtIBuy = (RadioButton) getActivity().findViewById(R.id.rdbtIBuy);
		rdbtFemaleTradeScdMkt = (RadioButton) getActivity().findViewById(R.id.rdbtFemaleTradeScdMkt);
		rdbtMaleTradeScdMkt = (RadioButton) getActivity().findViewById(R.id.rdbtMaleTradeScdMkt);
		
		dttxTitleTradeScdMkt = (EditText) getActivity().findViewById(R.id.dttxTitleTradeScdMkt);
		dttxArticleTradeScdMkt = (EditText) getActivity().findViewById(R.id.dttxArticleTradeScdMkt);
		dttxPhoneNumberTradeScdMkt = (EditText) getActivity().findViewById(R.id.dttxPhoneNumberTradeScdMkt);
		dttxLastNameTradeScdMkt = (EditText) getActivity().findViewById(R.id.dttxLastNameTradeScdMkt);
		
		bttnConfirmTradeScdMkt = (Button) getActivity().findViewById(R.id.bttnConfirmTradeScdMkt);
		
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		progressDialog.setTitle("提示"); 
		progressDialog.setMessage("请稍后..."); 
		progressDialog.setIcon(android.R.drawable.ic_dialog_info); 
		progressDialog.setIndeterminate(false); 
		progressDialog.setCancelable(false); 
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
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_SECONDARY_MARKET_FRAGMENT;
		
		super.onDestroy();
	}
}
