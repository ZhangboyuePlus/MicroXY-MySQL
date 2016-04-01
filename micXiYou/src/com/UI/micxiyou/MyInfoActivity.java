package com.UI.micxiyou;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import com.UI.MainActivity;
import com.UI.R;
import com.UI.LoginOrRegister.SignUpActivity;
import com.UI.micxiyou.myInfo.ContactUsFragment;
import com.UI.micxiyou.myInfo.DevelopMemberFragment;
import com.project.Util.BackTextViewController;
import com.project.Util.Constant;
import com.project.Util.MyAccountSharedPreferences;
import com.project.Util.MyInternet;
import com.project.Util.MyStack;
import com.project.webServices.accountServices.ModifyPassWord;
import com.project.webServices.accountServices.ModifyUserName;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfoActivity extends Fragment
{
	private TextView txtvUserName_MyInfo;
	private TextView txtvRegisteredEmial_MyInfo;

	private LinearLayout lnltContactUs;
	private LinearLayout lnltDeveloMember;
	private LinearLayout lnltUserNameInMyInfo; 
	private LinearLayout lnltModifyPassWordInMyInfo;
	private LinearLayout lnltLogOffInMyInfo;

	private LinearLayout lnltModifyPassWordInDialog;
	private EditText dtxtOldPassWordInDialog;
	private EditText dtxtNewPassWordInDialog;
	private EditText dtxtConfirmNewPassWordInDialog;
	private EditText dttxModifyUserNameInDialog;
	
	private String registedEmail;
	
	private AlertDialog ltdgModifyUserName;
	private AlertDialog ltdgModifyPassWord;
	private AlertDialog ltdgLogOffAccount;
	private ProgressDialog progressDialog;
	private MyInternet myInternet;
	
	private boolean isFragmentFirstShow = true;
	
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		MainActivity.frgtCurrentMyInfo = this;
		new BackTextViewController().setBackTextViewEnable(false);
		
		initFragment();
		dealAction();
	}

	private void dealAction()
	{
		lnltUserNameInMyInfo.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					if(ltdgModifyUserName != null)
						ltdgModifyUserName .show();
					else
						ltdgModifyUserName = new AlertDialog.Builder(MyInfoActivity.this.getActivity())
									.setTitle("输入新的用户名：")
									.setIcon(android.R.drawable.ic_dialog_info)
									.setView(dttxModifyUserNameInDialog)
									.setPositiveButton("确定", new confirmModifyUserNameListener())
									.setNegativeButton("取消", new cancelModifyUserNameListener())
									.show();
				}
			}
		);
		
		lnltModifyPassWordInMyInfo.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View arg0) 
				{
					if(ltdgModifyPassWord != null)
						ltdgModifyPassWord .show();
					else
					{
						Builder builder = new AlertDialog.Builder(MyInfoActivity.this.getActivity())
									.setTitle("修改密码：")
									.setIcon(android.R.drawable.ic_dialog_info)
									.setView(lnltModifyPassWordInDialog)
									.setPositiveButton("确定", new confirmModifyPassWordListener())
									.setNegativeButton("取消", new cancelModifyPassWordListener());

						ltdgModifyPassWord = builder.create();
						ltdgModifyPassWord.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						ltdgModifyPassWord.show();
					}
				}
			}
		);
		
		lnltLogOffInMyInfo.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View arg0) 
					{
						if(ltdgLogOffAccount != null)
							ltdgLogOffAccount.show();
						else
							ltdgLogOffAccount = new AlertDialog.Builder(MyInfoActivity.this.getActivity())
										.setTitle("确定注销当前账号？")
										.setIcon(android.R.drawable.ic_dialog_info)
										.setPositiveButton("确定", new confirmLogOffAccountListener())
										.setNegativeButton("取消", new cancelLogOffAccountListener())
										.show();
					}
				}
		);
		
		lnltContactUs.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View arg0) 
				{
					ContactUsFragment frgtContactUs = new ContactUsFragment();
					android.support.v4.app.FragmentTransaction transaction = 
							getActivity().getSupportFragmentManager().beginTransaction();
					transaction.replace(R.id.fragment_my_info, frgtContactUs);
					new MyStack(getActivity()).addToMyInfoStack(MyInfoActivity.this);
					transaction.commit();
				}
			}
		);
		
		lnltDeveloMember.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View arg0) 
				{
					DevelopMemberFragment frgtDevelop = new DevelopMemberFragment();
					android.support.v4.app.FragmentTransaction transaction = 
							getActivity().getSupportFragmentManager().beginTransaction();
					transaction.replace(R.id.fragment_my_info, frgtDevelop);
					new MyStack(getActivity()).addToMyInfoStack(MyInfoActivity.this);
					transaction.commit();
				}
			}
		);
	}
	
	private class cancelLogOffAccountListener implements  android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			ltdgLogOffAccount.cancel();
		}
	}
	
	private class confirmLogOffAccountListener implements android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			ltdgLogOffAccount.cancel();
			MyAccountSharedPreferences shared = new MyAccountSharedPreferences(getActivity());
			shared.deleteAccount();
			
			Intent intent = new Intent(MyInfoActivity.this.getActivity(), SignUpActivity.class);
			MyInfoActivity.this.startActivity(intent);
			
			getActivity().finish();
		}
	}
	
	private class confirmModifyPassWordListener implements android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1)
		{
			String oldPassWord = dtxtOldPassWordInDialog.getText().toString();
			String newPassWord = dtxtNewPassWordInDialog.getText().toString();
			String confirmPassWord = dtxtConfirmNewPassWordInDialog.getText().toString();
			
			if(isPassWordValid(oldPassWord) == true && isPassWordValid(newPassWord) 
						&& isPassWordValid(confirmPassWord) && newPassWord.equals(confirmPassWord))
			{
				if(myInternet.isInternetAvailable(MyInfoActivity.this.getActivity()) == true)
				{
					clearModifyPassWordDialogInPut();
					ltdgModifyPassWord.cancel();
					progressDialog.show();

					Thread modifyPassWordThread = new ModifyPassWordThread(registedEmail, oldPassWord, newPassWord);
					modifyPassWordThread.start();
				}
			}
			else
			{
				Toast.makeText(getActivity(), "输入信息有误，密码应由6-24个字母或数字组成！", Toast.LENGTH_LONG).show();
				clearModifyPassWordDialogInPut();
			}
		}
	}

	private boolean isPassWordValid(String passWord) 
	{
		boolean isValid = true;
		
		int strSize = passWord.length();

		if(strSize < 6 || strSize > 18)
			isValid = false;
		else
		{
			String patternString = "[a-zA-Z0-9]{6,18}";
			isValid = Pattern.matches(patternString, passWord);
		}
		
		return isValid;
	}

	public void clearModifyPassWordDialogInPut() 
	{
		dtxtOldPassWordInDialog.setText("");
		dtxtNewPassWordInDialog.setText("");
		dtxtConfirmNewPassWordInDialog.setText("");
	}

	private class cancelModifyPassWordListener implements  android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			ltdgModifyPassWord.cancel();
		}
	}
	
	private class confirmModifyUserNameListener implements android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			String newUserName = dttxModifyUserNameInDialog.getText().toString();
			if(newUserName != null && newUserName.length() > 0 && newUserName.length() < 24)
			{
				if(myInternet.isInternetAvailable(MyInfoActivity.this.getActivity()) == true)
				{
					ltdgModifyUserName.cancel();
					progressDialog.show();
					
					Thread modifyPassWordThread = new ModifyUserNameThread(registedEmail, newUserName);
					modifyPassWordThread.start();
				}
			}
			else
				Toast.makeText(getActivity(), "输入的信息有误， 请将长度控制在24个字符或8个汉字以内！", Toast.LENGTH_LONG).show();
		}
	}
	
	private class cancelModifyUserNameListener implements  android.content.DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			ltdgModifyUserName.cancel();
		}
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			progressDialog.cancel();
			if(msg.what == Constant.MSG_MODIFY_USER_NAME_SUCCESS)
			{
				String newUserName = msg.getData().getString("newUserName");
				txtvUserName_MyInfo.setText(newUserName);
				
				MyAccountSharedPreferences shared = new MyAccountSharedPreferences(getActivity());
				shared.modifyUserName(newUserName);
				
				Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_LONG).show();
			}
			else if(msg.what == Constant.MSG_MODIFY_USER_NAME_FAIL)
				Toast.makeText(getActivity(), "用户名已存在！", Toast.LENGTH_LONG).show();
			else if(msg.what == Constant.MSG_MODIFY_PASSWORD_SUCCESS)
			{
				String newPassWord = msg.getData().getString("newPassWord");
				MyAccountSharedPreferences shared = new MyAccountSharedPreferences(getActivity());
				shared.modifyPassWord(newPassWord);

				Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_LONG).show();
			}
			else if(msg.what == Constant.MSG_MODIFY_PASSWORD_FAIL)
				Toast.makeText(getActivity(), "修改失败！请确认旧密码输入正确！", Toast.LENGTH_LONG).show();
			else if(msg.what == Constant.MSG_SHOW_INTERNET_IS_ERROR)
				myInternet.showInternetIsError();
		}
	};
	
	private class ModifyPassWordThread extends Thread
	{
		private String Email;
		private String oldPassWord;
		private String newPassWord;
		
		public ModifyPassWordThread(String Email, String oldPassWord, String newPassWord)
		{
			this.Email = Email;
			this.oldPassWord = oldPassWord;
			this.newPassWord = newPassWord;
		}

		@Override
		public void run()
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<Boolean> modifyPassWord = new ModifyPassWord(Email, oldPassWord, newPassWord);
			Future<Boolean> future = pool.submit(modifyPassWord);

			try 
			{
				boolean ok = future.get();
				Message msg = new Message();

				if(ok)
				{
					msg.what = Constant.MSG_MODIFY_PASSWORD_SUCCESS;
					Bundle bundle = new Bundle();
					bundle.putString("newPassWord", newPassWord);
					msg.setData(bundle);
				}
				else
					msg.what = Constant.MSG_MODIFY_PASSWORD_FAIL;
				
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
	
	private class ModifyUserNameThread extends Thread
	{
		private String Email;
		private String newUserName;
		
		public ModifyUserNameThread(String Email, String newUserName)
		{
			this.Email = Email;
			this.newUserName = newUserName;
		}

		@Override
		public void run()
		{
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Callable<Boolean> modifyUserName = new ModifyUserName(Email, newUserName);
			Future<Boolean> future = pool.submit(modifyUserName);

			try 
			{
				boolean ok = future.get();
				Message msg = new Message();

				if(ok)
				{
					msg.what = Constant.MSG_MODIFY_USER_NAME_SUCCESS;
					Bundle bundle = new Bundle();
					bundle.putString("newUserName", newUserName);
					msg.setData(bundle);
				}
				else
					msg.what = Constant.MSG_MODIFY_USER_NAME_FAIL;
				
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
		txtvUserName_MyInfo = (TextView) getActivity().findViewById(R.id.txtvUserName_MyInfo);
		txtvRegisteredEmial_MyInfo = (TextView) getActivity().findViewById(R.id.txtvRegisteredEmial_MyInfo);
		
		lnltContactUs = (LinearLayout) getActivity().findViewById(R.id.lnltContactUs);
		lnltDeveloMember = (LinearLayout) getActivity().findViewById(R.id.lnltDeveloMember);
		
		lnltUserNameInMyInfo = (LinearLayout) getActivity().findViewById(R.id.lnltUserNameInMyInfo);
		lnltModifyPassWordInMyInfo = (LinearLayout) getActivity().findViewById(R.id.lnltModifyPassWordInMyInfo);
		lnltLogOffInMyInfo = (LinearLayout) getActivity().findViewById(R.id.lnltLogOffInMyInfo);
		lnltModifyPassWordInDialog = (LinearLayout) getActivity().getLayoutInflater().
						inflate(R.layout.dialog_modify_password, null);

		dtxtOldPassWordInDialog = (EditText) lnltModifyPassWordInDialog.findViewById(R.id.dtxtOldPassWordInDialog);
		dtxtNewPassWordInDialog = (EditText) lnltModifyPassWordInDialog.findViewById(R.id.dtxtNewPassWordInDialog);
		dtxtConfirmNewPassWordInDialog = (EditText) lnltModifyPassWordInDialog.findViewById(R.id.dtxtConfirmNewPassWordInDialog);
		
		dttxModifyUserNameInDialog = new EditText(getActivity());
		myInternet = new MyInternet(getActivity());
		
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		progressDialog.setTitle("提示"); 
		progressDialog.setMessage("请稍后..."); 
		progressDialog.setIcon(android.R.drawable.ic_dialog_info); 
		progressDialog.setIndeterminate(false); 
		progressDialog.setCancelable(false); 
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_my_info, container, false);
		
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) 
	{
		super.onHiddenChanged(hidden);
		if(hidden == false)
		{
			Constant.CURRENT_MAIN_PAGE_STATE = Constant.PAGE_STATE_MY_INFO_ACTIVITY;
			
			if(isFragmentFirstShow == true)
			{
				initView();
				isFragmentFirstShow = false;
			}
		}
	}

	private void initView() 
	{
		MyAccountSharedPreferences myShared = new MyAccountSharedPreferences(getActivity().getApplicationContext());
		String info[] = myShared.getAccount();
		txtvUserName_MyInfo.setText(info[0]);
		txtvRegisteredEmial_MyInfo.setText(info[1]);
		registedEmail = info[1];
	}

	public void onDestroy() 
	{
		super.onDestroy();
	}
}
