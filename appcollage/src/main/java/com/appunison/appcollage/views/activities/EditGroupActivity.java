package com.appunison.appcollage.views.activities;

import org.springframework.util.StringUtils;

import roboguice.inject.InjectView;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.EditGroupNameRequest;
import com.appunison.appcollage.model.pojo.request.GroupListRequest;
import com.appunison.appcollage.model.pojo.response.EditGroupNameResponse;
import com.appunison.appcollage.model.pojo.response.GroupResponse;
import com.appunison.appcollage.views.adapter.GroupAdapter;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.basewidgets.Validation;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EditGroupActivity extends AppCollageNetworkActivity{

	private String TAG = EditGroupActivity.class.getName();
	private @InjectView(R.id.ll_edit_group_container) LinearLayout mlayoutContainer;
	private @InjectView(R.id.edt_edit_group) EditText medtGroup;
	private @InjectView(R.id.list_edit_group) ListView mlistGroup;
	private @InjectView(R.id.btn_edit_group_cancel) Button mbtnCancel;
	private @InjectView(R.id.btn_edit_group_change) Button mbtnChange;
	private @InjectView(R.id.txt_title) TextView mtxtTitle;
	private @InjectView(R.id.txt_edit_group_no_result_found) TextView mtxtNoResult;
	
	private @InjectView(R.id.img_back) ImageView mimgBack;
	private ForeGroundTask service;
	private CustomSharedPreference mPref;
	private String userid;
	private GroupAdapter adapter;
	private String groupName, groupId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_edit_group_code);
		
		service=new ForeGroundTask(this, this);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, EditGroupActivity.this);
		userid = mPref.getString(SessionManager.KEY_USERID, null);
		
		mtxtTitle.setText(getResources().getString(R.string.edit_group_name));
		mimgBack.setImageResource(R.drawable.ic_action_back);
		
		//hit to get list of group
		service.callpostService(AppCollageConstants.url, populateCreatedGroupList(), GroupResponse.class, ActionType.GROUPLIST.getActionType());
		
		mbtnCancel.setOnClickListener(this);
		mbtnChange.setOnClickListener(this);
		mlayoutContainer.setOnClickListener(this);
		/**
		 * get list detail
		 * uses on item click listener
		 */
		mlistGroup.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//ToastCustom.underDevelopment(CreateGroupActivity.this);
				TextView textView=(TextView)view.findViewById(R.id.txt_group_list_name);
				groupId = (String) textView.getTag();
				groupName = (String) textView.getText();
				medtGroup.setText(groupName);
		}
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_edit_group_container:
			//hide keyboard click on outside
			InputMethodManager inputManager = (InputMethodManager)
			getSystemService(Context.INPUT_METHOD_SERVICE); 

			inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		case R.id.btn_edit_group_cancel:
			finish();
			break;
		case R.id.btn_edit_group_change:
			if(checkValidation())
			{
				service.callpostService(AppCollageConstants.url, populateEditGroup(), EditGroupNameResponse.class, ActionType.EDITGROUP.getActionType());
			}
			break;

		default:
			break;
		}
	}
	public void onProgress(int actionType, double progress, NetworkEventsEnum networkEvent,
			Object request) {

		// TODO Auto-generated method stub
		AppCollageLogger.d(TAG, "Web service on Progress"+ActionType.getDescriptionFromActionType(actionType));
	}
	public void onUpdate(int actionType ,NetworkEventsEnum networkEvent, Object request,
			Object response, Exception exception) {

		super.onUpdate(actionType, networkEvent, request, response, exception);
		switch (networkEvent) {
		case SUCCESS:

			if(actionType==ActionType.GROUPLIST.getActionType())
			{
				GroupResponse groupResponse=(GroupResponse)response;
				if(groupResponse.isResp())
				{
						adapter = new GroupAdapter(EditGroupActivity.this, groupResponse.getResult());
						mlistGroup.setAdapter(adapter);
						mtxtNoResult.setVisibility(View.GONE);
				}
				else
				{
					mtxtNoResult.setVisibility(View.VISIBLE);
				}
			}else if(actionType==ActionType.EDITGROUP.getActionType())
			{
				EditGroupNameResponse editGroupNameResponse = (EditGroupNameResponse) response;
				if (editGroupNameResponse.isResp()) {
					ToastCustom.makeText(this, editGroupNameResponse.getDesc(), Toast.LENGTH_SHORT);
					finish();
				}
				else{
					ToastCustom.makeText(this, editGroupNameResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actionType));
	}

	public void onpreExecute(int actionType, NetworkEventsEnum networkEvent, Object request) {
		
		super.onpreExecute(actionType, networkEvent, request);
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));
	}
	/**
	 * for get group list
	 */
	private GroupListRequest populateCreatedGroupList()
	{
		GroupListRequest groupListRequest = new GroupListRequest();
		groupListRequest.setAction(WebServiceAction.GROUPLISTACTION);
		groupListRequest.setId(userid);

		return groupListRequest;

	}
	/**
	 * for change group name
	 */
	private EditGroupNameRequest populateEditGroup()
	{
		EditGroupNameRequest request = new EditGroupNameRequest();
		request.setAction(WebServiceAction.EDITGROUPACTION);
		request.setGroup_id(groupId);
		request.setGroup_name(StringUtils.capitalize(medtGroup.getText().toString().toUpperCase()));
		
		return request;
		
	}
	/**
	 * check Validation
	 * @return
	 */
	private boolean checkValidation()
	{
		boolean valid = true;

		if(!Validation.hasText(medtGroup))
			valid = false;

		return valid;
	}
	
}
