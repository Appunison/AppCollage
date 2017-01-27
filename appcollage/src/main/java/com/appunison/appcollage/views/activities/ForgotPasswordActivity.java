package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;

import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.ForgotPasswordUser;
import com.appunison.appcollage.model.pojo.response.ForgotPasswordResponse;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.basewidgets.Validation;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.appcollage.R;
import com.appunison.task.ForeGroundTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 
 * @author appunison
 * This Activity is used to get Password.
 */
public class ForgotPasswordActivity extends AppCollageNetworkActivity{

	private String TAG=ForgotPasswordActivity.class.getName();
	private @InjectView (R.id.edt_forgot_userName) EditText medtUserName;
	private @InjectView (R.id.btn_forgot_cancel) Button mbtnCancel;
	private @InjectView (R.id.btn_forgot_send) Button mbtnSend;
	private ForeGroundTask service;
	private static final int FORGET=3;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_forgot_password);

		mtxtTitle.setText(getResources().getString(R.string.forgot_password_title));

		mbtnCancel.setOnClickListener(this);
		mbtnSend.setOnClickListener(this);
		service = new ForeGroundTask(this, this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_forgot_cancel:
			finish();
			break;

		case R.id.btn_forgot_send:
			if(checkValidation())
			{
				//ToastCustom.underDevelopment(ForgotPasswordActivity.this);
				service.callpostService(AppCollageConstants.url, populateForgotPassword(), ForgotPasswordResponse.class, ActionType.FORGOTPASSWORDUSER.getActionType());
			}
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
		// TODO Auto-generated method stub
		super.onUpdate(actiontype, networkevent, request, response, excption);
		
		switch (networkevent) {
		case SUCCESS:
			if(actiontype==ActionType.FORGOTPASSWORDUSER.getActionType())
			{
				ForgotPasswordResponse forgotPasswordResponse=(ForgotPasswordResponse)response;
				if(forgotPasswordResponse.isResp())
				{
					//ToastCustom.makeText(ForgotPasswordActivity.this, forgotPasswordResponse.getDesc(), Toast.LENGTH_SHORT);
					Intent intent= new Intent(ForgotPasswordActivity.this, SentLinkForgetActivity.class);
					startActivityForResult(intent,FORGET);
				}
				else
				{
					ToastCustom.makeText(ForgotPasswordActivity.this, forgotPasswordResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actiontype));
	}

	public void onpreExecute(int actionType,
			NetworkEventsEnum networkEvent, Object request) {
		// TODO Auto-generated method stub
		super.onpreExecute(actionType, networkEvent, request);
		switch (networkEvent) {
		case NETWORK_NOT_AVAILABLE:
			ToastCustom.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT);
			break;
		case STARTED:
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));
	}
	/**
	 * send data for json forgot password
	 */
	
	private ForgotPasswordUser populateForgotPassword()
	{
		ForgotPasswordUser fUser = new ForgotPasswordUser();
		fUser.setAction(WebServiceAction.FORGOTPASSWORD);
		fUser.setEmail(medtUserName.getText().toString());
		return fUser;
	}
   
	/*
	 *Check validation
	 * @return
	 */
	private boolean checkValidation()
	{
		boolean valid = true;

		if(!Validation.hasText(medtUserName))
			valid = false;
		return valid;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FORGET && resultCode == FORGET) {
			setResult(FORGET);
			finish();
			
		}
	}
}
