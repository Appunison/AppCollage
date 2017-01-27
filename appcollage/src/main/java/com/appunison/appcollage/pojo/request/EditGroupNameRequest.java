package com.appunison.appcollage.model.pojo.request;
/**
 * get group list
 * @author appunison
 *
 */
public class EditGroupNameRequest extends BaseRequest{

	private String group_id;
	private String group_name;
	
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	
}
