package com.appunison.appcollage.dialog;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.model.pojo.response.GalleryUser;
import com.appunison.appcollage.views.activities.ShowPhotosActivity;

public class DownloadDialog extends Dialog implements android.view.View.OnClickListener
{
	private Context context;
	private Button  mbtnNotNow, mbtnDownload;
	private TextView mtxtdialogAlert;
	private ArrayList<GalleryUser> user_Images;
	private String groupName, time, collage;
	
	public DownloadDialog(Context context, ArrayList<GalleryUser> user_Images,String groupName,String time,String collage) {
		super(context);
		this.context = context;
		this.user_Images = user_Images;
		this.groupName = groupName;
		this.time = time;
		this.collage = collage;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCanceledOnTouchOutside(false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.layout_dialog);
		
		mbtnNotNow = (Button) findViewById(R.id.btn_dialog_delete_group_no);
		mbtnDownload = (Button) findViewById(R.id.btn_dialog_delete_group_yes);
		mtxtdialogAlert = (TextView) findViewById(R.id.txt_dialog_alert);
		
		mtxtdialogAlert.setText(context.getResources().getString(R.string.download_msg));
		mbtnNotNow.setText(context.getResources().getString(R.string.not_now));
		mbtnDownload.setText(context.getResources().getString(R.string.download));
		
		mbtnNotNow.setOnClickListener(this);
		mbtnDownload.setOnClickListener(this);
		
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_dialog_delete_group_no:
			dismiss();
			break;
		case R.id.btn_dialog_delete_group_yes:
			//ToastCustom.underDevelopment(context);
			Intent intentphotos = new Intent(context, ShowPhotosActivity.class);
			intentphotos.putExtra("groupName", groupName);
			intentphotos.putExtra("time", time);
			intentphotos.putExtra("collage", collage);
			intentphotos.putExtra("arr_key", user_Images);
			intentphotos.putExtra("firstAction", true);
			//scontext.startActivity(intentphotos);
			((Activity)context).startActivityForResult(intentphotos,AppCollageConstants.INTENT_HOME);
			dismiss();
			break;

		default:
			break;
		}
	} 

}