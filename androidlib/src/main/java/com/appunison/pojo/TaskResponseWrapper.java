package com.appunison.pojo;
/**
 * 
 * @author appunison
 * This wrapper class is used
 * to get response on task background.
 */
public class TaskResponseWrapper {


	Object object;
	Exception exception;
	
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
}
