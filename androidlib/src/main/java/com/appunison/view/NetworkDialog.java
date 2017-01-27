package com.appunison.view;


import com.appunison.R;
import com.appunison.basewidgets.IProgressBar;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.observe.CustomObserver;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;
/**
 * this class made for
 * custom dialog
 * @author appunison
 *
 */

public class NetworkDialog extends Dialog implements CustomObserver, IProgressBar{

	private ProgressDialog progressbar;
	private Context context;
	
	public NetworkDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		
		intializeProgressBar(context);
	}

	public void intializeProgressBar(Context context) {
		// TODO Auto-generated method stub
		progressbar = new ProgressDialog(context);
		progressbar.setMessage("Loading...");
		progressbar.setIndeterminate(false);
		//progressbar.setMax(100);
		progressbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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

	public void onUpdate(int actiontype, NetworkEventsEnum networkevent,
			Object request, Object response, Exception excption) {
		// TODO Auto-generated method stub
		dismissProgressBar();
	switch (networkevent) {
		
		case EXCEPTION:
			ToastCustom.makeText(context, context.getResources().getString(R.string.exception_error), Toast.LENGTH_SHORT);
			break;
		case TIME_OUT:
			ToastCustom.makeText(context, context.getResources().getString(R.string.time_out), Toast.LENGTH_SHORT);
			break;
		case MSG_NOT_READABLE:
			ToastCustom.makeText(context, NetworkEventsEnum.MSG_NOT_READABLE.getDescription(), Toast.LENGTH_SHORT);
			break;
		default:
			break;
}
		
	}

	public void onpreExecute(int actionType,
			NetworkEventsEnum networkEventsEnum, Object request) {
		// TODO Auto-generated method stub
		showProgressBar();
		switch (networkEventsEnum) {
		case NETWORK_NOT_AVAILABLE:
			ToastCustom.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT);
			break;
		default:
			break;
		}
		
	}

	public void onProgress(int actiontype, double progress,
			NetworkEventsEnum networkeventsEnum, Object request) {
		// TODO Auto-generated method stub
		
	}

}
