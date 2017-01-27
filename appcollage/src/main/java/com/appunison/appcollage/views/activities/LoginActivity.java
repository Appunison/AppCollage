package com.appunison.appcollage.views.activities;

import java.util.Map;
import java.util.TimeZone;

import roboguice.inject.InjectView;
import twitter4j.User;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMRegistrar;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.IdType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.listener.ITwitter;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.AuthUser;
import com.appunison.appcollage.model.pojo.response.SignupResponse;
import com.appunison.appcollage.utils.LoginTwitter;
import com.appunison.appcollage.utils.TimeUtils;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.basewidgets.Validation;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.appcollage.R;
import com.appunison.task.ForeGroundTask;

public class LoginActivity extends AppCollageNetworkActivity implements ITwitter
{

	private @InjectView (R.id.img_option) ImageView moption;
	private String TAG = LoginActivity.class.getName();
	private @InjectView(R.id.edt_login_username_or_email) EditText medtUserName;
	private @InjectView(R.id.edt_login_password) EditText medtPassword;
	private @InjectView(R.id.chkbox_remember_me) CheckBox mchkRememberMe;
	private @InjectView(R.id.txt_login_signup) TextView mtxtSignUp;
	private @InjectView(R.id.txt_login_forgot_passwod) TextView mtxtForgotPassword;
	private @InjectView(R.id.btn_login_with_facebook) Button mbtnLoginFacebook;;
	private @InjectView(R.id.btn_login_with_twitter) Button mbtnLoginTwitter;
	private @InjectView(R.id.btn_login) Button mbtnLogin;
	private @InjectView(R.id.btn_login_cancel) Button mbtnCancel;
	private static final int FORGET=3;
	private SessionManager sessionManager;
	private ForeGroundTask service;
	
	public ForeGroundTask getService() {
		return service;
	}
	// Facebook Variables
	private Session.StatusCallback statusCallback =	new SessionStatusCallback();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);

		mtxtSignUp.setOnClickListener(this);
		mtxtForgotPassword.setOnClickListener(this);
		mbtnLoginFacebook.setOnClickListener(this);
		mbtnLoginTwitter.setOnClickListener(this);
		mbtnLogin.setOnClickListener(this);
		mbtnCancel.setOnClickListener(this);
		moption.setVisibility(View.INVISIBLE);
		mtxtTitle.setText(getResources().getString(R.string.login));

		service=new ForeGroundTask(this, this);
		sessionManager = new SessionManager(AppCollageConstants.PREF_NAME, LoginActivity.this);

		Map<String, Object> map = sessionManager.getUserDetails();
		if((Boolean)map.get(sessionManager.IS_REMEMBER) != null)
		{
			medtUserName.setText((String)map.get(sessionManager.KEY_NAME));
			medtPassword.setText((String)map.get(sessionManager.KEY_PASSWORD));
			mchkRememberMe.setChecked((Boolean)map.get(sessionManager.IS_REMEMBER));
		}

	}

	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.txt_login_signup:

			Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
			startActivityForResult(intent , AppCollageConstants.FINISH_OPENED_ACTIVITY);
			break;
		case R.id.txt_login_forgot_passwod:
			//ToastCustom.underDevelopment(LoginActivity.this);
			Intent intentForgot = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
			startActivityForResult(intentForgot, FORGET);
			break;

		case R.id.btn_login_with_facebook:
			userVerificationThroughFacebook();
			mbtnLoginFacebook.setOnClickListener(null);
			break;

		case R.id.btn_login_with_twitter:
			new LoginTwitter(this,this);
			mbtnLoginTwitter.setOnClickListener(null);
			break;

		case R.id.btn_login:
			if(checkValidation())
			{
				service.callpostService(AppCollageConstants.url, populateAuthUser(), SignupResponse.class, ActionType.LOGIN.getActionType());
				
				sessionManager.storeCredential(medtUserName.getText().toString(), medtPassword.getText().toString(), mchkRememberMe.isChecked());
			}
			break;
		case R.id.btn_login_cancel:
			finish();
			break;
		}
	}
	private boolean checkValidation()
	{
		boolean valid = true;

		if(!Validation.hasText(medtUserName))
			valid = false;
		if(!Validation.hasText(medtPassword))
			valid = false;
		return valid;
	}

	public void onProgress(int actionType, double progress, NetworkEventsEnum networkEvent,
			Object request) {

		AppCollageLogger.d(TAG, "Web service on Progress"+ActionType.getDescriptionFromActionType(actionType));
	}

	public void onUpdate(int actionType ,NetworkEventsEnum networkEvent, Object request,
			Object response, Exception exception) {
		super.onUpdate(actionType, networkEvent, request, response, exception);
		switch (networkEvent) {
		case EXCEPTION:

			break;
		case TIME_OUT:

			break;
		case SUCCESS:
			if(actionType==ActionType.LOGIN.getActionType())
			{
				SignupResponse signupResponse=(SignupResponse)response;
				if(signupResponse.isResp())
				{
					sessionManager.createLoginSession(signupResponse.getResult().getId());

					CustomSharedPreference mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, LoginActivity.this);
					//mPref.putString(AppCollageConstants.KEY_PASSWORD, medtPassword.getText().toString());
					mPref.putString(AppCollageConstants.KEY_INVITE, signupResponse.getResult().getInvite_text().toString());
					mPref.putString(AppCollageConstants.KEY_INITIATE, signupResponse.getResult().getInitiate_text().toString());
					mPref.putString(AppCollageConstants.KEY_RESPONSE, signupResponse.getResult().getResponse_text().toString());
					
					Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
					intent.putExtra("signupResponse", signupResponse);
					intent.setData(getIntent().getData());
					setResult(AppCollageConstants.FINISH_OPENED_ACTIVITY, intent);
					finish();
					
					
				}
				else
				{
					ToastCustom.makeText(LoginActivity.this, signupResponse.getDesc(), Toast.LENGTH_SHORT);
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
		case NETWORK_NOT_AVAILABLE:
			ToastCustom.makeText(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT);
			break;
		case STARTED:
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));

	}
	private AuthUser populateAuthUser()
	{
		AuthUser user=new AuthUser();
		user.setAction(WebServiceAction.LOGINACTION);
		user.setUser_id(medtUserName.getText().toString());
		user.setPassword(medtPassword.getText().toString());
		user.setId_type(IdType.WEB);
		//user.setTime_zone(TimeZone.getDefault().getDisplayName());
		user.setTime_zone(TimeUtils.getCurrentTimezoneOffset());
		String registrationId = GCMRegistrar.getRegistrationId(LoginActivity.this);
		user.setDevice_id(registrationId);
		user.setDevice_type("android");
		return user;
	}
	private AuthUser populateFBAuthUser(AuthUser user)
	{
		user.setAction(WebServiceAction.LOGINACTION);
		user.setId_type(IdType.FB);
		user.setTime_zone(TimeZone.getDefault().getDisplayName());
		String registrationId = GCMRegistrar.getRegistrationId(LoginActivity.this);
		user.setDevice_id(registrationId);
		return user;
	}

	
	
	// Facebook Integration
	private void userVerificationThroughFacebook(){

		Session.openActiveSession(LoginActivity.this, true, statusCallback);
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
						//String fullName = user.getFirstName()+" "+user.getLastName();
						//String dob = user.getBirthday();
						//String gender = user.getProperty("gender").toString();
						String facebookId =user.getId();

						AuthUser authUser = new AuthUser();		
						authUser.setUser_id(facebookId);

						service.callpostService(AppCollageConstants.url, populateFBAuthUser(authUser), SignupResponse.class, ActionType.LOGIN.getActionType());
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
		else if(requestCode == AppCollageConstants.FINISH_OPENED_ACTIVITY && responseCode == AppCollageConstants.FINISH_OPENED_ACTIVITY)
		{
			setResult(AppCollageConstants.FINISH_OPENED_ACTIVITY, intent);
			finish();
		}
	}
	
	
	public void authorized(User user) 
	{
		AuthUser authUser = new AuthUser();		
		authUser.setUser_id(""+user.getId());
		getService().callpostService(AppCollageConstants.url, populateTWAuthUser(authUser),SignupResponse.class, ActionType.LOGIN.getActionType());

	}
	/**
	 * 
	 * @param user
	 * @return
	 */
	private AuthUser populateTWAuthUser(AuthUser user)
	{
		user.setAction(WebServiceAction.LOGINACTION);
		user.setId_type(IdType.TW);
		user.setTime_zone(TimeZone.getDefault().getDisplayName());
		String registrationId = GCMRegistrar.getRegistrationId(this);
		user.setDevice_id(registrationId);
		return user;
	}
	
	
	
}
