package com.appunison.appcollage.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;
/**
 * 
 * @author appunison
 *
 */
public class DateTimeConverter {

	public static String yyyy_MM_dd= "yyyy-MM-dd";
	public static String dd_MM_yyyy= "dd-MM-yyyy";
	public static String dd_MMM_yyyy_EEEE= "dd MMM yyyy, EEEE";
	public static String dd_MMM_yyyy= "dd MMM yyyy";
	public static String hh_mm_a= "hh:mm a";
	public static String HH_mm_ss= "HH:mm:ss";
	public static String yyyy_MM_dd_HH_mm_ss= "yyyy-MM-dd HH:mm:ss";
	public String getCurrentTime() {
		
		final Calendar c = Calendar.getInstance();
		int DAY_OF_MONTH = c.get(Calendar.DAY_OF_MONTH);
		int MONTH = c.get(Calendar.MONTH);
		int YEAR = c.get(Calendar.YEAR);
		return DAY_OF_MONTH + "/" + (MONTH +1) + "/" + YEAR;
		
	}
	
	public String convertToDatabaseFormat(String inputDate) {
		Date date = null;
		try {
			SimpleDateFormat parser = new SimpleDateFormat("yyyy/mm/dd");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

			date = parser.parse(inputDate);
			Log.i("convertToDatabaseFormat", "try : " + formatter.format(date));
			return formatter.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("convertToDatabaseFormat", "catch : " + inputDate);
		return inputDate;
	}

	public String convertToCalendarDate(String inputDate) {
		if(inputDate.contains(" "))
		{
			return inputDate.split(" ")[0];
		}
		return inputDate;
	}
	public String convertToCalendarTime(String inputDate) {
		if(inputDate.contains(" "))
		{
			return inputDate.split(" ")[1];
		}
		return inputDate;
	}
	/***
	 * 
	 * @param inputDate
	 * @param format
	 * @return
	 */
	public Date convertToDateObject(String inputDate,String format)
	{
		Date date = null;
		try {
			SimpleDateFormat parser = new SimpleDateFormat(format);
			
			date = parser.parse(inputDate);

			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}

	public String convertDateFormat(String inputDate, String inputFormat, String outputFormat) {
		Date date = null;
		try {
			SimpleDateFormat parser = new SimpleDateFormat(inputFormat);
			SimpleDateFormat formatter = new SimpleDateFormat(outputFormat);

			date = parser.parse(inputDate);

			Log.i("convertToScreenFormat", "try : " + formatter.format(date));
			return formatter.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("convertToScreenFormat", "catch : " + inputDate);
		return inputDate;
	}
	
	
	
	
	
	public String Convert12to24(String time)
	{
		String convertedTime ="";
		try {
			SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
			SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = parseFormat.parse(time);        
			convertedTime=displayFormat.format(date);
			System.out.println("convertedTime : "+convertedTime);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return convertedTime;
		//Output will be 10:23 PM
	}
	public String Convert24to12(String time)
	{
		String convertedTime ="";
		try {
			SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
			SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = parseFormat.parse(time);        
			convertedTime=displayFormat.format(date);
			System.out.println("convertedTime : "+convertedTime);
		} catch (final ParseException e) {
			e.printStackTrace();
			return time;
		}
		return convertedTime;
		//Output will be 10:23 PM
	}
	/**
	 * 
	 * @param time
	 * @return
	 */
	public String getDateFromTimeStamp(String time)
	{
		long timeInSec = 0;
		try
		{
		timeInSec = Long.parseLong(time);
		}
		catch(NumberFormatException e){}
		Date date = new Date(timeInSec*1000L);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // the format of your date
		String formattedDate = sdf.format(date);
		return formattedDate;
		
	}
	
	
	
	
	
	/**
	 * 
	 * @param time
	 * @return
	 */
	
	public String getTimeFromTimeStamp(String time)
	{
		Date date = new Date(Long.parseLong(time)*1000L);
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); // the format of your date
		String formattedDate = sdf.format(date);
		return formattedDate;
			
	}
	
	
	
}
