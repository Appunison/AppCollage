package com.appunison.appcollage.views.adapter;

import java.util.Date;
import java.util.HashSet;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.listener.TaskComplete;
import com.appunison.appcollage.utils.CustomTimer;
import com.appunison.appcollage.views.activities.InitiateAppCollageActivity;
import com.appunison.persistence.CustomSharedPreference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RequestNotificationAdapter extends BaseAdapter {

	private Context context;

	private HashSet<String> notifications;
	private TaskComplete taskComplete;

	public RequestNotificationAdapter(Context context,
			HashSet<String> notifications, TaskComplete taskComplete) {

		this.context = context;
		if(this.notifications==null)
		this.notifications = notifications;
		this.taskComplete = taskComplete;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return notifications.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TextView mGroupName, mSender, mCounter;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(R.layout.layout_request_notification_details,
							parent, false);
			mGroupName = (TextView) convertView
					.findViewById(R.id.txt_req_noti_group_name);
			mSender = (TextView) convertView
					.findViewById(R.id.txt_req_noti_sender);
			mCounter = (TextView) convertView
					.findViewById(R.id.txt_req_noti_counter);

		notifications.size();
		if(position<notifications.size())
		{
		String value = (String) notifications.toArray()[position];
		final String[] list = value.split("#");

		// order endTime, groupName , requestID, userID, initiatedBy, msgid;
		final long systemTime = new Date().getTime();
		final long endTime = Long.parseLong(list[0]);

		if (endTime > systemTime) {
			mGroupName.setText(list[1]);
			mSender.setText("from:" + " " + list[4]);
			CustomTimer customTimer = new CustomTimer(context, mCounter,
					endTime, taskComplete);
			customTimer.timerStart();
			
		} else {
			taskComplete.onComplete();
		}
		convertView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CustomSharedPreference mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, context);
				boolean showingDialog = mPref.getBoolean(AppCollageConstants.SHOWING_DIALOG, false);
				
				if(!showingDialog && (endTime - systemTime)>0)
				{
				
				Intent intent = new Intent(context, InitiateAppCollageActivity.class);
				
				intent.putExtra("GroupName", list[1]);
				intent.putExtra("RequestID", list[2]);
				intent.putExtra("endTime", endTime);
				intent.putExtra("InitiatedBy", list[4]);
				intent.putExtra("msgid", list[5]);
				intent.putExtra("message", list[6]);

				intent.putExtra("appcollageRequest", true);
				
				((Activity)context).startActivityForResult(intent, AppCollageConstants.COLLAGE_REQUEST);
				((Activity)context).finish();
				mPref.putBoolean(AppCollageConstants.SHOWING_DIALOG, true);
				}
			}
		});
		}
		return convertView;
	}
	

}
