package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.appunison.appcollage.model.pojo.request.ChangePasswordRequest;
import com.appunison.appcollage.model.pojo.response.ChangePasswordResponse;
import com.appunison.appcollage.utils.DialogFactory;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.basewidgets.Validation;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;
import com.appunison.view.NetworkBaseActivity;

public class ChangePasswordActivity extends NetworkBaseActivity implements OnClickListener{

	private @InjectView (R.id.layout_back) LinearLayout mlayoutBack;
	private @InjectView (R.id.img_back) ImageView mimgBack;
	private @InjectView (R.id.img_option) ImageView mimgOption;
	private @InjectView (R.id.txt_title) TextView mtxtTitle;
	private @InjectView (R.id.layout_option) LinearLayout mlayoutOption;
	private @InjectView (R.id.edt_change_old_password) EditText medtOldPassword;
	private @InjectView (R.id.edt_change_new_password) EditText medtNewPassword;
	private @InjectView (R.id.edt_change_confirm_password) EditText medtConfirmPassword;
	private @InjectView (R.id.btn_change_password_save) Button mbtnSave;
	private CustomSharedPreference mPref;
	private ForeGroundTask service;
	private String TAG = ChangePasswordActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_change_password);
		
		service = new ForeGroundTask(this, this);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, ChangePasswordActivity.this);
		
		mimgBack.setImageResource(R.drawable.ic_action_back);
		mimgOption.setVisibility(View.VISIBLE);
		mtxtTitle.setText(getResources().getString(R.string._change_password));

		mlayoutBack.setOnClickListener(this);
		mlayoutOption.setOnClickListener(this);
		mbtnSave.setOnClickListener(this);
	}
	public void intializeProgressBar(Context context) {
		// TODO Auto-generated method stub
		progressbar = new DialogFactory(context).getTransParentDialog();
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.layout_option:
			//ToastCustom.underDevelopment(this);
			Intent intent = new Intent();
			intent.putExtra("screen", "finalHome");
			setResult(AppCollageConstants.FINAL_HOME, intent);
			finish();
			break;
		case R.id.btn_change_password_save:

			if(checkValidation())
			{
				//ToastCustom.underDevelopment(this);
				service.callpostService(AppCollageConstants.url, populateChangePass(), ChangePasswordResponse.class, ActionType.CHANGEPASSACTION.getActionType());
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
			if(actionType==ActionType.CHANGEPASSACTION.getActionType())
			{
				ChangePasswordResponse changePasswordResponse =(ChangePasswordResponse) response;
				if(changePasswordResponse.isResp())
				{
					ToastCustom.makeText(this, getResources().getString(R.string.change_pass_msg), Toast.LENGTH_SHORT);
					finish();
				}
				else{
					ToastCustom.makeText(this, getResources().getString(R.string.change_pass_error), Toast.LENGTH_SHORT);
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

		if(!Validation.hasText(medtOldPassword))
			valid = false;
		if(!Validation.limit(medtNewPassword, 6, 30, true))
			valid = false;
		else if(!Validation.isPasswordMatch(medtNewPassword, medtConfirmPassword))
			valid = false;

		return valid;
	}

	private ChangePasswordRequest populateChangePass()
	{
		String userid = mPref.getString(SessionManager.KEY_USERID, null);
		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
		
		changePasswordRequest.setAction(WebServiceAction.CHANGEPASSWORD);
		changePasswordRequest.setId(userid);
		changePasswordRequest.setNew_password(medtNewPassword.getText().toString());
		changePasswordRequest.setOld_password(medtOldPassword.getText().toString());
		
		return changePasswordRequest;
	}

}
