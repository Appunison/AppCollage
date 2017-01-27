package com.appunison.appcollage.views.activities;

import com.appunison.appcollage.R;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SentLinkForgetActivity extends AppCollageBaseActivity{

	private @InjectView (R.id.btn_sent_link_forget_ok) Button mbtnOk;
	private @InjectView (R.id.layout_option) LinearLayout moption;
	private static final int FORGET=3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sent_link_forget);
		mtxtTitle.setText(getResources().getString(R.string.forgot_password_title));
		
		moption.setVisibility(View.GONE);
		mbtnOk.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_sent_link_forget_ok:
			setResult(FORGET);
			finish();
			break;

		default:
			break;
		}
	}
}
