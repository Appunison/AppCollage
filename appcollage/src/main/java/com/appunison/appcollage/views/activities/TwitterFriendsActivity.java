package com.appunison.appcollage.views.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import roboguice.inject.InjectView;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.IdType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.CheckTwitterRequest;
import com.appunison.appcollage.model.pojo.request.Friends;
import com.appunison.appcollage.model.pojo.request.InviteViaContactRequest;
import com.appunison.appcollage.model.pojo.response.CheckTwitterResponse;
import com.appunison.appcollage.model.pojo.response.InviteViaContactResponse;
import com.appunison.appcollage.views.adapter.TwitterFriendsAdapter;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.appcollage.R;
import com.appunison.task.ForeGroundTask;


public class TwitterFriendsActivity extends AppCollageNetworkActivity  {

	private String TAG = TwitterFriendsActivity.class.getName();
	private @InjectView(R.id.txt_no_result_found) TextView txtNoResultFind;
	private @InjectView(R.id.list_twitter) ListView mlistTwFriends;
	private @InjectView(R.id.btn_done) Button mbtDone;
	
	private ForeGroundTask service;
	private List<Friends> friendsarr;
	private CustomSharedPreference mPref;
	private String groupId; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_twitter_friends);
		
		mtxtTitle.setText(getResources().getString(R.string.twitter));
		
        friendsarr = new ArrayList<Friends>();
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, TwitterFriendsActivity.this);
		service = new ForeGroundTask(this, this);
		
		groupId = getIntent().getStringExtra("groupid");
		
		new FindFriendList().execute();
		mbtDone.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_done:
			List<String> ids = new ArrayList<String>();
			TwitterFriendsAdapter adapter = (TwitterFriendsAdapter) mlistTwFriends.getAdapter();
			for (int i = 0; i < adapter.getCount(); i++) {
				if(adapter.getItem(i).isCheck())
				{
					ids.add(adapter.getItem(i).getFriendId());
				}
			}
			String[] idsarray = new String[ids.size()];
			idsarray = ids.toArray(idsarray);
			if(idsarray.length>0)
			{
			service.callpostService(AppCollageConstants.url, populateInviteViaTwitter(idsarray), InviteViaContactResponse.class, ActionType.INVITEVIACONTACT.getActionType());
			}
			else{
				ToastCustom.makeText(this, "No Follower exist.", Toast.LENGTH_SHORT);
			}
			break;

		default:
			break;
		}
	}
	
	public void onProgress(int actionType, double progress, NetworkEventsEnum networkEvent,
			Object request) {

		AppCollageLogger.d(TAG, "Web service on Progress"+ActionType.getDescriptionFromActionType(actionType));
	}

	public void onUpdate(int actionType ,NetworkEventsEnum networkEvent, Object request,
			Object response, Exception exception) {
		super.onUpdate(actionType, networkEvent, request, response, exception);
		switch (networkEvent) {
		case SUCCESS:
			if(actionType==ActionType.LOGIN.getActionType())
			{
				CheckTwitterResponse checkTwitterResponse=(CheckTwitterResponse)response;
				if(checkTwitterResponse.isResp())
				{
					txtNoResultFind.setVisibility(View.GONE);
					setAdapter(checkTwitterResponse.getResult());
				}
				else
				{
					//ToastCustom.makeText(TwitterFriendsActivity.this, checkTwitterResponse.getDesc(), Toast.LENGTH_SHORT);
					txtNoResultFind.setVisibility(View.VISIBLE);
				}
			}
			else if(actionType==ActionType.INVITEVIACONTACT.getActionType())
			{
				InviteViaContactResponse inviteResponse=(InviteViaContactResponse)response;
				if(inviteResponse.isResp())
				{
					ToastCustom.makeText(TwitterFriendsActivity.this, getString(R.string.invitation_sent_msg), Toast.LENGTH_SHORT);
					finish();
				}
				else
				{
					ToastCustom.makeText(TwitterFriendsActivity.this, inviteResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actionType));

	}

	public void onpreExecute(int actionType, NetworkEventsEnum networkEvent, Object request) {
		super.onpreExecute(actionType, networkEvent, request);
		
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));
	}
	/**
	 * Fetch all twitter follower
	 * @author appunison
	 *
	 */
	public class FindFriendList extends AsyncTask<Integer, Integer, String[]>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressBar();
		}  
		@Override
		protected String[] doInBackground(Integer... params) 
		{
			return getFollowers();
		}
		@Override
		protected void onPostExecute(String[] result) {
			
			CheckTwitterRequest request = new CheckTwitterRequest();
			request.setAction(WebServiceAction.CHECK_TWITTER);
			request.setTwitter_id(result);
			// Pass friend Ids of all friends
			service.callpostService(AppCollageConstants.url, request, CheckTwitterResponse.class, ActionType.LOGIN.getActionType());
		}

	}
	/**
	 * Get Followers of Twitter 
	 */
	public String[] getFollowers()
	{
        List<String> ids = new ArrayList<String>();

		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(AppCollageConstants.CONSUMER_KEY);
		builder.setOAuthConsumerSecret(AppCollageConstants.CONSUMER_SECRET);
		builder.setOAuthAccessToken(AppCollageConstants.ACCESS_TOKEN);
		builder.setOAuthAccessTokenSecret(AppCollageConstants.ACCESS_TOKEN_SECRET);
		Configuration conf = builder.build();
		Twitter twitter = new TwitterFactory(conf).getInstance();

        try {

            long lCursor = -1;
            PagableResponseList<User> followersIds = twitter.getFollowersList(twitter.getId(), lCursor);
            
            System.out.println(twitter.showUser(twitter.getId()).getName());
            System.out.println("==========================");
            
            for(int i = 0; i<followersIds.size();i++)
            {
            	User user =followersIds.get(i);
            	
            	Friends friends = new Friends();
            	friends.setFriendId(""+user.getId());
            	friends.setFriendName(user.getName());
            	friends.setTwImageUrl(user.getOriginalProfileImageURL());
            	
            	ids.add(""+user.getId());
            	friendsarr.add(friends);
            	
            	System.out.println(" : "+user.getId());
            }
            
            
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TwitterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String[] idsarr = new String[ids.size()];
        idsarr = ids.toArray(idsarr);

        return idsarr;
	}
	/**
	 * Set adapter of Friend using AppCollage
	 * @param ids
	 */
	private void setAdapter(String ids[])
	{
		List<String> idsarr= new ArrayList<String>(Arrays.asList(ids));//get id appcollage user
		// Filtering Follower using appcollage
		for (int i = 0; i < friendsarr.size();) {
			if(idsarr.contains(friendsarr.get(i).getFriendId())) // using appcollage
			{
				i++;
			}
			else
			{
				friendsarr.remove(i);
			}
		}
		mlistTwFriends.setAdapter(new TwitterFriendsAdapter(this, friendsarr));
		dismissProgressBar();
	}
	private InviteViaContactRequest populateInviteViaTwitter(String []idsarray)
	{
		String userid = mPref.getString(SessionManager.KEY_USERID, null);
		InviteViaContactRequest inviteRequest = new InviteViaContactRequest();
		 
		inviteRequest.setAction(WebServiceAction.INVITEGROUPUSER);
		inviteRequest.setGroup_id(groupId);
		inviteRequest.setMessage("");
		inviteRequest.setRequestby(idsarray);
		inviteRequest.setId_type(IdType.TW);
		inviteRequest.setInvitedby(userid);
		
		return inviteRequest;
	}
}
