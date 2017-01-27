package com.appunison.appcollage.model.pojo.response;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author appunison
 *
 */
@SuppressWarnings("serial")
public class GroupResponse extends BaseResponse implements Serializable{

	private List<Group> result;

	public List<Group> getResult() {
		return result;
	}

	public void setResult(List<Group> result) {
		this.result = result;
	}

}
