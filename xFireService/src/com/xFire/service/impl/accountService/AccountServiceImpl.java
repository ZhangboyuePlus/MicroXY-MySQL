package com.xFire.service.impl.accountService;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.mortbay.log.Log;

import com.xFire.service.accountService.AccountService;
import com.xFire.util.DButil;
import com.xFire.util.MailSenderInfo;
import com.xFire.util.SimpleMailSender;

public class AccountServiceImpl implements AccountService, Serializable
{
	private static final long serialVersionUID = 3791168280692285097L;
	private final String ACCOUNT_TABLE_NAME = "account";

	@Override
	public boolean modifyUserName(String Email, String newUserName) 
	{
		Connection conn = null;
		Statement statement = null;
		ResultSet rs  =null;

		String SQLStringCheck = "SELECT COUNT(*) FROM " + ACCOUNT_TABLE_NAME + " WHERE userName = \'"
				+ newUserName +  "\'";
		
		String SQLStringModify = "UPDATE " + ACCOUNT_TABLE_NAME + " SET userName =" +
				"\'" + newUserName + "\'" + " WHERE Email = \'" + Email 
					+ "\'";

		boolean ok = false;
		conn = DButil.connection();
		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLStringCheck);
			
			if(rs.next() && rs.getInt(1) == 0)
			{
				statement.execute(SQLStringModify);
				ok = true;
			}
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok = false;
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return ok;
	}

	@Override
	public boolean modifyPassWord(String Email, String oldPassWord, String newPassWord)
	{
		Connection conn = null;
		Statement statement = null;
		ResultSet rs  =null;

		String SQLStringCheck = "SELECT passWord FROM " + ACCOUNT_TABLE_NAME 
					+ " WHERE Email = \'" + Email + "\'" ;

		String SQLStringUpSet = "UPDATE " + ACCOUNT_TABLE_NAME + " SET passWord =" +
				"\'" + newPassWord + "\'" + " WHERE Email = \'" + Email 
					+ "\'";

		boolean ok = false;
		conn = DButil.connection();
		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLStringCheck);
			
			if(rs.next())
			{
				String oldPassWordInServer = rs.getString("passWord");
				if(oldPassWordInServer.equals(oldPassWord))
				{
					statement.execute(SQLStringUpSet);
					ok = true;
				}
			}
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok = false;
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return ok;
	}
	
	public String getPassWordByEmail(String registedEmail)
	{
		Connection conn = null;
		Statement statement = null;
		ResultSet rs  =null;

		String passWord = "";
		String SQLString = "SELECT passWord FROM " + this.ACCOUNT_TABLE_NAME + 
				" WHERE Email = \'" + registedEmail + "\'";
	
		conn = DButil.connection();
		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			
			if(rs.next())
				passWord = rs.getString("passWord");
			
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return passWord;
	}
	
	@Override
	public boolean sendPasswordToRegistedEmail(String registedEmail)
	{
		boolean isSuccess = false;
		
		String passWord = getPassWordByEmail(registedEmail);
		
		Log.info("passWord=#" + passWord + "#");
		if(passWord.equals("") == false)
		{
			MailSenderInfo mailInfo = new MailSenderInfo(); 
			mailInfo.setMailServerHost("smtp.qq.com"); 
			mailInfo.setMailServerPort("25");
			mailInfo.setValidate(true);
			mailInfo.setUserName("962368404@qq.com"); 
			mailInfo.setPassword("zby962368404199422");//您的邮箱密码 
			mailInfo.setFromAddress("962368404@qq.com");
			mailInfo.setToAddress(registedEmail);
			mailInfo.setSubject("来自微西邮团队的邮件！");
			mailInfo.setContent("您刚才向微西邮团队发送了查看密码的请求，您的密码是：" + passWord + " ,请不要泄露！"); 
			//这个类主要来发送邮件
			SimpleMailSender sms = new SimpleMailSender();
			sms.sendTextMail(mailInfo);//发送文体格式 
			//sms.sendHtmlMail(mailInfo);//发送html格式

			isSuccess = true;
		}

		return isSuccess;
	}
}
