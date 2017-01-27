package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;

import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.dialog.DefaultTextDialog;
import com.appunison.appcollage.model.pojo.response.SignupResponse;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.appcollage.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SettingActivity extends AppCollageBaseActivity{

	private String TAG  = SettingActivity.class.getName();
	private @InjectView(R.id.img_back)  ImageView mimgBack;
	private @InjectView(R.id.layout_setting_account) LinearLayout mlayoutAccount;
	private @InjectView(R.id.layout_setting_default_texts) LinearLayout mlayoutDefaultTexts;
	private @InjectView(R.id.layout_setting_help) LinearLayout mlayoutHelp;
	private @InjectView(R.id.layout_setting_remove) LinearLayout mlayoutRemove;
	private @InjectView(R.id.layout_setting_edit_grorp) LinearLayout mlayoutEditGroup;
	private @InjectView(R.id.layout_setting_delete) LinearLayout mlayoutDelete;
	private SessionManager sessionManager;
	private SignupResponse signupResponse;
	private CustomSharedPreference mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting);

		signupResponse = (SignupResponse) getIntent().getSerializableExtra("signupResponse");
		
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, SettingActivity.this);
		sessionManager = new SessionManager(AppCollageConstants.PREF_NAME, SettingActivity.this);
		mtxtTitle.setText(getResources().getString(R.string.action_settings));
		mimgBack.setImageResource(R.drawable.ic_action_back);

		mlayoutAccount.setOnClickListener(this);
		mlayoutDefaultTexts.setOnClickListener(this);
		mlayoutHelp.setOnClickListener(this);
		mlayoutRemove.setOnClickListener(this);
		mlayoutEditGroup.setOnClickListener(this);
		mlayoutDelete.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.layout_setting_account:
			//ToastCustom.underDevelopment(SettingActivity.this);
			Intent intentAccount = new Intent(SettingActivity.this, AccountActivity.class);
			intentAccount.putExtra("signupResponse", signupResponse);
			startActivityForResult(intentAccount, AppCollageConstants.INTENT_HOME);
			break;
		case R.id.layout_setting_default_texts:
			//ToastCustom.underDevelopment(SettingActivity.this);
			new DefaultTextDialog(SettingActivity.this).show();
			break;
		case R.id.layout_setting_help:
			//ToastCustom.underDevelopment(SettingActivity.this);
			Intent intentHelp = new Intent(SettingActivity.this, HelpActivity.class);
			intentHelp.putExtra("help", true);
			startActivityForResult(intentHelp, AppCollageConstants.INTENT_HOME);;
			break;
		case R.id.layout_setting_delete:
			//ToastCustom.underDevelopment(SettingActivity.this);
			Intent intentDelete = new Intent(SettingActivity.this, DeleteAccoutActivity.class);
			startActivityForResult(intentDelete, AppCollageConstants.INTENT_HOME);
			break;
		case R.id.layout_setting_edit_grorp:
			//ToastCustom.underDevelopment(SettingActivity.this);
			//sessionManager.logoutUser();
			/*Intent intentLogout = new Intent(SettingActivity.this, LogoutActivty.class);
			intentLogout.putExtra("signupResponse", signupResponse);
			startActivityForResult(intentLogout, AppCollageConstants.INTENT_HOME);*/
			Intent intentEditGroup = new Intent(SettingActivity.this, EditGroupActivity.class);
			startActivityForResult(intentEditGroup, AppCollageConstants.INTENT_HOME);
			//service.callpostService(AppCollageConstants.url, populateLogout(), LogoutResponse.class, ActionType.LOGOUT.getActionType());
			
			break;
		case R.id.layout_setting_remove:
			//ToastCustom.underDevelopment(SettingActivity.this);
			Intent intentRemove = new Intent(SettingActivity.this, RemoveFormGroupActivity.class);
			startActivity(intentRemove);
			
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == AppCollageConstants.INTENT_HOME)
		{
			if(data.getStringExtra("screen").equals("start"))
			{
			setResult(AppCollageConstants.INTENT_HOME, data);
			finish();
			}
		}
		else if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == AppCollageConstants.FINAL_HOME)
		{
			setResult(AppCollageConstants.FINAL_HOME, data);
			finish();
		}else if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == AppCollageConstants.SETTING_LOGOUT)
		{
			setResult(AppCollageConstants.SETTING_LOGOUT);
			finish();
		}else if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == 97)
		{
			setResult(97,data);
			signupResponse=(SignupResponse) data.getSerializableExtra("profileResponse");
		}
		else if(requestCode == AppCollageConstants.INTENT_HOME && resultCode == 11)
		{
			setResult(11,data);
			finish();
			//signupResponse=(SignupResponse) data.getSerializableExtra("profileResponse");
		}
	}
}
