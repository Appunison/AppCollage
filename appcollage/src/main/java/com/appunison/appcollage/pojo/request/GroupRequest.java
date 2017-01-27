package com.appunison.appcollage.model.pojo.request;
/**
 * 
 * @author appunison
 *
 */
public class GroupRequest extends BaseRequest{

	private String id, group_name;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getGroup_name() {
		return group_name;
	}


	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
}
