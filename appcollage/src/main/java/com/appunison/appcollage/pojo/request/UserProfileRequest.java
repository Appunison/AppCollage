package com.appunison.appcollage.model.pojo.request;

public class UserProfileRequest extends BaseRequest{
	private String id;
	private String device_id;

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
