package com.UI.LoginOrRegister;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.UI.R;
import com.project.Util.Constant;
import com.project.Util.MyInternet;
import com.project.webServices.accountServices.SendRegistedEmailToServer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetPassWordActivity extends Activity
{
	private Button bttnSendPassWordToEmail;
	private EditText dttxEmailAdressToSendPassWord;
	
	private MyInternet myInternet;
	private ProgressDialog pgdlSendPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_forget_pass_word);
		
		initActivity();
		dealAction();
	}
	
	private void dealAction() 
	{
		bttnSendPassWordToEmail.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						String Email = dttxEmailAdressToSendPassWord.getText().toString();
						if(IsEmailValid(Email) == true)
						{
							if(myInternet.isInternetAvailable(ForgetPassWordActivity.this) == true)
							{
								pgdlSendPassword.show();
								SendEmailToServer send = new SendEmailToServer(Email);
								send.start();
							}
							else
								myInternet.showInternetIsNotAvailable();
						}
						else
						{
							Toast.makeText(getApplicationContext(), "输入的邮箱地址不合法！", Toast.LENGTH_LONG).show();
						}
					}
				}
		);
	}

	protected boolean IsEmailValid(String strEmail)
	{
		boolean isValid = true;

		if(strEmail.contains("@"))
		{
			String strArray[] = strEmail.split("@");
			if(strArray.length != 2)
				isValid = false;
		}
		else 
			isValid = false;
		
		return isValid;
	}

	private void initActivity()
	{
		initSendPasswordDialog();
		
		myInternet = new MyInternet(getApplicationContext());
		bttnSendPassWordToEmail = (Button) findViewById(R.id.bttnSendPassWordToEmail);
		dttxEmailAdressToSendPassWord = (EditText) findViewById(R.id.dttxEmailAdressToSendPassWord);
	}

	private void initSendPasswordDialog() 
	{
		pgdlSendPassword = new ProgressDialog(this);
		pgdlSendPassword.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		pgdlSendPassword.setTitle("提示"); 
		pgdlSendPassword.setMessage("正在发送邮件,请稍后..."); 
		pgdlSendPassword.setIcon(android.R.drawable.ic_dialog_info); 
		pgdlSendPassword.setIndeterminate(false); 
		pgdlSendPassword.setCancelable(false);
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);

			if(pgdlSendPassword.isShowing() == true)
				pgdlSendPassword.cancel();
			
			if(msg.what == Constant.MSG_SUCCESS_SEND_PASSWORD_TO_REGISTED_EMAIL)
			{
				Toast.makeText(getApplicationContext(), "邮件发送成功！", Toast.LENGTH_LONG).show();
			}
			else if(msg.what == Constant.MSG_FAIL_SEND_PASSWORD_TO_REGISTED_EMAIL)
			{
				Toast.makeText(getApplicationContext(), "邮件发送失败！", Toast.LENGTH_LONG).show();
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				myInternet.showInternetIsError();
			}
		}
	};
	
	
	private class SendEmailToServer extends Thread
	{
		private String Email;
		
		public SendEmailToServer(String Email)
		{
			this.Email = Email;
		}

		@Override
		public void run() 
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<Boolean> sendRegistedEmailToServer = new SendRegistedEmailToServer(Email);
			Future<Boolean> future = pool.submit(sendRegistedEmailToServer);
			
			try 
			{
				boolean isSuccess = future.get();
				
				Message msg = new Message();
				if(isSuccess == true)
					msg.what = Constant.MSG_SUCCESS_SEND_PASSWORD_TO_REGISTED_EMAIL;
				else
					msg.what = Constant.MSG_FAIL_SEND_PASSWORD_TO_REGISTED_EMAIL;
				
				handler.sendMessage(msg);
			} catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				myInternet.sendMsgToHandlerInternetIsError(handler);
			} catch (ExecutionException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				myInternet.sendMsgToHandlerInternetIsError(handler);
			}
  		}
	}

}
