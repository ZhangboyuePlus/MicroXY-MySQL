package com.UI.LoginOrRegister;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import com.UI.R;
import com.project.Util.Constant;
import com.project.Util.MyAccountSharedPreferences;
import com.project.Util.MyInternet;
import com.project.webServices.LoginOrRegisterServices.CheckIsAccountRegister;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

public class SignUpActivity extends Activity
{
	private EditText dtxtEmail;
	private EditText dtxtPassWord;
	private TextView txtvForgetPassWord;
	private TextView txtvEmailIsNotValid;
	private TextView txtvPassWordIsNotValid;
	
	private Button bttnRegister;
	private Button bttnLogin;
	
	private MyInternet myInternet;

	private ProgressDialog pgdlSignUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign_up);
		
		initActivity();
		
		dealAction();
	}

	private void dealAction() 
	{
		bttnLogin.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						dealLoginOnClick();
					}
				}
		);

		bttnRegister.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						Intent intent = new Intent(SignUpActivity.this, com.UI.LoginOrRegister.RegisterActivity.class);
						SignUpActivity.this.startActivity(intent);
						SignUpActivity.this.finish();
					}
				}
		);
		
		txtvForgetPassWord.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						Intent intent = new Intent(SignUpActivity.this, ForgetPassWordActivity.class);
						startActivity(intent);
					}
				}
		);
	}
	
	private void dealLoginOnClick()
	{
		boolean isEmailValid = IsEmailValid();
		boolean isPassWordValid = IsPassWordValid();
		
		if(isEmailValid == true && isPassWordValid== true)
		{
			txtvEmailIsNotValid.setVisibility(View.INVISIBLE);
			txtvPassWordIsNotValid.setVisibility(View.INVISIBLE);
			
			String Email = dtxtEmail.getText().toString();
			String passWord = dtxtPassWord.getText().toString();
			
			loginAccount(Email, passWord);
		}
		else if(isEmailValid == false && isPassWordValid == false)
		{
			Toast.makeText(getApplicationContext(),"请输入信息！", Toast.LENGTH_LONG).show();
		}
		else 
		{
			if(isEmailValid == false)
			{
				txtvEmailIsNotValid.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(),"账号无效，请重新输入", Toast.LENGTH_LONG).show();
			}
			else
				txtvEmailIsNotValid.setVisibility(View.INVISIBLE);
			
			if(isPassWordValid == false)
			{
				txtvPassWordIsNotValid.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(),"密码无效，请重新输入", Toast.LENGTH_LONG).show();
			}
			else
				txtvPassWordIsNotValid.setVisibility(View.INVISIBLE);
		}
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);

			if(pgdlSignUp.isShowing() == true)
				pgdlSignUp.cancel();

			if(msg.what == Constant.MSG_ACCOUNT_HAS_REGISTERED )
			{
				String userName = msg.getData().getString("userName");
				String Email = msg.getData().getString("Email");
				String passWord = msg.getData().getString("passWord");

				MyAccountSharedPreferences mySharedPreferences = new MyAccountSharedPreferences(getApplicationContext());
				mySharedPreferences.saveAccount(Email, passWord, userName);
				
				clearInput();
				Intent intent = new Intent(SignUpActivity.this, com.UI.MainActivity.class);
				SignUpActivity.this.startActivity(intent);
				SignUpActivity.this.finish();
			}
			else if(msg.what == Constant.MSG_ACCOUNT_HAS_NOT_REGISTERED)
			{
				dtxtPassWord.setText("");
				Toast.makeText(getApplicationContext(),"账号或密码错误，请重新输入", Toast.LENGTH_LONG).show();
			}
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
			{
				dtxtPassWord.setText("");
				myInternet.showInternetIsError();
			}
		}
	};

	private class isAccountRegisteredThread extends Thread
	{
		private String Email;
		private String passWord;
		
		public isAccountRegisteredThread(String Email, String passWord)
		{
			this.Email = Email;
			this.passWord = passWord;
		}

		@Override
		public void run()
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<String> checkIsAccountRegister = new CheckIsAccountRegister(Email, passWord);
			Future<String> future = pool.submit(checkIsAccountRegister);

			try 
			{
				String info = future.get();
				Message msg = new Message();

				if(info.startsWith("1##"))
				{
					msg.what = Constant.MSG_ACCOUNT_HAS_REGISTERED;
					String userName = info.substring(3);
					Bundle bundle = new Bundle();
					bundle.putString("userName", userName);
					bundle.putString("Email", Email);
					bundle.putString("passWord", passWord);
					msg.setData(bundle);
				}
				else if(info.startsWith("2##"))
					msg.what = Constant.MSG_ACCOUNT_HAS_NOT_REGISTERED;
				
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
	
	private void loginAccount(String Email, String passWord)
	{
		if(myInternet.isInternetAvailable(this) == true)
		{
			pgdlSignUp.show();
			Thread thread = new isAccountRegisteredThread(Email, passWord);
			
			thread.start();
		}
		else
			myInternet.showInternetIsNotAvailable();
		
	}

	protected void clearInput()
	{
		dtxtEmail.setText("");
		dtxtPassWord.setText("");
	}

	protected boolean IsPassWordValid() 
	{
		boolean isValid = true;
		String passWord = dtxtPassWord.getText().toString();
		int strSize = passWord.length();

		if(strSize < 6 || strSize > 18)
			isValid = false;
		else
		{
			String patternString = "[a-zA-Z0-9]{6,18}";//似乎这一句就可以判断长度了
			isValid = Pattern.matches(patternString, passWord);
		}
		
		return isValid;
	}

	protected boolean IsEmailValid()
	{
		boolean isValid = true;
		String strEmail = dtxtEmail.getText().toString();

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
		dtxtEmail = (EditText) findViewById(R.id.dtxtEmail);
		dtxtPassWord = (EditText) findViewById(R.id.dtxtPassWord);
		txtvForgetPassWord = (TextView) findViewById(R.id.txtvForgetPassWord);
		txtvEmailIsNotValid = (TextView) findViewById(R.id.txtvEmailIsNotValid);
		txtvPassWordIsNotValid = (TextView) findViewById(R.id.txtvPassWordIsNotValid);
		
		bttnRegister = (Button) findViewById(R.id.bttnRegisterInSignUp);
		bttnLogin = (Button) findViewById(R.id.bttnLoginInSignUp);
		
		myInternet = new MyInternet(getApplicationContext());
		
		initSignUpProgressDialog();
		
		autoSignUp();
	}

	private void initSignUpProgressDialog() 
	{
		pgdlSignUp = new ProgressDialog(this);
		//设置进度条风格，风格为圆形，旋转的 
		pgdlSignUp.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		//设置ProgressDialog 标题 
		pgdlSignUp.setTitle("提示"); 
		//设置ProgressDialog 提示信息 
		pgdlSignUp.setMessage("正在登录中..."); 
		//设置ProgressDialog 标题图标 
		pgdlSignUp.setIcon(android.R.drawable.ic_dialog_info); 
		//设置ProgressDialog 的一个Button 
		//设置ProgressDialog 的进度条是否不明确 
		pgdlSignUp.setIndeterminate(false); 
		//设置ProgressDialog 是否可以按退回按键取消 
		pgdlSignUp.setCancelable(false);
	}

	private void autoSignUp()
	{
		MyAccountSharedPreferences shared = new MyAccountSharedPreferences(getApplicationContext());
		if(shared.isExistAccount())
		{
			String info[] = shared.getAccount();
			String Email = info[1];
			String passWord = info[2];
			
//			Log.i("tag","Email =#" + Email + "#");
//			Log.i("tag","passWord =#" + passWord + "#");
			
			loginAccount(Email, passWord);
		}
	}
}
