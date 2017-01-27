package com.appunison.appcollage.views.activities;

import java.util.ArrayList;

import roboguice.inject.InjectView;
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

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.dialog.FBImageShareActivity;
import com.appunison.appcollage.dialog.ShareSocialMediaDialog;
import com.appunison.appcollage.imageGetter.ImageLoader;
import com.appunison.appcollage.model.pojo.response.GalleryUser;
import com.appunison.appcollage.utils.DateTimeConverter;

public class ShowPhotosActivity extends FBImageShareActivity{

	private @InjectView(R.id.img_collage_back) ImageView mimgBack;
	private @InjectView(R.id.img_collage_share) ImageView mimgShare;
	private @InjectView(R.id.img_collage_download) ImageView mimgDownload;
	private @InjectView(R.id.img_collage_option) ImageView mimgOption;
	private @InjectView(R.id.img_virtual) ImageView mimgVitual;

	private @InjectView(R.id.txt_collage_group_name) TextView mtxtGroupName;
	private @InjectView(R.id.txt_collage_date_time) TextView mtxtTime;
	private @InjectView(R.id.btn_prevoius) Button mbtnPrevious;
	private @InjectView(R.id.btn_next) Button mbtnNext;
	private @InjectView(R.id.flipper_collage) ViewFlipper mflipperCollage;
	private @InjectView(R.id.img_collage_download) ImageView colageImage;
	private ImageLoader imageLoader;
	private Intent intent;
	private String groupName ,time, collage, indiviualPhoto;
	private ArrayList<GalleryUser> user_Images;
	
	private String TAG = ShowPhotosActivity.class.getSimpleName();
	float initialX, initialY;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_collage);
		intent = getIntent();
		colageImage.setImageResource(R.drawable.collage);
		imageLoader = new ImageLoader(this);
		download();
		
		user_Images = (ArrayList<GalleryUser>) intent.getSerializableExtra("arr_key");
		groupName = intent.getStringExtra("groupName");
		time = intent.getStringExtra("time");
		collage = intent.getStringExtra("collage");
		
		mtxtGroupName.setText(groupName.toUpperCase());
		
		DateTimeConverter converter = new DateTimeConverter();
		String date = converter.getDateFromTimeStamp(time);
		date = converter.convertDateFormat(date, DateTimeConverter.dd_MM_yyyy,
				DateTimeConverter.yyyy_MM_dd);
		String timeloacl = converter.getTimeFromTimeStamp(time);
		mtxtTime.setText(date+" "+timeloacl);

		for(int i=0;i<user_Images.size();i++)
		{
			setFlipperImage(user_Images.get(i).getImage(),user_Images.get(i).getName());
		}
		
		indiviualPhoto = user_Images.get(mflipperCollage.getDisplayedChild()).getImage();
		Log.d("index",""+mflipperCollage.getDisplayedChild());
		Log.d("indiviualPhoto",indiviualPhoto);
		
		mbtnNext.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				mflipperCollage.showNext();
				indiviualPhoto = user_Images.get(mflipperCollage.getDisplayedChild()).getImage();
				Log.d("index",""+mflipperCollage.getDisplayedChild());
				Log.d("indiviualPhoto",indiviualPhoto);
			}
		});

		mbtnPrevious.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				mflipperCollage.showPrevious();
				indiviualPhoto = user_Images.get(mflipperCollage.getDisplayedChild()).getImage();
				Log.d("index",""+mflipperCollage.getDisplayedChild());
				Log.d("indiviualPhoto",indiviualPhoto);
			}
		});
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
			//ToastCustom.underDevelopment(ShowPhotosActivity.this);
		/*	List<String> urls  = new ArrayList<String>();
			urls.add(collage);
			if(FileCache.filesForUrlExists(urls))
			{*/
				new ShareSocialMediaDialog(this, indiviualPhoto).show();
			//}
			
			break;
		case R.id.img_collage_download:
			//ToastCustom.underDevelopment(ShowPhotosActivity.this);
			if(intent.getBooleanExtra("firstAction", false))
			{
				finish();
			}
			else{
				Intent intentCollage = new Intent(ShowPhotosActivity.this, ShowCollageActivity.class);
				intentCollage.putExtra("groupName", groupName);
				intentCollage.putExtra("time", time);
				intentCollage.putExtra("collage", collage);
				intentCollage.putExtra("arr_key", user_Images);
				startActivityForResult(intentCollage, AppCollageConstants.INTENT_HOME);
				finish();
			}
			break;
		case R.id.img_collage_option:
			//ToastCustom.underDevelopment(ShowPhotosActivity.this);
			Intent intent = new Intent();
			intent.putExtra("screen", "finalHome");
			setResult(AppCollageConstants.FINAL_HOME, intent);
			finish();
			break;

		default:
			break;
		}
	}
	private void setFlipperImage(String url, String text)
	{
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.flipper_collage_image_container, null);
		TextView txt_user_name = (TextView) view.findViewById(R.id.txt_user_name);
		ImageView img_user = (ImageView) view.findViewById(R.id.img_user);

		imageLoader.DisplayImage(url, img_user);
		txt_user_name.setText(text);
		mflipperCollage.addView(view);
		
		
	}
	private void download()
	{
			ImageLoader loader = new ImageLoader(this);
			loader.DisplayImage(collage, mimgVitual);
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
	
/*	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    //mGestureDetector.onTouchEvent(event);
	 
	    int action = event.getActionMasked();
	 
	    switch (action) {
	 
	        case MotionEvent.ACTION_DOWN:
	            initialX = event.getX();
	            initialY = event.getY();
	 
	            Log.d(TAG, "Action was DOWN");
	            break;
	 
	        case MotionEvent.ACTION_MOVE:
	            Log.d(TAG, "Action was MOVE");
	            break;
	 
	        case MotionEvent.ACTION_UP:
	            float finalX = event.getX();
	            float finalY = event.getY();
	 
	            Log.d(TAG, "Action was UP");
	 
	            if (initialX < finalX) {
	            	Toast.makeText(getApplicationContext(), "Previous", Toast.LENGTH_SHORT).show();
	                Log.d(TAG, "Left to Right swipe performed");
	            }
	 
	            if (initialX > finalX) {
	            	Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_SHORT).show();
	                Log.d(TAG, "Right to Left swipe performed");
	            }
	 
	            if (initialY < finalY) {
	                Log.d(TAG, "Up to Down swipe performed");
	            }
	 
	            if (initialY > finalY) {
	                Log.d(TAG, "Down to Up swipe performed");
	            }
	 
	            break;
	 
	        case MotionEvent.ACTION_CANCEL:
	            Log.d(TAG,"Action was CANCEL");
	            break;
	 
	        case MotionEvent.ACTION_OUTSIDE:
	            Log.d(TAG, "Movement occurred outside bounds of current screen element");
	            break;
	    }
	 
	    return super.onTouchEvent(event);
	}*/


}
