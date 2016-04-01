package com.UI.home.ArticlesFound;

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
import com.project.bean.articleFound.ArticleFoundBean;
import com.project.webServices.articleFoundService.PublishArticleFound;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class IWantPublishArticleFoundFragment extends Fragment
{
	private RadioButton rdbtIPickUp;	//���ݿ��� kind�ֶΣ� ��Ϊ�񵽶���ʱ�� kind = ��1��
	private RadioButton rdbtILost;		//		       	��Ϊ��������ʱ�� kind = ��0��
	private RadioButton rdbtFemalePublishAtcFd;
	private RadioButton rdbtMalePublishAtcFd;
	
	private EditText dttxTitlePulishAtcFd;
	private EditText dttxArticlePublishAtcFd;
	private EditText dttxPhoneNumberPublishAtcFd;
	private EditText dttxLastNamePublishAtcFd;
	
	private Button bttnConfirmPublishAtcFd;
	
	private AlertDialog ltdgPublishArticleFound;
	private ProgressDialog progressDialog;
	private MyInternet myInternet;
	
	private ArticleFoundBean bean;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		initFragment();
		
		dealAction();
	}

	private void dealAction() 
	{
		bttnConfirmPublishAtcFd.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						if(checkInputIsValid() == true)
						{
							if(ltdgPublishArticleFound != null)
								ltdgPublishArticleFound.show();
							else
								ltdgPublishArticleFound = new AlertDialog.Builder(IWantPublishArticleFoundFragment.this.getActivity())
											.setTitle("ȷ��������Ϣ��")
											.setIcon(android.R.drawable.ic_dialog_info)
											.setPositiveButton("ȷ��", new ConfirmPublishListener())
											.setNegativeButton("ȡ��", new CancelPublishListener())
											.show();
						}
						
					}
				}
		);	
	}
	
	private class CancelPublishListener implements  android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			ltdgPublishArticleFound.cancel();
		}
	}
	
	private class ConfirmPublishListener implements android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			ltdgPublishArticleFound.cancel();

			if(myInternet.isInternetAvailable(getActivity()) == true)
			{
				progressDialog.show();
				
				bean = new ArticleFoundBean();
				bean.setTitle(dttxTitlePulishAtcFd.getText().toString());
				bean.setArticle(dttxArticlePublishAtcFd.getText().toString());
				bean.setTelephoneNumber(dttxPhoneNumberPublishAtcFd.getText().toString());
				
//				String date = DateFormat.getDateInstance().format(new Date());
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");     
				String date = sDateFormat.format(new java.util.Date());  
				bean.setTime(date);
				
				MyAccountSharedPreferences shared = new MyAccountSharedPreferences(getActivity());
				String publisherEmail = shared.getAccountEmail();
				bean.setPublisherEmail(publisherEmail);
				
				String lastName = dttxLastNamePublishAtcFd.getText().toString();
				String publisher = "";
				if(rdbtFemalePublishAtcFd.isChecked() == true)
					publisher = lastName + "Ůʿ";
				else if(rdbtMalePublishAtcFd.isChecked() == true)
					publisher = lastName + "����";
				bean.setPublisher(publisher);
				
				String kind = "";
				String state = "";
				//state״̬�� ��1������ δ����		��2������������		��3������δ�ҵ�		��4���������ҵ�
				if(rdbtIPickUp.isChecked() == true)
				{
					kind = "1";
					state = "1";
				}
				else if(rdbtILost.isChecked() == true)
				{
					kind = "0";
					state = "3";
				}
				bean.setKind(kind);
				bean.setState(state);
				
				PublishArticleFoundThread thread = new PublishArticleFoundThread(bean);
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
			if(msg.what == Constant.MSG_SUCCESS_PUBLISH_ARTICLE_FOUND)
			{
				//TODO ��ArticleFound���淢����Ϣ����ʾˢ�½��������Ӹշ�������Ϣ
				int newId = msg.getData().getInt("newId");
				bean.setId(newId);
				ArticlesFoundFragment.bean = bean;
				ArticlesFoundFragment.isNeedAddItem = true;
				
				Toast.makeText(getActivity(), "�����ɹ���", Toast.LENGTH_LONG).show();
			}
			else if(msg.what == Constant.MSG_FAIL_PUBLISH_ARTICLE_FOUND)
				Toast.makeText(getActivity(), "����ʧ�ܣ�", Toast.LENGTH_LONG).show();
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
				myInternet.showInternetIsError();
		}
	};
	
	private class PublishArticleFoundThread extends Thread
	{
		private ArticleFoundBean bean = new ArticleFoundBean();
		
		public PublishArticleFoundThread(ArticleFoundBean bean)
		{
			this.bean = bean;
		}

		@Override
		public void run()
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<Integer> publishArticleFound = new PublishArticleFound(bean);
			Future<Integer> future = pool.submit(publishArticleFound);

			try 
			{
				int newId = future.get();
				Message msg = new Message();

				if(newId != -1)
				{
					Bundle bundle = new Bundle();
					bundle.putInt("newId", newId);
					msg.setData(bundle);
					msg.what = Constant.MSG_SUCCESS_PUBLISH_ARTICLE_FOUND;
				}
				else
					msg.what = Constant.MSG_FAIL_PUBLISH_ARTICLE_FOUND;
				
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

	protected boolean checkInputIsValid() 
	{
		boolean isValid = true;
		
		String title = dttxTitlePulishAtcFd.getText().toString();
		String article = dttxArticlePublishAtcFd.getText().toString();
		String phoneNumber = dttxPhoneNumberPublishAtcFd.getText().toString();
		String lastName = dttxLastNamePublishAtcFd.getText().toString();

		if(rdbtIPickUp.isChecked() || rdbtILost.isChecked())
		{
			if(isValid == true && title != null && title.length() > 0 && title.length() < 20)
			{
				if(isValid == true && article != null && article.length() > 0 && article.length() < 200)
				{
					if(isValid == true && phoneNumber != null && phoneNumber.matches("[0-9]{7,11}"))
					{
						if(isValid == true && lastName != null && lastName.length() > 0 && lastName.length() < 6)
						{
							if(isValid == true && rdbtFemalePublishAtcFd.isChecked() || rdbtMalePublishAtcFd.isChecked())
								;
							else
							{
								isValid = false;
								Toast.makeText(getActivity(), "��ѡ���Ա�", Toast.LENGTH_LONG).show();
							}
						}
						else
						{
							isValid = false;
							Toast.makeText(getActivity(), "��������Ӧ�������������ڣ�", Toast.LENGTH_LONG).show();
						}
					}
					else
					{
						isValid = false;
						Toast.makeText(getActivity(), "�绰��������Ӧ��7-11������֮�䣡", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					isValid = false;
					Toast.makeText(getActivity(), "���������Ӧ��200���������ڣ�", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				isValid = false;
				Toast.makeText(getActivity(), "����ı���Ӧ��20���������ڣ�", Toast.LENGTH_LONG).show();
			}
		}
		else 
		{
			isValid = false;
			Toast.makeText(getActivity(), "��ѡ�񷢲����ͣ�", Toast.LENGTH_LONG).show();
		}
		
		return isValid;
	}

	private void initFragment() 
	{
		MainActivity.frgtCurrentHome = this;
		Constant.CURRENT_HOME_PAGE_STATE = Constant.PAGE_STATE_I_WANT_PUBLISH_ARTICLE_FOUND_FARGMENT;
		new BackTextViewController().setBackTextViewEnable(true);
		Constant.IS_FRAGMENT_HAS_BACK_BUTTON[Constant.INDEX_HOME_FRAGMENT] = true;
		
		myInternet = new MyInternet(getActivity());

		rdbtIPickUp = (RadioButton) getActivity().findViewById(R.id.rdbtIPickUp);
		rdbtILost = (RadioButton) getActivity().findViewById(R.id.rdbtILost);
		rdbtFemalePublishAtcFd = (RadioButton) getActivity().findViewById(R.id.rdbtFemalePublishAtcFd);
		rdbtMalePublishAtcFd = (RadioButton) getActivity().findViewById(R.id.rdbtMalePublishAtcFd);
		
		dttxTitlePulishAtcFd = (EditText) getActivity().findViewById(R.id.dttxTitlePulishAtcFd);
		dttxArticlePublishAtcFd = (EditText) getActivity().findViewById(R.id.dttxArticlePublishAtcFd);
		dttxPhoneNumberPublishAtcFd = (EditText) getActivity().findViewById(R.id.dttxPhoneNumberPublishAtcFd);
		dttxLastNamePublishAtcFd = (EditText) getActivity().findViewById(R.id.dttxLastNamePublishAtcFd);
		
		bttnConfirmPublishAtcFd = (Button) getActivity().findViewById(R.id.bttnConfirmPublishAtcFd);
		
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		progressDialog.setTitle("��ʾ"); 
		progressDialog.setMessage("���Ժ�..."); 
		progressDialog.setIcon(android.R.drawable.ic_dialog_info); 
		progressDialog.setIndeterminate(false); 
		progressDialog.setCancelable(false); 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.fragment_i_want_publish_article_found, container, false);
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
