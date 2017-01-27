package com.appunison.appcollage.utils;

import com.appunison.appcollage.R;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;



/**
 * 
 * @author appunison
 *
 */
public class DialogFactory {

	private Context mContext;
	 public DialogFactory(Context context)
	 {
		 this.mContext=context;
	 }
	 
	 /**
	  * Create Custom Transparent Progress Dialog
	  * @return
	  */
	public Dialog getTransParentDialog()
	{
		Dialog dialog=new Dialog(mContext);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(
						Service.LAYOUT_INFLATER_SERVICE);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		View view = inflater.inflate(R.layout.custom_progress_dialog, null);
		dialog.addContentView(view, params);
		dialog.setCancelable(false);
		///Make Dialog Transparent
		   final Window window = dialog.getWindow();
		   window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		
		// view
		return dialog;
	}
	
}

