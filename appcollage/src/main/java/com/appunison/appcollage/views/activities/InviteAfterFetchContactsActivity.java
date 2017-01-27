package com.appunison.appcollage.views.activities;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.IdType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.InviteViaContactRequest;
import com.appunison.appcollage.model.pojo.response.GroupResponse;
import com.appunison.appcollage.model.pojo.response.Invite;
import com.appunison.appcollage.model.pojo.response.InviteViaContactResponse;
import com.appunison.appcollage.utils.EditTextProperty;
import com.appunison.appcollage.utils.SendSms;
import com.appunison.appcollage.views.adapter.GroupAdapter;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;

public class InviteAfterFetchContactsActivity extends AppCollageNetworkActivity {

	private String TAG = InviteAfterFetchContactsActivity.class.getName();
	private @InjectView(R.id.txt_page_title)
	TextView mtxtPageTitle;
	private @InjectView(R.id.txt_counter)
	TextView mtxtCounter;
	private @InjectView(R.id.txt_click_here_to_enter_email_address_instead)
	TextView mtxtSetUserName;
	private @InjectView(R.id.edt_invite_via_contact)
	EditText medtContact;
	private @InjectView(R.id.edt_invite_via_contact_massage)
	EditText medtInviteMassage;
	private @InjectView(R.id.spinner_invite_via_contact_group)
	Spinner mspinnerGroup;
	private @InjectView(R.id.btn_invite_via_contact_send)
	Button mbtnSend;
	private String contactName, contactNumber, contactEmail;
	private String groupIdLocal, groupNameLocal;
	private int pos;
	private GroupResponse groupResponse;
	private GroupAdapter adapter;
	private Bundle bundle;
	private ForeGroundTask service;
	private CustomSharedPreference mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_invite_via_contacts);

		service = new ForeGroundTask(this, this);
		bundle = getIntent().getExtras();
		contactName = getIntent().getExtras().getString("contactName");
		contactNumber = getIntent().getExtras().getString("contactNumber");
		contactEmail = getIntent().getExtras().getString("contactEmail");
		pos = bundle.getInt("pos");
		groupResponse = (GroupResponse) bundle.getSerializable("groupResponse");
		/*
		 * medtInviteMassage.setText(getResources().getString(R.string.
		 * invite_msg_via_contact)+
		 * "\""+groupResponse.getResult().get(pos).getGroup_name
		 * ()+"\""+" "+getResources
		 * ().getString(R.string.invite_msg_via_contact1));
		 */

		mtxtTitle.setText(getResources().getString(R.string._invite));
		medtContact.setVisibility(View.GONE);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME,
				InviteAfterFetchContactsActivity.this);

		if (StringUtils.hasText(mPref.getString(AppCollageConstants.KEY_INVITE, null)))
		 {
			medtInviteMassage.setText(groupResponse.getResult().get(pos).getGroup_name().toUpperCase()+": "+mPref.getString(
					AppCollageConstants.KEY_INVITE, null));
		} else {
			medtInviteMassage.setText(getResources().getString(
					R.string.invite_msg_via_contact)+" "
					+ "\""
					+ groupResponse.getResult().get(pos).getGroup_name()
					+ "\""
					+ " "
					+ getResources()
					.getString(R.string.invite_msg_via_contact1));
		}

		mtxtSetUserName.setText("to " + contactName);
		mtxtPageTitle.setText(getResources().getString(R.string.via_contacts));
		mbtnSend.setOnClickListener(this);
		medtInviteMassage.setOnClickListener(this);

		adapter = new GroupAdapter(InviteAfterFetchContactsActivity.this,
				groupResponse.getResult());
		mspinnerGroup.setAdapter(adapter);
		mspinnerGroup.setSelection(pos);
		mspinnerGroup
		.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				TextView textView = (TextView) view
						.findViewById(R.id.txt_group_list_name);
				groupIdLocal = (String) textView.getTag();
				groupNameLocal = (String) textView.getText();
				position = mspinnerGroup.getSelectedItemPosition();
				// ToastCustom.makeText(InviteViaContactsActivity.this,
				// "groupId = "+groupIdLocal+" groupName = "+groupNameLocal+" position = "+position,
				// Toast.LENGTH_SHORT);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		EditTextProperty.disable(medtInviteMassage);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_invite_via_contact_send:
			service.callpostService(AppCollageConstants.url,
					populateInviteViacontact(), InviteViaContactResponse.class,
					ActionType.INVITEVIACONTACT.getActionType());
			break;
		case R.id.edt_invite_via_contact_massage:
			EditTextProperty.enable(medtInviteMassage);
			medtInviteMassage.addTextChangedListener(new TextWatcher() {
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					mtxtCounter.setText(String.valueOf(255 - s.length()));
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
		AppCollageLogger.d(
				TAG,
				"Web service on Progress"
						+ ActionType.getDescriptionFromActionType(actiontype));
	}

	public void onUpdate(int actionType, NetworkEventsEnum networkEvent,
			Object request, Object response, Exception excption) {
		// TODO Auto-generated method stub
		super.onUpdate(actionType, networkEvent, request, response, excption);

		switch (networkEvent) {
		case SUCCESS:
			if (actionType == ActionType.INVITEVIACONTACT.getActionType()) {
				InviteViaContactResponse inviteResponse = (InviteViaContactResponse) response;
				if(inviteResponse.isResp())
				{
					if (!CollectionUtils.isEmpty(inviteResponse.getResult())) {

						Invite invite = inviteResponse.getResult().get(0);
						String url = invite.getUrl();
						// write code to open sms composer
						if (invite.getId_type().equals(IdType.PH)
								&& StringUtils.hasText(url)) {
							// String message =
							// medtInviteMassage.getText().toString();
							SendSms.sendSMS(InviteAfterFetchContactsActivity.this,
									inviteResponse.getResult().get(0)
									.getRequestby(),
									groupNameLocal.toUpperCase() + ": "
											+ AppCollageConstants.AppCollage_INVITE_MSG
											+ "\n" + url);
						}
						Intent intent = new Intent(
								InviteAfterFetchContactsActivity.this,
								InviteSentActivity.class);
						intent.putExtra("groupName", groupNameLocal);
						intent.putExtra("userName", inviteResponse.getResult().get(0)
								.getRequestby());
						startActivityForResult(intent, 2);

					} }else {
						ToastCustom.makeText(this, inviteResponse.getDesc()
								.toString(), Toast.LENGTH_SHORT);
					}
			}
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate" + response + ","
				+ ActionType.getDescriptionFromActionType(actionType));
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
		AppCollageLogger.d(
				TAG,
				"Web service Start" + request + ","
						+ ActionType.getDescriptionFromActionType(actionType));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == 2) {
			setResult(2, data);
			finish();
		} else if (requestCode == AppCollageConstants.FINAL_HOME
				&& resultCode == AppCollageConstants.FINAL_HOME) {
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
	}

	private InviteViaContactRequest populateInviteViacontact() {
		String userid = mPref.getString(SessionManager.KEY_USERID, null);
		InviteViaContactRequest inviteRequest = new InviteViaContactRequest();

		inviteRequest.setAction(WebServiceAction.INVITEGROUPUSER);
		inviteRequest.setGroup_id(groupIdLocal);
		inviteRequest.setMessage(medtInviteMassage.getText().toString());
		if (contactEmail != null) {
			;
			inviteRequest.setRequestby(new String[] { contactEmail });
			inviteRequest.setId_type(IdType.Email);
		} else {
			inviteRequest.setRequestby(new String[] { contactNumber });
			inviteRequest.setId_type(IdType.PH);
		}
		inviteRequest.setInvitedby(userid);

		return inviteRequest;
	}
}
