package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.dialog.RemoveContactDialog;
import com.appunison.appcollage.model.pojo.request.EmailVerifyRequest;
import com.appunison.appcollage.model.pojo.response.UserVerifyResponse;
import com.appunison.appcollage.views.adapter.GroupMemberAdapter;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;
import com.appunison.appcollage.R;

public class ManageGroupActivity extends AppCollageNetworkActivity
{
	private @InjectView(R.id.layout_manage_group_gallery) LinearLayout mlayoutGallery;
	private @InjectView(R.id.layout_manage_group_member) LinearLayout mlayoutMembers;
	private @InjectView(R.id.layout_manage_group_inbox) LinearLayout mlayoutInbox;
	private @InjectView(R.id.layout_manage_group_invite) LinearLayout mlayoutInvite;
	private @InjectView(R.id.layout_manage_group_initiate) LinearLayout mlayoutInitiate;
	private @InjectView(R.id.layout_manage_group_remove) LinearLayout mlayoutRemove;
	private @InjectView(R.id.txt_manage_groupName)  TextView mtxtGroupName;
	private String groupId, groupName,userId;
	private GroupMemberAdapter groupMemberAdapter;
	private boolean pendingRequest,email_verification;
	private ForeGroundTask service;
	private String emailTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_manage_group);
		mlayoutGallery.setOnClickListener(this);
		mlayoutMembers.setOnClickListener(this);
		mlayoutInbox.setOnClickListener(this);
		mlayoutInvite.setOnClickListener(this);
		mlayoutInitiate.setOnClickListener(this);
		mlayoutRemove.setOnClickListener(this);
		groupId = getIntent().getExtras().getString("groupId");
		groupName = getIntent().getExtras().getString("groupName");
		pendingRequest = getIntent().getBooleanExtra("pendingRequest", false);
		email_verification = getIntent().getBooleanExtra("email_verification", false);
		CustomSharedPreference mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, ManageGroupActivity.this);
		//userid = getIntent().getStringExtra("user_id");
		userId = mPref.getString(SessionManager.KEY_USERID, null);
		service =new ForeGroundTask(this,this);
		mtxtTitle.setText(getResources().getString(R.string.group));
		mtxtGroupName.setText(groupName.toUpperCase());
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.layout_manage_group_gallery:
			//ToastCustom.underDevelopment(ManageGroupActivity.this);
			Intent intent = new Intent(ManageGroupActivity.this, GalleryActivity.class);
			intent.putExtra("groupId", groupId);
			intent.putExtra("groupName", groupName);
			intent.putExtra("ManageGroup", true);
			startActivityForResult(intent,AppCollageConstants.INTENT_HOME);
			break;
		case R.id.layout_manage_group_member:
			//ToastCustom.underDevelopment(ManageGroupActivity.this);
			Intent intentMember = new Intent(ManageGroupActivity.this, GroupMemberActivty.class);
			intentMember.putExtra("groupId", groupId);
			intentMember.putExtra("groupName", groupName);
			startActivityForResult(intentMember,AppCollageConstants.INTENT_HOME);
			break;
		case R.id.layout_manage_group_inbox:
			//ToastCustom.underDevelopment(ManageGroupActivity.this);
			/*Intent intentInbox = new Intent(ManageGroupActivity.this, InboxActivity.class);
			intentInbox.putExtra("groupId", groupId);
			intentInbox.putExtra("groupInbox", true);
			startActivityForResult(intentInbox,AppCollageConstants.INTENT_HOME);*/
			emailTask = "inbox";
			EmailVerifyRequest emailVerifyRequest1=new EmailVerifyRequest();
			emailVerifyRequest1.setAction(ActionType.USERVERIFYGROUPINBOX.getDesc());
			emailVerifyRequest1.setId(userId);
			service.callpostService(AppCollageConstants.url, emailVerifyRequest1, UserVerifyResponse.class, ActionType.USERVERIFYGROUPINBOX.getActionType());
			break;
		case R.id.layout_manage_group_invite:
			emailTask = "invite";
			EmailVerifyRequest emailVerifyRequest=new EmailVerifyRequest();
			emailVerifyRequest.setAction(ActionType.USERVERIFYGROUPINVITE.getDesc());
			emailVerifyRequest.setId(userId);
			service.callpostService(AppCollageConstants.url, emailVerifyRequest, UserVerifyResponse.class, ActionType.USERVERIFYGROUPINVITE.getActionType());
		
			
			break;
		case R.id.layout_manage_group_initiate:
			emailTask = "initiate";
			emailVerifyRequest=new EmailVerifyRequest();
			emailVerifyRequest.setAction(ActionType.USERVERIFYINITIATE.getDesc());
			emailVerifyRequest.setId(userId);
			service.callpostService(AppCollageConstants.url, emailVerifyRequest, UserVerifyResponse.class, ActionType.USERVERIFYINITIATE.getActionType());
		
			
			break;
		case R.id.layout_manage_group_remove:
			//ToastCustom.underDevelopment(ManageGroupActivity.this);
			
			new RemoveContactDialog(ManageGroupActivity.this, getResources().getString(R.string.remove_group), groupId).show();
			
			break;
		}
	}

	public void onUpdate(int actiontype, NetworkEventsEnum networkevent,
			Object request, Object response, Exception excption) {
		// TODO Auto-generated method stub
		super.onUpdate(actiontype, networkevent, request, response, excption);
		
		if(actiontype==ActionType.USERVERIFYGROUPINVITE.getActionType()||actiontype==ActionType.USERVERIFYINITIATE.getActionType()
				||actiontype==ActionType.USERVERIFYGROUPINBOX.getActionType())
		{
			
			 UserVerifyResponse userVerifyResponse=(UserVerifyResponse)response;
			
			if(userVerifyResponse.getResult().getUser_verification())
			{
				if(actiontype==ActionType.USERVERIFYGROUPINVITE.getActionType())
				{
					Intent intentInvite = new Intent(ManageGroupActivity.this, InviteActivity.class);
					intentInvite.putExtra("groupId", groupId);
					intentInvite.putExtra("pendingRequest", pendingRequest);
					intentInvite.putExtra("email_verification", email_verification);
					intentInvite.putExtra("manageflow", true);
					startActivityForResult(intentInvite,AppCollageConstants.INTENT_HOME);
				}
				else if(actiontype==ActionType.USERVERIFYINITIATE.getActionType())
				{
					Intent intentInitiate = new Intent(ManageGroupActivity.this, InitiateActivity.class);
					intentInitiate.putExtra("groupId", groupId);
					intentInitiate.putExtra("manageflow", true);
					intentInitiate.putExtra("email_verification", email_verification);
					intentInitiate.putExtra("startNotification", AppCollageConstants.AppCollage_REQUEST_INICIATE);
					startActivityForResult(intentInitiate, AppCollageConstants.COLLAGE_REQUEST);
			
				}else if(actiontype==ActionType.USERVERIFYGROUPINBOX.getActionType())
				{
					Intent intentInbox = new Intent(ManageGroupActivity.this, InboxActivity.class);
					intentInbox.putExtra("groupId", groupId);
					intentInbox.putExtra("groupInbox", true);
					startActivityForResult(intentInbox,AppCollageConstants.INTENT_HOME);
			
				}
			}
			else
			{
				Intent intentEmailVeri = new Intent(ManageGroupActivity.this, EmailVerificationActivity.class);
				if(emailTask.equals("initiate"))
				{
					intentEmailVeri.putExtra("initiate", "initiate");
				}else if(emailTask.equals("invite"))
				{
					intentEmailVeri.putExtra("invite", "invite");
				}else if(emailTask.equals("inbox"))
				{
					intentEmailVeri.putExtra("inbox", "inbox");
				}
				startActivity(intentEmailVeri);
		
			}
			 
			
		}
		
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
		else if(requestCode == AppCollageConstants.COLLAGE_REQUEST && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
	}
	public void onProgress(int arg0, double arg1, NetworkEventsEnum arg2,
			Object arg3) {
		// TODO Auto-generated method stub
		
	}
	
	

}
