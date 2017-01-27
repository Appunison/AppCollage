package com.appunison.appcollage.views.adapter;

import java.util.List;

import com.appunison.appcollage.imageGetter.ImageLoader;
import com.appunison.appcollage.model.pojo.response.GroupMember;
import com.appunison.appcollage.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupMemberAdapter extends BaseAdapter{

	private Context context;
	private List<GroupMember> listGroupMember;
	
	public GroupMemberAdapter(Context context,List<GroupMember> listGroupMember)
	{
		this.context=context;
		this.listGroupMember=listGroupMember;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		if(listGroupMember != null && listGroupMember.size() > 0)
			return listGroupMember.size();
		return 0;
	}

	public GroupMember getItem(int position) {
		// TODO Auto-generated method stub
		if(listGroupMember != null && listGroupMember.size() > 0)
			return listGroupMember.get(position);
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null)
		{	
			convertView = inflater.inflate(R.layout.layout_list_group_member, parent, false);
		}
		TextView mtxtMember = (TextView) convertView.findViewById(R.id.txt_group_member_name);
		TextView mtxtUserName = (TextView) convertView.findViewById(R.id.txt_group_user_name);
		ImageView imgMember = (ImageView) convertView.findViewById(R.id.img_group_member);
		
		GroupMember groupMember = getItem(position);
		mtxtMember.setText(groupMember.getName());
		mtxtUserName.setText(groupMember.getUsername());
		
		ImageLoader loader = new ImageLoader(context);
		loader.DisplayImage(groupMember.getMemberImage(), imgMember);
		
		return convertView;
	}
	
}
