package com.appunison.appcollage.views.activities;

import java.util.HashSet;

import org.springframework.util.StringUtils;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appunison.appcollage.R;
import com.appunison.appcollage.StartScreenActivity;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.EmailVerifyRequest;
import com.appunison.appcollage.model.pojo.request.GroupCodeRequest;
import com.appunison.appcollage.model.pojo.response.GroupCodeResponse;
import com.appunison.appcollage.model.pojo.response.SignupResponse;
import com.appunison.appcollage.model.pojo.response.UserVerifyResponse;
import com.appunison.appcollage.receiver.NotificationCountReceiver;
import com.appunison.appcollage.utils.NotificationCounterUtils;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;

public class HomeActivity extends AppCollageNetworkActivity{

	private String TAG  = HomeActivity.class.getName();
	private @InjectView(R.id.txt_home_notification_counter) TextView mtxtNotiCounter;
	private @InjectView(R.id.layout_home_gallery) LinearLayout mlayoutGallery;
	private @InjectView(R.id.layout_home_groups) LinearLayout mlayoutGroups;
	private @InjectView(R.id.layout_home_inbox) LinearLayout mlayoutInbox;
	private @InjectView(R.id.layout_home_invite) LinearLayout mlayoutInvite;
	private @InjectView(R.id.layout_home_initiate) LinearLayout mlayoutInitiate;
	private @InjectView(R.id.layout_home_settings) LinearLayout mlayoutSettings;
	private @InjectView(R.id.img_home_logout) ImageView mimgLogout;
	private @InjectView(R.id.img_home_back) ImageView mimgBack;
	private @InjectView(R.id.txt_home_Username)  TextView mtxtUserName;
	private @InjectView(R.id.txt_home_not_sure_what_to_do)  TextView mtxtSurity;
	private @InjectView(R.id.txt_home_click_here)  TextView mtxtClickHere;
	private SignupResponse signupResponse;
	private SessionManager sessionManager;
	private ForeGroundTask service;
	private CustomSharedPreference mPref;
	private HashSet<String> notifications;
	private NotificationCountReceiver receiver;
	private boolean hitservice = false;
	private String emailTask = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home);
		
		/*Intent intent =getIntent();
		System.out.println(intent);*/
	
		mlayoutGallery.setOnClickListener(this);
		mlayoutGroups.setOnClickListener(this);
		mlayoutInbox.setOnClickListener(this);
		mlayoutInvite.setOnClickListener(this);
		mlayoutSettings.setOnClickListener(this);
		mlayoutInitiate.setOnClickListener(this);
		mimgLogout.setOnClickListener(this);
		mtxtClickHere.setOnClickListener(this);
		service = new ForeGroundTask(this, this);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, HomeActivity.this);
		
		sessionManager = new SessionManager(AppCollageConstants.PREF_NAME, HomeActivity.this);
		signupResponse = (SignupResponse) getIntent().getSerializableExtra("signupResponse");
		
		mtxtUserName.setText(signupResponse.getResult().getFirst_name().toUpperCase()+ " " + signupResponse.getResult().getLast_name().toUpperCase());
		
		mimgBack.setVisibility(View.INVISIBLE);
		//mtxtUserName.setText(signupResponse.getResult().getFirst_name());
		mtxtSurity.setText(getResources().getString(R.string.not_sure_what_to_do)+ " " 
		+ StringUtils.capitalize(signupResponse.getResult().getFirst_name())
				+" "+StringUtils.capitalize(signupResponse.getResult().getLast_name())
				+ "?");
		
		//set notification counter
		
		setCountData();
		
		mtxtNotiCounter.setOnClickListener(this);
		receiver = new NotificationCountReceiver(mtxtNotiCounter); // Create the receiver
        registerReceiver(receiver, new IntentFilter("kmdaction")); // Reg
        checkForGroupInvitation();
	}
	/**
	 * for staus of group inviation
	 */
	
	private void checkForGroupInvitation()
	{
		if(getIntent().getData()!=null)
		{
			String url=getIntent().getData().toString();
			String code=url.split("=")[1];
			String userid = mPref.getString(SessionManager.KEY_USERID, null);
			GroupCodeRequest groupCodeRequest = new GroupCodeRequest();
			
			groupCodeRequest.setAction(WebServiceAction.GROUPCODEACTION);
			groupCodeRequest.setId(userid);
			groupCodeRequest.setUnique_key(code);
			//TOdo hit the Web
			service.callpostService(AppCollageConstants.url, groupCodeRequest, GroupCodeResponse.class, ActionType.GROUPCODE.getActionType());
		}
		/*else if(signupResponse.getResult().isPendingRequest())
		{
			Intent intentGroupCode = new Intent(this, SendGroupCodeActivity.class);
			intentGroupCode.putExtra("enter_time", true);
			intentGroupCode.putExtra("signupResponse", signupResponse);
			startActivity(intentGroupCode);
		}*/
	}
	
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.txt_home_notification_counter:
			//ToastCustom.underDevelopment(this);
			Intent intentNoti = new Intent(this, RequestNotificationActivity.class);
			startActivityForResult(intentNoti,AppCollageConstants.INTENT_HOME);
			//mtxtNotiCounter.setVisibility(View.INVISIBLE);
			break;
		
		case R.id.layout_home_gallery:
			
			//ToastCustom.underDevelopment(HomeActivity.this);
			Intent intentGallery = new Intent(HomeActivity.this, GalleryActivity.class);
			startActivityForResult(intentGallery,AppCollageConstants.INTENT_HOME);
			break;

		case R.id.layout_home_groups:
			
			//ToastCustom.underDevelopment(HomeActivity.this);
			Intent intentGroups = new Intent(HomeActivity.this, CreateGroupActivity.class);
			intentGroups.putExtra("email_verification", signupResponse.getResult().isEmail_verification());
			if(hitservice)
			{
				intentGroups.putExtra("pendingRequest", false);
			}else{
				intentGroups.putExtra("pendingRequest", signupResponse.getResult().isPendingRequest());
			}
			startActivityForResult(intentGroups,AppCollageConstants.INTENT_HOME);
			break;
		
		case R.id.layout_home_inbox:
			
			//ToastCustom.underDevelopment(HomeActivity.this);
//			Intent intentInbox = new Intent(HomeActivity.this, InboxActivity.class);
//			startActivityForResult(intentInbox,AppCollageConstants.INTENT_HOME);
			emailTask = "inbox";
			EmailVerifyRequest emailVerifyRequest1=new EmailVerifyRequest();
			emailVerifyRequest1.setAction(ActionType.USERVERIFYGROUPINBOX.getDesc());
			emailVerifyRequest1.setId(signupResponse.getResult().getId());
			service.callpostService(AppCollageConstants.url, emailVerifyRequest1, UserVerifyResponse.class, ActionType.USERVERIFYGROUPINBOX.getActionType());
			break;
		
		case R.id.layout_home_invite:
	
			emailTask = "invite";
			EmailVerifyRequest emailVerifyRequest=new EmailVerifyRequest();
			emailVerifyRequest.setAction(ActionType.USERVERIFYGROUPINVITE.getDesc());
			emailVerifyRequest.setId(signupResponse.getResult().getId());
			service.callpostService(AppCollageConstants.url, emailVerifyRequest, UserVerifyResponse.class, ActionType.USERVERIFYGROUPINVITE.getActionType());
			break;
		
		case R.id.layout_home_settings:
			
			//ToastCustom.underDevelopment(HomeActivity.this);
			Intent intentSetting = new Intent(HomeActivity.this, SettingActivity.class);
			intentSetting.putExtra("signupResponse", signupResponse);
			startActivityForResult(intentSetting, AppCollageConstants.INTENT_HOME);
			break;
		
		case R.id.img_home_logout:
	
			//ToastCustom.underDevelopment(HomeActivity.this);
			//sessionManager.logoutUser();
			//service.callpostService(AppCollageConstants.url, populateLogout(), LogoutResponse.class, ActionType.LOGOUT.getActionType());
			Intent intentLogout = new Intent(HomeActivity.this, LogoutActivty.class);
			intentLogout.putExtra("signupResponse", signupResponse);
			startActivityForResult(intentLogout, AppCollageConstants.INTENT_HOME);
			break;
			
		case R.id.layout_home_initiate:
			emailTask = "initiate";
			emailVerifyRequest=new EmailVerifyRequest();
			emailVerifyRequest.setAction(ActionType.USERVERIFYINITIATE.getDesc());
			emailVerifyRequest.setId(signupResponse.getResult().getId());
			service.callpostService(AppCollageConstants.url, emailVerifyRequest, UserVerifyResponse.class, ActionType.USERVERIFYINITIATE.getActionType());
			break;
			
		case R.id.txt_home_click_here:
			Intent intentClick = new Intent(HomeActivity.this, TutorialActivity.class);
			intentClick.putExtra("hidebutton", true);
			startActivity(intentClick);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppCollageConstants.INTENT_HOME&& resultCode==AppCollageConstants.INTENT_HOME)
		{
			if(data.getStringExtra("screen").equals("start"))
			{
			finish();
			Intent intent = new Intent(HomeActivity.this, StartScreenActivity.class);
			startActivity(intent);
			}
			else{
				
			}
			
		}
		else if(requestCode == AppCollageConstants.INTENT_HOME && resultCode==AppCollageConstants.SETTING_LOGOUT)
		{
			finish();
			Intent intent = new Intent(HomeActivity.this, StartScreenActivity.class);
			startActivity(intent);
		}
		else if (requestCode == AppCollageConstants.COLLAGE_REQUEST && resultCode  == AppCollageConstants.COLLAGE_REQUEST) {
		
		}
		else if(resultCode == AppCollageConstants.FINAL_HOME)
		{
			if(data.getStringExtra("screen").equals("finalHome"))
			{
				
			}
		}else if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == 97)
		{
			setResult(97,data);
			signupResponse=(SignupResponse) data.getSerializableExtra("profileResponse");
			mtxtUserName.setText(signupResponse.getResult().getFirst_name().toUpperCase()+ " " + signupResponse.getResult().getLast_name().toUpperCase());
			mtxtSurity.setText(getResources().getString(R.string.not_sure_what_to_do)+ " " 
					+ StringUtils.capitalize(signupResponse.getResult().getFirst_name())
							+" "+StringUtils.capitalize(signupResponse.getResult().getLast_name())
							+ "?");
		}
		else if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == 88)
		{
			   //mtxtNotiCounter.setVisibility(View.VISIBLE);
		}
		else if(resultCode == 11)
		{
			if(data.getStringExtra("screen").equals("account"))
			{
				//ToastCustom.makeText(HomeActivity.this, "account", Toast.LENGTH_SHORT);
				
			}else if(data.getStringExtra("screen").equals("account1"))
			{
				signupResponse=(SignupResponse) data.getSerializableExtra("profileResponse");
				mtxtUserName.setText(signupResponse.getResult().getFirst_name().toUpperCase()+ " " + signupResponse.getResult().getLast_name().toUpperCase());
				mtxtSurity.setText(getResources().getString(R.string.not_sure_what_to_do)+ " " 
						+ StringUtils.capitalize(signupResponse.getResult().getFirst_name())
								+" "+StringUtils.capitalize(signupResponse.getResult().getLast_name())
								+ "?");
			}
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
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
			if (actiontype == ActionType.GROUPCODE.getActionType()) {
				GroupCodeResponse groupCodeResponse = (GroupCodeResponse) response;
				if (groupCodeResponse.isResp()) {
					ToastCustom.makeText(this, groupCodeResponse.getDesc(), Toast.LENGTH_SHORT);
					//pendingRequest = false;
					hitservice = true;
				}
				else{
					ToastCustom.makeText(this, groupCodeResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			else if(actiontype==ActionType.USERVERIFYGROUPINVITE.getActionType()||actiontype==ActionType.USERVERIFYINITIATE.getActionType()
					||actiontype==ActionType.USERVERIFYGROUPINBOX.getActionType())
			{
				
				 UserVerifyResponse userVerifyResponse=(UserVerifyResponse)response;
				
				if(userVerifyResponse.getResult().getUser_verification())
				{
					if(actiontype==ActionType.USERVERIFYGROUPINVITE.getActionType())
					{
						Intent intentInvite = new Intent(HomeActivity.this, InviteActivity.class);
						intentInvite.putExtra("email_verification", signupResponse.getResult().isEmail_verification());
						if(hitservice)
						{
							intentInvite.putExtra("pendingRequest", false);
						}else{
						intentInvite.putExtra("pendingRequest", signupResponse.getResult().isPendingRequest());
						}
						startActivityForResult(intentInvite, AppCollageConstants.INTENT_HOME);
					}
					else if(actiontype==ActionType.USERVERIFYINITIATE.getActionType())
					{
						//ToastCustom.underDevelopment(HomeActivity.this);
							Intent intentInitiate = new Intent(HomeActivity.this, InitiateActivity.class);
							intentInitiate.putExtra("startNotification", AppCollageConstants.AppCollage_REQUEST_INICIATE);
							startActivityForResult(intentInitiate, AppCollageConstants.COLLAGE_REQUEST);
					}
					else if(actiontype==ActionType.USERVERIFYGROUPINBOX.getActionType())
					{
						//ToastCustom.underDevelopment(HomeActivity.this);
						Intent intentInbox = new Intent(HomeActivity.this, InboxActivity.class);
						startActivityForResult(intentInbox,AppCollageConstants.INTENT_HOME);
					}
				}
				else
				{
					Intent intentEmailVeri = new Intent(HomeActivity.this, EmailVerificationActivity.class);
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
			
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actiontype));
		
	}

	public void onpreExecute(int actionType,
			NetworkEventsEnum networkEventsEnum, Object request) {
		// TODO Auto-generated method stub
		super.onpreExecute(actionType, networkEventsEnum, request);
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		setCountData();
	}
	private void setCountData()
	{
		notifications = NotificationCounterUtils.removeExpiredData(HomeActivity.this);
		if(notifications!=null)
		{
			int count  = notifications.size();
			if(count>0)
			{
				mtxtNotiCounter.setText(""+count);
			}else{
				mtxtNotiCounter.setVisibility(View.GONE);
			}
			
		}else{
			mtxtNotiCounter.setVisibility(View.GONE);
			//mtxtNotiCounter.setText("0");
		}
	}


	
}
