package com.appunison.appcollage.views.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.appunison.appcollage.imageGetter.ImageLoader;
import com.appunison.appcollage.model.pojo.request.Friends;
import com.appunison.appcollage.R;

public class TwitterFriendsAdapter extends BaseAdapter{
	
	private Context context;
	private List<Friends> friendsarr;
	private ImageLoader imageLoader;

	
	public List<Friends> getFriendsarr() {
		return friendsarr;
	}

	public TwitterFriendsAdapter(Context context, List<Friends> friendsarr)
	{
		this.context = context;
		this.friendsarr = friendsarr;
		imageLoader = new ImageLoader(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		if(friendsarr!=null && friendsarr.size()>0)
			return friendsarr.size();
		return 0;
	}

	public Friends getItem(int position) {
		// TODO Auto-generated method stub
		if(friendsarr!=null && friendsarr.size()>0)
			return friendsarr.get(position);
		return null;
	}
	public void setItem(int position, Friends friends) {
		friendsarr.remove(position);
		friendsarr.add(position, friends);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.layout_list_twitter_friends, parent, false);
		
		ImageView imgTwFriend = (ImageView) convertView.findViewById(R.id.img_tw_friend);
		TextView txt_tw_friend_Name = (TextView) convertView.findViewById(R.id.txt_tw_friend_Name);
		//Button btn_invite_friend = (Button) convertView.findViewById(R.id.btn_invite_friend);
		final CheckBox checkbox_list_friend = (CheckBox) convertView.findViewById(R.id.checkbox_list_friend);
		
		final Friends friends = getItem(position);
		
		checkbox_list_friend.setChecked(friends.isCheck());
		imageLoader.DisplayImage(friends.getTwImageUrl(), imgTwFriend);
		checkbox_list_friend.setChecked(friends.isCheck());
		txt_tw_friend_Name.setText(friends.getFriendName());
		
		checkbox_list_friend.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				friends.setCheck(isChecked);
				setItem(position, friends);
			}
		});
		
		return convertView;
	}

}
