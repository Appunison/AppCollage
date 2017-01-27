package com.appunison.appcollage.constants;

public interface AppCollageConstants {
	
	//public final static String url="http://qa.appunisontech.com/AppCollage/1.0.0.0/services/redirectRequest";
	//public final static String url="http://test4live.com/AppCollage//services/redirectRequest";

	//final String sub_url = "http://192.168.1.220/appcollage/services/";
	//final String sub_url = "http://Test4live.com/AppCollage/services/";
	final String sub_url = "http://52.10.169.92/AppCollage/services/";
	
	//final String sub_url = "http://Qa.appunisontech.com/AppCollage/3.0.0.0/services/";
	//final String sub_url = "http://192.168.1.77/AppCollage/services/";
	
	public final static String url = sub_url + "redirectRequest";
	public final String upload_url_initiate = sub_url + "postdata";
	public final String upload_url_request = sub_url + "SendImgs";
	public final String upload_url_update_profile = sub_url + "UpdateProfile";
	
	public final static String PREF_NAME="appcollagepref";
	
	
	//default text
	
	public final static String KEY_INVITE="key invite";
	public final static String KEY_RESPONSE="key response";
	public final static String KEY_INITIATE="key initiate";
	public final static String KEY_PASSWORD="key password";
	
	
	
	
	
	
	
	public static boolean DEBUG_MODE=true;
	public static boolean ERROR_MODE=true;
	public static boolean WARN_MODE=true;
	
	public static String SHOWING_DIALOG = "showingdialog";
	
	public static int FINISH_OPENED_ACTIVITY = 104; // tO FINISH ALL ACTIVITY AFTER LOG IN
	public static int SETTING_LOGOUT = 99;//to log out from setting 
	public static int INTENT_HOME = 2; //managing flow of activity
	public static int COLLAGE_REQUEST = 555;//managing flow to send request for collage
	public static int IMAGE_CROP = 55;//managing flow image crop for profile
	public static int FINAL_HOME = 69;//managing flow for whole apps to home screen
	public static int AppCollage_REQUEST_FROM_INBOX=70;
	public static int DELETE_MSG=71;
	
	
	//twitter credential
	//public static String CONSUMER_KEY = "JXFq2C7b6FvBbwpjzhkWW5PSY";
    //public static String CONSUMER_SECRET = "57ksSrBmEszW1C2oNxOS9NDohBceMXhGa6MSpF4nTpxzZZN2G0";
    //public static String ACCESS_TOKEN = "2816441521-hxlZc1GrUFLdA9v4Tt3nMFZ6WeL0oMnoU7HBv8T";
    //public static String ACCESS_TOKEN_SECRET = "8YTWWJXmLDji49TRCvOKjgPH1RUSW9BNK84IgI8pXq3oQ";
	public static String CONSUMER_KEY = "Cxq1vI5TE4SFVpg1nUZjSKQSP";
	public static String CONSUMER_SECRET = "hTN3JYy1eOhtl9A06XRiWFlXaXWCRLBPE7WB5ajHauPVyiBUBT";
    public static String ACCESS_TOKEN = "166277435-LCVjiAoAcMN8xdO3Lohod9gpTmMHw6Ng8AGtbmIq";
    public static String ACCESS_TOKEN_SECRET = "LxQbjiBJpn46yo2hNRb2UzQpfWyX0ea7iLOmrSqI5eurM";
    
    

	public static int AppCollage_REQUEST_INICIATE = 1;
	public static int AppCollage_REQUEST_NOTIFICATION = 2;
	public static int AppCollage_REQUEST_INBOX = 3;
	public static int VIBRATE_TIME = 3000;
	public static String AppCollage_INVITE_MSG = "Join this group to go appcollage and see what your friends are doing.";
}
 