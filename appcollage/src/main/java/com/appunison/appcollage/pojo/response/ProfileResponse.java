package com.appunison.appcollage.model.pojo.response;

import java.io.Serializable;

/**
 * this class is uesd get
 * data of profile
 * after update
 * @author appunison
 *
 */
@SuppressWarnings("serial")
public class ProfileResponse extends BaseResponse implements Serializable{

	private Profile result;

	public Profile getResult() {
		return result;
	}

	public void setResult(Profile result) {
		this.result = result;
	}
}
