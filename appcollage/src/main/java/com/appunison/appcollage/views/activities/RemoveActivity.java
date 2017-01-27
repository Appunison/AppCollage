package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;

import com.appunison.appcollage.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class RemoveActivity extends AppCollageBaseActivity{
	
	private @InjectView (R.id.btn_remove_ok) Button mbtnOk;
	private @InjectView (R.id.layout_option) LinearLayout mlayoutOption;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_remove);
		mlayoutOption.setVisibility(View.INVISIBLE);
		mtxtTitle.setText(getResources().getString(R.string._remove));
		mbtnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_remove_ok:
			finish();
			break;
		}
	}
}
