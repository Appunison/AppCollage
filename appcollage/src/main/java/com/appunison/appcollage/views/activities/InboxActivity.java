package com.appunison.appcollage.views.activities;

import java.util.List;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.dialog.InboxSortDialog;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.InboxRequest;
import com.appunison.appcollage.model.pojo.response.Inbox;
import com.appunison.appcollage.model.pojo.response.InboxResponse;
import com.appunison.appcollage.views.adapter.InboxAdapter;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;

public class InboxActivity extends AppCollageNetworkActivity{
	
	private String TAG = InviteActivity.class.getName();
	private @InjectView (R.id.img_back) ImageView mimgBack;
	private @InjectView(R.id.list_inbox) ListView mlist_inbox;
	private @InjectView(R.id.btn_inbox_sort) Button mbtn_inbox_sort;
	private @InjectView(R.id.txt_no_result_found) TextView mtxt_no_result_found;
	private @InjectView(R.id.layout_inbox) RelativeLayout layout_inbox;
	
	private ForeGroundTask service;
	private CustomSharedPreference mPref;
	private InboxAdapter adapter;
	private String groupId;
	private Intent intent;
	private List<Inbox> inboxs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_inbox);
		
		mtxtTitle.setText(getResources().getString(R.string._inbox));
		mimgBack.setImageResource(R.drawable.ic_action_back);
		
		intent = getIntent();
		if(intent.getBooleanExtra("groupInbox", false))
		{
			groupId = getIntent().getStringExtra("groupId");
		}
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, InboxActivity.this);;
		service=new ForeGroundTask(this, this);
		service.callpostService(AppCollageConstants.url, populateInbox(), InboxResponse.class, ActionType.INBOX.getActionType());

		mbtn_inbox_sort.setOnClickListener(this);
		
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
			if(actiontype==ActionType.INBOX.getActionType())
			{
				InboxResponse inboxResponse =(InboxResponse)response;
				if(inboxResponse.isResp())
				{
					inboxs=inboxResponse.getResult();
					adapter = new InboxAdapter(InboxActivity.this, 0,inboxs);
					adapter.setDimentions(layout_inbox.getHeight(), layout_inbox.getWidth());
					mlist_inbox.setAdapter(adapter);
					if(inboxResponse.getResult().size()>0)
					{
						mbtn_inbox_sort.setVisibility(View.VISIBLE);
						mtxt_no_result_found.setVisibility(View.GONE);
					}
					else
					{
						mtxt_no_result_found.setVisibility(View.VISIBLE);
					}
				}
				else
				{
					mtxt_no_result_found.setVisibility(View.VISIBLE);
					//ToastCustom.makeText(InboxActivity.this, inboxResponse.getDesc(), Toast.LENGTH_SHORT);
					adapter = new InboxAdapter(InboxActivity.this, 0,inboxResponse.getResult());
					adapter.setDimentions(layout_inbox.getHeight(), layout_inbox.getWidth());
					mlist_inbox.setAdapter(adapter);
				}
			break;
			}
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actiontype));
	}

	private InboxRequest populateInbox()
	{
		String userid = mPref.getString(SessionManager.KEY_USERID, null);
		
		InboxRequest inboxRequest = new InboxRequest();
		inboxRequest.setAction(WebServiceAction.USERINBOXACTION);
		inboxRequest.setUser_id(userid);
		inboxRequest.setLimit("20");
		if(intent.getBooleanExtra("groupInbox", false))
		{
			inboxRequest.setGroup_id(groupId);
		}
		else{
			inboxRequest.setGroup_id("");
		}
		return inboxRequest;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_inbox_sort:
			//ToastCustom.underDevelopment(InboxActivity.this);
			new InboxSortDialog(InboxActivity.this,adapter).show();;
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppCollageConstants.COLLAGE_REQUEST && resultCode == AppCollageConstants.COLLAGE_REQUEST)
		{
			service.callpostService(AppCollageConstants.url, populateInbox(), InboxResponse.class, ActionType.INBOX.getActionType());
		}
		else if(requestCode == AppCollageConstants.COLLAGE_REQUEST&&(resultCode==AppCollageConstants.AppCollage_REQUEST_FROM_INBOX))
		{
			String msgid=data.getStringExtra("msgid");
			Inbox inbox=new Inbox();
			inbox.setMessage_id(msgid);
			if(inboxs.contains(inbox))
			{
				int index=inboxs.indexOf(inbox);
				inbox=inboxs.get(index);
				inbox.setRead_status("1");
			}
			adapter.notifyDataSetChanged();
		}
		else if(requestCode == AppCollageConstants.COLLAGE_REQUEST && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
		else if(requestCode == AppCollageConstants.DELETE_MSG && resultCode == AppCollageConstants.DELETE_MSG)
		{
			//ToastCustom.makeText(InboxActivity.this, "back", Toast.LENGTH_SHORT);
			service.callpostService(AppCollageConstants.url, populateInbox(), InboxResponse.class, ActionType.INBOX.getActionType());
		}
	}
}
