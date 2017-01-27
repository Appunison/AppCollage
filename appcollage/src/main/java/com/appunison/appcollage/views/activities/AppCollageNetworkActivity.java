package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.utils.DialogFactory;
import com.appunison.view.NetworkBaseActivity;

public abstract class AppCollageNetworkActivity extends NetworkBaseActivity implements OnClickListener
{
	@InjectView (R.id.layout_back) LinearLayout mlayoutBack;
	@InjectView (R.id.txt_title) TextView mtxtTitle;
	@InjectView (R.id.layout_option) LinearLayout mlayoutOption;
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mlayoutBack.setOnClickListener(this);
		mlayoutOption.setOnClickListener(this);
	}
	
	public void intializeProgressBar(Context context) {
		// TODO Auto-generated method stub
		progressbar = new DialogFactory(context).getTransParentDialog();
	}
	
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.layout_back:
			finish();
			break;
		case R.id.layout_option:
			clickOnHome();
			Intent intent = new Intent();
			intent.putExtra("screen", "finalHome");
			setResult(AppCollageConstants.FINAL_HOME, intent);
			finish();
			
		}
	
	}
	
	protected void clickOnHome(){
		
	}
	
}
