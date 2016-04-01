package com.xFire.service.impl.loginOrRegisterService;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.xFire.service.loginOrRegisterService.LoginOrRegisterService;
import com.xFire.util.DButil;

public class LoginOrRegisterServiceImpl implements LoginOrRegisterService, Serializable
{
	private static final long serialVersionUID = -741328805075557262L;
	private final String ACCOUNT_TABLE_NAME = "account";
	
	/**
	 * "1##"表示登录成功， 后面所加的内容为 userName；
	 * "2##"表示登录失败。
	 */
	@Override
	public String isAccountRegistered(String Email, String passWord) 
	{
		Connection conn = null;
		Statement statement = null;
		ResultSet rs  =null;

		String returnInfo = "";
		
		String SQLString = "SELECT userName FROM " + ACCOUNT_TABLE_NAME + " WHERE Email = \'" + Email 
					+ "\' AND passWord = \'" + passWord + "\'";
		
		conn = DButil.connection();
		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			
			if(rs.next())
				returnInfo = "1##" + rs.getString("userName");
			else 
				returnInfo = "2##";
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}

		return returnInfo;
	}

	/**
	 * 返回String ： “0”代表注册成功	 “1”代表用户名已被注册；	“2”代表Email已被注册；	"3"服务器出现异常
	 */
	@Override
	public String registerNewAccount(String userName, String Email,	String passWord) 
	{
		String flag = "";
		
		Connection conn = null;
		Statement statement = null;
		ResultSet rs  =null;

		String SQLStringUserName = "SELECT COUNT(*) FROM " + ACCOUNT_TABLE_NAME + 
					" WHERE userName = \'" + userName + "\'";	
		String SQLStringEmail = "SELECT COUNT(*) FROM " + ACCOUNT_TABLE_NAME + 
					" WHERE Email = \'" + Email + "\'";
		String SQLStringRegister = "INSERT INTO " + ACCOUNT_TABLE_NAME + "(userName, Email, passWord)" +
				" VALUES(\'" + userName + "\', \'" + Email + "\', \'" + passWord + "\')";
		
		conn = DButil.connection();
		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLStringUserName);
			
			if(rs.next() && rs.getInt(1) == 0)
			{
				rs = statement.executeQuery(SQLStringEmail);
				if(rs.next() && rs.getInt(1) == 0)
				{
					statement.execute(SQLStringRegister);
					flag = "0";
				}
				else
					flag = "2";
			}
			else
				flag = "1";
			
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = "3";
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return flag;
	}
}
