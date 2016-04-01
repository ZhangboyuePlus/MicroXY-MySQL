package com.UI.LoginOrRegister;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import com.UI.R;
import com.project.Util.Constant;
import com.project.Util.MyInternet;
import com.project.webServices.LoginOrRegisterServices.RegisterNewAccount;

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

public class RegisterActivity extends Activity 
{
	private EditText dtxtRegisterUserName;
	private EditText dtxtRegisterEmail;
	private EditText dtxtRegisterPassWord;
	private EditText dtxtRegisterConfirmPassWord;
	
	private TextView txtvUserNameIsNotValid;
	private TextView txtvRegisterEmailIsNotValid;
	private TextView txtvRegisterPassWordIsNotValid;
	private TextView txtvConfirmPassWordIsNotValid;
	
	private Button bttnLoginInRegister;
	private Button bttnRegisterInRegister;
	
	private ProgressDialog progressDialog;
	
	private MyInternet myInternet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		
		initActivity();
		
		dealAction();
	}

	private void dealAction() 
	{
		bttnRegisterInRegister.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						hideAllValidityTipView();
						
						String userName = dtxtRegisterUserName.getText().toString();
						String email = dtxtRegisterEmail.getText().toString();
						String passWord = dtxtRegisterPassWord.getText().toString();
						String confirmPassWord = dtxtRegisterConfirmPassWord.getText().toString();
						
						if(isAllInfoValid(userName, email, passWord, confirmPassWord) == true)
						{
							startRegisterAccountThread(userName, email, passWord);
						}
					}
				}
		);
		
		bttnLoginInRegister.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						Intent intent = new Intent(RegisterActivity.this, SignUpActivity.class);
						RegisterActivity.this.startActivity(intent);
						RegisterActivity.this.finish();
					}
				}
		);
	}

	protected void startRegisterAccountThread(String userName, String email, String passWord)
	{
		// TODO Auto-generated method stub
		if(myInternet.isInternetAvailable(RegisterActivity.this) == true)
		{
			progressDialog.show();
			RegisterNewAccountThread thread = new RegisterNewAccountThread(userName, email, passWord);
			thread.start();
		}
		else
			myInternet.showInternetIsNotAvailable();
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			progressDialog.cancel();
			
			if(msg.what == Constant.MSG_SUCCESS_REGISTER_NEW_ACCOUNT)
			{
				Toast.makeText(RegisterActivity.this, "注册成功，请登录！", Toast.LENGTH_LONG).show();
				
				clearAllInput();
				hideAllValidityTipView();
				
				Intent intent = new Intent(RegisterActivity.this, SignUpActivity.class);
				RegisterActivity.this.startActivity(intent);
				RegisterActivity.this.finish();
			}
			else if(msg.what == Constant.MSG_USER_NAME_HAVE_BEEN_REGISTERED)
			{
				txtvUserNameIsNotValid.setVisibility(View.VISIBLE);
				Toast.makeText(RegisterActivity.this, "该用户名已被注册！", Toast.LENGTH_LONG).show();
			}
			else if(msg.what == Constant.MSG_EMAIL_HAVE_BEEN_REGISTERED)
			{
				txtvRegisterEmailIsNotValid.setVisibility(View.VISIBLE);
				Toast.makeText(RegisterActivity.this, "该邮箱已被注册！", Toast.LENGTH_LONG).show();
			}
			else if(msg.what == Constant.MSG_FAULT_OCCUR)
			{
				Toast.makeText(RegisterActivity.this, "服务器出现异常，请稍后重试！", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	private class RegisterNewAccountThread extends Thread
	{
		private String userName;
		private String Email;
		private String passWord;
		
		public RegisterNewAccountThread(String userName, String Email, String passWord)
		{
			this.userName = userName;
			this.Email = Email;
			this.passWord = passWord;
		}

		@Override
		public void run() 
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<String> registerNewAccount = 
					new RegisterNewAccount(userName, Email, passWord);
			Future<String> future = pool.submit(registerNewAccount);

			String flag = "";
			try 
			{
				flag = future.get();
				int msgWhat = Constant.MSG_FAULT_OCCUR;
				
				if(flag.equals("0"))
					msgWhat = Constant.MSG_SUCCESS_REGISTER_NEW_ACCOUNT;				
				else if(flag.equals("1"))
					msgWhat = Constant.MSG_USER_NAME_HAVE_BEEN_REGISTERED;
				else if(flag.equals("2"))
					msgWhat = Constant.MSG_EMAIL_HAVE_BEEN_REGISTERED;
				else if(flag.equals("3"))
					msgWhat = Constant.MSG_FAULT_OCCUR;
				
				Message msg = new Message();
				msg.what = msgWhat;
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

	
	protected void hideAllValidityTipView()
	{
		txtvConfirmPassWordIsNotValid.setVisibility(View.INVISIBLE);
		txtvRegisterPassWordIsNotValid.setVisibility(View.INVISIBLE);
		txtvRegisterEmailIsNotValid.setVisibility(View.INVISIBLE);
		txtvUserNameIsNotValid.setVisibility(View.INVISIBLE);
	}

	protected void clearAllInput() 
	{
		dtxtRegisterUserName.setText("");
		dtxtRegisterEmail.setText("");
		dtxtRegisterPassWord.setText("");
		dtxtRegisterConfirmPassWord.setText("");
	}

	protected boolean isAllInfoValid(String userName, String email, String passWord, String confirmPassWord)
	{
		boolean isAllInfoValid = true;
		
		if(userName != null && userName.length() > 0 && userName.length() < 24)
		{
			if(isStringEmail(email) == true)
			{
				if(isPassWordValid(passWord) == true)
				{
					if(confirmPassWord != null && confirmPassWord.equals(passWord))
					{}
					else 
					{
						isAllInfoValid  = false;
						Toast.makeText(this, "确认密码与密码不相同！", Toast.LENGTH_LONG).show();
						txtvConfirmPassWordIsNotValid.setVisibility(View.VISIBLE);
					}
				}
				else 
				{
					isAllInfoValid  = false;
					Toast.makeText(this, "密码不合法，请将长度控制在6-18个字母或数字之内！", Toast.LENGTH_LONG).show();
					txtvRegisterPassWordIsNotValid.setVisibility(View.VISIBLE);
				}
			}
			else 
			{
				isAllInfoValid  = false;
				Toast.makeText(this, "邮箱地址不合法，请将长度控制在30个英文字母之内！", Toast.LENGTH_LONG).show();
				txtvRegisterEmailIsNotValid.setVisibility(View.VISIBLE);
			}
		}
		else 
		{
			isAllInfoValid  = false;
			Toast.makeText(this, "用户名不合法，请将长度控制在8个汉字或24个英文字母之内！", Toast.LENGTH_LONG).show();
			txtvUserNameIsNotValid.setVisibility(View.VISIBLE);
		}
		
		return isAllInfoValid;
	}

	private boolean isPassWordValid(String passWord) 
	{
		boolean isValid = true;
		
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

	private boolean isStringEmail(String email) 
	{
		boolean ok = true;
		
		if(email != null && email.length() > 0 && email.length() < 30 && email.contains("@"))
		{
			String strArray[] = email.split("@");
			if(strArray.length != 2)
				ok = false;
		}
		else  
			ok = false;
		
		return ok;
	}

	private void initActivity() 
	{
		dtxtRegisterUserName = (EditText) findViewById(R.id.dtxtRegisterUserName);
		dtxtRegisterEmail = (EditText) findViewById(R.id.dtxtRegisterEmail);
		dtxtRegisterPassWord = (EditText) findViewById(R.id.dtxtRegisterPassWord);
		dtxtRegisterConfirmPassWord = (EditText) findViewById(R.id.dtxtRegisterConfirmPassWord);
		
		txtvUserNameIsNotValid = (TextView) findViewById(R.id.txtvUserNameIsNotValid);
		txtvRegisterEmailIsNotValid = (TextView) findViewById(R.id.txtvRegisterEmailIsNotValid);
		txtvRegisterPassWordIsNotValid = (TextView) findViewById(R.id.txtvRegisterPassWordIsNotValid);
		txtvConfirmPassWordIsNotValid = (TextView) findViewById(R.id.txtvConfirmPassWordIsNotValid);
		
		bttnLoginInRegister = (Button) findViewById(R.id.bttnLoginInRegister);
		bttnRegisterInRegister = (Button) findViewById(R.id.bttnRegisterInRegister);

		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		progressDialog.setTitle("提示"); 
		progressDialog.setMessage("正在注册，请稍等..."); 
		progressDialog.setIcon(android.R.drawable.ic_dialog_info); 
		progressDialog.setIndeterminate(false); 
		progressDialog.setCancelable(false); 
	
		myInternet = new MyInternet(this);
	}
}
