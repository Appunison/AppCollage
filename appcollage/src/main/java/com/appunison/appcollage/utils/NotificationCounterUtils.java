package com.appunison.appcollage.utils;

import java.util.Date;
import java.util.HashSet;

import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.persistence.CustomSharedPreference;

import android.content.Context;

public class NotificationCounterUtils {

	public static HashSet<String> removeExpiredData(Context context){
		CustomSharedPreference customSharedPreference = new CustomSharedPreference(
				AppCollageConstants.PREF_NAME, context);
		HashSet<String> notifications = (HashSet<String>) customSharedPreference.getList(
				"notification", new HashSet<String>());
		
		for (int counter = 0; counter < notifications.size(); counter++) {
			String obj = (String) notifications.toArray()[counter];
			String value = (String) notifications.toArray()[counter];
			String[] list = value.split("#");

			// order endTime, groupName , requestID, userID, initiatedBy;
			long systemTime = new Date().getTime();
			long endTime = Long.parseLong(list[0]);

			if (endTime < systemTime) {
			
			notifications.remove(obj);
			notifications.size();
			}
		}
		customSharedPreference.saveList("notification", notifications);
		notifications = (HashSet<String>) customSharedPreference.getList(
				"notification", notifications);
		return notifications;
	}
	
	public static int removeAppCollageById(Context context,String id){
		CustomSharedPreference customSharedPreference = new CustomSharedPreference(
				AppCollageConstants.PREF_NAME, context);
		HashSet<String> notifications = (HashSet<String>) customSharedPreference.getList(
				"notification", new HashSet<String>());
		
		for (int counter = 0; counter < notifications.size(); counter++) {
			String obj = (String) notifications.toArray()[counter];
			String value = (String) notifications.toArray()[counter];
			String[] list = value.split("#");

			// order endTime, groupName , requestID, userID, initiatedBy;
			
			if (id.equalsIgnoreCase(list[2])) {
			notifications.remove(obj);
			}
		}
		customSharedPreference.saveList("notification", notifications);
		notifications = (HashSet<String>) customSharedPreference.getList(
				"notification", notifications);
		return notifications.size();
	}
}
