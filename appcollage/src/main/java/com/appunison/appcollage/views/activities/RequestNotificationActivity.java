package com.appunison.appcollage.views.activities;

import java.util.HashSet;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.listener.TaskComplete;
import com.appunison.appcollage.utils.NotificationCounterUtils;
import com.appunison.appcollage.views.adapter.RequestNotificationAdapter;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.util.BadgeUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class RequestNotificationActivity extends RoboActivity implements
		OnClickListener {

	private @InjectView(R.id.txt_home_notification_counter)
	TextView mtxtCounter;
	private @InjectView(R.id.txt_home_noti_no_result_found)
	TextView mtxtNoResultFound;
	private @InjectView(R.id.img_request_noti_close)
	ImageView mimgClose;
	private @InjectView(R.id.list_notification)
	ListView mlistNotification;
	private RequestNotificationAdapter adapter;
	private HashSet<String> notifications;
	private CustomSharedPreference customSharedPreference;
	private TaskComplete taskComplete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_request_notification);
		customSharedPreference = new CustomSharedPreference(
				AppCollageConstants.PREF_NAME, this);
		
		notifications = (HashSet<String>) customSharedPreference.getList(
				"notification", notifications);
		taskComplete = new TaskComplete() {
			
			public void onComplete() {
				// TODO Auto-generated method stub
				notifications = NotificationCounterUtils.removeExpiredData(RequestNotificationActivity.this);
				adapter = new RequestNotificationAdapter(RequestNotificationActivity.this,notifications, taskComplete);
				mlistNotification.setAdapter(adapter);
				int item_count = adapter.getCount();
				mtxtCounter.setText(""+item_count);
				if(item_count==0)
				{
					finish();
				}
				BadgeUtils.setBadge(RequestNotificationActivity.this, item_count);
				Intent broadcastIntent = new Intent("kmdaction");
		        broadcastIntent.putExtra("count", item_count);
		        sendBroadcast(broadcastIntent);
				BadgeUtils.setBadge(RequestNotificationActivity.this, item_count);
			}
		};
		
		if (notifications != null) {
			mlistNotification.setVisibility(View.VISIBLE);
			mtxtNoResultFound.setVisibility(View.GONE);
			notifications = NotificationCounterUtils.removeExpiredData(RequestNotificationActivity.this);
			adapter = new RequestNotificationAdapter(this,notifications, taskComplete);
			
			mlistNotification.setAdapter(adapter);
			int item_count = adapter.getCount();
			mtxtCounter.setText(""+item_count);
			if(item_count==0)
			{
				finish();
			}
			BadgeUtils.setBadge(RequestNotificationActivity.this, item_count);
			Intent broadcastIntent = new Intent("kmdaction");
	        broadcastIntent.putExtra("count", item_count);
	        sendBroadcast(broadcastIntent);
	        BadgeUtils.setBadge(RequestNotificationActivity.this, item_count);
		}
		else{
			mlistNotification.setVisibility(View.GONE);
			mtxtNoResultFound.setVisibility(View.VISIBLE);
			mtxtCounter.setText("0");
		}
		mimgClose.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		RequestNotificationActivity.this.overridePendingTransition(
				R.anim.right_to_left, R.anim.left_to_right);
		super.onResume();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_request_noti_close:
			setResult(88);
			finish();
			break;

		default:
			break;
		}

	}
	
}
