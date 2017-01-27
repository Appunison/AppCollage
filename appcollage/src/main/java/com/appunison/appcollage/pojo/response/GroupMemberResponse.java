package com.appunison.appcollage.model.pojo.response;

import java.util.List;
/**
 * list group member
 * @author appunison
 *
 */
public class GroupMemberResponse extends BaseResponse{

	private List<GroupMember> result;

	public List<GroupMember> getResult() {
		return result;
	}

	public void setResult(List<GroupMember> result) {
		this.result = result;
	}

}
