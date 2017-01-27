package com.appunison.appcollage.utils;

import java.io.InputStream;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.listener.ITwitter;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;

public class LoginTwitter {

	private Twitter twitter;
	private Context context;
	private ITwitter iTwitter;
	private RequestToken requestToken = null;
	private AccessToken accessToken;
	private String oauth_url,oauth_verifier,profile_url;
	private Dialog auth_dialog;
	private WebView web;
	private ProgressDialog progress;
	private Bitmap bitmap;
	private ProgressDialog progressbar;
	public LoginTwitter(Context context,ITwitter twitterInterface)
	{
		this.context = context;
		iTwitter=twitterInterface;
		intializeProgressBar(context);
		intializeVariable(context);
	}
	
	
	public void intializeProgressBar(Context context) {
		// TODO Auto-generated method stub
		progressbar = new ProgressDialog(context);
		progressbar.setMessage("Loading...");
		progressbar.setIndeterminate(false);
		//progressbar.setMax(100);
		progressbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressbar.setCancelable(false);	
	}
	public void showProgressBar() {
		// TODO Auto-generated method stub
		try
		{
			if(progressbar!=null)
			{
				if(!progressbar.isShowing())
				{
					progressbar.show();
				}
			}
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}

	}
	public void dismissProgressBar() {
		// TODO Auto-generated method stub
		try
		{
			if(progressbar!=null)
			{
				if(progressbar.isShowing())
				{
					progressbar.dismiss();
				}
			}
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void intializeVariable(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		//twitter = new TwitterFactory().getInstance();
		
		//twitter.setOAuthConsumer(AppCollageConstants.CONSUMER_KEY, AppCollageConstants.CONSUMER_SECRET);
		 ConfigurationBuilder cb = new ConfigurationBuilder();
		    cb.setDebugEnabled(true)
		            .setOAuthConsumerKey(AppCollageConstants.CONSUMER_KEY)
		            .setOAuthConsumerSecret(AppCollageConstants.CONSUMER_SECRET)
		            .setOAuthAccessToken(null)
		            .setOAuthAccessTokenSecret(null);

		    TwitterFactory tf = new TwitterFactory(cb.build());
		   twitter = tf.getInstance();
		new TokenGet().execute();
	}

	private class TokenGet extends AsyncTask<String, String, String> {
		
		
		@Override
		protected void onPreExecute() {
			
			showProgressBar();
			
		}
		
		@Override
		protected String doInBackground(String... args) {

			try {
				requestToken = twitter.getOAuthRequestToken();
				oauth_url = requestToken.getAuthorizationURL();
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return oauth_url;
		}
		@Override
		protected void onPostExecute(String oauth_url) {
			dismissProgressBar(); 
			if(oauth_url != null){
				Log.e("URL", oauth_url);
				auth_dialog = new Dialog(context);
				auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 

				auth_dialog.setContentView(R.layout.auth_dialog);
				web = (WebView)auth_dialog.findViewById(R.id.webv);
				web.getSettings().setJavaScriptEnabled(true);
				web.loadUrl(oauth_url);
				web.setWebViewClient(new WebViewClient() {
					boolean authComplete = false;
					@Override
					public void onPageStarted(WebView view, String url, Bitmap favicon){
						super.onPageStarted(view, url, favicon);
					}

					@Override
					public void onPageFinished(WebView view, String url) {
						super.onPageFinished(view, url);
						if (url.contains("oauth_verifier") && authComplete == false){
							authComplete = true;
							Log.e("Url",url);
							Uri uri = Uri.parse(url);
							oauth_verifier = uri.getQueryParameter("oauth_verifier");

							auth_dialog.dismiss();
							new AccessTokenGet().execute();
						}else if(url.contains("denied")){
							auth_dialog.dismiss();
							ToastCustom.makeText(context, "Sorry !, Permission Denied", Toast.LENGTH_SHORT);


						}
					}
				});
				auth_dialog.show();
				auth_dialog.setCancelable(true);



			}else{

				ToastCustom.makeText(context, "Network not available", Toast.LENGTH_SHORT);


			}
		}
	}
	
	private class AccessTokenGet extends AsyncTask<String, String, User> {


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(context);
			progress.setMessage("Fetching Data ...");
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setIndeterminate(true);
			progress.show();

		}


		@Override
		protected User doInBackground(String... args) {

			User user = null;
			try {

				accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier); 		   
				user = twitter.showUser(accessToken.getUserId());
				

			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	    
			return user;
		}
		@Override
		protected void onPostExecute(User user) {

			progress.hide();

			if(user!=null){
				
				iTwitter.authorized(user);
			}	
		}


	}
	
	
	/**
	 * share On twitter
	 */
	public void shareOnTwitter(String url){
		new DownloadImageTask().execute(url);
	 }
	
		
	//Async task to get image from given url
	
	private  class DownloadImageTask extends AsyncTask<String, Void, InputStream> {

		    private ProgressDialog mDialog;
		    
		    protected void onPreExecute() {
		    	
		        mDialog = ProgressDialog.show(context,"Please wait...", "Uploading data ...", true);
		    }

		    protected InputStream doInBackground(String... urls) {
		        String urldisplay = urls[0];
		        try {
		            InputStream in = new java.net.URL(urldisplay).openStream();
		            StatusUpdate status = new StatusUpdate("");
					status.setMedia("",in);
		            
		            twitter.updateStatus(status);
		            return in; 
		        } catch (Exception e) {
		            Log.e("Error", "image download error");
		            Log.e("Error", e.getMessage());
		            e.printStackTrace();
		           
		        }
		        return null;
		    }

		    protected void onPostExecute(InputStream result) {
		       mDialog.dismiss();
		    }
		}


	public void onProgress(int arg0, double arg1, NetworkEventsEnum arg2,
			Object arg3) {
		// TODO Auto-generated method stub
		
	}

}