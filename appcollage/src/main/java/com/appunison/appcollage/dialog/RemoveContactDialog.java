package com.appunison.appcollage.dialog;


import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.DeleteGroupUser;
import com.appunison.appcollage.model.pojo.response.DeleteGroupResponse;
import com.appunison.appcollage.views.activities.RemoveActivity;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.appcollage.R;
import com.appunison.task.ForeGroundTask;
import com.appunison.view.NetworkDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RemoveContactDialog extends NetworkDialog implements android.view.View.OnClickListener{

	/*	private @InjectView (R.id.txt_dialog_alert) TextView mtxtAlert;
	private @InjectView(R.id.btn_dialog_delete_group_yes) Button mbtnYes;
	private @InjectView(R.id.btn_dialog_delete_group_no) Button mbtnNo;*/
	private String TAG="";
	private TextView mtxtAlert;
	private Button mbtnYes;
	private Button mbtnNo;
	private Context context;
	private String alertText;
	private String groupId;
	private ForeGroundTask service;
	private CustomSharedPreference mPref;


	public RemoveContactDialog(Context context, String alertText, String groupId) {
		super(context);
		this.context = context;
		this.alertText = alertText;
		this.groupId = groupId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setCanceledOnTouchOutside(false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.layout_dialog);

		mtxtAlert = (TextView) findViewById(R.id.txt_dialog_alert);
		mbtnNo = (Button) findViewById(R.id.btn_dialog_delete_group_no);
		mbtnYes = (Button) findViewById(R.id.btn_dialog_delete_group_yes);

		service = new ForeGroundTask(this, context);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, context);

		mtxtAlert.setText(alertText);
		mbtnNo.setOnClickListener(this);
		mbtnYes.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_dialog_delete_group_no:
			dismiss();
			break;

		case R.id.btn_dialog_delete_group_yes:

			//ToastCustom.underDevelopment(context);
			service.callpostService(AppCollageConstants.url, populateDeleteGroup(), DeleteGroupResponse.class, ActionType.DELETEGROUP.getActionType());
			break;
		}
	}

	public void onProgress(int actiontype, double progress,
			NetworkEventsEnum networkeventsEnum, Object request) {
		// TODO Auto-generated method stub
		super.onProgress(actiontype, progress, networkeventsEnum, request);
		AppCollageLogger.d(TAG, "Web service on Progress"+ActionType.getDescriptionFromActionType(actiontype));
	}

	public void onUpdate(int actiontype, NetworkEventsEnum networkevent,
			Object request, Object response, Exception excption) {
		// TODO Auto-generated method stub
		super.onUpdate(actiontype, networkevent, request, response, excption);
		switch (networkevent) {
		case SUCCESS:
			if(actiontype==ActionType.DELETEGROUP.getActionType())
			{
				DeleteGroupResponse deleteGroupResponse=(DeleteGroupResponse)response;
				if(deleteGroupResponse.isResp())
				{
					Intent intent = new Intent(context, RemoveActivity.class);
					((Activity)context).startActivityForResult(intent, AppCollageConstants.INTENT_HOME);
					((Activity) context).setResult(001);
					dismiss();
					((Activity) context).finish();
				}
				else
				{
					ToastCustom.makeText(context, deleteGroupResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			break;
		default:
			break;
		}

		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actiontype));
	}

	public void onpreExecute(int actionType,
			NetworkEventsEnum networkEventsEnum, Object request) {
		// TODO Auto-generated method stub
		super.onpreExecute(actionType, networkEventsEnum, request);
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));

	}
	private DeleteGroupUser populateDeleteGroup(){
		String userId = mPref.getString(SessionManager.KEY_USERID, null);
		DeleteGroupUser deleteGroupUser = new DeleteGroupUser();

		deleteGroupUser.setAction(WebServiceAction.DELETEGROUPUSER);
		deleteGroupUser.setGroup_id(groupId);
		deleteGroupUser.setUser_id(userId);

		return deleteGroupUser;
	}
}
