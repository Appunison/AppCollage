package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.DefaultTextRequest;
import com.appunison.appcollage.model.pojo.response.DefaultTextResponse;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.basewidgets.Validation;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.persistence.CustomSharedPreference;
import com.appunison.session.SessionManager;
import com.appunison.task.ForeGroundTask;

public class SetDefaultTextActivity extends AppCollageNetworkActivity{

	private String TAG = SetDefaultTextActivity.class.getName();
	private @InjectView(R.id.ll_default_container) LinearLayout mllContainer;
	private @InjectView(R.id.txt_counter1) TextView mtxtCounter;
	private @InjectView (R.id.btn_default_text_send) Button mbtnSend;
	private @InjectView (R.id.edt_default_text_massage) EditText medtDefaultMassage;
	private @InjectView(R.id.img_back)  ImageView mimgBack;
	private @InjectView(R.id.txt_set_default) TextView mtxtDefault;
	private Intent intent;
	private ForeGroundTask service;
	private CustomSharedPreference mPref;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_default_text);

		service=new ForeGroundTask(this, this);
		mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, SetDefaultTextActivity.this);

		mimgBack.setImageResource(R.drawable.ic_action_back);
		mtxtTitle.setText(getResources().getString(R.string.set_default_text));
		intent = getIntent();
		//set text a/c to screen 

		if(intent.getBooleanExtra("textgoappcollage",false))
		{
			mtxtDefault.setText(getResources().getString(R.string._default_text_go_appcollage));
			String initite = mPref.getString(AppCollageConstants.KEY_INITIATE, null);
			if(!initite.equals(""))
			{
				medtDefaultMassage.setText(initite);
			}


			medtDefaultMassage.addTextChangedListener(new TextWatcher() 
			{
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				public void onTextChanged(CharSequence s, int start, int before, int count) {
					mtxtCounter.setText(String.valueOf(55-s.length()));
				}

				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});
		}
		else if(intent.getBooleanExtra("textinvite",false))
		{
			int maxLength = 255;
			//set for max length
			InputFilter[] fArray = new InputFilter[1];
			fArray[0] = new InputFilter.LengthFilter(maxLength);
			medtDefaultMassage.setFilters(fArray);
			mtxtCounter.setText(getResources().getString(R.string.text255));
			mtxtDefault.setText(getResources().getString(R.string._default_text_invite));

			String invite = mPref.getString(AppCollageConstants.KEY_INVITE, null);
			if(!invite.equals(""))
			{
				medtDefaultMassage.setText(invite);
			}

			medtDefaultMassage.addTextChangedListener(new TextWatcher() 
			{
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				public void onTextChanged(CharSequence s, int start, int before, int count) {
					mtxtCounter.setText(String.valueOf(255-s.length()));
				}

				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});
		}
		else if(intent.getBooleanExtra("textresponse",false))
		{
			mtxtDefault.setText(getResources().getString(R.string._default_text_response));

			String response = mPref.getString(AppCollageConstants.KEY_RESPONSE, null);
			if(!response.equals(""))
			{
				medtDefaultMassage.setText(response);
			}

			medtDefaultMassage.addTextChangedListener(new TextWatcher() 
			{
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				public void onTextChanged(CharSequence s, int start, int before, int count) {
					mtxtCounter.setText(String.valueOf(55-s.length()));
				}

				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});
		}



		mbtnSend.setOnClickListener(this);
		mllContainer.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_default_container:
			//hide keyboard click on outside
			InputMethodManager inputManager = (InputMethodManager)
			getSystemService(Context.INPUT_METHOD_SERVICE); 

			inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			break;

		case R.id.btn_default_text_send:
			if(checkValidation())
			{
				service.callpostService(AppCollageConstants.url, populateDefaultText(), DefaultTextResponse.class, ActionType.DEFAULTTEXT.getActionType());
			}
			break;

		default:
			break;
		}

	}
	public void onProgress(int actiontype, double progress,
			NetworkEventsEnum networkeventsEnum, Object request) {
		AppCollageLogger.d(TAG, "Web service on Progress"+ActionType.getDescriptionFromActionType(actiontype));
	}	

	public void onUpdate(int actionType, NetworkEventsEnum networkEvent,
			Object request, Object response, Exception exception) {
		super.onUpdate(actionType, networkEvent, request, response, exception);

		switch (networkEvent) {
		case SUCCESS:
			if(actionType==ActionType.DEFAULTTEXT.getActionType())
			{
				DefaultTextResponse defaultTextResponse = (DefaultTextResponse) response;
				if(defaultTextResponse.isResp())
				{
					ToastCustom.makeText(this, defaultTextResponse.getDesc(), Toast.LENGTH_SHORT);
					CustomSharedPreference mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, SetDefaultTextActivity.this);

					if(intent.getBooleanExtra("textgoappcollage",false))
					{
						mPref.putString(AppCollageConstants.KEY_INITIATE, defaultTextResponse.getResult().getInitiate_text().toString());
					}
					else if(intent.getBooleanExtra("textinvite",false))
					{
						mPref.putString(AppCollageConstants.KEY_INVITE, defaultTextResponse.getResult().getInvite_text().toString());
					}
					else if(intent.getBooleanExtra("textresponse",false))
					{
						mPref.putString(AppCollageConstants.KEY_RESPONSE, defaultTextResponse.getResult().getResponse_text().toString());
					}

					finish();
				}
				else{
					ToastCustom.makeText(this, defaultTextResponse.getDesc(), Toast.LENGTH_SHORT);
				}
			}
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actionType));
	}

	public void onpreExecute(int actionType,
			NetworkEventsEnum networkEvent, Object request) {
		super.onpreExecute(actionType, networkEvent, request);
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));
	}

	private DefaultTextRequest populateDefaultText()
	{

		String	userid = mPref.getString(SessionManager.KEY_USERID, null);
		DefaultTextRequest defaultTextRequest = new DefaultTextRequest();

		defaultTextRequest.setAction(WebServiceAction.DEFAULT_TEXT_ACTION);
		defaultTextRequest.setId(userid);
		defaultTextRequest.setText(medtDefaultMassage.getText().toString());
		if(intent.getBooleanExtra("textgoappcollage",false))
		{
			defaultTextRequest.setType("initiate");
		}
		else if(intent.getBooleanExtra("textinvite",false))
		{
			defaultTextRequest.setType("invite");
		}
		else if(intent.getBooleanExtra("textresponse",false))
		{
			defaultTextRequest.setType("response");
		}

		return defaultTextRequest;
	}
	/**
	 * check Validation
	 * @return
	 */
	private boolean checkValidation()
	{
		boolean valid = true;

		if(!Validation.hasText(medtDefaultMassage))
			valid = false;

		return valid;
	}
}
