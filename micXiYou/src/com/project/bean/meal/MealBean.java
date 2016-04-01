package com.project.bean.meal;

import java.io.Serializable;

public class MealBean implements Serializable
{
	private static final long serialVersionUID = 3045612648481089721L;

	private String name;
	private String price;
	private String restaurant;
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getPrice() 
	{
		return price;
	}
	
	public void setPrice(String price) 
	{
		this.price = price;
	}
	
	public String getRestaurant() 
	{
		return restaurant;
	}
	
	public void setRestaurant(String restaurant) 
	{
		this.restaurant = restaurant;
	}
}
