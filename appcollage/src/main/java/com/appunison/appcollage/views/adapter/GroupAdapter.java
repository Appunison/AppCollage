package com.appunison.appcollage.views.adapter;

import java.util.List;

import com.appunison.appcollage.model.pojo.response.Group;
import com.appunison.appcollage.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter{
	
	Context context;
	List<Group> listGroups;
	
	public GroupAdapter(Context context, List<Group> listGroups)
	{
		this.context = context;
		this.listGroups = listGroups;
	}
	
	public void AddItems(List<Group> listGroups)
	{
		this.listGroups.addAll(listGroups);
		notifyDataSetChanged();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		if(listGroups != null && listGroups.size() > 0)
			return listGroups.size();
		return 0;
	}

	public Group getItem(int position) {
		// TODO Auto-generated method stub
		if(listGroups != null && listGroups.size() > 0)
			return listGroups.get(position);
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.layout_group_list, parent, false);
		
		TextView mtxtGroupName = (TextView) convertView.findViewById(R.id.txt_group_list_name);
		
		Group group = getItem(position);
		mtxtGroupName.setTag(group.getGroup_id());
		mtxtGroupName.setText(group.getGroup_name().toUpperCase());
		
		return convertView;
	}

}
