package com.appunison.appcollage.dialog;

import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.views.activities.InviteViaContactsActivity;
import com.appunison.appcollage.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class FetchContactDialog extends Dialog implements android.view.View.OnClickListener{

	private Context context;
	private String alertText;
	private TextView mtxtAlert;
	private Button mbtnYes;
	private Button mbtnNo;
	private Bundle bundle;

	public FetchContactDialog(Context context,String alertText,Bundle bundle) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.alertText = alertText;
		this.bundle = bundle;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setCanceledOnTouchOutside(false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.layout_dialog);
		
		mtxtAlert = (TextView) findViewById(R.id.txt_dialog_alert);
		mbtnNo = (Button) findViewById(R.id.btn_dialog_delete_group_no);
		mbtnYes = (Button) findViewById(R.id.btn_dialog_delete_group_yes);
		
		// here we set the color string
		
		SpannableString spannable = new SpannableString(alertText);
		spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.yellow)), 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		
		mtxtAlert.setText(spannable);
		
		mbtnNo.setText(context.getResources().getString(R.string.not_now));
		mbtnYes.setText(context.getResources().getString(R.string.ok));
		mbtnNo.setOnClickListener(this);
		mbtnYes.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_dialog_delete_group_no:
			Intent intent = new Intent(context, InviteViaContactsActivity.class);
			intent.putExtras(bundle);
			((Activity)context).startActivityForResult(intent,AppCollageConstants.INTENT_HOME);
			dismiss();
			break;

		case R.id.btn_dialog_delete_group_yes:

			//ToastCustom.underDevelopment(context);
			/*Intent intentFetchContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
			((Activity) context).startActivityForResult(intentFetchContact, 12);*/
			Intent intentFetchContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			((Activity) context).startActivityForResult(intentFetchContact, 1);
			dismiss();
			break;
		}
	}
}
