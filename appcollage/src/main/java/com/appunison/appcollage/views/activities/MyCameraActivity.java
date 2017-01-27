package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.utils.BitmapMedia;
import com.appunison.appcollage.utils.CustomTimer;
import com.appunison.appcollage.utils.GetPath;
import com.appunison.appcollage.utils.RotateImage;
import com.appunison.view.BaseActivity;

public class MyCameraActivity extends BaseActivity implements OnClickListener {

	private @InjectView(R.id.txt_mycamera_timer)
	TextView mtxtTimer;
	private @InjectView(R.id.layout_mycamera)
	LinearLayout mlayoutMyCamera;
	private @InjectView(R.id.img_mycamera_collage)
	ImageView mimgForCollage;
	private @InjectView(R.id.btn_mycamera_retake)
	Button mbtnRetake;
	private @InjectView(R.id.btn_mycamera_ok)
	Button mbtnOk;
	private @InjectView(R.id.img_mycamera_back)
	ImageView mimgBack;
	private @InjectView(R.id.img_mycamera_option)
	ImageView mimgOption;
	private Uri path;
	public static Bitmap bitmapIntent;
	private String groupId, message, requestId;
	private int width, height;
	private Intent intent;

	private static final int CAMERA_REQUEST = 1888;
	private CustomTimer customTimer;
	private long endTime;
	private int requestType;
	private boolean isCameraOpen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_my_camera);

		intent = getIntent();
		width = (int) (intent.getExtras().getInt("width") * 1.5f);
		height = (int) (intent.getExtras().getInt("height") * 1.5f);
		requestType = intent.getIntExtra("startNotification", 0);
		if (requestType == AppCollageConstants.AppCollage_REQUEST_NOTIFICATION) {
			requestId = intent.getStringExtra("RequestID");
			endTime = intent.getLongExtra("endTime", 0);
			mtxtTimer.setVisibility(View.VISIBLE);

			customTimer = new CustomTimer(MyCameraActivity.this, mtxtTimer,
					endTime);
			customTimer.timerStart();
		} else {
			groupId = intent.getStringExtra("groupId");
			message = intent.getStringExtra("message");
		}
		if (savedInstanceState == null) {
			Intent cameraIntent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			isCameraOpen = true;
			startActivityForResult(cameraIntent, CAMERA_REQUEST);
		}
		mbtnRetake.setOnClickListener(this);
		mbtnOk.setOnClickListener(this);
		mimgBack.setOnClickListener(this);
		mimgOption.setOnClickListener(this);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onResume() {
		if (mimgForCollage != null) {
			mimgForCollage.setImageBitmap(bitmapIntent);
		}
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean("isCameraOpen", isCameraOpen);

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		isCameraOpen = savedInstanceState.getBoolean("isCameraOpen");

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.img_mycamera_back:
			finish();
			break;

		case R.id.img_mycamera_option:
			// ToastCustom.underDevelopment(MyCameraActivity.this);
			Intent intenthome = new Intent();
			intenthome.putExtra("screen", "home");
			setResult(AppCollageConstants.COLLAGE_REQUEST, intenthome);

			finish();
			break;

		case R.id.btn_mycamera_ok:
			Intent intentAction = new Intent(MyCameraActivity.this,
					CropOnImageActivity.class);
			// ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// bitmapIntent.compress(Bitmap.CompressFormat.PNG, 100, stream);
			// byte[] byteArray = stream.toByteArray();

			intentAction.putExtra("width", width);
			intentAction.putExtra("height", height);
			// intentAction.putExtra("path", byteArray);

			if (requestType == AppCollageConstants.AppCollage_REQUEST_NOTIFICATION) {
				intentAction.putExtra("requestId", requestId);
				intentAction.putExtra("startNotification", requestType);
				intentAction.putExtra("endTime", endTime);
			} else {
				intentAction.putExtra("groupId", groupId);
				intentAction.putExtra("message", message);
			}
			startActivityForResult(intentAction, AppCollageConstants.COLLAGE_REQUEST);

			break;

		case R.id.btn_mycamera_retake:

			Intent cameraIntent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			// cameraIntent.addFlags(flags)
			// path=MediaUtil.getOutputMediaFileUri(MediaUtil.MEDIA_TYPE_IMAGE);
			// cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,path);
			startActivityForResult(cameraIntent, CAMERA_REQUEST);
			break;

		default:
			break;
		}
	}

	Bitmap getBitmap(Uri uri) {
		if (uri != null && uri.getAuthority() != null
				&& getContentResolver() != null) {
			String[] fileColumn = { MediaStore.Images.Media.DATA };

			Cursor imageCursor = getContentResolver().query(uri, fileColumn,
					null, null, null);
			imageCursor.moveToFirst();

			int fileColumnIndex = imageCursor.getColumnIndex(fileColumn[0]);
			String picturePath = imageCursor.getString(fileColumnIndex);
			try
			{
				return BitmapFactory.decodeFile(picturePath);

			}
			catch(OutOfMemoryError e){
			   BitmapFactory.Options options =new BitmapFactory.Options();
				options.inSampleSize = 8;
				return BitmapFactory.decodeFile(picturePath,options);
			}
			
		}
		return null;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST) {
			if (resultCode == RESULT_OK) {
				path = data.getData();
				//mimgForCollage.setImageURI(path);
				
				bitmapIntent=getBitmap(path);
				if(bitmapIntent==null)
				{
					bitmapIntent = (Bitmap) data.getExtras().get("data");
					//bitmapIntent = Bitmap.createScaledBitmap(bitmapIntent, width, height, true);
					bitmapIntent = BitmapMedia.getResizedBitmap(bitmapIntent,
						width, height, bitmapIntent.toString());
					mimgForCollage.setImageBitmap(bitmapIntent);
				}else{
					try{
					bitmapIntent = BitmapMedia.getResizedBitmap(bitmapIntent,
							width, height, bitmapIntent.toString());
					bitmapIntent = RotateImage.rotateBitmap(GetPath.getPath(MyCameraActivity.this,
							path), bitmapIntent);
					mimgForCollage.setImageBitmap(bitmapIntent);
					}catch(OutOfMemoryError e)
					{
						bitmapIntent = BitmapMedia.getResizedBitmap(bitmapIntent,
								width, height, bitmapIntent.toString());
						mimgForCollage.setImageBitmap(bitmapIntent);
						
					}
					
				}
			} else {
				finish();
			}

		}
		if (requestCode == AppCollageConstants.COLLAGE_REQUEST) {
			if (resultCode == AppCollageConstants.COLLAGE_REQUEST) {
				if (data.getStringExtra("screen").equals("mycamera")) {

				} else {
					setResult(AppCollageConstants.COLLAGE_REQUEST, data);
					finish();
				}
			}
			if (resultCode == AppCollageConstants.AppCollage_REQUEST_FROM_INBOX) {
				setResult(resultCode);
				finish();
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (customTimer != null) {
			customTimer.timerStop(false);
		}
	}
}
