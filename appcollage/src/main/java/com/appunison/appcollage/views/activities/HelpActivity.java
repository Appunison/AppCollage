package com.appunison.appcollage.views.activities;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.appunison.appcollage.R;
import com.appunison.appcollage.constants.ActionType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.constants.WebServiceAction;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.request.CMSRequest;
import com.appunison.appcollage.model.pojo.response.CMSResponse;
import com.appunison.basewidgets.ToastCustom;
import com.appunison.constants.NetworkEventsEnum;
import com.appunison.task.ForeGroundTask;

public class HelpActivity extends AppCollageNetworkActivity{

	private String TAG = HelpActivity.class.getName();
	private @InjectView (R.id.img_back) ImageView mimgBack;
	private @InjectView (R.id.webView) WebView mwebView;
	private Intent intent;
	private ForeGroundTask service;
	private CMSResponse cmsResponse;
	private final String mimeType = "text/html";
    private final String encoding = "UTF-8";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_help);
		
		mimgBack.setImageResource(R.drawable.ic_action_back);
		
		service = new ForeGroundTask(this, this);
		
		intent = getIntent();
		if(intent.getBooleanExtra("help", false))
		{
			mtxtTitle.setText(getResources().getString(R.string._help));
			service.callpostService(AppCollageConstants.url, populate(), CMSResponse.class, ActionType.HELP.getActionType());
		}else if(intent.getBooleanExtra("pp", false))
		{
			mtxtTitle.setText(getResources().getString(R.string._privacy_policy));
			String htmldata = intent.getStringExtra("ppdata");
			mwebView.getSettings().setJavaScriptEnabled(true);
			mwebView.loadData(htmldata, mimeType, encoding);
		}else if(intent.getBooleanExtra("tc", false))
		{
			mtxtTitle.setText(getResources().getString(R.string._terms_of_service));
			String htmldata = intent.getStringExtra("tcdata");
			mwebView.getSettings().setJavaScriptEnabled(true);
			mwebView.loadData(htmldata, mimeType, encoding);
		}
		
		
		
		/*String url = "http://appunison-tech.com/";
		mwebView.getSettings().setJavaScriptEnabled(true);
		mwebView.loadUrl(url);*/
		
	}
	public void onProgress(int actiontype, double progress,
			NetworkEventsEnum networkeventsEnum, Object request) {
		// TODO Auto-generated method stub
		AppCollageLogger.d(TAG, "Web service on Progress"+ActionType.getDescriptionFromActionType(actiontype));
		
	}	

	public void onUpdate(int actionType, NetworkEventsEnum networkEvent,
			Object request, Object response, Exception exception) {
		super.onUpdate(actionType, networkEvent, request, response, exception);
		switch (networkEvent) {
		case SUCCESS:
			if(actionType==ActionType.HELP.getActionType())
			{
				cmsResponse = (CMSResponse) response;
				if(cmsResponse.isResp())
				{
					String htmldata = cmsResponse.getResult().getContent();
					mwebView.getSettings().setJavaScriptEnabled(true);
					mwebView.loadData(htmldata, mimeType, encoding);
				}
				else{
					ToastCustom.makeText(this, cmsResponse.getDesc().toString(), Toast.LENGTH_SHORT);
				}
			}
			
			break;
		}
		AppCollageLogger.d(TAG, "Web service ON UPdate"+response+","+ActionType.getDescriptionFromActionType(actionType));
		
	}

	public void onpreExecute(int actionType,
			NetworkEventsEnum networkEventsEnum, Object request) {
		// TODO Auto-generated method stub
		super.onpreExecute(actionType, networkEventsEnum, request);
		switch (networkEventsEnum) {
		case STARTED:
			break;
		default:
			break;
		}
		AppCollageLogger.d(TAG, "Web service Start"+request+","+ActionType.getDescriptionFromActionType(actionType));
	}
	
	private CMSRequest populate()
	{
		CMSRequest cmsRequest = new CMSRequest();
		cmsRequest.setAction(WebServiceAction.CMS_ACTION);
		cmsRequest.setType("Help");
		
		return cmsRequest;
	}
}
