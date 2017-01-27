package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;
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
import com.appunison.appcollage.model.pojo.request.DeleteMessageRequest;
import com.appunison.appcollage.model.pojo.response.DeleteMessageResponse;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.task.ForeGroundTask;
import com.appunison.view.NetworkBaseActivity;

public class DeleteMessage extends NetworkBaseActivity implements OnClickListener{

	private String TAG = DeleteMessage.class.getName();
	private @InjectView(R.id.btn_delete_msg_cancel) Button mbtnCancel;
	private @InjectView(R.id.btn_delete_msg_ok) Button mbtnOk;
	private ForeGroundTask service;
	private Intent intent;
	private String msgId;
	private String msgType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_delete_msg);
		
		service = new ForeGroundTask(this, this);
		
		intent = getIntent();
		msgId = intent.getStringExtra("msgId");
		msgType = intent.getStringExtra("msgType");
		
		mbtnCancel.setOnClickListener(this);
		mbtnOk.setOnClickListener(this);
	}	

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_delete_msg_cancel:
			finish();
			break;
		case R.id.btn_delete_msg_ok:
			service.callpostService(AppCollageConstants.url, populateMessage(), DeleteMessageResponse.class, ActionType.DELETEMESSAGE.getActionType());
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
			if(actionType==ActionType.DELETEMESSAGE.getActionType())
			{
				DeleteMessageResponse deleteMessageResponse = (DeleteMessageResponse) response;
				if (deleteMessageResponse.isResp()) {
					ToastCustom.makeText(DeleteMessage.this, deleteMessageResponse.getDesc(), Toast.LENGTH_SHORT);
					setResult(AppCollageConstants.DELETE_MSG);
					finish();
				}
				else{
					ToastCustom.makeText(DeleteMessage.this, deleteMessageResponse.getDesc(), Toast.LENGTH_SHORT);
				}
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
	
	private DeleteMessageRequest populateMessage()
	{
		DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest();
		
		deleteMessageRequest.setAction(WebServiceAction.DELETEMESSAGEACTION);
		deleteMessageRequest.setMessage_type(msgType);
		deleteMessageRequest.setMsg_id(msgId);
		
		return deleteMessageRequest;
	}
}
