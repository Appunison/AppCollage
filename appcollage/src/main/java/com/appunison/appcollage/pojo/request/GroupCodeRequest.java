package com.appunison.appcollage.model.pojo.request;
/**
 * send unique key for
 * for group invitiation
 * @author appunison
 *
 */
public class GroupCodeRequest extends BaseRequest{

	private String id;
	private String unique_key;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUnique_key() {
		return unique_key;
	}
	public void setUnique_key(String unique_key) {
		this.unique_key = unique_key;
	}
}
