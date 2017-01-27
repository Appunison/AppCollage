package com.appunison.pojo.request;

import org.codehaus.jackson.annotate.JsonProperty;

public class Login extends BaseRequest{

	private String username;
	private String password;
	@JsonProperty("username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@JsonProperty("password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
