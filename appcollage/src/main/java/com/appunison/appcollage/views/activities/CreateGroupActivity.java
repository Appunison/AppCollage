package com.appunison.appcollage.views.activities;

import java.util.ArrayList;

import org.springframework.util.StringUtils;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.GroupListRequest;
import com.appunison.appcollage.model.pojo.request.GroupRequest;
import com.appunison.appcollage.model.pojo.response.Group;
import com.appunison.appcollage.model.pojo.response.GroupResponse;
import com.appunison.appcollage.views.adapter.GroupAdapter;
import com.appunison.basewidgets.Validation;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.appcollage.R;
import com.appunison.task.ForeGroundTask;

public class CreateGroupActivity extends AppCollageNetworkActivity
{

	private String TAG=CreateGroupActivity.class.getName();
	private @InjectView(R.id.layout_option) LinearLayout mOption;
	private @InjectView(R.id.edt_create_group) EditText medtCreateGroup;
	private @InjectView(R.id.btn_add_group) Button mbtnAddGroup;
	private @InjectView(R.id.list_current_created_group) ListView mlistCreatedGroup;
	private @InjectView(R.id.txt_title) TextView mtxtTitle;
	private @InjectView(R.id.txt_no_result_found) TextView mtxtNoResult;
	
	private @InjectView(R.id.img_back)  ImageView mimgBack;
	private ForeGroundTask service;
	//private AsyncTask<Object, Double, TaskResponseWrapper> createGroupTask;
	private String userid;
	/*private int page_no = 1;
	private boolean loadingMore = false;
	private boolean hasMoreItem = true;*/
	private GroupAdapter adapter;
	private CustomSharedPreference mPref;
	private boolean pendingRequest,email_verification;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_create_group);

		pendingRequest = getIntent().getBooleanExtra("pendingRequest", false);
		email_verification = getIntent().getBooleanExtra("email_verification", false);
		
		//mtxtTitle.setText(getResources().getString(R.string.groups));
		mbtnAddGroup.setOnClickListener(this);

		mtxtTitle.setText(getResources().getString(R.string.group));
		mimgBack.setImageResource(R.drawable.ic_action_back);
		service=new ForeGroundTask(this, this);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, CreateGroupActivity.this);
		//userid = getIntent().getStringExtra("user_id");
		userid = mPref.getString(SessionManager.KEY_USERID, null);
		//signupTask=service.callpostService(AppCollageConstants.url, populateCreatedGroupList(""+page_no), GroupResponse.class, ActionType.GROUPLIST.getActionType());
		//mlistCreatedGroup.setOnScrollListener(new ScrollListener());
		service.callpostService(AppCollageConstants.url, populateCreatedGroupList(), GroupResponse.class, ActionType.GROUPLIST.getActionType());
		
		/**
		 * get list detail
		 * uses on item click listener
		 */
		mlistCreatedGroup.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//ToastCustom.underDevelopment(CreateGroupActivity.this);
				TextView textView=(TextView)view.findViewById(R.id.txt_group_list_name);
				String groupId = (String) textView.getTag();
				String groupName = (String) textView.getText();
				// ToastCustom.makeText(CreateGroupActivity.this, "groupId = "+groupId+" groupName = "+groupName, Toast.LENGTH_SHORT);
				Intent intent = new Intent(CreateGroupActivity.this, ManageGroupActivity.class);
				intent.putExtra("groupId", groupId);
				intent.putExtra("groupName", groupName);
				intent.putExtra("pendingRequest", pendingRequest);
				intent.putExtra("email_verification", email_verification);
				startActivityForResult(intent, 001);
				//CreateGroupActivity.this.finish();
		}
		});
	}

	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.btn_add_group:
			//ToastCustom.underDevelopment(CreateGroupActivity.this);
			if(checkValidation())
			{
				service.callpostService(AppCollageConstants.url, populateCreateGroup(), GroupResponse.class, ActionType.GROUP.getActionType());
			}
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

			if(actionType==ActionType.GROUP.getActionType())
			{
				GroupResponse groupResponse=(GroupResponse)response;
				if(groupResponse.isResp())
				{
					//ToastCustom.makeText(CreateGroupActivity.this, "Krishna", Toast.LENGTH_SHORT);
				/*	page_no = 1;
					hasMoreItem = true;
					loadingMore = false;*/
					adapter = new GroupAdapter(CreateGroupActivity.this, groupResponse.getResult());
					mlistCreatedGroup.setAdapter(adapter);
					medtCreateGroup.setText("");
					mtxtNoResult.setVisibility(View.GONE);
				}
				else
				{
					mtxtNoResult.setVisibility(View.VISIBLE);
					//ToastCustom.makeText(CreateGroupActivity.this, groupResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			else if(actionType==ActionType.GROUPLIST.getActionType())
			{
				GroupResponse groupResponse=(GroupResponse)response;
				if(groupResponse.isResp())
				{
					/*if(page_no == 1)
					{*/
						adapter = new GroupAdapter(CreateGroupActivity.this, groupResponse.getResult());
						mlistCreatedGroup.setAdapter(adapter);
						mtxtNoResult.setVisibility(View.GONE);
					//}
					/*else
					{
						if(groupResponse.getResult().size()==0)
						{
							hasMoreItem = false;
						}
						else
						{
							int visibleItemPosition = adapter.getCount();
							adapter.AddItems(groupResponse.getResult());
							mlistCreatedGroup.setSelection(visibleItemPosition);
						}
					}*/
				}
				else
				{
					mtxtNoResult.setVisibility(View.VISIBLE);
					//ToastCustom.makeText(CreateGroupActivity.this, groupResponse.getDesc(), Toast.LENGTH_SHORT);
					adapter = new GroupAdapter(CreateGroupActivity.this, new ArrayList<Group>());
					mlistCreatedGroup.setAdapter(adapter);
				}
				//loadingMore = false;
			}
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actionType));
	}

	public void onpreExecute(int actionType, NetworkEventsEnum networkEvent, Object request) {
		
		super.onpreExecute(actionType, networkEvent, request);
		switch (networkEvent) {
		case STARTED:
			/*if(actionType==ActionType.GROUPLIST.getActionType())
			{
				loadingMore = true;
			}*/
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));
	}
	/**
	 * check Validation
	 * @return
	 */
	private boolean checkValidation()
	{
		boolean valid = true;

		if(!Validation.hasText(medtCreateGroup))
			valid = false;

		return valid;
	}
	/**
	 * create group and
	 * @return update group list
	 */

	private GroupRequest populateCreateGroup() {
		GroupRequest groupRequest = new GroupRequest();
		groupRequest.setAction(WebServiceAction.GROUPACTION);
		groupRequest.setGroup_name(medtCreateGroup.getText().toString().trim().toUpperCase());
		groupRequest.setId(userid);

		return groupRequest;
	}

	/**
	 * show group which is already created
	 * @return  group list
	 */

	//private GroupListRequest populateCreatedGroupList(String pageNo)
	private GroupListRequest populateCreatedGroupList()
	{
		GroupListRequest groupListRequest = new GroupListRequest();
		groupListRequest.setAction(WebServiceAction.GROUPLISTACTION);
		groupListRequest.setId(userid);
		//groupListRequest.setPage_no(pageNo);

		return groupListRequest;

	}

	/**
	 *This listener  use
	 *on scroll	
	 * @author appunison
	 *
	 */
	/*private class ScrollListener implements OnScrollListener
	{
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			int lastInScreen = firstVisibleItem + visibleItemCount;   
			if((lastInScreen == totalItemCount) && !(loadingMore) && hasMoreItem){    
				page_no++;
				signupTask=service.callpostService(AppCollageConstants.url, populateCreatedGroupList(""+page_no), GroupResponse.class, ActionType.GROUPLIST.getActionType());
			}
		}

	}*/
	/**
	 * update group list
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 001 && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
		else if (requestCode == 001) {
			/*page_no = 1;
			loadingMore = false;
			hasMoreItem = true;
			createGroupTask=service.callpostService(AppCollageConstants.url, populateCreatedGroupList(""+page_no), GroupResponse.class, ActionType.GROUPLIST.getActionType());*/
			service.callpostService(AppCollageConstants.url, populateCreatedGroupList(), GroupResponse.class, ActionType.GROUPLIST.getActionType());
		}
		
	}

}
