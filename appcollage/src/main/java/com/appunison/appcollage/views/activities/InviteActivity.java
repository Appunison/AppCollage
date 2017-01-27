package com.appunison.appcollage.views.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import roboguice.inject.InjectView;
import twitter4j.User;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.model.GraphUser;
import com.appunison.appcollage.AppCollageApplication;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.IdType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.dialog.FetchContactDialog;
import com.appunison.appcollage.listener.ITwitter;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.GroupListRequest;
import com.appunison.appcollage.model.pojo.request.InviteViaContactRequest;
import com.appunison.appcollage.model.pojo.response.GroupResponse;
import com.appunison.appcollage.model.pojo.response.InviteViaContactResponse;
import com.appunison.appcollage.model.pojo.response.SignupResponse;
import com.appunison.appcollage.utils.LoginTwitter;
import com.appunison.appcollage.views.adapter.GroupAdapter;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.appcollage.R;
import com.appunison.task.ForeGroundTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InviteActivity extends AppCollageNetworkActivity implements ITwitter{

	private String TAG = InviteActivity.class.getName();
	private @InjectView(R.id.btn_invite_enter_group_code) Button mbtnGroupCode;
	private @InjectView(R.id.txt_title) TextView mtxtTitle;
	private @InjectView(R.id.layout_option) LinearLayout mlayoutOption;
	private @InjectView(R.id.spinner_invite_group) Spinner mSpinnerInviteGroup;
	private @InjectView(R.id.layout_invite_contact) LinearLayout mlayoutContact;
	private @InjectView(R.id.layout_invite_email) LinearLayout mlayoutEmail;
	private @InjectView(R.id.layout_invite_sms) LinearLayout mlayoutSMS;
	private @InjectView(R.id.layout_invite_appcollage_id) LinearLayout mlayoutAppCollageId;
	private @InjectView(R.id.img_invite_facebook) ImageView mimgFacebook;
	private @InjectView(R.id.img_invite_twitter) ImageView mimgTwitter;
	private @InjectView(R.id.img_back) ImageView mimgBack;
	private ForeGroundTask service;
	private GroupAdapter adapter;
	private String userid;
	private CustomSharedPreference mPref;
	private GroupResponse groupResponse;
	private int pos;
	private Bundle bundle;
	private Intent intent;
	private String groupId;
	private boolean pendingRequest;
	private SignupResponse signupResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_invite);

		intent = getIntent();
		if(intent.getBooleanExtra("manageflow", false))
		{
			groupId = intent.getStringExtra("groupId");
			pendingRequest = intent.getBooleanExtra("pendingRequest", false);
			if(pendingRequest)
			{
				mbtnGroupCode.setVisibility(View.VISIBLE);
			}else{
				mbtnGroupCode.setVisibility(View.GONE);
			}
		}
		if(intent.getBooleanExtra("pendingRequest", false))
		{
			mbtnGroupCode.setVisibility(View.VISIBLE);
		}
		
		
		mtxtTitle.setText(getResources().getString(R.string._invite));
		mimgBack.setImageResource(R.drawable.ic_action_back);
		
		service=new ForeGroundTask(this, this);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, InviteActivity.this);
		userid = mPref.getString(SessionManager.KEY_USERID, null);
		service.callpostService(AppCollageConstants.url, populateCreatedGroupList(), GroupResponse.class, ActionType.GROUPLIST.getActionType());

		mlayoutOption.setOnClickListener(this);
		mlayoutContact.setOnClickListener(this);
		mlayoutEmail.setOnClickListener(this);
		mlayoutSMS.setOnClickListener(this);
		mlayoutAppCollageId.setOnClickListener(this);
		mimgFacebook.setOnClickListener(this);
		mimgTwitter.setOnClickListener(this);
		mbtnGroupCode.setOnClickListener(this);
		
		bundle = new Bundle();
		/**
		 * select item from spinner
		 */

		mSpinnerInviteGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView textView=(TextView)view.findViewById(R.id.txt_group_list_name);
				//groupId = (String) textView.getTag();
				//groupName = (String) textView.getText();
				position = mSpinnerInviteGroup.getSelectedItemPosition();
				pos = position;
				//ToastCustom.makeText(InviteActivity.this, "groupId = "+groupId+" groupName = "+groupName+" position = "+position, Toast.LENGTH_SHORT);
				
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
        lifecycleHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChanged(session, state, exception);
            }
        });
        lifecycleHelper.onCreate(savedInstanceState);
	}

	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_invite_enter_group_code:
			Intent intentCode = new Intent(this, SendGroupCodeActivity.class);
			intentCode.putExtra("invite", true);
			startActivity(intentCode);
			break;
		case R.id.layout_invite_contact:
			//ToastCustom.underDevelopment(InviteActivity.this);
			if(groupResponse.getResult().size()>0)
			{
			Intent intentContact = new Intent(InviteActivity.this, ContactOptionActvity.class);
			bundle.putInt("pos", pos);
			bundle.putSerializable("groupResponse", groupResponse);
			intentContact.putExtras(bundle);
			startActivityForResult(intentContact, AppCollageConstants.INTENT_HOME);
			}
			else{
				ToastCustom.makeText(InviteActivity.this, getResources().getString(R.string.group_not_exist), Toast.LENGTH_SHORT);
			}
			break;

		case R.id.layout_invite_email:
			if(groupResponse.getResult().size()>0)
			{
			Intent intenEmail = new Intent(InviteActivity.this, InviteViaEmailActivity.class);
			bundle.putInt("pos", pos);
			bundle.putSerializable("groupResponse", groupResponse);
			intenEmail.putExtras(bundle);
			startActivityForResult(intenEmail, AppCollageConstants.INTENT_HOME);
			}
			else{
				ToastCustom.makeText(InviteActivity.this, getResources().getString(R.string.group_not_exist), Toast.LENGTH_SHORT);
			}
			break;

		case R.id.layout_invite_sms:
			//ToastCustom.underDevelopment(InviteActivity.this);
			if(groupResponse.getResult().size()>0)
			{
				//NativeLauncherUtil.sendSms(InviteActivity.this,getResources().getString(R.string.default_text_sms));
				Intent intentSms = new Intent(InviteActivity.this, InviteViaSmsActivity.class);
				bundle.putInt("pos", pos);
				bundle.putSerializable("groupResponse", groupResponse);
				intentSms.putExtras(bundle);
				startActivityForResult(intentSms, AppCollageConstants.INTENT_HOME);
			}
			else{
				ToastCustom.makeText(InviteActivity.this, getResources().getString(R.string.group_not_exist), Toast.LENGTH_SHORT);
			}
			break;

		case R.id.layout_invite_appcollage_id:
			
			//ToastCustom.underDevelopment(InviteActivity.this);
			if(groupResponse.getResult().size()>0)
			{
			Intent intentAppCollage = new Intent(InviteActivity.this, InviteViaAppCollageId.class);
			bundle.putInt("pos", pos);
			bundle.putSerializable("groupResponse", groupResponse);
			intentAppCollage.putExtras(bundle);
			startActivityForResult(intentAppCollage, AppCollageConstants.INTENT_HOME);
			}
			else{
			ToastCustom.makeText(InviteActivity.this, getResources().getString(R.string.group_not_exist), Toast.LENGTH_SHORT);
			}
			break;

		case R.id.img_invite_facebook:
			
			if(groupResponse.getResult().size()>0)
			{
				startPickFriendsActivity();
			}
			else{
				ToastCustom.makeText(InviteActivity.this, getResources().getString(R.string.group_not_exist), Toast.LENGTH_SHORT);
			}
			break;


		case R.id.img_invite_twitter:
			
			if(groupResponse.getResult().size()>0)
			{
				new LoginTwitter(this, this);
			}
			else{
				ToastCustom.makeText(InviteActivity.this, getResources().getString(R.string.group_not_exist), Toast.LENGTH_SHORT);
			}
			break;
		}

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
			if(actiontype==ActionType.GROUPLIST.getActionType())
			{
				groupResponse=(GroupResponse)response;
				if(groupResponse.isResp())
				{
						adapter = new GroupAdapter(InviteActivity.this, groupResponse.getResult());
						mSpinnerInviteGroup.setAdapter(adapter);
						if(intent.getBooleanExtra("manageflow", false))
						{
							mSpinnerInviteGroup.setSelection(getIndex(groupId, groupResponse));
						}
				}
				else
				{
					ToastCustom.makeText(InviteActivity.this, groupResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			break;
			}
			else if(actiontype==ActionType.INVITEVIACONTACT.getActionType())
			{
				InviteViaContactResponse inviteViaContactResponse = (InviteViaContactResponse)response;
				if(inviteViaContactResponse.isResp())
				{
					ToastCustom.makeText(InviteActivity.this, getString(R.string.invitation_sent_msg), Toast.LENGTH_SHORT);
				}
				else
				{
					ToastCustom.makeText(InviteActivity.this, groupResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
		default:
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
	 * show group which is already created
	 * @return  group list
	 */
	
	private GroupListRequest populateCreatedGroupList()
	{
		GroupListRequest groupListRequest = new GroupListRequest();
		groupListRequest.setAction(WebServiceAction.GROUPLISTACTION);
		groupListRequest.setId(userid);
		//groupListRequest.setPage_no(pageNo);

		return groupListRequest;

	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == AppCollageConstants.INTENT_HOME)
		{
			if(data.getStringExtra("screen").equals("home"))
			{
				setResult(AppCollageConstants.INTENT_HOME, data);
				finish();				
			}
			if(data.getStringExtra("screen").equals("fetch_invite_contact"))
			{
				new FetchContactDialog(InviteActivity.this, getResources().getString(R.string.fetch_contact_msg),data.getExtras()).show();
			}
		}
		else if(requestCode == 1)
		{
		
			  if (resultCode == Activity.RESULT_OK) {

				  String cNumber = null, cEmail = null, cname=null;
				     Uri contactData = data.getData();
				     Cursor c =  managedQuery(contactData, null, null, null, null);
				     if (c.moveToFirst()) {
				    	 
				         String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
				         String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				           if (hasPhone.equalsIgnoreCase("1")) {
				          Cursor phones = getContentResolver().query( 
				                       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
				                       ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, 
				                       null, null);
				             if(phones.moveToFirst())
				             {
				             cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				             }
				           }
				           

					          Cursor email = getContentResolver().query( 
					                       ContactsContract.CommonDataKinds.Email.CONTENT_URI,null, 
					                       ContactsContract.CommonDataKinds.Email.CONTACT_ID +" = "+ id, 
					                       null, null);
					             if(email.moveToFirst())
					             {
					             cEmail = email.getString(email.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					             }
					           
				         cname = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				         
			             /*System.out.println("Name is:"+name);
			             System.out.println("Number is:"+cNumber);
			             System.out.println("Email is:"+cEmail);*/
			            // ToastCustom.makeText(InviteActivity.this, "name = "+cname+" number = "+cNumber+" Email = "+cEmail, Toast.LENGTH_SHORT);
			             if(cNumber == null && cEmail == null)
			             {
			            	 ToastCustom.makeText(InviteActivity.this, getResources().getString(R.string.fetch_contact_msg_for_empty), Toast.LENGTH_LONG);
			             }
			             else{
			            	 Intent intent = new Intent(InviteActivity.this,InviteAfterFetchContactsActivity.class);
			   	    	  intent.putExtra("contactName", cname);
			   	    	  intent.putExtra("contactNumber", cNumber);
			   	    	  intent.putExtra("contactEmail",cEmail);
			   	    	  intent.putExtras(bundle);
			   	    	  startActivityForResult(intent,AppCollageConstants.INTENT_HOME);
			             }
				     }
				   }
		}
		else if(requestCode == PICK_FRIENDS_ACTIVITY)
		{
			displaySelectedFriends(resultCode);
		}
		else if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
		else if(requestCode == 64206)
		{
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		}
	}
	
	private static final List<String> PERMISSIONS = new ArrayList<String>() {
        {
            add("user_friends");
            add("public_profile");
        }
    };
    private static final int PICK_FRIENDS_ACTIVITY = 107;
    private UiLifecycleHelper lifecycleHelper;
    boolean pickFriendsWhenSessionOpened;

    @Override
    protected void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
       // AppEventsLogger.activateApp(this);
    }

    private boolean ensureOpenSession() {
        if (Session.getActiveSession() == null ||
                !Session.getActiveSession().isOpened()) {
            Session.openActiveSession(
                    this, 
                    true, 
                    PERMISSIONS,
                    new Session.StatusCallback() {
                        public void call(Session session, SessionState state, Exception exception) {
                            onSessionStateChanged(session, state, exception);
                        }
                    });
            return false;
        }
        return true;
    }
    
    private boolean sessionHasNecessaryPerms(Session session) {
        if (session != null && session.getPermissions() != null) {
            for (String requestedPerm : PERMISSIONS) {
                if (!session.getPermissions().contains(requestedPerm)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private List<String> getMissingPermissions(Session session) {
        List<String> missingPerms = new ArrayList<String>(PERMISSIONS);
        if (session != null && session.getPermissions() != null) {
            for (String requestedPerm : PERMISSIONS) {
                if (session.getPermissions().contains(requestedPerm)) {
                    missingPerms.remove(requestedPerm);
                }
            }
        }
        return missingPerms;
    }

    private void onSessionStateChanged(final Session session, SessionState state, Exception exception) {
        if (state.isOpened() && !sessionHasNecessaryPerms(session)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.need_perms_alert_text);
            builder.setPositiveButton(
                    R.string.need_perms_alert_button_ok, 
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            session.requestNewReadPermissions(
                                    new NewPermissionsRequest(InviteActivity.this, getMissingPermissions(session)));
                        }
                    });
            builder.setNegativeButton(
                    R.string.need_perms_alert_button_quit,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.show();
        } else if (pickFriendsWhenSessionOpened && state.isOpened()) {
            pickFriendsWhenSessionOpened = false;

            startPickFriendsActivity();
        }
    }

    private void displaySelectedFriends(int resultCode) {
        AppCollageApplication application = (AppCollageApplication) getApplication();

        Collection<GraphUser> selection = application.getSelectedUsers();
        ArrayList<String> ids = new ArrayList<String>();

        if (selection != null && selection.size() > 0) {
            for (GraphUser user : selection) {
            	ids.add(user.getId());
            }
            service.callpostService(AppCollageConstants.url, populateInviteViacontact(ids), InviteViaContactResponse.class, ActionType.INVITEVIACONTACT.getActionType());
        } 
        else
        {
        	ToastCustom.makeText(InviteActivity.this, getString(R.string.no_friend_selected), Toast.LENGTH_SHORT);
        }
    }
    

      private void startPickFriendsActivity() {
        if (ensureOpenSession()) {
            Intent intent = new Intent(this, PickFriendsActivity.class);
            PickFriendsActivity.populateParameters(intent, null, true, true);
            startActivityForResult(intent, PICK_FRIENDS_ACTIVITY);
        } else {
            pickFriendsWhenSessionOpened = true;
        }
    }
      
      private InviteViaContactRequest populateInviteViacontact(List<String> ids)
  	{
    	  String[] idsArr = new String[ids.size()];
    	  idsArr = ids.toArray(idsArr);
    	  
  		String userid = mPref.getString(SessionManager.KEY_USERID, null);
  		InviteViaContactRequest inviteRequest = new InviteViaContactRequest();
  		 
  		inviteRequest.setAction(WebServiceAction.INVITEGROUPUSER);
  		String groupId = groupResponse.getResult().get(mSpinnerInviteGroup.getSelectedItemPosition()).getGroup_id();
  		inviteRequest.setGroup_id(groupId);
  		inviteRequest.setMessage("");
  			inviteRequest.setRequestby(idsArr);
  			inviteRequest.setId_type(IdType.FB);
  		inviteRequest.setInvitedby(userid);
  		
  		return inviteRequest;
  	}

      
	public void authorized(User user) {
	
		String groupId=groupResponse.getResult().get(mSpinnerInviteGroup.getSelectedItemPosition()).getGroup_id();
		Intent intent = new Intent(this,TwitterFriendsActivity.class);
		intent.putExtra("groupid", groupId);
		this.startActivity(intent);

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
