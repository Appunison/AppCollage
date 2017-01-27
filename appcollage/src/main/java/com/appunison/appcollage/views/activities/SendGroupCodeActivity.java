package com.appunison.appcollage.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.GroupCodeRequest;
import com.appunison.appcollage.model.pojo.response.GroupCodeResponse;
import com.appunison.appcollage.model.pojo.response.SignupResponse;
import com.appunison.appcollage.utils.DialogFactory;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.basewidgets.Validation;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;
import com.appunison.view.NetworkBaseActivity;

public class SendGroupCodeActivity extends NetworkBaseActivity implements OnClickListener{

	private String TAG = SendGroupCodeActivity.class.getName();
	private EditText medtGroupCode;
	private Button mbtnOk;
	private Button mbtnCancel;
	private ForeGroundTask service;
	private CustomSharedPreference mPref;
	private SignupResponse signupResponse; 
	private Intent intentGet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_send_group_code);
		
		medtGroupCode = (EditText) findViewById(R.id.edt_group_code);
		mbtnCancel = (Button) findViewById(R.id.btn_group_code_cancel);
		mbtnOk = (Button) findViewById(R.id.btn_group_code_ok);
		
		service = new ForeGroundTask(this, this);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, SendGroupCodeActivity.this);
		
		intentGet = getIntent();
		if (intentGet.getBooleanExtra("enter_time", false)) {
			signupResponse = (SignupResponse) intentGet.getSerializableExtra("signupResponse");
		}
		
		mbtnCancel.setOnClickListener(this);
		mbtnOk.setOnClickListener(this);
	}
	public void intializeProgressBar(Context context) {
		// TODO Auto-generated method stub
		progressbar = new DialogFactory(context).getTransParentDialog();
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_group_code_cancel:
			finish();
			
			break;
		case R.id.btn_group_code_ok:
			if(checkValidation())
			{
				service.callpostService(AppCollageConstants.url, populateGroupCode(), GroupCodeResponse.class, ActionType.GROUPCODE.getActionType());
			}
			break;

		default:
			break;
		}
		
	}
	
	public void onProgress(int actiontype, double progress,
			NetworkEventsEnum networkeventsEnum, Object request) {
		// TODO Auto-generateBaseResponsed method stub
		AppCollageLogger.d(TAG, "Web service on Progress"+ActionType.getDescriptionFromActionType(actiontype));
	}	

	public void onUpdate(int actiontype, NetworkEventsEnum networkevent,
			Object request, Object response, Exception excption) {
		// TODO Auto-generated method stub
		super.onUpdate(actiontype, networkevent, request, response, excption);
		switch (networkevent) {
		case SUCCESS:
			if (actiontype == ActionType.GROUPCODE.getActionType()) {
				GroupCodeResponse groupCodeResponse = (GroupCodeResponse) response;
				if (groupCodeResponse.isResp()) {
					ToastCustom.makeText(this, groupCodeResponse.getDesc(), Toast.LENGTH_SHORT);
					finish();
					
				}
				else{
					ToastCustom.makeText(this, groupCodeResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actiontype));
		
	}

	public void onpreExecute(int actionType,
			NetworkEventsEnum networkEventsEnum, Object request) {
		// TODO Auto-generated method stub
		super.onpreExecute(actionType, networkEventsEnum, request);
	}

	private GroupCodeRequest populateGroupCode()
	{
		String userid = mPref.getString(SessionManager.KEY_USERID, null);
		GroupCodeRequest groupCodeRequest = new GroupCodeRequest();
		
		groupCodeRequest.setAction(WebServiceAction.GROUPCODEACTION);
		groupCodeRequest.setId(userid);
		groupCodeRequest.setUnique_key(medtGroupCode.getText().toString());
		
		return groupCodeRequest;
		
	}
	/**
	 * check Validation
	 * @return
	 */
	private boolean checkValidation()
	{
		boolean valid = true;

		if(!Validation.hasText(medtGroupCode))
			valid = false;

		return valid;
	}
}
