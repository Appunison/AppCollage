package com.appunison.appcollage.model.pojo.response;
/**
 * 
 * @author appunison
 *
 */
public class DefaultTextResponse extends BaseResponse{

	public User getResult() {
		return result;
	}

	public void setResult(User result) {
		this.result = result;
	}

	private User result;
}
