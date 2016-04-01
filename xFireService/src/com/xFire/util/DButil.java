package com.xFire.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DButil 
{
	private static final String DATA_BASE_RESOURSE_NAME = "MICRO_XIYOU";

/*	public DButil()
	{
		connection();
	}
	
	public static void main(String args[])
	{
		new DButil();
	}
*/	
	
	/**
	 * ����MySQL ���ݿ�
	 */
	public static Connection connection()
	{
		Connection conn = null;

        String url = "jdbc:mysql://localhost:3306/" + DATA_BASE_RESOURSE_NAME + "?zeroDateTimeBehavior=convertToNull";//sa�������  
        try 
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, "root", "zby199422");
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
        return conn;
	}
	
	public static void disconnection(Connection conn, Statement stmt,  ResultSet rs)
	{
		try 
		{
            if (rs != null)
					rs.close();
            if (stmt != null)  
                    stmt.close();  
            if (conn != null)  
                    conn.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

	/**
	 * ����SQLServer ���ݿ�
	 */
/*	public static Connection connection()
	{
		Connection conn = null;

        String url = "jdbc:sqlserver://localhost:1433;databaseName=" + DATA_BASE_RESOURSE_NAME + ";user=sa;password=zby199422";//sa�������  
        try 
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url);
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
        return conn;
	}
	
	public static void disconnection(Connection conn, Statement stmt,  ResultSet rs)
	{
		try 
		{
            if (rs != null)
					rs.close();
            if (stmt != null)  
                    stmt.close();  
            if (conn != null)  
                    conn.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
*/
	/**
	 * ����access���ݿ�
	 */
/*	public static Connection connection()
	{
		Connection conn = null;
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection("jdbc:odbc:" + DATA_BASE_RESOURSE_NAME);	
		}catch(ClassNotFoundException cnfe)
		{	
			System.out.println("JdbcOdbcDriver������");
		}catch(SQLException sqle)
		{
			System.out.println("�������ݿ��쳣��");
		}
		
		return conn;
	}

	public static void disconnection(Connection conn)
	{
		try 
		{
			if(!conn.isClosed())
			{
				conn.close();
				conn = null;
			}
		} catch (SQLException e) 
		{
			System.out.println("�Ͽ����ݿ��쳣��");
		}
	}
*/
}
