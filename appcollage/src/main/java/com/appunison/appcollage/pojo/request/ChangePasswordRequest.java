package com.appunison.appcollage.model.pojo.request;
/**
 * this class is 
 * used for change Passwod
 * request
 * @author appunison
 *
 */
public class ChangePasswordRequest extends BaseRequest{

	private String old_password, new_password,id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOld_password() {
		return old_password;
	}

	public void setOld_password(String old_password) {
		this.old_password = old_password;
	}

	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}
}
