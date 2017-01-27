package com.appunison.appcollage.views.activities;

import org.springframework.util.CollectionUtils;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.GroupListRequest;
import com.appunison.appcollage.model.pojo.request.GroupMemberRequest;
import com.appunison.appcollage.model.pojo.response.GroupMemberResponse;
import com.appunison.appcollage.model.pojo.response.GroupResponse;
import com.appunison.appcollage.utils.EditTextProperty;
import com.appunison.appcollage.views.adapter.GroupAdapter;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.appcollage.R;
import com.appunison.task.ForeGroundTask;

public class InitiateActivity extends AppCollageNetworkActivity{
	
	
	private String TAG = InitiateActivity.class.getName();
	private @InjectView(R.id.txt_counter) TextView mtxtCounter;
	private @InjectView(R.id.layout_initiate) LinearLayout mlayoutInitiate;
	private @InjectView(R.id.spinner_initiate_group) Spinner mSpinnerInitiateGroup;
	private @InjectView(R.id.btn_initiate) Button mbtnInitiate;
	private @InjectView(R.id.img_back) ImageView mimgBack;
	private @InjectView(R.id.edt_initiate_msg) EditText medtInitiateMsg;
	private ForeGroundTask service;
	private GroupAdapter adapter;
	private String userid;
	private CustomSharedPreference mPref;
	private GroupResponse groupResponse;
	private Intent intent;
	private String groupId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_initiate);
		
		intent = getIntent();
		if(intent.getBooleanExtra("manageflow", false))
		{
			groupId = intent.getStringExtra("groupId");
		}
		
		mtxtTitle.setText(getResources().getString(R.string.initiate_appcollage));
		mimgBack.setImageResource(R.drawable.ic_action_back);
		mbtnInitiate.setOnClickListener(this);
		medtInitiateMsg.setOnClickListener(this);
		
		EditTextProperty.disable(medtInitiateMsg);
		
		service=new ForeGroundTask(this, this);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, InitiateActivity.this);
		userid = mPref.getString(SessionManager.KEY_USERID, null);
		service.callpostService(AppCollageConstants.url, populateCreatedGroupList(), GroupResponse.class, ActionType.GROUPLIST.getActionType());
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.edt_initiate_msg:
			EditTextProperty.enable(medtInitiateMsg);
			 //uploadImage(userid, groupResponse.getResult().get(mSpinnerInitiateGroup.getSelectedItemPosition()).getGroup_id(), "");
			//ToastCustom.makeText(this, ""+medtInitiateMsg.getText().toString().length(), Toast.LENGTH_LONG);
			medtInitiateMsg.addTextChangedListener(new TextWatcher() 
			{
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				public void onTextChanged(CharSequence s, int start, int before, int count) {
					mtxtCounter.setText(String.valueOf(155-s.length()));
				}

				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
			break;
		
		case R.id.btn_initiate:
			//ToastCustom.underDevelopment(InitiateActivity.this);
			
			if(groupResponse!=null)
			{	
				if((!CollectionUtils.isEmpty(groupResponse.getResult())))
				{
					service.callpostService(AppCollageConstants.url, populateGroupMember(), GroupMemberResponse.class, ActionType.GROUPMEMBER.getActionType());
					/*Intent intent = new Intent(InitiateActivity.this, MyCameraActivity.class);
					intent.putExtra("groupId", groupResponse.getResult().get(mSpinnerInitiateGroup.getSelectedItemPosition()).getGroup_id());
					intent.putExtra("width", mlayoutInitiate.getWidth());
					intent.putExtra("height", mlayoutInitiate.getHeight());
					intent.putExtra("message", medtInitiateMsg.getText().toString());
					startActivityForResult(intent, AppCollageConstants.COLLAGE_REQUEST);*/
				}
				else{
					ToastCustom.makeText(InitiateActivity.this, getResources().getString(R.string.group_not_exist), Toast.LENGTH_SHORT);
				}
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
			if(actiontype==ActionType.GROUPLIST.getActionType())
			{
				groupResponse=(GroupResponse)response;
				if(groupResponse.isResp())
				{
						adapter = new GroupAdapter(InitiateActivity.this, groupResponse.getResult());
						mSpinnerInitiateGroup.setAdapter(adapter);
						if(intent.getBooleanExtra("manageflow", false))
						{
							mSpinnerInitiateGroup.setSelection(getIndex(groupId, groupResponse));
						}
				}
				else
				{
					ToastCustom.makeText(InitiateActivity.this, groupResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			else if(actiontype==ActionType.GROUPMEMBER.getActionType())
			{
				GroupMemberResponse groupMemberResponse=(GroupMemberResponse)response;
				if(groupMemberResponse.isResp())
				{
					if(groupMemberResponse.getResult().size()>=2)
					{
						//String k=medtInitiateMsg.getHint().toString();
						Intent intent = new Intent(InitiateActivity.this, MyCameraActivity.class);
						intent.putExtra("groupId", groupResponse.getResult().get(mSpinnerInitiateGroup.getSelectedItemPosition()).getGroup_id());
						intent.putExtra("width", mlayoutInitiate.getWidth());
						intent.putExtra("height", mlayoutInitiate.getHeight());
						intent.putExtra("message", medtInitiateMsg.getHint().toString());
						startActivityForResult(intent, AppCollageConstants.COLLAGE_REQUEST);
					}
					else{
						ToastCustom.makeText(this, getResources().getString(R.string.initiate_massage), Toast.LENGTH_SHORT);
					}
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
	
	/**
	 * show group list
	 * @return  group list
	 */
	
	private GroupListRequest populateCreatedGroupList()
	{
		GroupListRequest groupListRequest = new GroupListRequest();
		groupListRequest.setAction(WebServiceAction.GROUPLISTACTION);
		groupListRequest.setId(userid);

		return groupListRequest;

	}
	/**
	 * count group member
	 * @return
	 */
	private GroupMemberRequest populateGroupMember()
	{
		GroupMemberRequest groupMemberRequest = new GroupMemberRequest();
		groupMemberRequest.setAction(WebServiceAction.GROUPMEMBERACTION);
		//groupMemberRequest.setGroup_id("260");
		groupMemberRequest.setGroup_id(groupResponse.getResult().get(mSpinnerInitiateGroup.getSelectedItemPosition()).getGroup_id());
		return groupMemberRequest;
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AppCollageConstants.COLLAGE_REQUEST && resultCode  == AppCollageConstants.COLLAGE_REQUEST) {
			if(data.getStringExtra("screen").equals("home"))
			{
				setResult(AppCollageConstants.COLLAGE_REQUEST);
				finish();
			}
		}
		else if(requestCode == AppCollageConstants.FINAL_HOME && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
	}
	private int getIndex(String groupid, GroupResponse groupResponse)
	{
		for(int i=0; i<groupResponse.getResult().size(); i++)
		{
			if(groupid.equals(groupResponse.getResult().get(i).getGroup_id()))
				return i;
				
		}
		return 0;
	}

}
