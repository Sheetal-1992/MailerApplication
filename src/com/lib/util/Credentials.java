package com.lib.util;

public class Credentials {

	String user_name;
	String password;
	
	
	public Credentials(String u_name,String u_password)
	{
		user_name = u_name;
		password = u_password;	
	}
	
	
	public String getEmailId()
	{
		return user_name;
	}
	public String getPassword()
	{
		return password;
	}
	
}
