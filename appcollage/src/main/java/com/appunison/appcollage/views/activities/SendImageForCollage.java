package com.appunison.appcollage.views.activities;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.utils.CustomTimer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SendImageForCollage extends RoboActivity implements OnClickListener{

	private @InjectView(R.id.txt_send_Image_timer) TextView mtxtTimer;
	private @InjectView(R.id.txt_send_Image) TextView mtxt1;
	private @InjectView(R.id.txt_send_Image_2) TextView mtxt2;
	
	private @InjectView(R.id.btn_send_Image_no) Button mbtnNotNow;
	private @InjectView(R.id.btn_send_Image_appcollage) Button mbtnAppCollage;
	
	private CustomTimer customTimer;
	private int requestType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_send_image_for_collage);
		
		SpannableString spannable1 = new SpannableString(getResources().getString(R.string.send_msg_collage));
		spannable1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow)), 20, 25, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		
		mtxt1.setText(spannable1);
		SpannableString spannable2 = new SpannableString(getResources().getString(R.string.send_msg_collage1));
		spannable2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow)), 14, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		
		mtxt2.setText(spannable2);
		
		mbtnAppCollage.setOnClickListener(this);
		mbtnNotNow.setOnClickListener(this);
		Intent intent = getIntent();
		requestType = intent.getIntExtra("startNotification", 0);
		if(requestType == AppCollageConstants.AppCollage_REQUEST_NOTIFICATION)
		{
			long endTime = intent.getLongExtra("endTime", 0);
			mtxtTimer.setVisibility(View.VISIBLE);
			
			customTimer = new CustomTimer(SendImageForCollage.this, mtxtTimer, endTime);
			customTimer.timerStart();
		}
		else
		{
			mtxtTimer.setVisibility(View.INVISIBLE);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send_Image_no:
			
			Intent intent = new Intent();
			intent.putExtra("screen", "home");
			setResult(AppCollageConstants.COLLAGE_REQUEST, intent);
			
			finish();
			break;
		case R.id.btn_send_Image_appcollage:
			if(requestType == AppCollageConstants.AppCollage_REQUEST_NOTIFICATION || requestType == AppCollageConstants.AppCollage_REQUEST_INBOX)
			{
				Intent intentMyCamera = new Intent();
				intentMyCamera.putExtra("screen", "mycamera");
				setResult(AppCollageConstants.COLLAGE_REQUEST, intentMyCamera);
			}
			else{
			Intent intentInitiate = new Intent();
			intentInitiate.putExtra("screen", "initiate");
			setResult(AppCollageConstants.COLLAGE_REQUEST, intentInitiate);
			}
			finish();
			
			break;

		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(customTimer!=null)
		{
			customTimer.timerStop(false);
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}
}
