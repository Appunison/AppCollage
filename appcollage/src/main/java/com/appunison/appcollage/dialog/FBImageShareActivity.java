package com.appunison.appcollage.dialog;

import java.util.Arrays;

import roboguice.activity.RoboActivity;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.FacebookDialog;
import com.appunison.appcollage.R;
import com.appunison.appcollage.imageGetter.ImageLoader;
import com.appunison.basewidgets.ToastCustom;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Toast;

public abstract class FBImageShareActivity extends RoboActivity implements OnClickListener{
	
    private UiLifecycleHelper uiHelper;
    private final String PENDING_ACTION_BUNDLE_KEY = "com.appunison.appcollage:PendingAction";

    private String url;
    private boolean canPresentShareDialogWithPhotos;
    
    private Session.StatusCallback callback = new Session.StatusCallback() {
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	 canPresentShareDialogWithPhotos   = FacebookDialog.canPresentShareDialog(this,
                 FacebookDialog.ShareDialogFeature.PHOTOS);
    	 uiHelper = new UiLifecycleHelper(this, callback);
         uiHelper.onCreate(savedInstanceState);
    }
    private class ImageAsync extends AsyncTask<Void, Void, Bitmap>
    {

		@Override
		protected Bitmap doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return new ImageLoader(FBImageShareActivity.this).getBitmap(url);
		}
    	@Override
    	protected void onPostExecute(Bitmap bitmap) {
    		// TODO Auto-generated method stub
    		super.onPostExecute(bitmap);
        	
            if (canPresentShareDialogWithPhotos) {
            	if(bitmap != null)
            	{
            		try
            		{
            			FacebookDialog shareDialog = createShareDialogBuilderForPhoto(bitmap).build();
            			uiHelper.trackPendingDialogCall(shareDialog.present());
            		}
            		catch(FacebookException e)
            		{
            			ToastCustom.makeText(FBImageShareActivity.this, getString(R.string.fb_not_install_msg), Toast.LENGTH_LONG);
            		}
            	}
            	else
            	{
            		// No Image on url
            	}
            } 
            else if (hasPublishPermission()) {
                Request request = Request.newUploadPhotoRequest(Session.getActiveSession(), bitmap, new Request.Callback() {
                    public void onCompleted(Response response) {
                        showPublishResult(getString(R.string.photo_post), response.getGraphObject(), response.getError());
                    }
                });
                
                request.executeAsync();
            } else {
            	Session.openActiveSession(FBImageShareActivity.this, true, callback);
            }
        
    	}
    }
    
    private void showPublishResult(String message, GraphObject result, FacebookRequestError error) {
        String alertMessage = null;
        if (error == null) {
            alertMessage = getString(R.string.successfully_posted_post, message);
        } else {
            alertMessage = error.getErrorMessage();
        }
        Toast.makeText(FBImageShareActivity.this, alertMessage, Toast.LENGTH_SHORT).show();
    }


    private FacebookDialog.PhotoShareDialogBuilder createShareDialogBuilderForPhoto(Bitmap... photos) {
        return new FacebookDialog.PhotoShareDialogBuilder(this)
                .addPhotos(Arrays.asList(photos));
    }
    
    private boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if ((exception instanceof FacebookOperationCanceledException ||
                exception instanceof FacebookAuthorizationException)) {
                new AlertDialog.Builder(FBImageShareActivity.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
            new ImageAsync().execute();
        }
    }
    
    public void handlePendingAction(String url) {
        this.url = url;
        new ImageAsync().execute();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
       // AppEventsLogger.activateApp(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, "onSaveInstanceState");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
        if(requestCode == Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE && resultCode == RESULT_OK)
        {
        	canPresentShareDialogWithPhotos = true;
            new ImageAsync().execute();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    
    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }
        

        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
        }
    };
    

}
