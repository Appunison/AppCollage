package com.appunison.util;

import java.util.concurrent.Executors;

import com.appunison.pojo.TaskResponseWrapper;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
/**
 * This class contains method to execute
 * task on different platforms.
 * @author appunison
 *
 */
public class TaskExecutor {

	
	/**
	 * Execute task in cached Threadpool above honeycomb 
	 * and normally below that.
	 * @param task
	 * @param params
	 */
	public static void executeonCachedThreadPool(AsyncTask<Object,Double,TaskResponseWrapper> task,Object params)
	{
		if(VERSION.SDK_INT>=VERSION_CODES.HONEYCOMB)
		{
			executeAboveHoneyComb(task, params);
		}	
		else
		{
			if(params==null)
			task.execute(params);
			else
			task.execute();
		}
	}
	/**
	 * @param task
	 * @param params
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void executeAboveHoneyComb(AsyncTask<Object,Double,TaskResponseWrapper> task,Object params)
	{
		if(params==null)
			task.executeOnExecutor(Executors.newCachedThreadPool());
		else
		task.executeOnExecutor(Executors.newCachedThreadPool(), params);
		
	}
	
}
	

