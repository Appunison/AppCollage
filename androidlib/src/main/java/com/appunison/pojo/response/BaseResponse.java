package com.appunison.pojo.response;

import org.codehaus.jackson.annotate.JsonProperty;

public class BaseResponse {
private Boolean isError;
private String desc;
@JsonProperty("iserror")
public Boolean getIsError() {
	return isError;
}
public void setIsError(Boolean isError) {
	this.isError = isError;
}
@JsonProperty("desc")
public String getDesc() {
	return desc;
}
public void setDesc(String desc) {
	this.desc = desc;
}

	
}
