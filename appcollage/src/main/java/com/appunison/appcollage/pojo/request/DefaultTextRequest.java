package com.appunison.appcollage.model.pojo.request;
/**
 * this class is used
 * to send  & save default
 * text on server
 * @author appunison
 *
 */
public class DefaultTextRequest extends BaseRequest{

	private String id, text, type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
