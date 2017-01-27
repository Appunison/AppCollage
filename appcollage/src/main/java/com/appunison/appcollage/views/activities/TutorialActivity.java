package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;

import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TutorialActivity extends AppCollageBaseActivity
{
	private @InjectView(R.id.layout_readTutorial) LinearLayout mlayoutTutorial;
	private @InjectView(R.id.txt_bottom) TextView mtxtBottom;
	private @InjectView (R.id.img_option) ImageView moption;
	
	private boolean hidebutton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_tutorial);
		
		moption.setVisibility(View.INVISIBLE);
		mtxtTitle.setText(getResources().getString(R.string.welcome_to_appcollage));
		mlayoutTutorial.setOnClickListener(this);
		hidebutton = getIntent().getBooleanExtra("hidebutton", false);
		
		if(hidebutton)
		{
			mtxtBottom.setVisibility(View.VISIBLE);
			mlayoutTutorial.setVisibility(View.VISIBLE);
			
		}
		else
		{
			mtxtBottom.setVisibility(View.VISIBLE);
			mlayoutTutorial.setVisibility(View.VISIBLE);
		}
	}

	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.layout_readTutorial:
			if(hidebutton)
			{
				Intent intent = new Intent(TutorialActivity.this, InitiateActivity.class);
			
				startActivity(intent);
				finish();
			}else{
			     Intent intent = new Intent(TutorialActivity.this, SignUpActivity.class);
			     intent.setData(intent.getData());
			     startActivityForResult(intent , AppCollageConstants.FINISH_OPENED_ACTIVITY);
			}
			break;
			
		}
	
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppCollageConstants.FINISH_OPENED_ACTIVITY && resultCode == AppCollageConstants.FINISH_OPENED_ACTIVITY)
		{
			setResult(AppCollageConstants.FINISH_OPENED_ACTIVITY, data);
			finish();
		}
		if(requestCode == AppCollageConstants.FINISH_OPENED_ACTIVITY && resultCode == 24)
		{
			//setResult(AppCollageConstants.FINISH_OPENED_ACTIVITY, data);
			setResult(24);
			finish();
		}

	}

}
