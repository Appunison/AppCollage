package com.appunison.appcollage.model.pojo.request;
/**
 * model use for to
 * recover forgot password
 * @author appunison
 *
 */

public class ForgotPasswordUser extends BaseRequest{

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String email;
}
