package com.appunison.appcollage.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.EmailVerifyRequest;
import com.appunison.appcollage.model.pojo.response.EmailVerifyResponse;
import com.appunison.appcollage.utils.DialogFactory;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;
import com.appunison.view.NetworkBaseActivity;

public class EmailVerificationActivity extends NetworkBaseActivity implements OnClickListener{

	private TextView mAlertText;
	private Button mNotNow;
	private Button mOk;
	private CustomSharedPreference mPref;
	private String userid;
	private String TAG = EmailVerificationActivity.class.getName();
	private ForeGroundTask service;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_email_verfication);
		
		service = new ForeGroundTask(this, this);
		
		mNotNow = (Button) findViewById(R.id.btn_email_veri_not_now);
		mOk = (Button) findViewById(R.id.btn_email_veri_ok);
		mAlertText = (TextView) findViewById(R.id.txt_email_veri_alert);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, EmailVerificationActivity.this);
		userid = mPref.getString(SessionManager.KEY_USERID, null);
		
		
		if(getIntent().hasExtra("initiate"))
		{
			mAlertText.setText(getResources().getString(R.string.email_verfication_initiate));
		}
		else if(getIntent().hasExtra("invite")){
			mAlertText.setText(getResources().getString(R.string.email_verfication_invite));
		}else if(getIntent().hasExtra("inbox")){
			mAlertText.setText(getResources().getString(R.string.email_verfication_inbox));
		}
		
		
		mNotNow.setOnClickListener(this);
		mOk.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_email_veri_not_now:
			finish();
			break;
		case R.id.btn_email_veri_ok:
			service.callpostService(AppCollageConstants.url, populateEmailVerification(), EmailVerifyResponse.class, ActionType.EMAILVERIFY.getActionType());
			break;

		default:
			break;
		}
	}
	public void intializeProgressBar(Context context) {
		// TODO Auto-generated method stub
		progressbar = new DialogFactory(context).getTransParentDialog();
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
			if(actionType==ActionType.EMAILVERIFY.getActionType())
			{
				EmailVerifyResponse emailVerifyResponse = (EmailVerifyResponse) response;
				if (emailVerifyResponse.isResp()) {
					finish();
				}
				else{
					ToastCustom.makeText(EmailVerificationActivity.this, emailVerifyResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actionType));
				
	}

	public void onpreExecute(int actionType,
			NetworkEventsEnum networkEventsEnum, Object request) {
		// TODO Auto-generated method stub
		super.onpreExecute(actionType, networkEventsEnum, request);
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));

	}
	
	private EmailVerifyRequest populateEmailVerification()
	{
		EmailVerifyRequest request = new EmailVerifyRequest();
		request.setAction(WebServiceAction.EMAILVERIACTION);
		request.setId(userid);
		
		return request;
	}

}
