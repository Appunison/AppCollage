package com.appunison.observe;

import com.appunison.constants.NetworkEventsEnum;

/**
 * Custom Observer class to observe
 * Events.
 * @author appunison
 *
 */
public interface CustomObserver{
	/**
	 * 
	 * @param actiontype
	 * @param networkevent
	 * @param request
	 * @param response
	 */
	public void onUpdate(int actiontype,NetworkEventsEnum networkevent,Object request,Object response,Exception excption);
	/**
	 * @param actiontype
	 * @param progress
	 * @param request
	 * @param networkeventsEnum
	 */
	public void onProgress(int actiontype,double progress,NetworkEventsEnum networkeventsEnum,Object request);
	/**
	 * 
	 * @param actionType
	 * @param request
	 * @param networkEventsEnum
	 */
	public void onpreExecute(int actionType,NetworkEventsEnum networkEventsEnum,Object request);
	
	
	
}
