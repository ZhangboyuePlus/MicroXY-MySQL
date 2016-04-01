package com.xFire.service.loginOrRegisterService;

public interface LoginOrRegisterService
{
	public String isAccountRegistered(String Email, String passWord);
	
	public String registerNewAccount(String userName, String Email, String passWord);
}
