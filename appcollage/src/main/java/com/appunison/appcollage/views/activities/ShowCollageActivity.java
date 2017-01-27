package com.appunison.appcollage.views.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.dialog.DownloadDialog;
import com.appunison.appcollage.dialog.FBImageShareActivity;
import com.appunison.appcollage.dialog.ShareSocialMediaDialog;
import com.appunison.appcollage.imageGetter.FileCache;
import com.appunison.appcollage.imageGetter.ImageLoader;
import com.appunison.appcollage.model.pojo.response.GalleryUser;
import com.appunison.appcollage.utils.DateTimeConverter;
import com.appunison.appcollage.R;

import roboguice.inject.InjectView;

public class ShowCollageActivity extends FBImageShareActivity{

	private @InjectView(R.id.img_collage_back) ImageView mimgBack;
	private @InjectView(R.id.img_collage_share) ImageView mimgShare;
	private @InjectView(R.id.img_collage_download) ImageView mimgDownload;
	private @InjectView(R.id.img_collage_option) ImageView mimgOption;
	private @InjectView(R.id.txt_collage_group_name) TextView mtxtGroupName;
	private @InjectView(R.id.txt_collage_date_time) TextView mtxtTime;
	private @InjectView(R.id.btn_prevoius) Button mbtnPrevious;
	private @InjectView(R.id.btn_next) Button mbtnNext;
	private @InjectView(R.id.flipper_collage) ViewFlipper mflipperCollage;
	private ArrayList<GalleryUser> user_Images;
	private List<String> urls;
	private String groupName ,time;
	private String collage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_collage);
		
		urls = new ArrayList<String>();
		
		Intent intent = getIntent();
		if(intent.getBooleanExtra("collageNotification", false))
		{
			mimgDownload.setVisibility(View.INVISIBLE);
			mimgShare.setVisibility(View.INVISIBLE);
			mimgOption.setVisibility(View.INVISIBLE);
		}
		user_Images = (ArrayList<GalleryUser>) intent.getSerializableExtra("arr_key");
		collage = intent.getStringExtra("collage");
		groupName = intent.getStringExtra("groupName");
		time = intent.getStringExtra("time");
		mtxtGroupName.setText(groupName.toUpperCase());
		DateTimeConverter converter = new DateTimeConverter();
		String date = converter.getDateFromTimeStamp(time);
		Log.d("Createddate1", ": "+date);
		date = converter.convertDateFormat(date, DateTimeConverter.dd_MM_yyyy,
				DateTimeConverter.yyyy_MM_dd);
		String timeconverted = converter.getTimeFromTimeStamp(time);
		mtxtTime.setText(date+" "+timeconverted);
		
		mbtnNext.setVisibility(View.INVISIBLE);
		mbtnPrevious.setVisibility(View.INVISIBLE);
		
		
	    setFlipperImage(collage);
	    
	    mimgBack.setOnClickListener(this);
		mimgShare.setOnClickListener(this);
		mimgDownload.setOnClickListener(this);
		mimgOption.setOnClickListener(this);
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_collage_back:
			finish();
			break;
		case R.id.img_collage_share:
			//ToastCustom.underDevelopment(ShowCollageActivity.this);
			new ShareSocialMediaDialog(this, collage).show();
			break;
		case R.id.img_collage_download:
			//ToastCustom.underDevelopment(ShowCollageActivity.this);
			for(int i=0;i<user_Images.size();i++)
			{
				urls.add(user_Images.get(i).getImage());
			}
			if(FileCache.filesForUrlExists(urls))
			{
				Intent intentphotos = new Intent(ShowCollageActivity.this, ShowPhotosActivity.class);
				intentphotos.putExtra("groupName", groupName);
				intentphotos.putExtra("time", time);
				intentphotos.putExtra("collage", collage);
				intentphotos.putExtra("arr_key", user_Images);
				//startActivity(intentphotos);
				startActivityForResult(intentphotos, AppCollageConstants.INTENT_HOME);
				finish();
			}
			else{
				new DownloadDialog(this,user_Images, groupName, time, collage).show();
			}
			
			break;
		case R.id.img_collage_option:
			//ToastCustom.underDevelopment(ShowCollageActivity.this);
			Intent intent = new Intent();
			intent.putExtra("screen", "finalHome");
			setResult(AppCollageConstants.FINAL_HOME, intent);
			finish();
			break;

		default:
			break;
		}
	}
	 private void setFlipperImage(String url)
	    {
	    	 LayoutInflater inflater = (LayoutInflater) this
	                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	         View view = inflater.inflate(R.layout.flipper_collage_image_container, null);
	         TextView txt_user_name = (TextView) view.findViewById(R.id.txt_user_name);
	         ImageView img_user = (ImageView) view.findViewById(R.id.img_user);
	         txt_user_name.setVisibility(view.INVISIBLE);
	         
	         new ImageLoader(ShowCollageActivity.this).DisplayImage(url, img_user);
	         
	         mflipperCollage.addView(view);
	    }
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
	}
}
