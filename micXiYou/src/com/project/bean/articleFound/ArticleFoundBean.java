package com.project.bean.articleFound;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class ArticleFoundBean implements KvmSerializable
{
	private int id;
	private String title;
	private String publisher;
	private String time;
	private String state;
	private String kind;
	private String article;
	private String telephoneNumber;
	private String publisherEmail;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public String getPublisher() 
	{
		return publisher;
	}
	
	public void setPublisher(String publisher) 
	{
		this.publisher = publisher;
	}
	
	public String getTime() 
	{
		return time;
	}
	
	public void setTime(String time) 
	{
		this.time = time;
	}
	
	public String getState() 
	{
		return state;
	}
	
	public void setState(String state) 
	{
		this.state = state;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article)
	{
		this.article = article;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getPublisherEmail() {
		return publisherEmail;
	}

	public void setPublisherEmail(String publisherEmail) {
		this.publisherEmail = publisherEmail;
	}

	@Override
	public Object getProperty(int arg) 
	{
		String str = "";
		
		if(arg == 0)
			str = this.id + "";
		else if(arg == 1)
			str = this.title;
		else if(arg == 2)
			str = this.publisher;
		else if(arg == 3)
			str = this.time;
		else if(arg == 4)
			str = this.state;
		else if(arg == 5)
			str = this.kind;
		else if(arg == 6)
			str = this.article;
		else if(arg == 7)
			str = this.telephoneNumber;
		else if(arg == 8)
			str = this.publisherEmail;

		return str;
	}

	@Override
	public int getPropertyCount() 
	{
		return 9;
	}

	@Override
	public void getPropertyInfo(int i, Hashtable arg1, PropertyInfo info)
	{
		if(i == 0)
		{
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "id";
		}
		else if(i == 1)
		{
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "title";
			
		}
		else if(i == 2)
		{
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "publisher";
			
		}
		else if(i == 3)
		{
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "time";
			
		}
		else if(i == 4)
		{
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "state";
			
		}
		else if(i == 5)
		{
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "kind";
			
		}
		else if(i == 6)
		{
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "article";
			
		}
		else if(i == 7)
		{
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "telephoneNumber";
			
		}
		else if(i == 8)
		{
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "publisherEmail";
			
		}
	}

	@Override
	public void setProperty(int arg, Object object)
	{
		String str = object.toString();
		
		if(arg == 0)
			;
		else if(arg == 1)
			this.title  = str; 
		else if(arg == 2)
			this.publisher  = str; 
		else if(arg == 3)
			this.time  = str; 
		else if(arg == 4)
			this.state  = str; 
		else if(arg == 5)
			this.kind  = str; 
		else if(arg == 6)
			this.article  = str; 
		else if(arg == 7)
			this.telephoneNumber  = str; 
		else if(arg == 8)
			this.publisherEmail  = str; 
		}
}
