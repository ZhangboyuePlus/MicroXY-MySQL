package com.xFire.bean.meal;

import java.io.Serializable;

public class RestaurantBean implements Serializable
{
	private static final long serialVersionUID = -1975376172241962629L;
	
	private String id;
	private String name;
	private String telephoneNumber;
	private String isInXRY;
	
	public String getId()
	{
		return this.id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getTelephoneNumber() 
	{
		return telephoneNumber;
	}
	
	public void setTelephoneNumber(String telephoneNumber) 
	{
		this.telephoneNumber = telephoneNumber;
	}
	
	public String getIsInXRY() 
	{
		return isInXRY;
	}
	
	public void setIsInXRY(String isInXRY) 
	{
		this.isInXRY = isInXRY;
	}
}
