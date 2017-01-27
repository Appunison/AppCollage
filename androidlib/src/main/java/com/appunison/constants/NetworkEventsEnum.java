package com.appunison.constants;
/**
 * This enum describes differents types 
 * of network evnts 
 * @author appunison
 *
 */
public enum NetworkEventsEnum {
	
	CANCELLED(901,"Event Cancelled"),
	SUCCESS(902,"Event Succed"),
	EXCEPTION(903,"Exception"),
	NETWORK_NOT_AVAILABLE(904,"Network not available"),
	TIME_OUT(905,"Time out"),
	MSG_NOT_READABLE(906,"Http Message Not Readble Exception"),
	STARTED(907,"STARTED");
	
	private int code;
	private String description;
	
	NetworkEventsEnum(int code, String description)
	{
		this.code=code;
		this.description=description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
