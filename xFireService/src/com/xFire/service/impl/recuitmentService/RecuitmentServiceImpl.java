package com.xFire.service.impl.recuitmentService;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.xFire.bean.recuitment.RecuitmentBean;
import com.xFire.service.recuitmentService.RecuitmentService;
import com.xFire.util.DButil;

public class RecuitmentServiceImpl implements RecuitmentService, Serializable
{
	private static final long serialVersionUID = 7474069343932463875L;
	
	private Connection conn;
	private Statement statement;
	private ResultSet rs;
	
	private final String TABLE_NAME_RECUITMENT = "recuitment";
	
/*	public RecuitmentServiceimpl()
	{
		int count = getRecuitmentCount();
		System.out.println("Count = "  + count);
		getRecuitmentTitle(count, 14);
		
		System.out.println(getRecuitmentContentByPosition(1));
	}
	
	public static void main(String args[])
	{
		new RecuitmentServiceimpl();
	}
*/
	public int getRecuitmentCount() 
	{
		int count = 0;
		String SQLString = "SELECT COUNT(*) FROM " + TABLE_NAME_RECUITMENT;

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
	public List<RecuitmentBean> getRecuitmentTitle(int haveGotRecuitmentCount, int lineSize) 
	{
		int recuitmentCount = getRecuitmentCount();
		int haveNotGotRecuitmentCount = recuitmentCount - haveGotRecuitmentCount;
		
		List<RecuitmentBean> list = new ArrayList<RecuitmentBean>();
		if(haveNotGotRecuitmentCount <= 0)
		{
			list = null;
		}
		else
		{
//			String SQLString = "SELECT TOP " + lineSize + " * FROM (SELECT TOP " + haveNotGotRecuitmentCount + " * FROM " +
//					TABLE_NAME_RECUITMENT + " ORDER BY id ASC) aa ORDER BY id DESC";

			//MySQL			
			String SQLString = "SELECT * FROM " + TABLE_NAME_RECUITMENT + " ORDER BY id DESC" 
					+ " LIMIT " + haveGotRecuitmentCount + "," + (haveGotRecuitmentCount + lineSize);

			conn = DButil.connection();
			try 
			{
				statement = conn.createStatement();
				rs = statement.executeQuery(SQLString);
				while(rs.next())
				{
					RecuitmentBean bean = new RecuitmentBean();
					String title = rs.getString("title") + "";
					String time = rs.getString("time") + "";
					int readCount = rs.getInt("readCount");
					
					bean.setTitle(title);
					bean.setReadCount(readCount);
					bean.setTime(time);
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
	public String getRecuitmentContentByPosition(int position) 
	{
		int recuitmentCount = getRecuitmentCount(); 
		int realPos = recuitmentCount - position;
		
		String content = "";
		String SQLString = "SELECT article FROM " + TABLE_NAME_RECUITMENT + " WHERE id = " + realPos ;
		
		conn = DButil.connection();

		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			
			if(rs.next())
				content = rs.getString("article");
			
			plusReadCountById(realPos);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}finally
		{
			DButil.disconnection(conn, statement, rs);
		}
		
		return content;
	}
	
	private boolean plusReadCountById(int id)
	{
		boolean Ok = false;
		int readCount = 0;
		
		String SQLStrGetReadCount = "SELECT readCount FROM " + TABLE_NAME_RECUITMENT + 
				" WHERE id =" + id;
		
//		conn = DButil.connection();
		try 
		{
//			statement = conn.createStatement();
			rs = statement.executeQuery(SQLStrGetReadCount);
			
			if(rs.next())
			{
				readCount = rs.getInt("readCount");
				
				String SQLStrPlusReadCount = "UPDATE " + TABLE_NAME_RECUITMENT + 
						" SET readCount =" + ++readCount;
				statement.execute(SQLStrPlusReadCount);
				Ok = true;
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return Ok;
	}
}
