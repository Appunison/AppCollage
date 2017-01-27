package com.appunison.appcollage.views.activities;

import org.springframework.util.StringUtils;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.DeleteAccountRequest;
import com.appunison.appcollage.model.pojo.response.ChangePasswordResponse;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.basewidgets.Validation;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;

public class DeleteAccoutActivity extends AppCollageNetworkActivity{

	private @InjectView (R.id.layout_back) LinearLayout mlayoutBack;
	private @InjectView (R.id.img_back) ImageView mimgBack;
	private @InjectView (R.id.img_option) ImageView mimgOption;
	private @InjectView (R.id.txt_title) TextView mtxtTitle;
	private @InjectView (R.id.layout_option) LinearLayout mlayoutOption;
	private @InjectView (R.id.edt_delete_account_password) EditText medtPassword;
	private @InjectView (R.id.edt_delete_account_re_password) EditText medtRePassword;
	private @InjectView (R.id.btn_delete_account) Button mbtnDelete;
	private CustomSharedPreference mPref;
	private ForeGroundTask service;
	private String TAG = DeleteAccoutActivity.class.getName();
	private SessionManager sessionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_delete_account);
		
		mimgBack.setImageResource(R.drawable.ic_action_back);
		mimgOption.setVisibility(View.VISIBLE);
		mtxtTitle.setText(getResources().getString(R.string.delete_account));
		
		sessionManager = new SessionManager(AppCollageConstants.PREF_NAME, DeleteAccoutActivity.this);
		service = new ForeGroundTask(this, this);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, DeleteAccoutActivity.this);
		
		mbtnDelete.setOnClickListener(this);
	}

	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_delete_account:
			if(checkValidation())
			{
				service.callpostService(AppCollageConstants.url, populateDeleteAccount(), ChangePasswordResponse.class, ActionType.DELETEACCOUNTACTION.getActionType());
			}
			break;
			
		default:
			break;
		}
	}
	
	public void onProgress(int actiontype, double progress,
			NetworkEventsEnum networkeventsEnum, Object request) {
		AppCollageLogger.d(TAG, "Web service on Progress"+ActionType.getDescriptionFromActionType(actiontype));
	}	

	public void onUpdate(int actionType, NetworkEventsEnum networkEvent,
			Object request, Object response, Exception exception) {
		super.onUpdate(actionType, networkEvent, request, response, exception);
		switch (networkEvent) {
		case SUCCESS:
			if(actionType==ActionType.DELETEACCOUNTACTION.getActionType())
			{
				ChangePasswordResponse changePasswordResponse =(ChangePasswordResponse) response;
				if(changePasswordResponse.isResp())
				{
					ToastCustom.makeText(this, changePasswordResponse.getDesc(), Toast.LENGTH_SHORT);
					Intent intent = new Intent();
					intent.putExtra("screen", "start");
					setResult(AppCollageConstants.INTENT_HOME, intent);
					sessionManager.logoutUser();
					finish();
				}
				else{
					ToastCustom.makeText(this, changePasswordResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			break;
		default:
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
	 * validation
	 * @return
	 */
	private boolean checkValidation()
	{
		boolean valid = true;

		if(!Validation.isPasswordMatch(medtPassword, medtRePassword))
			valid = false;
		/*if(!(medtPassword.getText().toString().length()>0))
		{
			medtPassword.setText("required");
			valid = false;
		}
		else if(medtPassword.getText().toString().equals(medtRePassword.getText().toString()))
		{
			valid = true;
		}else{
			medtRePassword.setError("Password does not match");
		}*/
		

		return valid;
	}
	private DeleteAccountRequest populateDeleteAccount()
	{
		String userid = mPref.getString(SessionManager.KEY_USERID, null);
		DeleteAccountRequest deleteAccountRequest = new DeleteAccountRequest();
		
		deleteAccountRequest.setAction(WebServiceAction.DELETEACCOUNT);
		deleteAccountRequest.setId(userid);
		deleteAccountRequest.setPassword(medtPassword.getText().toString());
		
		return deleteAccountRequest;
	}
}
