package com.appunison.basewidgets;

import android.content.Context;

/**
 * This Interface contains method to 
 * show,update and dismiss a progressbar.    
 * @author appunison
 *
 */
public interface IProgressBar {
	
	/**
	 * intialize progress bar.
	 */
	public void intializeProgressBar(Context context);
	/**
	 * show progress bar.
	 */
	public void showProgressBar();
	/**
	 * dismiss progress bar.
	 */
	public void dismissProgressBar();
	/**
	 * update progress bar values.
	 * @param value
	 */
	public void updateProgressBar(double value);
	
}
