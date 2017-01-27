package com.appunison.appcollage.model.pojo.response;

import java.util.List;

public class InboxResponse extends BaseResponse{

	private List<Inbox> result;

	public List<Inbox> getResult() {
		return result;
	}

	public void setResult(List<Inbox> result) {
		this.result = result;
	}

}
