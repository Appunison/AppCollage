package com.appunison.appcollage.model.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CMS {

	@JsonProperty("Content")
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
