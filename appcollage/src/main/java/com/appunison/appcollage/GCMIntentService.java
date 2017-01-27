package com.appunison.appcollage;

import java.util.Date;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.utils.NotificationCounterUtils;
import com.appunison.appcollage.views.activities.InitiateAppCollageActivity;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.util.BadgeUtils;
import com.appunison.view.BaseActivity;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCM Tutorial::Service";

	// Use your PROJECT ID from Google API into SENDER_ID
	public static final String SENDER_ID = "747792614868"; // AppCollage

	public static final int IPA = 1;

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {

		Log.i(TAG, "onRegistered: registrationId=" + registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {

		Log.i(TAG, "onUnregistered: registrationId=" + registrationId);
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		// TODO Auto-generated method stub
		super.onDeletedMessages(context, total);
		// BadgeUtils.setNotificationCounttoZero(context);
	
	}

	@SuppressLint("NewApi")
	@Override
	protected void onMessage(Context context, Intent data) {
		SessionManager sessionManager=new SessionManager(AppCollageConstants.PREF_NAME, context);
		if(!sessionManager.isLoggedIn())
		{
			return;
		}
		if((data.getStringExtra("RequestType").equals("CollageRequest")))
		{
				if((data.getStringExtra("message")==null)||
					(data.getStringExtra("GroupName")==null)||
					(data.getStringExtra("GroupId")==null)||
					(data.getStringExtra("collage_image")==null)||
					(data.getStringExtra("collage_id")==null)||
					(data.getStringExtra("Createddate")==null))
					{
						return;
					}
		}
		
		String userId=data.getStringExtra("UserId");
		String loggedInUserId=(String)sessionManager.getUserDetails().get(SessionManager.KEY_USERID);
		if((userId!=null)&&(loggedInUserId!=null))
		{
			if(!userId.equals(loggedInUserId))
			{
				return;
			}
		}
		
		/*Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone ringtoneSound = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);

		if (ringtoneSound != null) {
		    ringtoneSound.play();
		}
		
		Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(AppCollageConstants.VIBRATE_TIME);*/

		CustomSharedPreference mPref = new CustomSharedPreference(
				AppCollageConstants.PREF_NAME, context);

		boolean showingDialog = mPref.getBoolean(AppCollageConstants.SHOWING_DIALOG,
				false);


		String RequestType = data.getStringExtra("RequestType");
		Intent 	intent = new Intent(context, InitiateAppCollageActivity.class);
		String message = "";
		if (RequestType.equals("PhotoRequest")) {

			String time = data.getStringExtra("Time");
			String timeZone = data.getStringExtra("TimeZone");

			// Time in milliseconds
			long serverTime = Long.parseLong(time) * 1000;

			long systemTime = new Date().getTime();
			long endTime = 0;
			/*
			 * if(serverTime > systemTime) { endTime = systemTime + 5*60*1000; }
			 * else {
			 */
			endTime = serverTime + 5 * 60 * 1000;
			// }
			// set data in Hashset
			String groupName = data.getStringExtra("GroupName");
			String requestID = data.getStringExtra("RequestID");
			String userID = data.getStringExtra("UserID");
			String initiatedBy = data.getStringExtra("InitiatedBy");
			String msgid = data.getStringExtra("msgid");
			String msg = data.getStringExtra("message");

			String notificationData = "" + endTime + "#" + groupName + "#"
					+ requestID + "#" + userID + "#" + initiatedBy + "#"
					+ msgid + "#" + msg;
			// Save data in shared prefrences
			HashSet<String> setv = new HashSet<String>();
			HashSet<String> set = (HashSet<String>) mPref.getList(
					"notification", setv);
			set.add(notificationData);
			mPref.saveList("notification", set);

			HashSet<String> notifications = NotificationCounterUtils
					.removeExpiredData(context);

			if (notifications != null) {
				BadgeUtils.setBadge(context, notifications.size());
				Intent broadcastIntent = new Intent("kmdaction");
				broadcastIntent.putExtra("count", notifications.size());
				sendBroadcast(broadcastIntent);
			}

			AppCollageLogger.d("serverTime", "" + serverTime);
			AppCollageLogger.d("systemTime", "" + systemTime);
			AppCollageLogger.d("endTime", "" + endTime);

			int requestCode = 1005;

			if (intent != null) {
				if(RequestType.equals("CollageRequest"))
				{
					intent.putExtra("GroupName", data.getStringExtra("GroupName"));
					intent.putExtra("GroupId", data.getStringExtra("GroupId"));
					intent.putExtra("message", data.getStringExtra("message"));
					intent.putExtra("collage_image",
							data.getStringExtra("collage_image"));
					intent.putExtra("collage_id", data.getStringExtra("collage_id"));
					intent.putExtra("Createddate",
							data.getStringExtra("Createddate"));
					

					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				}else if(RequestType.equals("PhotoRequest"))
				{
					message = data.getStringExtra("InitiatedBy") + " "
							+ "from your group" + " "
							+ data.getStringExtra("GroupName") + " "
							+ "has initiated a appcollage";
					intent.putExtra("GroupName", data.getStringExtra("GroupName"));
					intent.putExtra("RequestID", data.getStringExtra("RequestID"));
					intent.putExtra("message", data.getStringExtra("message"));
					intent.putExtra("Time", time);
					intent.putExtra("TimeZone", timeZone);
					intent.putExtra("endTime", endTime);

					Log.d("msgid", ":" + msgid);
					intent.putExtra("msgid", data.getStringExtra("msgid"));
					intent.putExtra("UserID", data.getStringExtra("UserID"));
					intent.putExtra("InitiatedBy",
							data.getStringExtra("InitiatedBy"));

					intent.putExtra("appcollageRequest", true);

					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				}
				
				
				if (!(BaseActivity.isAppOpen())) {
					PendingIntent pIntent = PendingIntent.getActivity(this,
							requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					// Create the notification with a notification builder
					Notification notification = new Notification.Builder(this)
					.setSmallIcon(R.drawable.appcollage_launcher)
					.setWhen(System.currentTimeMillis())
					.setContentTitle(context.getString(R.string.app_name))
					.setContentText(message)
					.setContentIntent(pIntent)
					/*.setSound(
							RingtoneManager
							.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))*/
							.getNotification();
					// Remove the notification on click
					notification.flags |= Notification.FLAG_AUTO_CANCEL;

					 // Play default notification sound
			        notification.defaults |= Notification.DEFAULT_SOUND;
			         
			        // Vibrate if vibrate is enabled
			        notification.defaults |= Notification.DEFAULT_VIBRATE;
					
					NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					manager.notify(requestCode, notification);

					// Wake Android Device when notification received
					PowerManager pm = (PowerManager) context
							.getSystemService(Context.POWER_SERVICE);
					final PowerManager.WakeLock mWakelock = pm.newWakeLock(
							PowerManager.FULL_WAKE_LOCK
							| PowerManager.ACQUIRE_CAUSES_WAKEUP,
							"GCM_PUSH");
					mWakelock.acquire();

					// Timer before putting Android Device to sleep mode.
					Timer timer = new Timer();
					TimerTask task = new TimerTask() {
						public void run() {
							mWakelock.release();
						}
					};
					timer.schedule(task, 2000);
				}
				else {
					// Create the notification with a notification builder
					Notification notification = new Notification.Builder(this).getNotification();
					// Remove the notification on click
					notification.flags |= Notification.FLAG_AUTO_CANCEL;

					 // Play default notification sound
			        notification.defaults |= Notification.DEFAULT_SOUND;
			         
			        // Vibrate if vibrate is enabled
			        notification.defaults |= Notification.DEFAULT_VIBRATE;
					NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			        manager.notify(requestCode, notification);
				}

			}
			if (!showingDialog && (endTime - systemTime) > 0) {

				// Open a new activity called GCMMessageView
				message = data.getStringExtra("InitiatedBy") + " "
						+ "from your group" + " "
						+ data.getStringExtra("GroupName") + " "
						+ "has initiated a appcollage";
				intent.putExtra("GroupName", data.getStringExtra("GroupName"));
				intent.putExtra("RequestID", data.getStringExtra("RequestID"));
				intent.putExtra("Time", time);
				intent.putExtra("TimeZone", timeZone);
				intent.putExtra("endTime", endTime);

				Log.d("msgid", ":" + msgid);
				intent.putExtra("msgid", data.getStringExtra("msgid"));
				intent.putExtra("UserID", data.getStringExtra("UserID"));
				intent.putExtra("InitiatedBy",
						data.getStringExtra("InitiatedBy"));

				intent.putExtra("appcollageRequest", true);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				if ((BaseActivity.isAppOpen()))
				startActivity(intent);

				mPref.putBoolean(AppCollageConstants.SHOWING_DIALOG, true);

			}
		} else if (RequestType.equals("CollageRequest")) {
			if (!showingDialog) {

				message = data.getStringExtra("message");
				intent.putExtra("GroupName", data.getStringExtra("GroupName"));
				intent.putExtra("GroupId", data.getStringExtra("GroupId"));
				intent.putExtra("message", message);
				intent.putExtra("collage_image",
						data.getStringExtra("collage_image"));
				intent.putExtra("collage_id", data.getStringExtra("collage_id"));
				intent.putExtra("Createddate",
						data.getStringExtra("Createddate"));
				intent.putExtra("userId", userId);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				if ((BaseActivity.isAppOpen()))
				startActivity(intent);
			}
		}

	}

	@Override
	protected void onError(Context arg0, String errorId) {

		Log.e(TAG, "onError: errorId=" + errorId);
	}

}