package com.appunison.appcollage.model.pojo.request;

public class InboxRequest extends BaseRequest {

	private String user_id, limit, group_id;

	public String getUser_id() {
		return user_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

}
