package com.appunison.appcollage.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.util.Log;

public class TimeUtils {

	/*public static long getTimeInOwnTimeZone(String time, String timeZone)
    {
    SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sourceFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
    Date parsed;
    long l = 0;
	try {
		
		parsed = sourceFormat.parse(time);
		
	    TimeZone tz = TimeZone.getTimeZone(timeZone);
	    destFormat.setTimeZone(tz);

	    String result = destFormat.format(parsed);
	    Log.i("result", ": "+result);

		Date dt = destFormat.parse(time);                                        
		l = dt.getTime();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} // => Date is in UTC now
	return l;
    }*/
	/*public static long getUTCTime()
    {
    	long time = 0;
    	SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

    	//Local time zone   
    	SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    	//Time in GMT
    	try {
    		Date date = dateFormatLocal.parse(dateFormatGmt.format(new Date()));
    		Log.i("utc", ": "+dateFormatLocal.format(date));
    		time = date.getTime();
    	} catch (ParseException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return time;
    }*/
	
	
	public static String getCurrentTimezoneOffset() {

	    TimeZone tz = TimeZone.getDefault();  
	    Calendar cal = GregorianCalendar.getInstance(tz);
	    int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

	    String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
	    offset = (offsetInMillis >= 0 ? "+" : "-") + offset;

	    return offset;
	} 

}
