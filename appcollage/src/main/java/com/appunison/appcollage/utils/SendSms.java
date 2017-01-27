package com.appunison.appcollage.utils;

import com.appunison.basewidgets.ToastCustom;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SendSms 
{
	public static void sendSMS(Context mContext, String phoneNumber,String message){
		
		
		Log.i("SMS", phoneNumber);
		Log.i("SMS message : ",message+phoneNumber);
		
		try {
			  
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNumber, null, message, null, null);				
			//ToastCustom.makeText(mContext, "Message Sent",Toast.LENGTH_LONG);
		  } catch (Exception ex) {
			  ToastCustom.makeText(mContext,
				ex.getMessage().toString(),
				Toast.LENGTH_LONG);
			ex.printStackTrace();
		  }
		
	}

}
