package com.appunison.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.appunison.R;
import com.appunison.basewidgets.IProgressBar;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.observe.CustomObserver;

public class NetworkBaseFragmentActivity extends BaseFragmentActivity implements CustomObserver, IProgressBar{

	protected Dialog progressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		intializeProgressBar(NetworkBaseFragmentActivity.this);
	}
	
	public void intializeProgressBar(Context context) {
		// TODO Auto-generated method stub
		
		progressbar = new ProgressDialog(context);
		
		ProgressDialog progressDialog=(ProgressDialog)progressbar;
		
		progressDialog.setMessage("Loading...");
		progressDialog.setIndeterminate(false);
		//progressbar.setMax(100);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressbar.setCancelable(false);	
	}
	public void showProgressBar() {
		// TODO Auto-generated method stub
		if(progressbar!=null)
		{
			if(!progressbar.isShowing())
			{
				progressbar.show();
			}
		}		
	}
	public void dismissProgressBar() {
		// TODO Auto-generated method stub
		if(progressbar!=null)
		{
			if(progressbar.isShowing())
			{
				progressbar.dismiss();
			}
		}		
	}
	public void updateProgressBar(double value) {
		// TODO Auto-generated method stub
		
	}
	
	public void onUpdate(int actionType ,NetworkEventsEnum networkEvent, Object request,
			Object response, Exception exception) {
				switch (networkEvent) {
					case EXCEPTION:
						ToastCustom.makeText(NetworkBaseFragmentActivity.this, getResources().getString(R.string.exception_error), Toast.LENGTH_SHORT);
						break;
					case TIME_OUT:
						ToastCustom.makeText(NetworkBaseFragmentActivity.this, getResources().getString(R.string.time_out), Toast.LENGTH_SHORT);
						break;
					case MSG_NOT_READABLE:
						ToastCustom.makeText(NetworkBaseFragmentActivity.this, NetworkEventsEnum.MSG_NOT_READABLE.getDescription(), Toast.LENGTH_SHORT);
						break;
					default:
						break;
			}
				dismissProgressBar();
	}

	
	public void onpreExecute(int actionType, NetworkEventsEnum networkEvent, Object request) {
		switch (networkEvent) {
		case NETWORK_NOT_AVAILABLE:
			ToastCustom.makeText(NetworkBaseFragmentActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT);
			break;
		default:
			break;
		}
		showProgressBar();
	}

	public void onProgress(int actiontype, double progress,
			NetworkEventsEnum networkeventsEnum, Object request) {
		// TODO Auto-generated method stub
		
	}

}