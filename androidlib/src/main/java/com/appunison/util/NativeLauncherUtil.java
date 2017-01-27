package com.appunison.util;

import java.io.File;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/**
 * This class is use to
 * open native composer
 * mail & sms
 * @author appunison
 */

public class NativeLauncherUtil {

	/**
	 * open native composer
	 * for sms
	 */

	public static void sendSms(Context context, String defaultText)
	{
		try{
		/** 
		 *  Creating an intent to initiate view action 
		 */
		Intent intent = new Intent("android.intent.action.VIEW");

		/** 
		 * creates an sms uri 
		 * */
		Uri data = Uri.parse("sms:");

		/** 
		 * Setting sms uri to the intent */
		intent.setData(data);
		intent.putExtra("sms_body", defaultText);

		/** Initiates the SMS compose screen, because the activity contain ACTION_VIEW and sms uri */
		context.startActivity(intent);
		}
		catch(ActivityNotFoundException e){
			//ToastCustom.makeText(context, "Device not support!", Toast.LENGTH_SHORT);
		}
	}

	/**
	 * open native composer
	 * for email
	 */

	public static void sendMail(Context context, File filelocation)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/png");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] {""});
		intent.putExtra(Intent.EXTRA_SUBJECT, "");
		intent.putExtra(Intent.EXTRA_TEXT, "");
		// the attachment
		intent .putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filelocation));
		context.startActivity(Intent.createChooser(intent, ""));
	}
}
