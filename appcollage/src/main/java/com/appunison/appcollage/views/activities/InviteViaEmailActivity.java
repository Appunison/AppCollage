package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.IdType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.InviteViaContactRequest;
import com.appunison.appcollage.model.pojo.response.GroupResponse;
import com.appunison.appcollage.model.pojo.response.InviteViaContactResponse;
import com.appunison.appcollage.utils.EditTextProperty;
import com.appunison.appcollage.views.adapter.GroupAdapter;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.basewidgets.Validation;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.pojo.TaskResponseWrapper;
import com.appunison.session.SessionManager;
import com.appunison.appcollage.R;
import com.appunison.task.ForeGroundTask;

public class InviteViaEmailActivity extends AppCollageNetworkActivity{

	private String TAG = InviteViaEmailActivity.class.getName();
	private @InjectView(R.id.txt_page_title) TextView mtxtPageTitle;
	private @InjectView(R.id.txt_counter) TextView mtxtCounter;
	private @InjectView(R.id.spinner_invite_via_contact_group) Spinner mspinnerGroup;
	private @InjectView(R.id.edt_invite_via_contact) EditText medtContact;
	private @InjectView(R.id.edt_invite_via_contact_massage) EditText medtInviteMassage;
	private @InjectView(R.id.btn_invite_via_contact_send) Button mbtnSend;
	private @InjectView(R.id.txt_click_here_to_enter_email_address_instead) TextView mtxtClickHereEnterMail;
	private GroupAdapter adapter;
	private GroupResponse groupResponse;
	private String groupId, groupName,groupIdLocal,groupNameLocal;
	private int pos;
	private ForeGroundTask service;
	private AsyncTask<Object, Double, TaskResponseWrapper> inviteViaEmailTask;
	private CustomSharedPreference mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_invite_via_contacts);

		
		mtxtTitle.setText(getResources().getString(R.string._invite));
		mtxtPageTitle.setText(getResources().getString(R.string.via_email_address));
		medtContact.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
		medtContact.setHint(getResources().getString(R.string.enter_email_address));
		mtxtClickHereEnterMail.setVisibility(View.INVISIBLE);
		mbtnSend.setOnClickListener(this);
		medtInviteMassage.setOnClickListener(this);
		
		service = new ForeGroundTask(this, this);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, InviteViaEmailActivity.this);
		String msg = mPref.getString(AppCollageConstants.KEY_INVITE, null);
		
		Bundle bundle = getIntent().getExtras();
		pos =bundle.getInt("pos");
		groupResponse=(GroupResponse) bundle.getSerializable("groupResponse");

		/*medtInviteMassage.setText(getResources().getString(R.string.invite_msg_via_contact)+
				"\""+groupResponse.getResult().get(pos).getGroup_name()+"\""+" "+getResources().getString(R.string.invite_msg_via_contact1));*/

		adapter = new GroupAdapter(InviteViaEmailActivity.this, groupResponse.getResult());
		mspinnerGroup.setAdapter(adapter);
		mspinnerGroup.setSelection(pos);
		mspinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView textView=(TextView)view.findViewById(R.id.txt_group_list_name);
				groupIdLocal = (String) textView.getTag();
				groupNameLocal = (String) textView.getText();
				position = mspinnerGroup.getSelectedItemPosition();
				//ToastCustom.makeText(InviteViaContactsActivity.this, "groupId = "+groupIdLocal+" groupName = "+groupNameLocal+" position = "+position, Toast.LENGTH_SHORT);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		
		if(msg==null || msg.equals(""))
		{
			medtInviteMassage.setText(getResources().getString(R.string.invite_msg_via_contact)+" "+
					"\""+groupResponse.getResult().get(pos).getGroup_name()+"\""+" "+getResources().getString(R.string.invite_msg_via_contact1));
			
		}
		else{
			medtInviteMassage.setText(groupResponse.getResult().get(pos).getGroup_name().toUpperCase()+": "+msg);
		}
		EditTextProperty.disable(medtInviteMassage);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_invite_via_contact_send:
			if(checkValidation())
			{
				inviteViaEmailTask = service.callpostService(AppCollageConstants.url, populateInviteViaEmail(), InviteViaContactResponse.class, ActionType.INVITEVIACONTACT.getActionType());
			}
			break;
			
		case R.id.edt_invite_via_contact_massage:
			EditTextProperty.enable(medtInviteMassage);
			medtInviteMassage.addTextChangedListener(new TextWatcher() 
			{
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				public void onTextChanged(CharSequence s, int start, int before, int count) {
					mtxtCounter.setText(String.valueOf(255-s.length()));
				}

				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
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
					Intent intent = new Intent(InviteViaEmailActivity.this, InviteSentActivity.class);
					intent.putExtra("groupName", groupNameLocal);
					intent.putExtra("userName", medtContact.getText().toString());
					startActivityForResult(intent,2);
				}
				else
				{
					ToastCustom.makeText(InviteViaEmailActivity.this, inviteResponse.getDesc(), Toast.LENGTH_SHORT);
					/*Intent intent = new Intent(InviteViaEmailActivity.this, InviteSentActivity.class);
					intent.putExtra("groupName", groupNameLocal);
					intent.putExtra("userName", medtContact.getText().toString());
					startActivityForResult(intent,2);*/
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode ==2 && resultCode==2)
		{
			setResult(2, data);
			finish();
		}
		else if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
	}
	/**
	 * check validation
	 */
	private boolean checkValidation()
	{
		boolean valid = true;
		if(!Validation.isEmailAddress(medtContact,true))
			valid = false;
		/*if(!Validation.hasText(medtInviteMassage))
			valid = false;*/

		return valid;
	}
	private InviteViaContactRequest populateInviteViaEmail()
	{
		String userid = mPref.getString(SessionManager.KEY_USERID, null);
		InviteViaContactRequest inviteRequest = new InviteViaContactRequest();

		inviteRequest.setAction(WebServiceAction.INVITEGROUPUSER);
		inviteRequest.setGroup_id(groupIdLocal);
		inviteRequest.setMessage(medtInviteMassage.getText().toString());
		inviteRequest.setRequestby(new String[]{medtContact.getText().toString()});
		inviteRequest.setId_type(IdType.Email);
		inviteRequest.setInvitedby(userid);

		return inviteRequest;
	}
}
