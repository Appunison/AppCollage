package com.appunison.appcollage.model.pojo.request;
/**
 * this model uses in twitter
 * friends adapter
 * @author appunison
 *
 */
public class Friends {

	private String twImageUrl, friendId, friendName;
	private boolean check;

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getTwImageUrl() {
		return twImageUrl;
	}

	public void setTwImageUrl(String twImageUrl) {
		this.twImageUrl = twImageUrl;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
}
