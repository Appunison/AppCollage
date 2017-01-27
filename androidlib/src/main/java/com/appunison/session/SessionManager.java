package com.appunison.session;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.appunison.persistence.CustomSharedPreference;
/**
 * This Class is used to manage Session
 * @author appunison
 *
 */
public class SessionManager {
	   
	public final String IS_REMEMBER = "remember_me";
	public final String IS_LOGIN="is_login";
	public final String KEY_NAME="key_name";
	public final String KEY_PASSWORD="key_password";
	
	public static final String KEY_USERID="key_userid";
	
	private CustomSharedPreference preference;
	
		public SessionManager(String prefName, Context context){
	        this.preference = new CustomSharedPreference(prefName, context);
	    }
	    /**
	     * Create Login Session
	     * @param userId
	     */
	    public void createLoginSession(String userId){
	    	Map<String, Object> map=new HashMap<String, Object>();
	    	map.put(IS_LOGIN, Boolean.TRUE);
	    	map.put(KEY_USERID, userId);
	    	preference.save(map); 
	    	 
	    }   
	    /**
	     * Store username and Password
	     * @param name
	     * @param password
	     * @param rememberMe
	     */
	    public void storeCredential(String name, String password, boolean rememberMe){
	    	Map<String, Object> map=new HashMap<String, Object>();
	    	map.put(IS_REMEMBER, rememberMe);
	    	if(rememberMe)
	    	{
	    	map.put(KEY_NAME, name);
	    	map.put(KEY_PASSWORD, password);
	    	}
	    	else
	    	{
		    	map.put(KEY_NAME, "");
		    	map.put(KEY_PASSWORD, "");
	    	}
	    	preference.save(map); 
	    	 
	    }   
	     
	    /**
	     * Get stored session data
	     * */
	    public HashMap<String, Object> getUserDetails(){
	        HashMap<String, Object> user = new HashMap<String, Object>();
	        // user name
	        user.put(IS_LOGIN, (Boolean)preference.getValue(IS_LOGIN, Boolean.class));
	        // user name
	        user.put(KEY_NAME, (String)preference.getValue(KEY_NAME, String.class));
	         // user password id
	        user.put(KEY_PASSWORD, (String)preference.getValue(KEY_PASSWORD, String.class));
	        // return remember me Status
	        user.put(IS_REMEMBER, (Boolean)preference.getValue(IS_REMEMBER, Boolean.class));
	        // user id
	        user.put(KEY_USERID, (String)preference.getValue(KEY_USERID, String.class));
	        // return user
	        return user;
	    }
	    
	     
	    /**
	     * Clear session details
	     * */
	    public void logoutUser(){
	        // Clearing all data from Shared Preferences
	    	Map<String, Object> map=new HashMap<String, Object>();
	    	map.put(IS_LOGIN, Boolean.FALSE);
	    	map.put(KEY_USERID, "");
	    	preference.save(map); 
	        }
	     
	    /**
	     * check login
	     * @return
	     */
	    public Boolean isLoggedIn(){
	        return (Boolean)preference.getValue(IS_LOGIN, Boolean.class);
	    }

		public CustomSharedPreference getPreference() {
			return preference;
		}
	
}
