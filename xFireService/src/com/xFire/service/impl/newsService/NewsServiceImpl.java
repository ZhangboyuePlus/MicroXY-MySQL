package com.xFire.service.impl.newsService;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.mortbay.log.Log;

import com.xFire.bean.news.SchoolAnnouncementBean;
import com.xFire.bean.news.SchoolInfoBean;
import com.xFire.bean.news.SchoolNewsBean;
import com.xFire.service.newsService.NewsService;
import com.xFire.util.DButil;

public class NewsServiceImpl implements NewsService ,Serializable
{
	private static final long serialVersionUID = -7824519957094685478L;
	private Connection conn;
	private Statement statement;
	private ResultSet rs;
	
	private final String TABLE_NAME_SCHOOL_NEWS = "schoolNews";
	private final String TABLE_NAEM_SCHOOL_ANNOUNCEMENT = "schoolAnnouncement";
	private final String TABLE_NAEM_SCHOOL_INFO = "schoolInfo";
	
/*	public NewsServiceImpl()
	{
//		Log.info( "newsCount = " + getCountByTableName(TABLE_NAME_SCHOOL_NEWS));
		getSchoolNews(21, 21);
	}
	
	public static void main(String a[])
	{
		new NewsServiceImpl();
	}
*/
	
	private void plusNewReadCount(int realPos, String tableName)
	{
/*		int realPos;
		
		if(tableName.equals(this.TABLE_NAEM_SCHOOL_ANNOUNCEMENT))
			realPos = getSchoolAnnouncementCount() - position;
		else if(tableName.equals(this.TABLE_NAEM_SCHOOL_INFO))
			realPos = getSchoolInfoCount() - position;
		else if(tableName.equals(this.TABLE_NAME_SCHOOL_NEWS))
			realPos = getSchoolNewsCount() - position;
*/		
		String SQLStrGetCount = "SELECT readCount FROM " + tableName + " WHERE id = " + realPos;
		
//		conn = DButil.connection();
		try 
		{
//			statement = conn.createStatement();
			rs = statement.executeQuery(SQLStrGetCount);
			
			if(rs.next())
			{
				int newCount = rs.getInt("readCount") + 1; 
				String SQLStrPlusCount = "UPDATE " + tableName + " SET readCount = " + newCount
						+ " WHERE id = " + realPos;
				
				statement.execute(SQLStrPlusCount);
			}
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public String getSchoolAnnouncementContentByPosition(int position) 
	{
		int schoolAnnouncementCount = getSchoolAnnouncementCount();
		int realPos = schoolAnnouncementCount - position;
		
		String article = "";
		String SQLString = "SELECT article FROM " + TABLE_NAEM_SCHOOL_ANNOUNCEMENT + " WHERE id = " + realPos ;
		
		conn = DButil.connection();

		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			
			if(rs.next())
				article = rs.getString("article");
			
			plusNewReadCount(realPos, TABLE_NAEM_SCHOOL_ANNOUNCEMENT);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return article;
	}


	@Override
	public String getSchoolInfoContentByPosition(int position) 
	{
		int schoolInfoCount = getSchoolInfoCount();
		int realPos = schoolInfoCount - position;
		
		String article = "";
		String SQLString = "SELECT article FROM " + TABLE_NAEM_SCHOOL_INFO + " WHERE id = " + realPos ;
		
		conn = DButil.connection();

		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			
			if(rs.next())
				article = rs.getString("article");

			plusNewReadCount(realPos, TABLE_NAEM_SCHOOL_INFO);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return article;
	}

	@Override
	public String getSchoolNewsContentByPosition(int position)
	{
		int schoolNewsCount = getSchoolNewsCount(); 
		int realPos = schoolNewsCount - position;

		String article = "";
		String SQLString = "SELECT article FROM " + TABLE_NAME_SCHOOL_NEWS + " WHERE id = " + realPos ;
		
		conn = DButil.connection();

		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			
			if(rs.next())
				article = rs.getString("article");

			plusNewReadCount(realPos, TABLE_NAME_SCHOOL_NEWS);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return article;
	}

	//TODO
	/*
	 *function		根据传来的参数 ， 取得数据库中剩下的数据倒序后的那一页数据  
	 *param		int haveNotGotNewsCount： 数据总数 count - 传出的数据  = 剩下的数据个数 ，即就是数据正序后，所要取出数据的起点
	 *return
	 */
	@Override
	public List<SchoolNewsBean> getSchoolNews(int haveGotNewsCount, int lineSize)
	{
		int schoolNewsCount = getSchoolNewsCount(); 
		int haveNotGotNewsCount = schoolNewsCount - haveGotNewsCount;
		
		List<SchoolNewsBean> list = new ArrayList<SchoolNewsBean>();
		if(haveNotGotNewsCount <= 0 )
		{
			list = null;
		}
		else
		{
//			String SQLString = "SELECT TOP " + lineSize + " * FROM (SELECT TOP " + haveNotGotNewsCount + " * FROM " + 
//					TABLE_NAME_SCHOOL_NEWS + " ORDER BY id ASC) aa ORDER BY id DESC";

			//MySQL			
			String SQLString = "SELECT * FROM " + TABLE_NAME_SCHOOL_NEWS + " ORDER BY id DESC" 
				+ " LIMIT " + haveGotNewsCount + "," + (haveGotNewsCount + lineSize);
			
			Log.info(SQLString);
			
			SchoolNewsBean bean = null;

			conn = DButil.connection();
			try 
			{
				statement = conn.createStatement();
				rs = statement.executeQuery(SQLString);
				while(rs.next())
				{
					bean = new SchoolNewsBean();
					bean.setTitle(rs.getString("title"));
					bean.setReadCount(rs.getInt("readCount"));
					bean.setTime(rs.getDate("time") + "");
	/*				Log.info("id=" + rs.getInt("id") + "  title=" + rs.getString("title") 
								+ "   time=" + rs.getDate("time").toString() + "   readCount=" + 
								rs.getInt("readCount"));
	*/				
					list.add(bean);
				}
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}finally
			{
				DButil.disconnection(conn, statement, rs);
			}
		}

		return list;
	}

	@Override
	public List<SchoolAnnouncementBean> getSchoolAnnouncement(int haveGotAnnouncementCount, int lineSize) 
	{
		List<SchoolAnnouncementBean> list = new ArrayList<SchoolAnnouncementBean>();

		int announcementCount = getSchoolAnnouncementCount();
		int haveNotGotAnnouncementCount = announcementCount - haveGotAnnouncementCount;
		if(haveNotGotAnnouncementCount <= 0)
		{
			list = null;
		}
		else
		{
//			String SQLString = "SELECT TOP " + lineSize + " * FROM (SELECT TOP " + haveNotGotAnnouncementCount + " * FROM " + 
//					TABLE_NAEM_SCHOOL_ANNOUNCEMENT + " ORDER BY id ASC) aa ORDER BY id DESC";

			//MySQL			
			String SQLString = "SELECT * FROM " + TABLE_NAEM_SCHOOL_ANNOUNCEMENT + " ORDER BY id DESC" 
					+ " LIMIT " + haveGotAnnouncementCount + "," + (haveGotAnnouncementCount + lineSize);
	
			SchoolAnnouncementBean bean = null;

			conn = DButil.connection();
			try 
			{
				statement = conn.createStatement();
				rs = statement.executeQuery(SQLString);
				
				while(rs.next())
				{
					bean = new SchoolAnnouncementBean();
					bean.setTitle(rs.getString("title"));
					bean.setReadCount(rs.getInt("readCount"));
					bean.setTime(rs.getDate("time") + "");
					
					list.add(bean);
				}
			} catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally
			{
				DButil.disconnection(conn, statement, rs);
			}
		}
		
		return list;
	}

	@Override
	public List<SchoolInfoBean> getSchoolInfo(int haveGotInfoCount, int lineSize) 
	{
		List<SchoolInfoBean> list = new ArrayList<SchoolInfoBean>();

		int schoolInfoCount = getSchoolInfoCount();
		int haveNotGotInfoCount = schoolInfoCount - haveGotInfoCount;
		
		if(haveNotGotInfoCount <= 0)
		{
			list = null;
		}
		else
		{
			SchoolInfoBean bean = null;
			
//			String SQLString = "SELECT TOP " + lineSize + " * FROM (SELECT TOP " + haveNotGotInfoCount + " * FROM " +
//					TABLE_NAEM_SCHOOL_INFO + " ORDER BY id ASC) aa ORDER BY id DESC";
			//MySQL			
			String SQLString = "SELECT * FROM " + TABLE_NAEM_SCHOOL_INFO + " ORDER BY id DESC" 
					+ " LIMIT " + haveGotInfoCount + "," + (haveGotInfoCount + lineSize);

//			Log.info(SQLString);
			conn = DButil.connection();
			try 
			{
				statement = conn.createStatement();
				rs = statement.executeQuery(SQLString);
				while(rs.next())
				{
					bean = new SchoolInfoBean();
					bean.setTitle(rs.getString("title"));
					bean.setReadCount(rs.getInt("readCount"));
					bean.setTime(rs.getDate("time") + "");
					
					list.add(bean);
				}
			} catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally
			{
				DButil.disconnection(conn, statement, rs);
			}
		}
		
		return list;
	}

	public int getCountByTableName(String TableName)
	{
		int count = 0;
		String SQLString = "SELECT COUNT(*) FROM " + TableName;

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

	public int getSchoolNewsCount() 
	{
		return getCountByTableName(TABLE_NAME_SCHOOL_NEWS);
	}

	public int getSchoolAnnouncementCount() 
	{
		return getCountByTableName(TABLE_NAEM_SCHOOL_ANNOUNCEMENT);
	}

	public int getSchoolInfoCount() 
	{
		return getCountByTableName(TABLE_NAEM_SCHOOL_INFO);
	}
}
