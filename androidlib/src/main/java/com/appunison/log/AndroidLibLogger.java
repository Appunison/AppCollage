package com.appunison.log;

import android.util.Log;

import com.appunison.constants.LibConstant;

/**
 * This class log a/c to different mode enabled or not.
 * @author appunison
 *
 */
public class AndroidLibLogger {
	
	
	/**
	 * log if error is enabled
	 * @param tag
	 * @param message
	 */
	 public static void e(String tag,String message)
	 {
		 if(LibConstant.ERROR_MODE)
		 {
			 Log.e(tag, message);
		 }
	 
	 }
	 /**
	  * log if debug is enabled
	  * @param tag
	  * @param mesage
	  */
	 public static void d(String tag,String mesage )
	 {
		 if(LibConstant.DEBUG_MODE)
		 {
			 Log.d(tag, mesage);
		 }
	 }
	 /**
	  * log if warn is enabled
	  * @param tag
	  * @param message
	  */
	 public static void w(String tag,String message)
	 {
		 if(LibConstant.WARN_MODE)
		 {
			 Log.w(tag, message);
		 }
	 }
}
