package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.GalleryRequest;
import com.appunison.appcollage.model.pojo.request.UpdateMessageRequest;
import com.appunison.appcollage.model.pojo.request.UpdateMessageResponse;
import com.appunison.appcollage.model.pojo.response.GalleryResponse;
import com.appunison.appcollage.views.adapter.GalleryAdapter;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;
import com.appunison.appcollage.R;
import com.appunison.basewidgets.ToastCustom;

public class GalleryActivity extends AppCollageNetworkActivity{

	private String TAG = GalleryActivity.class.getName();
	private @InjectView(R.id.edt_gallery_search) EditText medtGroupNameSearch;
	private @InjectView(R.id.img_gallery_search) ImageView mimgSearch;
	private @InjectView(R.id.list_gallery) ListView mlistGallery;
	private @InjectView (R.id.img_back) ImageView mimgBack;
	private @InjectView(R.id.txt_gallery_no_result_found) TextView mtxtNoResult;
	private @InjectView(R.id.txt_group_name) TextView mtxtGroupName;
	private GalleryAdapter adapter;

	private CustomSharedPreference mPref;
	private ForeGroundTask service;
	private String groupId, groupName, messageid;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_gallery);
		mimgBack.setImageResource(R.drawable.ic_action_back);
		intent = getIntent();
		if(intent.getBooleanExtra("ManageGroup", false))
		{
			groupId = intent.getStringExtra("groupId");
			groupName = intent.getStringExtra("groupName");
			mtxtGroupName.setVisibility(View.VISIBLE);
			mtxtGroupName.setText(groupName);
			medtGroupNameSearch.setHint(getResources().getString(R.string.search_by_date));
		}
		

		service = new ForeGroundTask(this, this);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, GalleryActivity.this);

		mtxtTitle.setText(getResources().getString(R.string._gallery));
		service.callpostService(AppCollageConstants.url, populateGallery(), GalleryResponse.class, ActionType.GALLARYINO.getActionType());

		medtGroupNameSearch.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(intent.getBooleanExtra("ManageGroup", false))
				{
					adapter.filterWithDate(medtGroupNameSearch.getText().toString());
				}
				else{
					adapter.filterWithName(medtGroupNameSearch.getText().toString());
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

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
			if(actionType==ActionType.GALLARYINO.getActionType())
			{
				GalleryResponse galleryResponse=(GalleryResponse)response;
				if(galleryResponse.isResp())
				{
					if(intent.getBooleanExtra("ManageGroup", false))
					{
						adapter = new GalleryAdapter(GalleryActivity.this,galleryResponse.getResult(),true);
						mlistGallery.setAdapter(adapter);
						if(galleryResponse.getResult().size()==0)
						{
							mtxtNoResult.setVisibility(View.VISIBLE);
							mlistGallery.setVisibility(View.GONE);
						}
						if(intent.getBooleanExtra("hasmassageid", false))
						{
							messageid = intent.getStringExtra("messageid");
							UpdateMessageRequest masRequest = new UpdateMessageRequest();
							masRequest.setAction(WebServiceAction.UPDATEMESSAGESTATUSACTION);
							masRequest.setMessage_id(messageid);
							masRequest.setMessage_type("CollageCompletion");
							masRequest.setRead_status("1");
							service.callpostService(AppCollageConstants.url, masRequest, UpdateMessageResponse.class, ActionType.UPDATEMESSGESTATUS.getActionType());
						}
					}
					else{
						adapter = new GalleryAdapter(GalleryActivity.this,galleryResponse.getResult(),false);
						mlistGallery.setAdapter(adapter);
						if(galleryResponse.getResult().size()==0)
						{
							mtxtNoResult.setVisibility(View.VISIBLE);
							mlistGallery.setVisibility(View.GONE);
						}
					}
				}
				else
				{
					mtxtNoResult.setVisibility(View.VISIBLE);
					mlistGallery.setVisibility(View.GONE);
				}
			}else if(actionType==ActionType.UPDATEMESSGESTATUS.getActionType())
			{
				UpdateMessageResponse baseResponse=(UpdateMessageResponse)response;
				if(baseResponse.isResp())
				{
					//ToastCustom.makeText(this, baseResponse.getDesc(), Toast.LENGTH_SHORT);
				}
				else
				{
					ToastCustom.makeText(this, baseResponse.getDesc(), Toast.LENGTH_SHORT);
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
	private GalleryRequest populateGallery()
	{
		String userid = mPref.getString(SessionManager.KEY_USERID, null);
		GalleryRequest galleryRequest = new GalleryRequest();

		galleryRequest.setAction(WebServiceAction.GALLERYINFO);
		if(intent.getBooleanExtra("ManageGroup", false))
		{
			galleryRequest.setGroup_id(groupId);
		}
		//galleryRequest.setGroup_id("");
		galleryRequest.setId(userid);

		return galleryRequest;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
	}
}
