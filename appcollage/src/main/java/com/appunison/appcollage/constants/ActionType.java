package com.appunison.appcollage.constants;
/**
 * this enum contains ActionType for 
 * Different Events.
 * @author appunison
 *
 */
public enum ActionType {
	SIGNUP(201,"Signup Action "),
	LOGIN(202,"Login Action"),
	GROUP(203,"Create Group Action"),
	GROUPLIST(204,"GroupList"),
	USERPROFILE(205,"UserProfile"),
	DELETEGROUP(206,"deleteGroupUser"),
	FORGOTPASSWORDUSER(207,"CreatNewPassword"),
	INVITEVIACONTACT(208,"InviteGroupUser Action"),
	GROUPMEMBER(209,"GroupUser Action"),
	INBOX(210,"Inbox Action"),
	UPDATEMESSGESTATUS(211,"Update Message Status Action"),
	UPLOADIMAGE(212,"Upload Multi Part Image"),
	GALLARYINO(213,"galleryino"),
	CHANGEPASSACTION(214,"ChangePassword"),
	DELETEACCOUNTACTION(215,"DeleteAccount"),
	DEFAULTTEXT(216,"DefaultText"),
	LOGOUT(217,"LogoutUser"),
	HELP(218,"Help"),
	PP(219,"privacy policy"),
	TC(220,"term of services"),
	GROUPCODE(221,"GoupInvitation"),
	EDITGROUP(222,"EditGroup"),
	NOAppCollage(223,"NoAppCollage"),
	DELETEMESSAGE(224,"DeleteMessage"),
	EMAILVERIFY(225,"sendEmailVerification"),
	USERVERIFYINITIATE(226,"UserVerification"),
	USERVERIFYGROUPINVITE(227,"UserVerification"),
	USERVERIFYGROUPINBOX(228,"UserVerification");

	
	private int actionType;
	private String desc;
	private ActionType(int actionType,String desc) {
		this.actionType=actionType;
		this.desc=desc;
	}
	
	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static String getDescriptionFromActionType(int action)
	{
		
		for(ActionType actionTypeEnum:values())
		{
			if(actionTypeEnum.getActionType()==action)
			{
				return actionTypeEnum.getDesc();
			}
		}
		
		return null; 
	}
	
	
}
