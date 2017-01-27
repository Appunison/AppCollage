package com.appunison.appcollage.model.pojo.response;

/**
 * 
 * @author appunison
 *
 */
public class BaseResponse {
	
	private boolean resp;
	private String desc;
	public boolean isResp() {
		return resp;
	}
	public void setResp(boolean resp) {
		this.resp = resp;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
