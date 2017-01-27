package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.NoAppCollageRequest;
import com.appunison.appcollage.model.pojo.response.NoAppCollageResponse;
import com.appunison.appcollage.utils.CustomTimer;
import com.appunison.appcollage.utils.DialogFactory;
import com.appunison.appcollage.utils.NotificationCounterUtils;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;
import com.appunison.util.BadgeUtils;
import com.appunison.view.NetworkBaseActivity;


public class InitiateAppCollageActivity extends NetworkBaseActivity implements OnClickListener{

	private String TAG = InitiateAppCollageActivity.class.getName();
	private @InjectView(R.id.layout_appcollage) RelativeLayout mlayoutAppCollage;
	private @InjectView(R.id.txt_initiate_appcollage_timer) TextView mtxtTimer;
	private @InjectView(R.id.txt_initiate_appcollage_header) TextView mtxtGroupHeader;
	private @InjectView(R.id.txt_initiate_appcollage_user) TextView mtxtuser;
	private @InjectView(R.id.txt_initiate_appcollage_group) TextView mtxtGroup;
	private @InjectView(R.id.txt_initiate_appcollage_group_msg) TextView mtxtGroupMsg;
	private @InjectView(R.id.txt) TextView mtxt;
	private @InjectView(R.id.btn_initiate_appcollage_cancel) Button mbtnCancel;

	private @InjectView(R.id.btn_initiate_appcollage_no) Button mbtnNoAppCollage;
	private @InjectView(R.id.btn_initiate_appcollage) Button mbtnAppCollage;

	private String GroupName, RequestID, /*Time, TimeZone, UserID,*/ UserName;
	private CustomTimer customTimer;
	private CustomSharedPreference mPref;
	private long endTime;
	private boolean appcollageRequest;
	private String collage_image, collage_id, Createddate;
	private ForeGroundTask service;
	private String msgid;
	private boolean fromInbox=false;
	private String userId;
	private String msg;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_initiate_appcollage);

		service = new ForeGroundTask(this, this);

		SpannableString spannable1 = new SpannableString(getResources().getString(R.string.initiate_appcollage_msg4));
		spannable1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow)), 16, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		mtxt.setText(spannable1);

		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, InitiateAppCollageActivity.this);
		boolean showingDialog = mPref.getBoolean(AppCollageConstants.SHOWING_DIALOG, false);

		Intent intent = getIntent();
		appcollageRequest = intent.getBooleanExtra("appcollageRequest", false);

		mbtnNoAppCollage.setOnClickListener(this);
		mbtnAppCollage.setOnClickListener(this);
		mbtnCancel.setOnClickListener(this);

		if(appcollageRequest)
		{
			mbtnCancel.setVisibility(View.VISIBLE);
			GroupName = intent.getStringExtra("GroupName");
			//GroupName = GroupName.toUpperCase();
			RequestID = intent.getStringExtra("RequestID");
			msgid = intent.getStringExtra("msgid");
			msg = intent.getStringExtra("message");
			/*Time = intent.getStringExtra("Time");
		TimeZone = intent.getStringExtra("TimeZone");
		UserID = intent.getStringExtra("UserID");*/
			UserName = intent.getStringExtra("InitiatedBy");
			endTime = intent.getLongExtra("endTime", 0);
			userId=intent.getStringExtra("userId");
			Log.d("RequestID",RequestID);
			Log.d("UserName", UserName);
			Log.d("msgid", ":"+msgid);
			mtxtGroupHeader.setText(getResources().getString(R.string.AppCollage_request));
			mtxtuser.setText(UserName + " "+getResources().getString(R.string.initiate_appcollage_msg2_user));
			mtxtGroup.setText("\""+GroupName+"\"");
			mtxtGroupMsg.setText(msg);

			//endTime = SystemClock.uptimeMillis()+5*60*1000;
			customTimer = new CustomTimer(InitiateAppCollageActivity.this, mtxtTimer, endTime);
			customTimer.timerStart();
		}
		else
		{
			GroupName = intent.getStringExtra("GroupName");
			//GroupName = GroupName.toUpperCase();
			//GroupId = intent.getStringExtra("GroupId");
			msg = intent.getStringExtra("message");
			Createddate = intent.getStringExtra("Createddate");
			collage_image = intent.getStringExtra("collage_image");
			collage_id = intent.getStringExtra("collage_id");
			Log.d("Createddate", ": "+Createddate);
			mtxtTimer.setVisibility(View.GONE);
			mtxtuser.setVisibility(View.GONE);
			mtxtGroup.setVisibility(View.GONE);
			mbtnCancel.setVisibility(View.GONE);
			mtxtGroupHeader.setText(getResources().getString(R.string.collage_ready));
			mbtnNoAppCollage.setText(getResources().getString(R.string.cancel));
			mbtnAppCollage.setText(getResources().getString(R.string.view));
			mtxt.setText(msg);
			//mtxt.setText(getResources().getString(R.string.collage_msg)+" "+"\""+GroupName+"\"");
			mbtnAppCollage.setOnClickListener(this);
		}
		fromInbox=intent.getBooleanExtra("fromInbox", false);

	}
	public void intializeProgressBar(Context context) {
		// TODO Auto-generated method stub
		progressbar = new DialogFactory(context).getTransParentDialog();
	}
	public void onClick(View v) {
		
		
		switch (v.getId()) {
		case R.id.btn_initiate_appcollage_cancel:
			if(appcollageRequest)
			{
				checkForValidUser();
				mPref.putBoolean(AppCollageConstants.SHOWING_DIALOG, false);
			}
			
			finish();
			break;
		case R.id.btn_initiate_appcollage_no:
			if(appcollageRequest)
			{
				checkForValidUser();
				service.callpostService(AppCollageConstants.url, populateNoAppCollage(), NoAppCollageResponse.class, ActionType.NOAppCollage.getActionType());
				mPref.putBoolean(AppCollageConstants.SHOWING_DIALOG, false);

			}
			else{
				finish();
			}
			break;
		case R.id.btn_initiate_appcollage:
			if(appcollageRequest)
			{
				checkForValidUser();
				Intent intent = new Intent(InitiateAppCollageActivity.this, MyCameraActivity.class);
				intent.putExtra("width", mlayoutAppCollage.getWidth());
				intent.putExtra("height", mlayoutAppCollage.getHeight());
				intent.putExtra("RequestID", RequestID);
				intent.putExtra("endTime", endTime);
				intent.putExtra("startNotification", AppCollageConstants.AppCollage_REQUEST_NOTIFICATION);
				startActivityForResult(intent, AppCollageConstants.COLLAGE_REQUEST);
			}
			else
			{
				Intent intent = new Intent(InitiateAppCollageActivity.this, ShowCollageActivity.class);
				intent.putExtra("collage", collage_image);
				intent.putExtra("groupName", GroupName);
				intent.putExtra("time", Createddate);
				intent.putExtra("collageNotification", true);
				startActivityForResult(intent, AppCollageConstants.COLLAGE_REQUEST);
				finish();
			}
			break;
		default:
			break;
		}
	}

	private void checkForValidUser()
	{
		SessionManager sessionManager=new SessionManager(AppCollageConstants.PREF_NAME, InitiateAppCollageActivity.this);
		String loggedInUserId=(String)sessionManager.getUserDetails().get(SessionManager.KEY_USERID);
		if((userId!=null)&&(loggedInUserId!=null))
		{
			if(!userId.equals(loggedInUserId))
			{
				ToastCustom.makeText(this, getString(R.string.not_valid_user), Toast.LENGTH_SHORT);
				return;
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(customTimer!=null)
		{
			customTimer.timerStop(false);
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  

		if (requestCode == AppCollageConstants.COLLAGE_REQUEST )
		{
			if(resultCode  == AppCollageConstants.COLLAGE_REQUEST)
			{	
				if(data.getStringExtra("screen").equals("home"))
				{
					mPref.putBoolean(AppCollageConstants.SHOWING_DIALOG, false);
					finish();
				}
				else
				{
					if(fromInbox)
					{
						setResult(AppCollageConstants.AppCollage_REQUEST_FROM_INBOX,getIntent());
					}
				}
			}
			if(resultCode==AppCollageConstants.AppCollage_REQUEST_FROM_INBOX)
			{
				if(fromInbox)
				{
					setResult(AppCollageConstants.AppCollage_REQUEST_FROM_INBOX,getIntent());
				}
				finish();

			}


		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
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
			if(actionType == ActionType.NOAppCollage.getActionType())
			{
				NoAppCollageResponse noAppCollageResponse = (NoAppCollageResponse) response;
				if (noAppCollageResponse.isResp()) {
					ToastCustom.makeText(this, noAppCollageResponse.getDesc(), Toast.LENGTH_SHORT);
					int item_count=
							NotificationCounterUtils.removeAppCollageById(InitiateAppCollageActivity.this, RequestID);
					setResult(AppCollageConstants.AppCollage_REQUEST_FROM_INBOX,getIntent());
					BadgeUtils.setBadge(InitiateAppCollageActivity.this, item_count);
					Intent broadcastIntent = new Intent("kmdaction");
					broadcastIntent.putExtra("count", item_count);
					sendBroadcast(broadcastIntent);
					NotificationManager notificationManager = (NotificationManager) 
							getSystemService(Context.NOTIFICATION_SERVICE);
					notificationManager.cancelAll();

				}
				else{
					ToastCustom.makeText(this, noAppCollageResponse.getDesc(), Toast.LENGTH_SHORT);
				}
				finish();
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
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));

	}
	private NoAppCollageRequest populateNoAppCollage()
	{
		NoAppCollageRequest request = new NoAppCollageRequest();
		String userid = mPref.getString(SessionManager.KEY_USERID, null);

		request.setAction(WebServiceAction.NOAppCollageACTION);
		/*request.setRequest_id(RequestID);
		request.setUser_id(userid);*/
		request.setMsgid(msgid);

		return request;

	}
}
