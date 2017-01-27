package com.appunison.appcollage.model.pojo.response;

import java.io.Serializable;

/**
 * this model is used for
 * create group
 * @author appunison
 *
 */
@SuppressWarnings("serial")
public class Group implements Serializable{

	private String group_id, group_name;

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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.group_name;
	}
}
