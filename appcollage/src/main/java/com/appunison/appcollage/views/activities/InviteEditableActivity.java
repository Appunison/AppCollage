package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.IdType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.InviteViaContactRequest;
import com.appunison.appcollage.model.pojo.response.InviteViaContactResponse;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.basewidgets.Validation;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.appcollage.R;
import com.appunison.task.ForeGroundTask;

public class InviteEditableActivity extends AppCollageNetworkActivity{

	private String TAG = InviteEditableActivity.class.getName();
	private @InjectView(R.id.txt_invite_editable_groupName) TextView mtxtGroupName;
	private @InjectView(R.id.txt_invite_editable_userName) TextView mtxtUserName;
	private @InjectView(R.id.edt_invite_editable_msg) EditText medtMassage;
	private @InjectView(R.id.btn_invite_editable_send) Button mbtnSend;
	private String msg,username,groupName,groupId;
	private ForeGroundTask service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_invite_editable);

		groupId = getIntent().getExtras().getString("groupIdLocal");
		groupName = getIntent().getExtras().getString("groupNameLocal");
		username = getIntent().getExtras().getString("username");
		msg=getIntent().getExtras().getString("msg");
		service = new ForeGroundTask(this, this);

		mtxtGroupName.setText(getResources().getString(R.string.to)+groupName);
		mtxtUserName.setText(getResources().getString(R.string._for)+username);
		medtMassage.setText(msg);

		mtxtTitle.setText(getResources().getString(R.string._invite));
		mbtnSend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_invite_editable_send:
			if (checkValidation()) {
				service.callpostService(AppCollageConstants.url, populateInviteViacontact(), InviteViaContactResponse.class, ActionType.INVITEVIACONTACT.getActionType());
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

	public void onUpdate(int actionType, NetworkEventsEnum networkEvent,
			Object request, Object response, Exception exception) {
		// TODO Auto-generated method stub
		super.onUpdate(actionType, networkEvent, request, response, exception);

		switch (networkEvent) {
		case SUCCESS:
			if(actionType==ActionType.INVITEVIACONTACT.getActionType())
			{
				InviteViaContactResponse inviteResponse=(InviteViaContactResponse)response;
				if(inviteResponse.isResp())
				{
					Intent intent = new Intent(InviteEditableActivity.this, InviteSentActivity.class);
					intent.putExtra("groupName", groupName);
					intent.putExtra("userName", username);
					startActivityForResult(intent,2);
				}
				else
				{
					ToastCustom.makeText(InviteEditableActivity.this, inviteResponse.getDesc(), Toast.LENGTH_SHORT);
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
		// TODO Auto-generated method stub
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
	 * check validation
	 */
	private boolean checkValidation()
	{
		boolean valid = true;
		if(!Validation.hasText(medtMassage))
			valid = false;

		return valid;
	}
	private InviteViaContactRequest populateInviteViacontact()
	{
		InviteViaContactRequest inviteRequest = new InviteViaContactRequest();

		inviteRequest.setAction(WebServiceAction.INVITEGROUPUSER);
		inviteRequest.setGroup_id(groupId);
		medtMassage.setClickable(true);
		inviteRequest.setMessage(medtMassage.getText().toString());
		inviteRequest.setRequestby(new String[]{username});
		inviteRequest.setId_type(IdType.AppCollage);

		return inviteRequest;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 2 && resultCode == 2)
		{
			setResult(2, data);
			finish();
		}
		else if(requestCode == AppCollageConstants.FINAL_HOME && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
	}
}
