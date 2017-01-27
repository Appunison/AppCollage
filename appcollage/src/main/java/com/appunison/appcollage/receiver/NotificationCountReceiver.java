package com.appunison.appcollage.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class NotificationCountReceiver extends BroadcastReceiver{
	private TextView textView;
	public NotificationCountReceiver(TextView textView) {
		this.textView = textView;
	}
@Override
public void onReceive(Context context, Intent intent) {
	int count = intent.getIntExtra("count", 0);
	if(count>0)
	{
		this.textView.setVisibility(View.VISIBLE);
		this.textView.setText(""+count);
	}
	else{
		this.textView.setVisibility(View.GONE);
	}
	
}
}
