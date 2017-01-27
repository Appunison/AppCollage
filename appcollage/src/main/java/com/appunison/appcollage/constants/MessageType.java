package com.appunison.appcollage.constants;
/**
 * this enum contains ActionType for 
 * Different Events.
 * @author appunison
 *
 */
public enum MessageType {
	
	PROMOTIONAL("Promotional"),
	INVITATION("Invitation"),
	AppCollage_REQUEST("AppCollageRequest"),
	COLLAGE("CollageCompletion");
	
	
	private String desc;
	private MessageType(String desc) {
		this.desc=desc;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static MessageType getMessageType(String desc)
	{
		
		for(MessageType actionTypeEnum:values())
		{
			if(actionTypeEnum.getDesc().equals(desc))
			{
				return actionTypeEnum;
			}
		}
		
		return null; 
	}
	
	
}
