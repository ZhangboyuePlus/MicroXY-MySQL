package com.xFire.service.impl.articlesFoundService;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.xFire.bean.articleFound.ArticleFoundBean;
import com.xFire.service.articlesFoundService.ArticlesFoundService;
import com.xFire.util.DButil;

/**
 *	数据库中 kind字段， 当为捡到东西时， kind = “1”,    	当为丢掉东西时， kind = “0” 
 */
public class ArticlesFoundServiceImpl implements ArticlesFoundService, Serializable
{
	private static final long serialVersionUID = -1784935766060667677L;
	private final String TABLE_NAME_ARTICLES_FOUND = "articlesFound";
	
	private Connection conn;
	private Statement statement;
	private ResultSet rs;
	
	public int getArticleFoundCount() 
	{
		int count = 0;
		String SQLString = "SELECT COUNT(*) FROM " + TABLE_NAME_ARTICLES_FOUND;

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

	
	/**
	 * state状态： ”1“代表 未认领		”2“代表已认领		”3“代表未找到		”4“代表已找到
	 */
	@Override
	public List<ArticleFoundBean> getArticleFoundTitle(int haveGotAtcFdCount, int lineSize)
	{
		int articleFoundCount = getArticleFoundCount();
		int haveNotGotArticleFoundCount = articleFoundCount - haveGotAtcFdCount;
		
		List<ArticleFoundBean> list = new ArrayList<ArticleFoundBean>();
		if(haveNotGotArticleFoundCount <= 0)
		{
			list = null;
		}
		else
		{
//SQLServer
//			String SQLString = "SELECT TOP " + lineSize + " * FROM (SELECT TOP " + haveNotGotArticleFoundCount + 
//						" * FROM " + TABLE_NAME_ARTICLES_FOUND + " ORDER BY id ASC) aa ORDER BY id DESC";
			
//MySQL			
			String SQLString = "SELECT * FROM " + TABLE_NAME_ARTICLES_FOUND + " ORDER BY id DESC" 
					+ " LIMIT " + haveGotAtcFdCount + "," + (haveGotAtcFdCount + lineSize);

			conn = DButil.connection();
			try 
			{
				statement = conn.createStatement();
				rs = statement.executeQuery(SQLString);
				while(rs.next())
				{
					ArticleFoundBean bean = new ArticleFoundBean();
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
	public List<ArticleFoundBean> getMyArticleFoundPublishedTitle(int haveGotAtcFdCount, 
				int lineSize, String registedEmail)
	{
		int articleFoundCount = getMyArticleFoundPublishedCount(registedEmail);
		int haveNotGotArticleFoundCount = articleFoundCount - haveGotAtcFdCount;
		
		List<ArticleFoundBean> list = new ArrayList<ArticleFoundBean>();
		if(haveNotGotArticleFoundCount <= 0)
		{
			list = null;
		}
		else
		{
//			String SQLString = "SELECT TOP " + lineSize + " * FROM (SELECT TOP " + haveNotGotArticleFoundCount + 
//						" * FROM " + TABLE_NAME_ARTICLES_FOUND + " WHERE publisherEmail = \'"
//						+ registedEmail + "\' ORDER BY id ASC) aa ORDER BY id DESC";

			//MySQL			
			String SQLString = "SELECT * FROM " + TABLE_NAME_ARTICLES_FOUND + 
					" WHERE publisherEmail = \'" + registedEmail + "\'" + " ORDER BY id DESC "
					+ " LIMIT " + haveGotAtcFdCount + "," + (haveGotAtcFdCount + lineSize) ;
			
			conn = DButil.connection();
			try 
			{
				statement = conn.createStatement();
				rs = statement.executeQuery(SQLString);
				while(rs.next())
				{
					ArticleFoundBean bean = new ArticleFoundBean();
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

	private int getMyArticleFoundPublishedCount(String registedEmail)
	{
		int count = 0;
		String SQLString = "SELECT COUNT(*) FROM " + TABLE_NAME_ARTICLES_FOUND + " WHERE publisherEmail = \'" + 
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
	public ArticleFoundBean getAtcFdContentAndTeleNumberByid(int id) 
	{
		ArticleFoundBean bean = new ArticleFoundBean();
		
		String SQLString = "SELECT article, telephoneNumber FROM " + TABLE_NAME_ARTICLES_FOUND 
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
	public boolean dealThisPublishedById(int id, String stateNum)
	{
		boolean isDealSuccess = false;
		
		String newState = stateNum.equals("1") ? "2" : (stateNum.equals("3") ? "4" : stateNum);
		
		String SQLString = "UPDATE " + TABLE_NAME_ARTICLES_FOUND + " SET state = \'" 
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
	public int publishArticleFound(String title, String publisher, String time, String state,
					String kind, String article, String telephoneNumber, String publisherEmail) 
	{
		int newId = getArticleFoundCount() + 1;
		
		String SQLString = "INSERT INTO " + TABLE_NAME_ARTICLES_FOUND + "(id, title, publisher, time, state, kind, "
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
/*	@Override
public boolean publishArticleFound(ArticleFoundBean bean) 
{
	boolean isSuccess = false;
	
	String title = bean.getTitle();
	String publisher = bean.getPublisher();
	String time = bean.getTime();
	String state = bean.getState();
	String kind = bean.getKind();
	String article = bean.getArticle();
	String telephoneNumber = bean.getTelephoneNumber();
	String publisherEmail = bean.getPublisherEmail();
	
	int newId = getArticleFoundCount() + 1;
	
	String SQLString = "INSERT INTO " + TABLE_NAME_ARTICLES_FOUND + "(id, title, publisher, time, state, kind, "
			+ " article, telephoneNumber, publisherEmail) VALUES(" + newId + ",\'" + title + "\',\'" + publisher
			+ "\', \'" + time + "\', \'" + state + "\', \'" + kind + "\', \'" + article + "\', \'" 
				+ telephoneNumber + "\', \'" + publisherEmail + "\')";
	
	Log.info("SQLString = " + SQLString);
	
	conn = DButil.connection();
	
	try 
	{
		statement = conn.createStatement();
		statement.execute(SQLString);
		
		isSuccess = true;
	} catch (SQLException e) 
	{
		e.printStackTrace();
		isSuccess = false;
	}
	
	return isSuccess;
}
*/