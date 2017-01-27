package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;

import com.appunison.appcollage.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InviteSentActivity extends AppCollageBaseActivity{
	
	private @InjectView(R.id.edt_invite_sent_group_name) EditText medtGroupName;
	private @InjectView(R.id.txt_invite_sent_to_user_name) TextView mtxtUserName;
	private @InjectView(R.id.txt_invite_sent_another_person_group) TextView mtxtInviteAnotherUser;
	private @InjectView(R.id.btn_invite_sent_not_now) Button mbtnNotNow;
	private @InjectView(R.id.btn_sent_invite) Button mbtnInvite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_invite_sent);
		
		mtxtTitle.setText(getResources().getString(R.string.invite_sent));
		
		//getIntent().getExtras().getString("groupName");
		getIntent().getExtras().getString("userName");
		
		medtGroupName.setText(getIntent().getExtras().getString("groupName"));
		mtxtUserName.setText(getIntent().getExtras().getString("userName")+"!");
		mtxtInviteAnotherUser.setText(getResources().getString(R.string.invition_sent_to_group_nmae)+"\""+getIntent().getExtras().getString("groupName")+"\""+"?");
		
		mbtnNotNow.setOnClickListener(this);
		mbtnInvite.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.btn_invite_sent_not_now:

			Intent intent = new Intent();
			intent.putExtra("screen", "home");
			setResult(2, intent);
			
			finish();
			
			break;
		case R.id.btn_sent_invite:

			Intent intentInvite = new Intent();
			intentInvite.putExtra("screen", "invite");
			setResult(2, intentInvite);

			finish();
			break;
		}
	}
}
