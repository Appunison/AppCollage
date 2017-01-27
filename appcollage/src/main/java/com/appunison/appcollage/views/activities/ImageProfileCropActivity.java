package com.appunison.appcollage.views.activities;

import java.io.ByteArrayOutputStream;

import com.edmodo.cropper.CropImageView;
import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.utils.BitmapMedia;
import com.appunison.appcollage.utils.GetPath;
import com.appunison.view.BaseActivity;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class ImageProfileCropActivity extends BaseActivity implements OnClickListener{

	private @InjectView(R.id.img_profile_crop) CropImageView mimgProfile;
	private @InjectView(R.id.btn_profile_crop_ok) Button mbtnOk;
	private static final int CAMERA_REQUEST = 1888; 
	//private Uri path;
	private Intent intent;
	private int width, height;
	private Bitmap croppedImage;
	private static Bitmap bitmapIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_profile_crop);
		
		intent = getIntent();
		width = intent.getIntExtra("width", 0);
		height = intent.getIntExtra("height", 0);
		
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		//path=MediaUtil.getOutputMediaFileUri(MediaUtil.MEDIA_TYPE_IMAGE);
		//cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,path);
		startActivityForResult(cameraIntent, CAMERA_REQUEST); 
		
		mbtnOk.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_profile_crop_ok:
			croppedImage=mimgProfile.getCroppedImage();
			BitmapMedia.saveImagetoSdcard(croppedImage, ImageProfileCropActivity.this);
			String croppedImagePath =GetPath.getPath(ImageProfileCropActivity.this, getImageUri(croppedImage));
			Intent intent = new Intent();
			intent.putExtra("imagePath", croppedImagePath);
			setResult(AppCollageConstants.IMAGE_CROP, intent);
			
			finish();
			break;

		default:
			break;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if (requestCode == CAMERA_REQUEST ) {  
			if(resultCode ==RESULT_OK)
			{	
				bitmapIntent = (Bitmap) data.getExtras().get("data");
				bitmapIntent = BitmapMedia.getResizedBitmap(bitmapIntent,
						width, height, bitmapIntent.toString());
				mimgProfile.setImageBitmap(bitmapIntent);
			
			}
			else 
			{
				finish();
			}
			
		}
	}
	/**
	 * convert bitmap to uri
	 * @param inImage
	 * @return
	 */
	public Uri getImageUri(Bitmap inImage) {
		  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		  inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		  String path = Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
		  return Uri.parse(path);
		}
	
}
