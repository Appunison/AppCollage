package com.appunison.appcollage.dialog;


import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.listener.ReadMessage;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.UpdateMessageRequest;
import com.appunison.appcollage.model.pojo.request.UpdateMessageResponse;
import com.appunison.appcollage.model.pojo.response.Inbox;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.appcollage.R;
import com.appunison.task.ForeGroundTask;
import com.appunison.view.NetworkDialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowInboxDetailDialog extends NetworkDialog implements android.view.View.OnClickListener{

	private String TAG = ShowInboxDetailDialog.class.getName();
	private Context context;
	private ForeGroundTask service;
	private Inbox inbox;
	private ReadMessage readMessage;
	private TextView mtxtAlert,mtxtFrom;
	private Button mbtnReject, mbtnAccept, mbtnCancel;
	private LinearLayout mlin_lay_dialog_accept_reject;
	private boolean isUnRead = false;

	public ShowInboxDetailDialog(Context context, Inbox inbox, ReadMessage readMessage, boolean isUnRead) {
		super(context);
		this.context = context;
		this.inbox = inbox;
		this.readMessage = readMessage;
		this.isUnRead = isUnRead;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setCanceledOnTouchOutside(false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.layout_dialog_status_acceptance);

		mtxtFrom = (TextView) findViewById(R.id.txt_from);
		mtxtAlert = (TextView) findViewById(R.id.txt_dialog_alert);
		mbtnCancel = (Button) findViewById(R.id.btn_cancel);
		mbtnReject = (Button) findViewById(R.id.btn_dialog_reject);
		mbtnAccept = (Button) findViewById(R.id.btn_dialog_accept);
		mlin_lay_dialog_accept_reject = (LinearLayout) findViewById(R.id.lin_lay_dialog_accept_reject);

		if(!isUnRead) {
			mlin_lay_dialog_accept_reject.setVisibility(View.GONE);
		}

		mtxtFrom.setText(inbox.getFrom());
		mtxtAlert.setText(inbox.getMessage());
		mbtnReject.setOnClickListener(this);
		mbtnAccept.setOnClickListener(this);
		mbtnCancel.setOnClickListener(this);
		
		service = new ForeGroundTask(this, context);

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_cancel:
			dismiss();
			break;
		
		case R.id.btn_dialog_accept:
			service.callpostService(AppCollageConstants.url, populateUpdateMessageRequest("1"), UpdateMessageResponse.class, ActionType.UPDATEMESSGESTATUS.getActionType());
			break;
		case R.id.btn_dialog_reject:
			service.callpostService(AppCollageConstants.url, populateUpdateMessageRequest("0"), UpdateMessageResponse.class, ActionType.UPDATEMESSGESTATUS.getActionType());
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
			if(actiontype==ActionType.UPDATEMESSGESTATUS.getActionType())
			{
				UpdateMessageResponse baseResponse=(UpdateMessageResponse)response;
				if(baseResponse.isResp())
				{
					readMessage.onClick();
					dismiss();
				}
				else
				{
					ToastCustom.makeText(context, baseResponse.getDesc(), Toast.LENGTH_SHORT);
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
	private UpdateMessageRequest populateUpdateMessageRequest(String readStatus){
		UpdateMessageRequest request = new UpdateMessageRequest();
		request.setAction(WebServiceAction.UPDATEMESSAGESTATUSACTION);
		request.setMessage_id(inbox.getMessage_id());
		request.setMessage_type(inbox.getMessage_type());
		request.setRead_status(readStatus);

		return request;
	}
}
