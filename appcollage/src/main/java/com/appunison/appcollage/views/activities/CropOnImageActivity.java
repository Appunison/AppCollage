package com.appunison.appcollage.views.activities;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import roboguice.inject.InjectView;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.response.ImageForCollageResponse;
import com.appunison.appcollage.utils.BitmapMedia;
import com.appunison.appcollage.utils.CustomTimer;
import com.appunison.appcollage.utils.DialogFactory;
import com.appunison.appcollage.utils.GetPath;
import com.appunison.appcollage.utils.NotificationCounterUtils;
import com.appunison.appcollage.utils.SoftKeyboardHandledLinearLayout;
import com.appunison.appcollage.utils.SoftKeyboardHandledLinearLayout.SoftKeyboardVisibilityChangeListener;
import com.appunison.appcollage.utils.TextOnImage;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;
import com.appunison.view.NetworkBaseActivity;

public class CropOnImageActivity extends NetworkBaseActivity implements
		OnClickListener {

	private String TAG = CropOnImageActivity.class.getName();
	private @InjectView(R.id.txt_mag_counter)
	TextView mtxtCounter;
	private @InjectView(R.id.img_mycamera_back)
	ImageView mimgBack;
	private @InjectView(R.id.img_mycamera_option)
	ImageView mimgOption;
	private @InjectView(R.id.img_write_text)
	CropImageView mimgMain;

	private @InjectView(R.id.txt_crop_image_timer)
	TextView mtxtTimer;
	private @InjectView(R.id.txt_write_text_on_tap)
	TextView mtxtWriteTextOnTap;
	private @InjectView(R.id.edt_write_text_on_image)
	EditText medtWriteTextOnImage;
	private @InjectView(R.id.btn_write_text_default)
	Button mbtnSetDefaultText;
	private @InjectView(R.id.layout_container)
	SoftKeyboardHandledLinearLayout mainView;

	private final int PIC_CROP = 1;
	private Bitmap uri;
	private String groupId, userid, requestId;
	private ForeGroundTask service;
	private CustomSharedPreference mPref;
	private boolean tapListner = false;
	private Bitmap croppedImage;
	private String message;
	private ImageForCollageResponse imageForCollageResponse;
	private int width, height;
	private Intent intent;
	private CustomTimer customTimer;
	private long endTime;
	private int requestType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_crop_on_image);

		intent = getIntent();
		//byte[] byteArray = getIntent().getByteArrayExtra("path");
		//uri = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		uri=MyCameraActivity.bitmapIntent;
		width = uri.getWidth();
		height = uri.getHeight();
		//width = intent.getExtras().getInt("width");
		//height = intent.getExtras().getInt("height");

		requestType = intent.getIntExtra("startNotification", 0);

		if (requestType == AppCollageConstants.AppCollage_REQUEST_NOTIFICATION) {
			requestId = intent.getStringExtra("requestId");
			endTime = intent.getLongExtra("endTime", 0);
			mtxtTimer.setVisibility(View.VISIBLE);

			customTimer = new CustomTimer(CropOnImageActivity.this, mtxtTimer,
					endTime);
			customTimer.timerStart();

		} else {
			groupId = intent.getStringExtra("groupId");
			message = intent.getStringExtra("message");
		}

		service = new ForeGroundTask(this, this);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME,
				CropOnImageActivity.this);
		userid = mPref.getString(SessionManager.KEY_USERID, null);

		// mimgMain.setImageBitmap(MediaUtil.loadBitmap("file:///"+uri.getPath()));

		mimgMain.getmCropOverlayView().setFixedAspectRatio(true);
		
		if (uri != null) {
			try {
//				Bitmap myImg = BitmapMedia.getResizedBitmap(uri, width, height,
//						uri.toString());
				mimgMain.setImageBitmap(uri);
				
			} catch (OutOfMemoryError e) {
				
			}
		}
		
		mimgMain.getmCropOverlayView().setVisibility(View.GONE);

		mimgBack.setOnClickListener(this);
		mimgOption.setOnClickListener(this);
		mtxtWriteTextOnTap.setOnClickListener(this);
		mbtnSetDefaultText.setOnClickListener(this);
		/**
		 * check keyboard is visible or hidden
		 */
		mainView.setOnSoftKeyboardVisibilityChangeListener(new SoftKeyboardVisibilityChangeListener() {
			public void onSoftKeyboardShow() {
				mbtnSetDefaultText.setVisibility(View.INVISIBLE);
				// Toast.makeText(CropOnImageActivity.this, "present",
				// Toast.LENGTH_SHORT).show();
			}

			public void onSoftKeyboardHide() {
				// Toast.makeText(CropOnImageActivity.this, "absent",
				// Toast.LENGTH_SHORT).show();
				mtxtWriteTextOnTap.setText(medtWriteTextOnImage.getText()
						.toString());
				mbtnSetDefaultText.setVisibility(View.VISIBLE);
				medtWriteTextOnImage.setVisibility(View.GONE);
				mtxtWriteTextOnTap.setVisibility(View.VISIBLE);
				mtxtCounter.setVisibility(View.GONE);
			}
		});

		// Listner for Edittext

		medtWriteTextOnImage.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				mbtnSetDefaultText.setText(getResources().getString(
						R.string.go_appcollage));

				if (mimgMain.getmCropOverlayView().getVisibility() == View.GONE)
					mimgMain.getmCropOverlayView().setVisibility(View.VISIBLE);
			}
		});

	}
	public void intializeProgressBar(Context context) {
		// TODO Auto-generated method stub
		progressbar = new DialogFactory(context).getTransParentDialog();
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_mycamera_back:
			finish();
			break;

		case R.id.img_mycamera_option:
			// ToastCustom.underDevelopment(CropOnImageActivity.this);
			Intent intent = new Intent();
			intent.putExtra("screen", "home");
			setResult(AppCollageConstants.COLLAGE_REQUEST, intent);

			finish();
			break;
		case R.id.txt_write_text_on_tap:
			mtxtWriteTextOnTap.setVisibility(View.GONE);
			medtWriteTextOnImage.setVisibility(View.VISIBLE);
			mtxtCounter.setVisibility(View.VISIBLE);
			tapListner = true;
			medtWriteTextOnImage.addTextChangedListener(new TextWatcher() {
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					mtxtCounter.setText(String.valueOf(55 - s.length()));
				}

				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});

			break;

		case R.id.btn_write_text_default:
			if (mbtnSetDefaultText.getText().toString()
					.equals(getResources().getString(R.string.go_appcollage))) {
				// if (mtxtWriteTextOnTap.getVisibility() == View.VISIBLE) {
				// Its visible
				// ToastCustom.makeText(this, "text", Toast.LENGTH_SHORT);
				// TextOnImage.drawTextToBitmap(this,
				// mimgMain.getCroppedImage(), 400, 400,
				// mtxtWriteTextOnTap.getText().toString());
				/*
				 * } else { // Either gone or invisible
				 * //ToastCustom.makeText(this, "edit", Toast.LENGTH_SHORT);
				 * 
				 * croppedImage=TextOnImage.drawTextToBitmap(this,
				 * mimgMain.getCroppedImage(), 400, 400,
				 * medtWriteTextOnImage.getText().toString()); }
				 */
				croppedImage = TextOnImage.drawTextToBitmap(this,
						mimgMain.getCroppedImage(), 768, 1024,
						mtxtWriteTextOnTap.getText().toString());
				BitmapMedia.saveImagetoSdcard(croppedImage,
						CropOnImageActivity.this);
				String croppedImagePath = GetPath.getPath(
						CropOnImageActivity.this, getImageUri(croppedImage));
				if (requestType == AppCollageConstants.AppCollage_REQUEST_NOTIFICATION) {
					uploadImageAtRequest(userid, requestId, croppedImagePath);
				} else {
					uploadImageAtInitiate(userid, groupId, message,
							croppedImagePath);
				}
			} else {
				if (!tapListner) {
					// mtxtWriteTextOnTap.setText("Hello Text!");
					if (requestType == AppCollageConstants.AppCollage_REQUEST_NOTIFICATION) {
						String response = mPref.getString(
								AppCollageConstants.KEY_RESPONSE, null);
						if (response.equals("")) {
							ToastCustom.makeText(this, getResources()
									.getString(R.string.defaulttext),
									Toast.LENGTH_SHORT);
							mtxtWriteTextOnTap.setClickable(true);
						} else {
							mtxtWriteTextOnTap.setText(response);
							mbtnSetDefaultText.setText(getResources()
									.getString(R.string.go_appcollage));
							if (mimgMain.getmCropOverlayView().getVisibility() == View.GONE)
								mimgMain.getmCropOverlayView().setVisibility(
										View.VISIBLE);
							mtxtWriteTextOnTap.setClickable(true);
						}
					} else {
						String intiate = mPref.getString(
								AppCollageConstants.KEY_INITIATE, null);
						if (intiate.equals("")) {
							ToastCustom.makeText(this, getResources()
									.getString(R.string.defaulttext),
									Toast.LENGTH_SHORT);
							mtxtWriteTextOnTap.setClickable(true);
						} else {
							mtxtWriteTextOnTap.setText(intiate);
							mbtnSetDefaultText.setText(getResources()
									.getString(R.string.go_appcollage));
							if (mimgMain.getmCropOverlayView().getVisibility() == View.GONE)
								mimgMain.getmCropOverlayView().setVisibility(
										View.VISIBLE);
							mtxtWriteTextOnTap.setClickable(true);
						}
					}
					mtxtWriteTextOnTap.setClickable(true);
					/*
					 * mbtnSetDefaultText.setText(getResources().getString(R.string
					 * .go_appcollage));
					 * if(mimgMain.getmCropOverlayView().getVisibility
					 * ()==View.GONE)
					 * mimgMain.getmCropOverlayView().setVisibility
					 * (View.VISIBLE);
					 */
				} else {
					mtxtWriteTextOnTap.setVisibility(View.VISIBLE);
					medtWriteTextOnImage.setVisibility(View.GONE);
					/*
					 * if(requestType ==
					 * AppCollageConstants.AppCollage_REQUEST_NOTIFICATION) {
					 * mtxtWriteTextOnTap
					 * .setText(mPref.getString(AppCollageConstants.KEY_RESPONSE,
					 * null)); } else{
					 * mtxtWriteTextOnTap.setText(mPref.getString
					 * (AppCollageConstants.KEY_INITIATE, null)); }
					 */
					// mtxtWriteTextOnTap.setText("Hello Text!");
					if (requestType == AppCollageConstants.AppCollage_REQUEST_NOTIFICATION) {
						String response = mPref.getString(
								AppCollageConstants.KEY_RESPONSE, null);
						if (response.equals("")) {
							ToastCustom.makeText(this, getResources()
									.getString(R.string.defaulttext),
									Toast.LENGTH_SHORT);
							mtxtWriteTextOnTap.setClickable(true);
						} else {
							mtxtWriteTextOnTap.setText(response);
							mbtnSetDefaultText.setText(getResources()
									.getString(R.string.go_appcollage));
							if (mimgMain.getmCropOverlayView().getVisibility() == View.GONE)
								mimgMain.getmCropOverlayView().setVisibility(
										View.VISIBLE);
							mtxtWriteTextOnTap.setClickable(true);
						}
					} else {
						String intiate = mPref.getString(
								AppCollageConstants.KEY_INITIATE, null);
						if (intiate.equals("")) {
							mtxtWriteTextOnTap.setClickable(true);
							ToastCustom.makeText(this, getResources()
									.getString(R.string.defaulttext),
									Toast.LENGTH_SHORT);
						} else {
							mtxtWriteTextOnTap.setText(intiate);
							mbtnSetDefaultText.setText(getResources()
									.getString(R.string.go_appcollage));
							if (mimgMain.getmCropOverlayView().getVisibility() == View.GONE)
								mimgMain.getmCropOverlayView().setVisibility(
										View.VISIBLE);
							mtxtWriteTextOnTap.setClickable(true);
						}
					}
					mtxtWriteTextOnTap.setClickable(true);
					/*
					 * mbtnSetDefaultText.setText(getResources().getString(R.string
					 * .go_appcollage));
					 * if(mimgMain.getmCropOverlayView().getVisibility
					 * ()==View.GONE)
					 * mimgMain.getmCropOverlayView().setVisibility
					 * (View.VISIBLE);
					 */
				}
			}

			break;
		}
	}

	public void onProgress(int actiontype, double progress,
			NetworkEventsEnum networkeventsEnum, Object request) {
		// TODO Auto-generated method stub
		AppCollageLogger.d(
				TAG,
				"Web service on Progress"
						+ ActionType.getDescriptionFromActionType(actiontype));
	}

	public void onUpdate(int actiontype, NetworkEventsEnum networkevent,
			Object request, Object response, Exception excption) {
		super.onUpdate(actiontype, networkevent, request, response, excption);
		switch (networkevent) {
		case SUCCESS:
			if (actiontype == ActionType.UPLOADIMAGE.getActionType()) {
				imageForCollageResponse = (ImageForCollageResponse) response;
				if (imageForCollageResponse.isResp()) {
					ToastCustom.makeText(CropOnImageActivity.this,
							imageForCollageResponse.getDesc(),
							Toast.LENGTH_SHORT);
					if (requestType == AppCollageConstants.AppCollage_REQUEST_NOTIFICATION) {
						/*
						 * intentAction.putExtra("startNotification",
						 * requestType); intentAction.putExtra("endTime",
						 * endTime);
						 */
						NotificationManager notificationManager = (NotificationManager) 
								getSystemService(Context.NOTIFICATION_SERVICE);
						notificationManager.cancelAll();
						NotificationCounterUtils.removeAppCollageById(
								CropOnImageActivity.this, requestId);

						setResult(AppCollageConstants.AppCollage_REQUEST_FROM_INBOX);
						finish();
					} else {
						Intent intentAction = new Intent(
								CropOnImageActivity.this,
								SendImageForCollage.class);
						startActivityForResult(intentAction,
								AppCollageConstants.COLLAGE_REQUEST);
					}
					if(uri!=null)
					{
						uri.recycle();
					}
				} else {
					ToastCustom.makeText(CropOnImageActivity.this,
							imageForCollageResponse.getDesc(),
							Toast.LENGTH_SHORT);
				}
				break;
			}
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate" + response + ","
				+ ActionType.getDescriptionFromActionType(actiontype));
		
	}

	public void onpreExecute(int actionType,
			NetworkEventsEnum networkEventsEnum, Object request) {
		// TODO Auto-generated method stub
		super.onpreExecute(actionType, networkEventsEnum, request);

	}

	/**
	 * convert bitmap to uri
	 * 
	 * @param inImage
	 * @return
	 */
	public Uri getImageUri(Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(getContentResolver(), inImage,
				"Title", null);
		return Uri.parse(path);
	}

	/**
	 * upload image
	 * 
	 * @param userId
	 * @param groupId
	 * @param Message
	 */
	private void uploadImageAtInitiate(String userId, String groupId,
			String Message, String croppedImagePath) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String currentTime = sdf.format(new Date());

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", userId);
		map.add("group_id", groupId);
		map.add("message", Message);
		map.add("device_time", currentTime);
		map.add("Connection","close");
		
		Log.i("path", Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		Log.i("path", Environment.getExternalStorageDirectory().toString());

		map.add("user_img", new FileSystemResource(croppedImagePath));

		service.callpostService(AppCollageConstants.upload_url_initiate, map,
				ImageForCollageResponse.class,
				ActionType.UPLOADIMAGE.getActionType());
	}

	/**
	 * call after notification
	 * 
	 * @param userId
	 * @param requestId
	 * @param croppedImagePath
	 */
	private void uploadImageAtRequest(String userId, String requestId,
			String croppedImagePath) {

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", userId);
		map.add("request_id", requestId);
		map.add("Connection","close");
		Log.i("path", Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		Log.i("path", Environment.getExternalStorageDirectory().toString());

		map.add("user_img", new FileSystemResource(croppedImagePath));

		service.callpostService(AppCollageConstants.upload_url_request, map,
				ImageForCollageResponse.class,
				ActionType.UPLOADIMAGE.getActionType());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AppCollageConstants.COLLAGE_REQUEST
				&& resultCode == AppCollageConstants.COLLAGE_REQUEST) {
			setResult(AppCollageConstants.COLLAGE_REQUEST, data);
			finish();
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
