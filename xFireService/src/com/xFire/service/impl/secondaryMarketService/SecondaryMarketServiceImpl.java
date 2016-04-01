package com.xFire.service.impl.secondaryMarketService;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.xFire.bean.secondaryMarket.SecondaryMarketBean;
import com.xFire.service.secondaryMarketService.SecondaryMarketService;
import com.xFire.util.DButil;

/**
 * state状态： ”1“代表 未买到		”2“代表已买到		”3“代表未售出		”4“代表已售出
 */
public class SecondaryMarketServiceImpl implements SecondaryMarketService, Serializable
{
	private static final long serialVersionUID = 8978205432244956670L;
	
	private final String TABLE_NAME_SECONDARY_MARKET = "secondaryMarket";
	
	private Connection conn;
	private Statement statement;
	private ResultSet rs;

	public int getSecondaryMarketCount() 
	{
		int count = 0;
		String SQLString = "SELECT COUNT(*) FROM " + TABLE_NAME_SECONDARY_MARKET;

		conn = DButil.connection();
		try
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			if(rs.next())
				count = rs.getInt(1);
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return count;
	}
	
	@Override
	public List<SecondaryMarketBean> getSecondaryMarketTitle(int haveGotScdMktCount, int lineSize) 
	{
		int secondaryMarketCount = getSecondaryMarketCount();
		int haveNotGotSecondaryMarketCount = secondaryMarketCount - haveGotScdMktCount;
		
		List<SecondaryMarketBean> list = new ArrayList<SecondaryMarketBean>();
		if(haveNotGotSecondaryMarketCount <= 0)
		{
			list = null;
		}
		else
		{
//			String SQLString = "SELECT TOP " + lineSize + " * FROM (SELECT TOP " + haveNotGotSecondaryMarketCount + 
//						" * FROM " + TABLE_NAME_SECONDARY_MARKET + " ORDER BY id ASC) aa ORDER BY id DESC";

			//MySQL			
			String SQLString = "SELECT * FROM " + TABLE_NAME_SECONDARY_MARKET + " ORDER BY id DESC "
					+ " LIMIT " + haveGotScdMktCount + "," + (haveGotScdMktCount + lineSize) ;
			
			conn = DButil.connection();
			try 
			{
				statement = conn.createStatement();
				rs = statement.executeQuery(SQLString);
				while(rs.next())
				{
					SecondaryMarketBean bean = new SecondaryMarketBean();
					String title = rs.getString("title");
					String publisher = rs.getString("publisher");
					String time = rs.getString("time");
					String state = rs.getString("state");
					int id = rs.getInt("id");
					
					bean.setId(id);
					bean.setTitle(title);
					bean.setTime(time);
					bean.setPublisher(publisher);
					bean.setState(state);
					list.add(bean);
				}
			} catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				list = null;
			}finally
			{
				DButil.disconnection(conn, statement, rs);
			}
		}
		
		return list;
	}

	@Override
	public List<SecondaryMarketBean> getMySecondaryMarketPublishedTitle(
					int haveGotScdMktCount, int lineSize, String registedEmail) 
	{
		int secondaryMarketCount = getMySecondaryMarketPublishedCount(registedEmail);
		int haveNotGotSecondaryMarketCount = secondaryMarketCount - haveGotScdMktCount;
		
		List<SecondaryMarketBean> list = new ArrayList<SecondaryMarketBean>();
		if(haveNotGotSecondaryMarketCount <= 0)
		{
			list = null;
		}
		else
		{
//			String SQLString = "SELECT TOP " + lineSize + " * FROM (SELECT TOP " + haveNotGotSecondaryMarketCount + 
//						" * FROM " + TABLE_NAME_SECONDARY_MARKET + " WHERE publisherEmail = \'"
//						+ registedEmail + "\' ORDER BY id ASC) aa ORDER BY id DESC";

			//MySQL			
			String SQLString = "SELECT * FROM " + TABLE_NAME_SECONDARY_MARKET + 
					" WHERE publisherEmail = \'" + registedEmail + "\'" + " ORDER BY id DESC "
					+ " LIMIT " + haveGotScdMktCount + "," + (haveGotScdMktCount + lineSize) ;

			conn = DButil.connection();
			try 
			{
				statement = conn.createStatement();
				rs = statement.executeQuery(SQLString);
				while(rs.next())
				{
					SecondaryMarketBean bean = new SecondaryMarketBean();
					String title = rs.getString("title");
					String publisher = rs.getString("publisher");
					String time = rs.getString("time");
					String state = rs.getString("state");
					int id = rs.getInt("id");
					
					bean.setId(id);
					bean.setTitle(title);
					bean.setTime(time);
					bean.setPublisher(publisher);
					bean.setState(state);
					list.add(bean);
				}
			} catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				list = null;
			}finally
			{
				DButil.disconnection(conn, statement, rs);
			}
		}
		
		return list;
	}
	
	private int getMySecondaryMarketPublishedCount(String registedEmail)
	{
		int count = 0;
		String SQLString = "SELECT COUNT(*) FROM " + TABLE_NAME_SECONDARY_MARKET + " WHERE publisherEmail = \'" + 
				registedEmail + "\'";

		conn = DButil.connection();
		try
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			if(rs.next())
				count = rs.getInt(1);
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return count;
	}

	@Override
	public SecondaryMarketBean getScdMktContentAndTeleNumberByid(int id) 
	{
		SecondaryMarketBean bean = new SecondaryMarketBean();
		
		String SQLString = "SELECT article, telephoneNumber FROM " + TABLE_NAME_SECONDARY_MARKET 
						+ " WHERE id = "  + id ;
		
		conn = DButil.connection();
		
		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			
			if(rs.next())
			{
				bean.setArticle(rs.getString("article"));
				bean.setTelephoneNumber(rs.getString("telephoneNumber"));
			}
			
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			bean = null;
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return bean;
	}

	@Override
	public boolean dealThisTradeById(int id, String stateNum) 
	{
		boolean isDealSuccess = false;
		
		String newState = stateNum.equals("1") ? "2" : (stateNum.equals("3") ? "4" : stateNum);
		
		String SQLString = "UPDATE " + TABLE_NAME_SECONDARY_MARKET + " SET state = \'" 
					+ newState + "\' WHERE id = " + id;
		
		conn = DButil.connection();
		
		try 
		{
			statement = conn.createStatement();
			statement.execute(SQLString);
			isDealSuccess = true;
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return isDealSuccess;
	}

	@Override
	public int publishSecondaryMarket(String title, String publisher, String time, String state,
			String kind, String article, String telephoneNumber, String publisherEmail) 
	{
		int newId = getSecondaryMarketCount() + 1;
		
		String SQLString = "INSERT INTO " + TABLE_NAME_SECONDARY_MARKET + "(id, title, publisher, time, state, kind, "
				+ " article, telephoneNumber, publisherEmail) VALUES(" + newId + ",\'" + title + "\',\'" + publisher
				+ "\', \'" + time + "\', \'" + state + "\', \'" + kind + "\', \'" + article + "\', \'" 
					+ telephoneNumber + "\', \'" + publisherEmail + "\')";
		
		conn = DButil.connection();
		try 
		{
			statement = conn.createStatement();
			statement.execute(SQLString);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return newId;
	}

}
