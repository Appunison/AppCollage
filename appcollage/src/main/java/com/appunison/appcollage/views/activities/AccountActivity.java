package com.appunison.appcollage.views.activities;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appunison.basewidgets.ToastCustom;
import com.appunison.basewidgets.Validation;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.task.ForeGroundTask;
import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.imageGetter.ImageLoader;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.response.SignupResponse;

public class AccountActivity extends AppCollageNetworkActivity{

	private @InjectView (R.id.layout_back) LinearLayout mlayoutBack;
	private @InjectView (R.id.layout_option) LinearLayout mlayoutOption;
	private @InjectView (R.id.layout_account) LinearLayout mlayoutAccount;
	private @InjectView (R.id.img_back) ImageView mimgBack;
	private @InjectView (R.id.img_account_profile) ImageView mimgProfile;
	private @InjectView(R.id.btn_account_cancel) Button mbtnCancel;
	private @InjectView(R.id.btn_account_save) Button mbtnSave;
	private @InjectView(R.id.txt_account_change_password) TextView mtxtChangePassword;
	private @InjectView(R.id.edt_account_first_name) EditText medtFirstName;
	private @InjectView(R.id.edt_account_last_name) EditText medtLastName;
	private @InjectView(R.id.edt_account_email_address) EditText medtEmailAdress;
	private @InjectView(R.id.edt_account_phone_number) EditText medtPhoneNumber;
	private static final int CAMERA_REQUEST = 1888; 
	private Uri path;
	private SignupResponse signupResponse;
	private String imagePath;
	private CustomSharedPreference mPref;
	private ForeGroundTask service;
	private String TAG;
	private ImageLoader imageLoader;
	private String oldEmail;
	private SignupResponse profileResponse;
	private boolean flag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_account);
		
		TAG = AccountActivity.class.getName();
		signupResponse = (SignupResponse) getIntent().getSerializableExtra("signupResponse");
		mtxtTitle.setText(getResources().getString(R.string.my_account));
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, AccountActivity.this);
		service=new ForeGroundTask(this, this);
		imageLoader = new ImageLoader(this);
		mimgBack.setImageResource(R.drawable.ic_action_back);
		
		if(signupResponse.getResult().getProfile_photo()!=null)
		{
			//setImage
			
	        imageLoader.DisplayImage(signupResponse.getResult().getProfile_photo(), mimgProfile);
	        
		}
		if(StringUtils.hasLength(signupResponse.getResult().getFacebook_id()) || 
				StringUtils.hasLength(signupResponse.getResult().getTwitter_id()))
		{
			mtxtChangePassword.setVisibility(View.GONE);
		}else{
			mtxtChangePassword.setVisibility(View.VISIBLE);
		}
		medtFirstName.setText(signupResponse.getResult().getFirst_name());
		medtLastName.setText(signupResponse.getResult().getLast_name());
		medtEmailAdress.setText(signupResponse.getResult().getEmail());
		medtPhoneNumber.setText(signupResponse.getResult().getPhone_number());
		mimgProfile.setOnClickListener(this);
		mbtnCancel.setOnClickListener(this);
		mbtnSave.setOnClickListener(this);
		mtxtChangePassword.setOnClickListener(this);
	}
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//super.onClick(v);
		switch (v.getId()) {
		case R.id.layout_back:
			if(flag)
			{
				Intent intent2 = new Intent();
				intent2.putExtra("profileResponse", profileResponse);
				setResult(97, intent2);
			}
			finish();
			break;
		case R.id.layout_option:
			//ToastCustom.makeText(AccountActivity.this, "account", Toast.LENGTH_SHORT);
			Intent intent1 = new Intent();
			if(flag)
			{
				intent1.putExtra("profileResponse", profileResponse);
				intent1.putExtra("screen", "account1");
			}else{
				intent1.putExtra("screen", "account");
			}
			setResult(11, intent1);
			finish();
			break;
		case R.id.btn_account_cancel:
			finish();
			break;
		case R.id.txt_account_change_password:
			//ToastCustom.underDevelopment(AccountActivity.this);
			Intent intent = new Intent(this, ChangePasswordActivity.class);
			startActivityForResult(intent, AppCollageConstants.INTENT_HOME);
			break;
		case R.id.img_account_profile:
			
			Intent intentCrop = new Intent(AccountActivity.this, ImageProfileCropActivity.class);
			intentCrop.putExtra("width", mlayoutAccount.getWidth());
			intentCrop.putExtra("height", mlayoutAccount.getHeight());
			startActivityForResult(intentCrop, AppCollageConstants.IMAGE_CROP);
				
			
			break;
		case R.id.btn_account_save:
			//ToastCustom.underDevelopment(AccountActivity.this);
			if(checkValidation())
			{
				oldEmail = signupResponse.getResult().getEmail();
				//String userid = mPref.getString(SessionManager.KEY_USERID, null);
				uploadImageAtInitiate(signupResponse.getResult().getId(), 
						medtFirstName.getText().toString(),
						medtLastName.getText().toString(),
						medtPhoneNumber.getText().toString(),
						medtEmailAdress.getText().toString(),
						imagePath);
			}
			break;
		
		default:
			break;
		}
	}
	
	public void onProgress(int actiontype, double progress,
			NetworkEventsEnum networkeventsEnum, Object request) {
		// TODO Auto-generated method stub
		AppCollageLogger.d(TAG, "Web service on Progress"+ActionType.getDescriptionFromActionType(actiontype));
	}	

	public void onUpdate(int actiontype, NetworkEventsEnum networkevent,
			Object request, Object response, Exception excption) {
		super.onUpdate(actiontype, networkevent, request, response, excption);
		switch (networkevent) {
		case SUCCESS:
			if(actiontype==ActionType.UPLOADIMAGE.getActionType())
			{
				profileResponse=(SignupResponse)response;
				if(profileResponse.isResp())
				{
					if(oldEmail.equals(medtEmailAdress.getText().toString()))
					{
						ToastCustom.makeText(AccountActivity.this, profileResponse.getDesc(), Toast.LENGTH_SHORT);
					}else if(profileResponse.getDesc().equals("email already exists.")){
						ToastCustom.makeText(AccountActivity.this, profileResponse.getDesc(), Toast.LENGTH_SHORT);
					}else{
						ToastCustom.makeText(AccountActivity.this, "For Email change updation, Please verify your email", 
								Toast.LENGTH_LONG);
					}
					
					imageLoader.DisplayImage(profileResponse.getResult().getProfile_photo(), mimgProfile);
					medtFirstName.setText(profileResponse.getResult().getFirst_name());
					medtLastName.setText(profileResponse.getResult().getLast_name());
					//medtEmailAdress.setText(profileResponse.getResult().getEmail());
					medtEmailAdress.setText(oldEmail);
					medtPhoneNumber.setText(profileResponse.getResult().getPhone_number());
					Intent intent = new Intent();
					
					profileResponse.getResult().setResponse_text(signupResponse.getResult().getResponse_text());
					profileResponse.getResult().setInitiate_text(signupResponse.getResult().getInitiate_text());
					profileResponse.getResult().setInvite_text(signupResponse.getResult().getInvite_text());
					flag = true;
					
					intent.putExtra("profileResponse", profileResponse);
					setResult(97, intent);
				}
				else
				{
					ToastCustom.makeText(AccountActivity.this, profileResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			break;
			}
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actiontype));
	}

	public void onpreExecute(int actionType,
			NetworkEventsEnum networkEventsEnum, Object request) {
		super.onpreExecute(actionType, networkEventsEnum, request);
	}
	
	private boolean checkValidation()
	{
		boolean valid = true;
		if(!Validation.hasText(medtFirstName))
			valid = false;	
		if(!Validation.hasText(medtLastName))
			valid = false;	
		/*if(!Validation.isAlphabet(medtFirstName, true))
			valid = false;
		if(!Validation.isAlphabet(medtLastName, true))
			valid = false;*/
		if(signupResponse.getResult().getFacebook_id().equals(""))
		{
		if(!Validation.isEmailAddress(medtEmailAdress, true))
			valid = false;
		}
		return valid;
	}

	/**
	 * upload image in edit profile
	 * @param userId
	 * @param groupId
	 * @param Message
	 */
	private void uploadImageAtInitiate(String userId, String fName, String lName, String phoneNumber, String email, String croppedImagePath)
	{
		
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", userId);
		map.add("first_name", fName);
		map.add("last_name", lName);
		map.add("phone_number", phoneNumber);
		map.add("email", email);
		map.add("Connection", "Close");
		
		Log.i("path", Environment.getExternalStorageDirectory().getAbsolutePath());
		Log.i("path", Environment.getExternalStorageDirectory().toString());
		
        if(croppedImagePath!=null)
        {
        	map.add("user_img", new FileSystemResource(croppedImagePath));
        	//map.add("user_img", new FileSystemResource(signupResponse.getResult().getProfile_photo()));
        }
		
		service.callpostService(AppCollageConstants.upload_url_update_profile, map, SignupResponse.class, ActionType.UPLOADIMAGE.getActionType());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppCollageConstants.IMAGE_CROP && resultCode == AppCollageConstants.IMAGE_CROP)
		{
			imagePath = data.getStringExtra("imagePath");
			Log.d("imagePath",imagePath);
			Uri myUri = Uri.parse(imagePath);
			mimgProfile.setImageURI(myUri);
		}
		else if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
	}
	
	/*@Override
	protected void clickOnHome() {
		super.clickOnHome();
		signupResponse.getResult().setProfile_photo(profileResponse.getResult().getProfile_photo());
		
	}*/
}
