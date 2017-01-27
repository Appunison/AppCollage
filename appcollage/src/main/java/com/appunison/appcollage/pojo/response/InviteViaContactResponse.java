package com.appunison.appcollage.model.pojo.response;

import java.util.List;

public class InviteViaContactResponse extends BaseResponse{

	private List<Invite> result;

	public List<Invite> getResult() {
		return result;
	}

	public void setResult(List<Invite> result) {
		this.result = result;
	}

}
