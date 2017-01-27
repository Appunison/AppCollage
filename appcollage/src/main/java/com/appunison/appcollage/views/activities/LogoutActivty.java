package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.LogoutRequest;
import com.appunison.appcollage.model.pojo.response.LogoutResponse;
import com.appunison.appcollage.model.pojo.response.SignupResponse;
import com.appunison.appcollage.utils.DialogFactory;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;
import com.appunison.view.NetworkBaseActivity;

public class LogoutActivty extends NetworkBaseActivity implements OnClickListener{

	private String TAG = LogoutActivty.class.getName();
	private @InjectView (R.id.btn_logout_cancel) Button mbtnCancel;
	private @InjectView (R.id.btn_logout_ok) Button mbtnOk;
	private ForeGroundTask service;
	private Intent intent;
	private SignupResponse signupResponse;
	private SessionManager sessionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_logout);
		
		intent = getIntent();
		signupResponse = (SignupResponse) intent.getSerializableExtra("signupResponse");
		sessionManager = new SessionManager(AppCollageConstants.PREF_NAME, LogoutActivty.this);
		service = new ForeGroundTask(this, this);
		
		
		mbtnCancel.setOnClickListener(this);
		mbtnOk.setOnClickListener(this);
	
	}
	
	public void intializeProgressBar(Context context) {
		// TODO Auto-generated method stub
		progressbar = new DialogFactory(context).getTransParentDialog();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_logout_cancel:
			finish();
			break;
		case R.id.btn_logout_ok:
			service.callpostService(AppCollageConstants.url, populateLogout(), LogoutResponse.class, ActionType.LOGOUT.getActionType());
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

	public void onUpdate(int actionType, NetworkEventsEnum networkEvent,
			Object request, Object response, Exception exception) {
		super.onUpdate(actionType, networkEvent, request, response, exception);
		switch (networkEvent) {
		case SUCCESS:
			if(actionType==ActionType.LOGOUT.getActionType())
			{
				LogoutResponse logoutResponse=(LogoutResponse)response;
//				if(logoutResponse.isResp())
//				{
					sessionManager.logoutUser();
					ToastCustom.makeText(LogoutActivty.this, "User logout successfully", Toast.LENGTH_SHORT);
					setResult(AppCollageConstants.SETTING_LOGOUT);
					finish();
//				}
//				else {
//					ToastCustom.makeText(LogoutActivty.this, logoutResponse.getDesc(), Toast.LENGTH_SHORT);
//				}
			}
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actionType));
	}

	public void onpreExecute(int actionType,
			NetworkEventsEnum networkEventsEnum, Object request) {
		super.onpreExecute(actionType, networkEventsEnum, request);
		switch (networkEventsEnum) {
		case STARTED:
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));
	}
	/**
	 * logout
	 * @return
	 */
	private LogoutRequest populateLogout()
	{
		//String userid = mPref.getString(SessionManager.KEY_USERID, null);
		LogoutRequest logout = new LogoutRequest();
		logout.setAction(WebServiceAction.LOGOUT_ACTION);
		logout.setId(signupResponse.getResult().getId());
		
		return logout;
	}

}
