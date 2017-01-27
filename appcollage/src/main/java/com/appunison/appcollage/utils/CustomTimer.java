package com.appunison.appcollage.utils;

import java.util.Date;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.listener.TaskComplete;
import com.appunison.basewidgets.ToastCustom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class CustomTimer {

	private Context context;
	private Handler customHandler;
	private long timeInMilliseconds = 0L;
	private long timeSwapBuff = 0L;
	private	long updatedTime = 0L;
	private TextView mtxtTimer;
	private long endTime;
	private TaskComplete taskComplete;
	public CustomTimer(Context context, TextView mtxtTimer, long endTime, TaskComplete taskComplete)
	{
		this(context, mtxtTimer, endTime);
		this.taskComplete = taskComplete;
	}
	public CustomTimer(Context context, TextView mtxtTimer, long endTime)
	{
		this.context = context;
		this.mtxtTimer = mtxtTimer;
		this.endTime = endTime;
		customHandler = new Handler();
	}
	public void timerStart()
	{
		customHandler.postDelayed(updateTimerThread, 900);
	}
	public void timerStop(boolean timeFinished)
	{
		customHandler.removeCallbacks(updateTimerThread);
		if(timeFinished)
		{
		Activity activity = ((Activity)context);
		Intent intent = new Intent();
		intent.putExtra("screen", "home");
		activity.setResult(AppCollageConstants.COLLAGE_REQUEST, intent);
		
		ToastCustom.makeText(activity, activity.getString(R.string.AppCollage_is_expired), Toast.LENGTH_SHORT);
		
		activity.finish();
		}
	}
	private Runnable updateTimerThread = new Runnable() {

		public void run() {
			
			timeInMilliseconds = endTime - new Date().getTime();
			
			updatedTime = timeSwapBuff + timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			if(timeInMilliseconds > 0)
			{
				customHandler.postDelayed(this, 900);
			}
			else{
				if(taskComplete!=null)
				{
					customHandler.removeCallbacks(updateTimerThread);
					taskComplete.onComplete();
					taskComplete = null;
				}
				else
				{
					timerStop(true);
				}
			}
			mtxtTimer.setText("" + mins + ":"+ String.format("%02d", secs));
			//Log.d("TIME","" + mins + ":" + String.format("%02d", secs));
			
		}

	};
}
