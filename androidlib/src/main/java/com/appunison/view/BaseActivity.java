package com.appunison.view;

import roboguice.activity.RoboActivity;

/**
 * Contain all the basic functionality that a normal activity of a project
 * should have
 * 
 * @author appunison
 * 
 */
public  abstract  class BaseActivity extends RoboActivity {

	private  static boolean isAppOpen=false;
	@Override
	protected void onResume() {
		setAppOpen(true);
		super.onResume();
	}

	public static boolean isAppOpen() {
		return isAppOpen;
	}

	public static void setAppOpen(boolean isAppOpen) {
		BaseActivity.isAppOpen = isAppOpen;
	}

	@Override
	protected void onPause() {
		setAppOpen(false);
		super.onPause();
	}
	
	
	
}
