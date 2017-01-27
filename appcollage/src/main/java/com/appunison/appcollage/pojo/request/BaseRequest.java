package com.appunison.appcollage.model.pojo.request;

/**
 * Base Request class extended by 
 * all the Request Class
 * @author appunison
 *
 *
 */
public class BaseRequest {

	private String action;
	
	/**
	 * it resturns action
	 * @return
	 */
	public String getAction() {
		return action;
	   
	}
	/**
	 * 
	 * @param action
	 */
	public void setAction(String action) {
		this.action = action;
	}
	
}
