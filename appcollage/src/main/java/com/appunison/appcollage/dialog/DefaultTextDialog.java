package com.appunison.appcollage.dialog;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.views.activities.SetDefaultTextActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

public class DefaultTextDialog extends Dialog implements android.view.View.OnClickListener{

	private Context context;
	private LinearLayout mlayoutDefaultTextInvite, mlayoutDefaultTextGoAppCollage, mlayoutDefaultTextResponse;

	public DefaultTextDialog(Context context) {
		super(context,android.R.style.Theme_NoTitleBar_OverlayActionModes);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setCanceledOnTouchOutside(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.layout_dialog_default_texts);

		mlayoutDefaultTextInvite = (LinearLayout) findViewById(R.id.layout_default_text_invite);
		mlayoutDefaultTextGoAppCollage = (LinearLayout) findViewById(R.id.layout_default_text_go_appcollage);
		mlayoutDefaultTextResponse = (LinearLayout) findViewById(R.id.layout_default_text_response);

		mlayoutDefaultTextInvite.setOnClickListener(this);
		mlayoutDefaultTextGoAppCollage.setOnClickListener(this);
		mlayoutDefaultTextResponse.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_default_text_go_appcollage:
			//ToastCustom.underDevelopment(context);
			Intent intentGoAppCollage = new Intent(context, SetDefaultTextActivity.class);
			intentGoAppCollage.putExtra("textgoappcollage", true);
			((Activity)context).startActivityForResult(intentGoAppCollage, AppCollageConstants.INTENT_HOME);
			break;
		case R.id.layout_default_text_invite:
			//ToastCustom.underDevelopment(context);
			Intent intentInvite = new Intent(context, SetDefaultTextActivity.class);
			intentInvite.putExtra("textinvite", true);
			((Activity)context).startActivityForResult(intentInvite, AppCollageConstants.INTENT_HOME);
			break;
		case R.id.layout_default_text_response:
			//ToastCustom.underDevelopment(context);
			Intent intentResponse = new Intent(context, SetDefaultTextActivity.class);
			intentResponse.putExtra("textresponse", true);
			((Activity)context).startActivityForResult(intentResponse, AppCollageConstants.INTENT_HOME);
			break;

		default:
			break;
		}

	}

}
