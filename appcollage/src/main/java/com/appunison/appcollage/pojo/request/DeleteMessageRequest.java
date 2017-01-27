package com.appunison.appcollage.model.pojo.request;

public class DeleteMessageRequest extends BaseRequest{
	
	private String msg_id;
	private String message_type;
	
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public String getMessage_type() {
		return message_type;
	}
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	
}
