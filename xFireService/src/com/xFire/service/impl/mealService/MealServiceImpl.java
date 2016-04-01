package com.xFire.service.impl.mealService;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.mortbay.log.Log;

import com.xFire.bean.meal.MealBean;
import com.xFire.bean.meal.RestaurantBean;
import com.xFire.service.mealService.MealService;
import com.xFire.util.DButil;

public class MealServiceImpl implements MealService, Serializable
{
	private static final long serialVersionUID = -7622516869848057795L;

	private Connection conn;
	private Statement statement;
	private ResultSet rs;
	
	private final String TABLE_NAME_MEALS = "meals";
	private final String TABLE_NAEM_RESTAURANT = "restaurant";

	@Override
	public List<RestaurantBean> getRestaurantByLocation(String isInXRY) 
	{
		List<RestaurantBean> list = new ArrayList<RestaurantBean>();

		String SQLString = "SELECT id, name, telephoneNumber FROM " + TABLE_NAEM_RESTAURANT 
							+ " WHERE isInXRY = \'" + isInXRY + "\'";
		conn = DButil.connection();
		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			while(rs.next())
			{
				RestaurantBean bean = new RestaurantBean();
				String name = rs.getString("name");
				String telephoneNumber = rs.getString("telephoneNumber");
				String id = rs.getInt("id") + "";
				bean.setName(name);
				bean.setTelephoneNumber(telephoneNumber);
				bean.setId(id);
				
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
		
		return list;
	}

	@Override
	public List<MealBean> getMealByRestaurantId(String id) 
	{
		List<MealBean> list = new ArrayList<MealBean>();

		String SQLString = "SELECT name, price FROM " + TABLE_NAME_MEALS + " WHERE restaurant = \'" + id + "\'";

		conn = DButil.connection();
		try 
		{
			statement = conn.createStatement();
			rs = statement.executeQuery(SQLString);
			while(rs.next())
			{
				MealBean bean = new MealBean();
				String name = rs.getString("name");
				String price = rs.getString("price");
				bean.setName(name);
				bean.setPrice(price);
				
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
		
		return list;
	}
}
