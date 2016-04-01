package com.xFire.bean.user;

import java.io.Serializable;

public class User implements Serializable
{
	private static final long serialVersionUID = 5864834624609189565L;
	
	private String name;
	private String Email;
	private String passWord;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
}
