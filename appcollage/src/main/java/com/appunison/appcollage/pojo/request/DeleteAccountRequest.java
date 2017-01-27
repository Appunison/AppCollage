package com.appunison.appcollage.model.pojo.request;

/**
 * 
 * @author appunison
 *
 */
public class DeleteAccountRequest extends BaseRequest{

	private String id, password;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
