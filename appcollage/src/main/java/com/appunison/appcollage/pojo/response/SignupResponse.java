package com.appunison.appcollage.model.pojo.response;

import java.io.Serializable;


@SuppressWarnings("serial")
public class SignupResponse extends BaseResponse implements Serializable{

	private User result;

	public User getResult() {
		return result;
	}

	public void setResult(User result) {
		this.result = result;
	}

	
	
}
