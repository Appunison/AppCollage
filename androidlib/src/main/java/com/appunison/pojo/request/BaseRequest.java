package com.appunison.pojo.request;

import org.codehaus.jackson.annotate.JsonProperty;

public class BaseRequest {

	private String actionType;

	@JsonProperty("actiontype")
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	
	
}
