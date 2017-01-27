package com.appunison.appcollage.views.activities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import roboguice.inject.InjectView;
import twitter4j.User;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMRegistrar;
import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.IdType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.listener.ITwitter;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.CMSRequest;
import com.appunison.appcollage.model.pojo.request.NewUser;
import com.appunison.appcollage.model.pojo.response.CMSResponse;
import com.appunison.appcollage.model.pojo.response.SignupResponse;
import com.appunison.appcollage.utils.CountryUtils;
import com.appunison.appcollage.utils.LoginTwitter;
import com.appunison.appcollage.utils.TimeUtils;
import com.appunison.appcollage.views.adapter.CountryCodeAdapter;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.basewidgets.Validation;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;
import com.appunison.util.NetworkUtil;

public class SignUpActivity extends AppCollageNetworkActivity implements ITwitter
{
	private String TAG=SignUpActivity.class.getName();
	private @InjectView (R.id.privacy_policy) TextView mprivacyPolicy;
	private @InjectView (R.id.terms_of_service) TextView mtermOfService;
	private @InjectView (R.id.img_option) ImageView moption;
	private @InjectView(R.id.edt_signUp_first_name) EditText medtFirstName;
	private @InjectView(R.id.edt_signUp_last_name) EditText medtLastName;
	private @InjectView(R.id.edt_signUp_user_name) EditText medtUserName;
	private @InjectView(R.id.edt_signUp_email_address) EditText medtEmail;
	//private @InjectView(R.id.edt_signUp_phone) EditText medtPhone;
	private @InjectView(R.id.edt_signUp_password) EditText medtPassword;
	private @InjectView(R.id.btn_sign_up_with_facebook) Button mbtnSignUpFacebook;
	private @InjectView(R.id.btn_sign_up_with_twitter) Button mbtmSignUpTwitter;
	private @InjectView(R.id.btn_signup) Button mbtnSignUp;
	private @InjectView(R.id.btn_sgin_up_cancel) Button mbtnCancel;
	private @InjectView(R.id.edit_country_code_phone_cell) EditText medit_country_code_phone_cell;
	private @InjectView(R.id.txt_country_code_cell) TextView mtxt_country_code_cell;
	private @InjectView(R.id.spin_country_code_cell) Spinner mspin_country_code_cell;
	
	
	private SessionManager sessionManager;
	private ForeGroundTask service;
	private String mUserName="", mPassword="";

	// Facebook Variables
	private Session.StatusCallback statusCallback =	new SessionStatusCallback();
	public ForeGroundTask getService() {
		return service;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sign_up);

		moption.setVisibility(View.INVISIBLE);
		mbtmSignUpTwitter.setOnClickListener(this);
		mbtnSignUpFacebook.setOnClickListener(this);
		mbtnSignUp.setOnClickListener(this);
		mbtnCancel.setOnClickListener(this);
		mprivacyPolicy.setOnClickListener(this);
		mtermOfService.setOnClickListener(this);
		service=new ForeGroundTask(this, this);

		sessionManager = new SessionManager(AppCollageConstants.PREF_NAME, SignUpActivity.this);

		mtxtTitle.setText(getResources().getString(R.string.signup));
		//get hash key
		showHashKey();
		// To Set Country Flag
		SpinnerAdapter spinnerAdapter = (SpinnerAdapter) new CountryCodeAdapter(this, R.layout.layout_countrycode);
    	mspin_country_code_cell.setAdapter(spinnerAdapter);
    	CountryUtils.setDefaultCountry(this, mspin_country_code_cell);
    	mspin_country_code_cell.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				mtxt_country_code_cell.setText(CountryUtils.countryIsdCode[arg2]);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_sign_up_with_facebook:
			if(NetworkUtil.isNetworkAvailable(SignUpActivity.this))
			{
				userVerificationThroughFacebook();
			}else{
				ToastCustom.makeText(SignUpActivity.this, "Network not available", Toast.LENGTH_SHORT);
			}
			
			break;
		case R.id.privacy_policy:
			service.callpostService(AppCollageConstants.url, populateCmsPP(), CMSResponse.class, ActionType.PP.getActionType());
			/*Intent intentPP = new Intent(SignUpActivity.this, HelpActivity.class);
			intentPP.putExtra("pp", true);
			startActivity(intentPP);*/
			break;

		case R.id.terms_of_service:
			service.callpostService(AppCollageConstants.url, populateCmsTc(), CMSResponse.class, ActionType.TC.getActionType());
			/*Intent intentTC = new Intent(SignUpActivity.this, HelpActivity.class);
			intentTC.putExtra("tc", true);
			startActivity(intentTC);*/
			break;

		case R.id.btn_sign_up_with_twitter:

			new LoginTwitter(this,this);
			break;
		case R.id.btn_signup:
			if(checkValidation())
			{
				service.callpostService(AppCollageConstants.url, populateNewUser(), SignupResponse.class, ActionType.SIGNUP.getActionType());
			}
			break;
		case R.id.btn_sgin_up_cancel:
			finish();
			break;
		}

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
		if(!Validation.isUserName(medtUserName,1,50, true))
		{
			valid = false;
		}
		else if(!Validation.limit(medtUserName, 5, 50, true))
		{
			valid = false;
		}
		if(!Validation.isEmailAddress(medtEmail, true))
			valid = false;
		//if(medit_country_code_phone_cell.getText().length()>0)
		//{
			if(!Validation.isPhoneNumber(medit_country_code_phone_cell, true))
				valid = false;
		//}
		if(!Validation.limit(medtPassword, 6, 30, true))
			valid = false;

		return valid;
	}

	private NewUser populateNewUser()
	{
		NewUser user=new NewUser();
		user.setAction(WebServiceAction.SIGNUPACTION);
		user.setId_type(IdType.WEB);
		mUserName = medtUserName.getText().toString().trim();
		mPassword = medtPassword.getText().toString().trim();
		user.setUsername(mUserName);
		user.setEmail(medtEmail.getText().toString().trim());
		user.setFirst_name(medtFirstName.getText().toString().trim());
		user.setLast_name(medtLastName.getText().toString().trim());
		user.setPhone_number(medit_country_code_phone_cell.getText().toString().trim());
		user.setPassword(mPassword);
		//user.setTime_zone(TimeZone.getDefault().getDisplayName());
		user.setTime_zone(TimeUtils.getCurrentTimezoneOffset());
		String registrationId = GCMRegistrar.getRegistrationId(SignUpActivity.this);
		user.setDevice_id(registrationId);
		//user.setProfile_photo("121212");
		user.setProfile_photo("");
		user.setDevice_type("android");
		user.setCountry_code(mtxt_country_code_cell.getText().toString());
		return user;
	}

	public void onProgress(int actionType, double progress, NetworkEventsEnum networkEvent,
			Object request) {
		AppCollageLogger.d(TAG, "Web service on Progress"+ActionType.getDescriptionFromActionType(actionType));
	}

	public void onUpdate(int actionType ,NetworkEventsEnum networkEvent, Object request,
			Object response, Exception exception) {
		super.onUpdate(actionType, networkEvent, request, response, exception);

		switch (networkEvent) {
		case SUCCESS:
			if(actionType==ActionType.SIGNUP.getActionType())
			{
				SignupResponse signupResponse=(SignupResponse)response;
				if(signupResponse.isResp())
				{
					
//					if(signupResponse.getResult().getId_type().equals(IdType.WEB)){
						ToastCustom.makeText(SignUpActivity.this, "Your account is Created Successfully. Verify your email to start using AppCollage",
								Toast.LENGTH_LONG);
//						setResult(24);
//						finish();
//					}else{
						sessionManager.storeCredential(mUserName, mPassword, false);
						sessionManager.createLoginSession(signupResponse.getResult().getId());;
						Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
						intent.putExtra("signupResponse", signupResponse);
						intent.setData(getIntent().getData());
						setResult(AppCollageConstants.FINISH_OPENED_ACTIVITY, intent);
						finish();
//					}
				}
				else
				{
					ToastCustom.makeText(SignUpActivity.this, signupResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			else if(actionType==ActionType.PP.getActionType())
			{
				CMSResponse cmsResponse = (CMSResponse) response;
				if(cmsResponse.isResp())
				{
					//ToastCustom.makeText(SignUpActivity.this, cmsResponse.getResult().getContent(), Toast.LENGTH_SHORT);
					Intent intentPP = new Intent(SignUpActivity.this, HelpActivity.class);
					intentPP.putExtra("pp", true);
					intentPP.putExtra("ppdata", cmsResponse.getResult().getContent());
					startActivity(intentPP);
				}
				else {
					ToastCustom.makeText(SignUpActivity.this, cmsResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			else if(actionType==ActionType.TC.getActionType())
			{
				CMSResponse cmsResponse = (CMSResponse) response;
				if(cmsResponse.isResp())
				{
					Intent intentTC = new Intent(SignUpActivity.this, HelpActivity.class);
					intentTC.putExtra("tc", true);
					intentTC.putExtra("tcdata", cmsResponse.getResult().getContent());
					startActivity(intentTC);

				}
				else {
					ToastCustom.makeText(SignUpActivity.this, cmsResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actionType));

	}

	public void onpreExecute(int actionType, NetworkEventsEnum networkEvent, Object request) {
		super.onpreExecute(actionType, networkEvent, request);
		switch (networkEvent) {
		case STARTED:
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));

	}

	private NewUser populateFacebookNewUser(NewUser user)
	{
		user.setAction(WebServiceAction.SIGNUPACTION);
		user.setId_type(IdType.FB);
		user.setTime_zone(TimeZone.getDefault().getDisplayName());
		String registrationId = GCMRegistrar.getRegistrationId(SignUpActivity.this);
		user.setDevice_id(registrationId);
		user.setProfile_photo("121212");
		return user;
	}

	private CMSRequest populateCmsPP()
	{
		CMSRequest cmsRequest = new CMSRequest();
		cmsRequest.setAction(WebServiceAction.CMS_ACTION);
		cmsRequest.setType("PrivacyPolicy");
		return cmsRequest;
	}
	private CMSRequest populateCmsTc()
	{
		CMSRequest cmsRequest = new CMSRequest();
		cmsRequest.setAction(WebServiceAction.CMS_ACTION);
		cmsRequest.setType("TermsOfService");
		return cmsRequest;
	}


	// Facebook Integration
	public void showHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.appunison.appcollage", PackageManager.GET_SIGNATURES); //You package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }
	private void userVerificationThroughFacebook(){

		List<String> permissions = new ArrayList<String>();
		permissions.add("email");

		Session.openActiveSession(SignUpActivity.this, true, permissions, statusCallback);

	}

	private class SessionStatusCallback implements Session.StatusCallback {
		public void call(Session session, SessionState state, Exception exception) {
			// Respond to session state changes, ex: updating the view
			if(state.isOpened())
			{	
				Request.newMeRequest(session, new Request.GraphUserCallback() {

					public void onCompleted(GraphUser user, Response response) {
						if(user!=null)
						{
							String email ="";
							//String fullName = user.getFirstName()+" "+user.getLastName();
							//String dob = user.getBirthday();
							//String gender = user.getProperty("gender").toString();
							String facebookId =user.getId();
							Log.d("facebookId",facebookId);
							if(user.getProperty("email")!=null)
							{
								email = user.getProperty("email").toString();
							}


							NewUser newUser=new NewUser();
							newUser.setEmail("");
							newUser.setFirst_name(user.getFirstName());
							newUser.setLast_name(user.getLastName());
							newUser.setFacebook_id(facebookId);

							service.callpostService(AppCollageConstants.url, populateFacebookNewUser(newUser), SignupResponse.class, ActionType.SIGNUP.getActionType());
						}
					}
				}).executeAsync();	

			}

		}
	}
	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		if(requestCode == Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE) // Facebook
		{
			Session session= Session.getActiveSession();
			if(session!=null)
			{
				Session.getActiveSession().onActivityResult(this, requestCode,
						responseCode, intent);

			}
		}
	}


	public void authorized(User user)
	{
		NewUser newUser=new NewUser();
		newUser.setFirst_name(user.getName());
		newUser.setLast_name("");
		newUser.setTwitter_id(""+user.getId());
		getService().callpostService(AppCollageConstants.url, populateTWNewUser(newUser,""+user.getName()),SignupResponse.class, ActionType.SIGNUP.getActionType());


	}

	/**
	 * populate TwNew User
	 * @param user
	 * @param name
	 * @return
	 */
	private NewUser populateTWNewUser(NewUser user,String name)
	{
		user.setAction(WebServiceAction.SIGNUPACTION);
		user.setId_type(IdType.TW);
		user.setFirst_name(name);
		//user.setTime_zone(TimeZone.getDefault().getDisplayName());
		user.setTime_zone(TimeUtils.getCurrentTimezoneOffset());
		String registrationId = GCMRegistrar.getRegistrationId(this);
		user.setDevice_id(registrationId);
		return user;
	}
	

	
}
