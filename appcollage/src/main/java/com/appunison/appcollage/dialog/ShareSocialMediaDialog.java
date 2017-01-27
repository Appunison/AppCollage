package com.appunison.appcollage.dialog;

import java.io.File;

import twitter4j.User;

import com.appunison.appcollage.R;
import com.appunison.appcollage.imageGetter.ImageLoader;
import com.appunison.appcollage.listener.ITwitter;
import com.appunison.appcollage.utils.LoginTwitter;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.util.NativeLauncherUtil;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ShareSocialMediaDialog extends Dialog implements android.view.View.OnClickListener,ITwitter
{
	private Activity activity;
	private Button mbtnCancel;
	private LinearLayout mlayoutShareFacebook, mlayoutShareTwitter, mlayoutShareEmail;
	private String collage;
	private LoginTwitter loginTwitter;

	public ShareSocialMediaDialog(Activity activity, String collage) {
		super(activity);
		this.activity = activity;
		this.collage = collage;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCanceledOnTouchOutside(false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.layout_dialog_share);

		mlayoutShareFacebook = (LinearLayout) findViewById(R.id.layout_dialog_share_facebook);
		mlayoutShareTwitter = (LinearLayout) findViewById(R.id.layout_dialog_share_twitter);
		mlayoutShareEmail = (LinearLayout) findViewById(R.id.layout_dialog_share_email);
		mbtnCancel = (Button) findViewById(R.id.btn_dialog_share_cancel);

		mlayoutShareFacebook.setOnClickListener(this);
		mlayoutShareTwitter.setOnClickListener(this);
		mlayoutShareEmail.setOnClickListener(this);
		mbtnCancel.setOnClickListener(this);
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_dialog_share_cancel:
			dismiss();
			break;
		case R.id.layout_dialog_share_facebook:
			((FBImageShareActivity)activity).handlePendingAction(collage);
			dismiss();
			break;
		case R.id.layout_dialog_share_twitter:
			//ToastCustom.underDevelopment(context);

			 loginTwitter=new LoginTwitter(activity,this);
			 dismiss();
			loginTwitter.shareOnTwitter(collage);

			break;
		case R.id.layout_dialog_share_email:
			//ToastCustom.underDevelopment(activity);
			ImageLoader loader = new ImageLoader(activity);
			if(collage !=null && loader.getFile(collage)!=null)
			{
				File path = loader.getFile(collage);
				NativeLauncherUtil.sendMail(activity,path);
				dismiss();
			}
			else{
				ToastCustom.makeText(activity, "Please download image", Toast.LENGTH_SHORT);
			}
			
			break;


		default:
			break;
		}
	}

	public void authorized(User user) {
		// TODO Auto-generated method stub
		if(loginTwitter!=null)
		{
			loginTwitter.shareOnTwitter(collage);
		}
	} 

}
