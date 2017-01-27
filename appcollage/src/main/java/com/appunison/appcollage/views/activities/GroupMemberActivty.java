package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.GroupMemberRequest;
import com.appunison.appcollage.model.pojo.response.GroupMemberResponse;
import com.appunison.appcollage.views.adapter.GroupMemberAdapter;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.appcollage.R;
import com.appunison.task.ForeGroundTask;

public class GroupMemberActivty extends AppCollageNetworkActivity{

	private String TAG = GroupMemberActivty.class.getName();
	private @InjectView(R.id.txt_group_name) TextView mtxtGroupName;
	private @InjectView(R.id.txt_no_result_found) TextView mtxtNoResultFound;
	private @InjectView(R.id.list_group_member_name) ListView mlistMemberName;
	private GroupMemberAdapter groupMemberAdapter;
	private String groupId, groupName;
	private ForeGroundTask service;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_group_members);
		
		groupId = getIntent().getExtras().getString("groupId");
		groupName = getIntent().getExtras().getString("groupName");
		service = new ForeGroundTask(this, this);
		mtxtTitle.setText(getResources().getString(R.string.group_members));
		mtxtGroupName.setText(groupName);
		
		service.callpostService(AppCollageConstants.url, populateGroupMember(), GroupMemberResponse.class, ActionType.GROUPMEMBER.getActionType());
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
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

			if(actionType==ActionType.GROUPMEMBER.getActionType())
			{
				GroupMemberResponse groupMemberResponse=(GroupMemberResponse)response;
				if(groupMemberResponse.isResp())
				{
					groupMemberAdapter = new GroupMemberAdapter(GroupMemberActivty.this, groupMemberResponse.getResult());
					mlistMemberName.setAdapter(groupMemberAdapter);
					mtxtNoResultFound.setVisibility(View.GONE);
				}
				else
				{
					mtxtNoResultFound.setVisibility(View.VISIBLE);
					//ToastCustom.makeText(GroupMemberActivty.this, groupMemberResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			break;
		default:
			break;
		}
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
	
	private GroupMemberRequest populateGroupMember()
	{
		GroupMemberRequest groupMemberRequest = new GroupMemberRequest();
		groupMemberRequest.setAction(WebServiceAction.GROUPMEMBERACTION);
		//groupMemberRequest.setGroup_id("260");
		groupMemberRequest.setGroup_id(groupId);
		return groupMemberRequest;
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppCollageConstants.FINAL_HOME && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
	}
}
