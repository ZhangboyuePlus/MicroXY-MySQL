package com.xFire.service.accountService;

public interface AccountService 
{
	public boolean modifyUserName(String Email, String newUserName);
	
	public boolean modifyPassWord(String Email, String oldPassWord, String newPassWord);
	
	public boolean sendPasswordToRegistedEmail(String registedEmail);
}
