package com.example.cadpart;

public class CADPartUserBean {
	
	private String emailAdd;
	private String token;
	private int logins;

	public CADPartUserBean (String emailAdd, String token, int logins) {
		
		this.emailAdd = emailAdd;
		this.token = token;
		this.logins = logins;
		
	}

	public String getUserName () {
		return emailAdd;
	}
	
	public String getToken () {
		return token;
	}
	
	public int getLogins() {
		return logins;
	}
	
}
