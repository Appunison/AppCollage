package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;

import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContactOptionActvity extends AppCollageBaseActivity{

	private @InjectView (R.id.btn_contact_option_type_my_contacts) Button mbtnTypeMyContacts;
	private @InjectView (R.id.btn_contact_option_use_address_book) Button mbtnUseAddressBook;
	private Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_contact_option);
		
		mtxtTitle.setText(getResources().getString(R.string._use_address_book));
		mbtnTypeMyContacts.setOnClickListener(this);
		mbtnUseAddressBook.setOnClickListener(this);
		bundle = getIntent().getExtras();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_contact_option_type_my_contacts:
			Intent intent = new Intent(ContactOptionActvity.this, InviteViaContactsActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, AppCollageConstants.INTENT_HOME);
			break;
			
		case R.id.btn_contact_option_use_address_book:
			//ToastCustom.underDevelopment(ContactOptionActvity.this);
			//new FetchContactDialog(ContactOptionActvity.this, getResources().getString(R.string.fetch_contact_msg)).show();
			Intent intent1 = new Intent();
			intent1.putExtra("screen", "fetch_invite_contact");
			intent1.putExtras(bundle);
			setResult(AppCollageConstants.INTENT_HOME, intent1);
			
			finish();
			break;

		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == AppCollageConstants.INTENT_HOME)
		{
			setResult(AppCollageConstants.INTENT_HOME, data);
			finish();
		}
		else if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}
	}
}
