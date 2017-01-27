package com.appunison.appcollage.model.pojo.request;
/**
 * this model is used
 * for invitation via contact
 * @author appunison
 *
 */
public class InviteViaContactRequest extends BaseRequest{

	private String group_id, id_type, message, invitedby;
	private String requestby[] = new String[]{};

	public String[] getRequestby() {
		return requestby;
	}

	public void setRequestby(String[] requestby) {
		this.requestby = requestby;
	}

	public String getInvitedby() {
		return invitedby;
	}

	public void setInvitedby(String invitedby) {
		this.invitedby = invitedby;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
