package com.appunison.appcollage;


import com.google.android.gcm.GCMRegistrar;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.UserProfileRequest;
import com.appunison.appcollage.model.pojo.response.SignupResponse;
import com.appunison.appcollage.utils.DialogFactory;
import com.appunison.appcollage.views.activities.HomeActivity;
import com.appunison.appcollage.views.activities.LoginActivity;
import com.appunison.appcollage.views.activities.TutorialActivity;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.appcollage.R;
import com.appunison.task.ForeGroundTask;
import com.appunison.view.NetworkBaseActivity;

import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class StartScreenActivity extends NetworkBaseActivity implements OnClickListener{

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
	private String TAG=StartScreenActivity.class.getName();
	private @InjectView(R.id.btn_start_screen_login) Button mbtnLogin;
	private @InjectView(R.id.btn_start_screen_sign_up) Button mbtnSignUp;
	private SessionManager sessionManager;
	private ForeGroundTask service;
	
	public void intializeProgressBar(Context context) {
		// TODO Auto-generated method stub
		progressbar = new DialogFactory(context).getTransParentDialog();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_start_screen);
        
        mbtnLogin.setOnClickListener(this);
        mbtnSignUp.setOnClickListener(this);
        service=new ForeGroundTask(this, this);
        
        //sessionManager = new SessionManager(AppCollageConstants.PREF_NAME, StartScreenActivity.this);
        
		if(!GCMRegistrar.isRegistered(StartScreenActivity.this));
		{
			GCMRegistrar.register(StartScreenActivity.this, GCMIntentService.SENDER_ID);
		}

        checkLoggedIn();
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_start_screen_login:
			//ToastCustom.underDevelopment(StartScreenActivity.this);
			Intent intentLogin = new Intent(StartScreenActivity.this, LoginActivity.class);
			intentLogin.setData(getIntent().getData());
			startActivityForResult(intentLogin,AppCollageConstants.FINISH_OPENED_ACTIVITY);
			break;

		case R.id.btn_start_screen_sign_up:
			Intent intentSignUp = new Intent(StartScreenActivity.this, TutorialActivity.class);
			intentSignUp.setData(getIntent().getData());
			startActivityForResult(intentSignUp,AppCollageConstants.FINISH_OPENED_ACTIVITY);
			break;
		}
		
	}
	
	/**
	 * check session of 
	 * loggedIn user
	 */
	private void checkLoggedIn()
	{
		sessionManager = new SessionManager(AppCollageConstants.PREF_NAME, StartScreenActivity.this);
		sessionManager.getPreference().putBoolean(AppCollageConstants.SHOWING_DIALOG, false);
		 if(sessionManager.isLoggedIn())
		    {
			  service.callpostService(AppCollageConstants.url, populateUserProfile(), SignupResponse.class, ActionType.USERPROFILE.getActionType());
		    }
	}

	
	private UserProfileRequest populateUserProfile()
	{
		/*CustomSharedPreference mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, StartScreenActivity.this);
		mPref.getString(AppCollageConstants.KEY_PASSWORD, null);*/
		String userId = sessionManager.getPreference().getString(SessionManager.KEY_USERID, null);
		UserProfileRequest userProfile =new UserProfileRequest();
		userProfile.setAction(WebServiceAction.USERPROFILEACTION);
		userProfile.setId(userId);
		String registrationId = GCMRegistrar.getRegistrationId(StartScreenActivity.this);
		userProfile.setDevice_id(registrationId);
		return userProfile;
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
						if(actionType==ActionType.USERPROFILE.getActionType())
						{
							SignupResponse signupResponse=(SignupResponse)response;
							if(signupResponse.isResp())
							{
								sessionManager.getPreference().putString(SessionManager.KEY_USERID, signupResponse.getResult().getId());
								
								CustomSharedPreference mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, StartScreenActivity.this);
								mPref.putString(AppCollageConstants.KEY_INVITE, signupResponse.getResult().getInvite_text().toString());
								mPref.putString(AppCollageConstants.KEY_INITIATE, signupResponse.getResult().getInitiate_text().toString());
								mPref.putString(AppCollageConstants.KEY_RESPONSE, signupResponse.getResult().getResponse_text().toString());
								Intent intent = new Intent(StartScreenActivity.this, HomeActivity.class);
								intent.putExtra("signupResponse", signupResponse);
								intent.setData(getIntent().getData());
								startActivity(intent);
								finish();
								
							}
							else
							{
								ToastCustom.makeText(StartScreenActivity.this, signupResponse.getDesc(), Toast.LENGTH_SHORT);
							}
						}
						break;
					default:
						break;
			}
			dismissProgressBar();
			AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actionType));
	
	}

	public void onpreExecute(int actionType, NetworkEventsEnum networkEvent, Object request) {
		switch (networkEvent) {
		case NETWORK_NOT_AVAILABLE:
			ToastCustom.makeText(StartScreenActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT);
			break;
		case STARTED:
			showProgressBar();
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppCollageConstants.FINISH_OPENED_ACTIVITY && resultCode == AppCollageConstants.FINISH_OPENED_ACTIVITY)
		{
			startActivity(data);
			finish();
		}
	}
}

