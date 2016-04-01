package com.UI.home.secondaryMarket;

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
import com.project.bean.secondaryMarket.SecondaryMarketBean;
import com.project.webServices.secondaryMarketService.DealThisTradeById;
import com.project.webServices.secondaryMarketService.GetScdMktContentAndTeleNumberByid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  �����Ƕ��ֽ��׵�����ҳ�棬 ������תҳ����ҵĽ�����תҳ�湫�ô��࣬
 *  ���������ж�isFromMyTrade = getArguments().getBoolean("isFromMyTrade")����٣�
 *  ��isFromMyTrade = true, ����� ���������Ƿ��ѱ������������Ƿ���ʾ����Ĵ���ť��
 *  ��isFromMyTrade = false, ����ʾ����ť��
 *  	��ֻ�е�����isFromMyTrade = true && ����δ����ʱ������ʾ��ť 
 *  
 *  �����ڵ���ʱ����Ҫͨ��bundle���� title�� publisher�� time�� state�� articleId, itemPosition
 */

/**
 * state״̬�� ��1������ δ��		��2����������		��3������δ�۳�		��4���������۳�
 */

public class SecondaryMarketContentFragment extends Fragment
{
	private boolean isNeedShowDealButton = false;
	private String dealBttnStateNum;
	private String articleId;
	private int itemPosition;
	
	private TextView txtvScdMktTitleOfContent;
	private TextView txtvScdMktPublisherOfContent;
	private TextView txtvScdMktTimeOfContent;
	private TextView txtvScdMktStateOfContent;
	private TextView txtvScdMktTelePhNumOfContent;
	private TextView txtvScdMktContent;
	private TextView txtvConfirmToDealTrade;
	
	private MyInternet myInternet;
	private SecondaryMarketBean bean ;
	
	private AlertDialog ltdgDealThisTrade;
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
			txtvConfirmToDealTrade = (TextView) getActivity().findViewById(R.id.txtvConfirmToDealTrade);
			txtvConfirmToDealTrade.setVisibility(View.VISIBLE);
			
			String temTipString = "";
			if(dealBttnStateNum.equals("1"))
			{
				txtvConfirmToDealTrade.setText("����");
				temTipString = "ȷ�����򵽣�";
			}
			else if(dealBttnStateNum.equals("3"))
			{
				txtvConfirmToDealTrade.setText("���Ѿ��۳�");
				temTipString = "ȷ�����Ѿ��۳���";
			}
			final String ltdgTipString = temTipString;
			
			txtvConfirmToDealTrade.setOnClickListener
			(
					new OnClickListener()
					{
						@Override
						public void onClick(View arg0) 
						{
							if(ltdgDealThisTrade != null)
								ltdgDealThisTrade.show();
							else
								ltdgDealThisTrade = new AlertDialog.Builder(SecondaryMarketContentFragment.this.getActivity())
											.setTitle(ltdgTipString)
											.setIcon(android.R.drawable.ic_dialog_info)
											.setPositiveButton("ȷ��", new ConfirmDealThisTradeListener())
											.setNegativeButton("ȡ��", new CancelDealThisTradeListener())
											.show();
						}
					}
			);
		}
	}
	
	private class CancelDealThisTradeListener implements android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			ltdgDealThisTrade.cancel();
		}
	}

	private class ConfirmDealThisTradeListener implements android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			ltdgDealThisTrade.cancel();
			
			progressDialog.show();
			new DealThisTradeByIdThread(Integer.parseInt(SecondaryMarketContentFragment.this.articleId),
						SecondaryMarketContentFragment.this.dealBttnStateNum).start();
		}
	}
	
		
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			if(msg.what == Constant.MSG_HAVE_GOT_SECONDARY_MARKET_CONTENT)
			{
				String teleNumber = bean.getTelephoneNumber();
				String content = bean.getArticle();
				
				txtvScdMktTelePhNumOfContent.setText(teleNumber);
				txtvScdMktContent.setText(content);
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				myInternet.showInternetIsError();
			}
			else if(msg.what == Constant.MSG_SUCCESS_DEAL_THIS_TRADE_BY_ID)
			{
				progressDialog.cancel();
				String newStateText = "";
				if(dealBttnStateNum.equals("1"))
					newStateText = "����";
				else if(dealBttnStateNum.equals("3"))
					newStateText = "���۳�";
				
				txtvScdMktStateOfContent.setText(newStateText);
				txtvConfirmToDealTrade.setVisibility(View.INVISIBLE);
				MySecondaryMarketTradeFragment.isNeedModifyItemState = true;
				MySecondaryMarketTradeFragment.positionOfNeedModifyItem = itemPosition;
				Toast.makeText(getActivity(), "����ɹ���", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	
	private class DealThisTradeByIdThread extends Thread
	{
		private int id;
		private String stateNum;
		
		public DealThisTradeByIdThread(int id, String stateNum)
		{
			this.id = id;
			this.stateNum = stateNum;
		}

		@Override
		public void run() 
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<Boolean> dealThisTradeById = new DealThisTradeById(id, stateNum);
			Future<Boolean> future = pool.submit(dealThisTradeById);

			try 
			{
				boolean isSuccess = future.get();
				
				Message msg = new Message();
				if(isSuccess == true)
					msg.what = Constant.MSG_SUCCESS_DEAL_THIS_TRADE_BY_ID;
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

	private void initFragment() 
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_SECONDARY_MARKET_CONTENT_FRAGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;

		txtvScdMktTitleOfContent = (TextView) getActivity().findViewById(R.id.txtvScdMktTitleOfContent);
		txtvScdMktPublisherOfContent = (TextView) getActivity().findViewById(R.id.txtvScdMktPublisherOfContent);
		txtvScdMktTimeOfContent = (TextView) getActivity().findViewById(R.id.txtvScdMktTimeOfContent);
		txtvScdMktStateOfContent = (TextView) getActivity().findViewById(R.id.txtvScdMktStateOfContent);
		txtvScdMktTelePhNumOfContent = (TextView) getActivity().findViewById(R.id.txtvScdMktTelePhNumOfContent);
		txtvScdMktContent = (TextView) getActivity().findViewById(R.id.txtvScdMktContent);
		
		myInternet = new MyInternet(getActivity());
		
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		progressDialog.setTitle("��ʾ"); 
		progressDialog.setMessage("���Ժ�..."); 
		progressDialog.setIcon(android.R.drawable.ic_dialog_info); 
		progressDialog.setIndeterminate(false); 
		progressDialog.setCancelable(false); 

		Bundle bundle = this.getArguments();
		itemPosition = bundle.getInt("itemPosition");
		String title = bundle.getString("title");

		txtvScdMktTitleOfContent.setText(title);
		txtvScdMktPublisherOfContent.setText(bundle.getString("publisher"));
		txtvScdMktTimeOfContent.setText(bundle.getString("time"));
		
		String stateText = bundle.getString("state");
		txtvScdMktStateOfContent.setText(stateText);
		
		dealBttnStateNum = getStateNumByStateText(stateText); 

		if(bundle.getBoolean("isFromMyTrade") == true 
				&& (dealBttnStateNum.equals("1") || dealBttnStateNum.equals("3")))
			isNeedShowDealButton = true;
		
		articleId = bundle.getString("articleId");
		loadContentAndTeleNumber(Integer.parseInt(articleId));
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
			Callable<SecondaryMarketBean> getScdMktContentAndTeleNumberByid = new GetScdMktContentAndTeleNumberByid(id);
			Future<SecondaryMarketBean> future = pool.submit(getScdMktContentAndTeleNumberByid);
			
			try 
			{
				bean = new SecondaryMarketBean();
				bean = future.get();
				
				Message msg = new Message();
				if(bean != null)
				{
					msg.what = Constant.MSG_HAVE_GOT_SECONDARY_MARKET_CONTENT;
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
		
		if(stateText.equals("δ��"))
			stateNum = "1";
		else if(stateText.equals("����"))
			stateNum = "2";
		else if(stateText.equals("δ�۳�"))
			stateNum = "3";
		else if(stateText.equals("���۳�"))
			stateNum = "4";

		return stateNum;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.fragment_secondary_market_content, container, false);
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
