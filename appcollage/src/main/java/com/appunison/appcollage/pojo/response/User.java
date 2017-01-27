package com.appunison.appcollage.model.pojo.response;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User extends Profile implements Serializable{

	private String invite_text, response_text, initiate_text;
	private boolean pendingRequest,email_verification;
	private String id_type;

	public String getInvite_text() {
		return invite_text;
	}

	public void setInvite_text(String invite_text) {
		this.invite_text = invite_text;
	}

	public String getResponse_text() {
		return response_text;
	}

	public void setResponse_text(String response_text) {
		this.response_text = response_text;
	}

	public String getInitiate_text() {
		return initiate_text;
	}

	public void setInitiate_text(String initiate_text) {
		this.initiate_text = initiate_text;
	}

	public boolean isPendingRequest() {
		return pendingRequest;
	}

	public void setPendingRequest(boolean pendingRequest) {
		this.pendingRequest = pendingRequest;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public boolean isEmail_verification() {
		return email_verification;
	}

	public void setEmail_verification(boolean email_verification) {
		this.email_verification = email_verification;
	}


	
}
