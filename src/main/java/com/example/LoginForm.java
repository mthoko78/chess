package com.example;

import java.util.Date;

public class LoginForm {
	
	private String username;
    private String password;
    private Date date;
    
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}	
	
	@Override
	public String toString() {
		return "LoginForm [username=" + username + ", password=" + password + "]";
	}
	
    
}
