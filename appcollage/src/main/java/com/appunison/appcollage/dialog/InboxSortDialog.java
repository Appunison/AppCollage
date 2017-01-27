package com.appunison.appcollage.dialog;

import java.util.Collections;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.appunison.appcollage.constants.SortEnum;
import com.appunison.appcollage.utils.Comparators;
import com.appunison.appcollage.utils.SortUtils;
import com.appunison.appcollage.views.adapter.InboxAdapter;
import com.appunison.appcollage.R;

public class InboxSortDialog extends Dialog implements android.view.View.OnClickListener{

	private Context context;
	private LinearLayout mlayoutDate;
	private LinearLayout mlayoutGroups;
	private LinearLayout mlayoutMissed;
	private LinearLayout mlayoutParticipated;
	private LinearLayout mlayoutInvitations;
	private LinearLayout mlayoutAttachments;
	
	private InboxAdapter inboxAdadpter;
	 
	
/*	public InboxAdapter getInboxAdadpter() {
		return inboxAdadpter;
	}

	public void setInboxAdadpter(InboxAdapter inboxAdadpter) {
		this.inboxAdadpter = inboxAdadpter;
	}*/

	public InboxSortDialog(Context context,InboxAdapter inboxAdadpter ) {
		//super(context);
		super(context, android.R.style.Theme_NoTitleBar_OverlayActionModes);
		this.context = context;
		this.inboxAdadpter = inboxAdadpter;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setCanceledOnTouchOutside(false);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = this.getWindow(); 
		window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
		setContentView(R.layout.layout_sort_inbox);
		
		mlayoutDate = (LinearLayout) findViewById(R.id.layout_inbox_sort_date);
		mlayoutGroups = (LinearLayout) findViewById(R.id.layout_inbox_sort_groups);
		mlayoutMissed = (LinearLayout) findViewById(R.id.layout_inbox_sort_missed);
		mlayoutParticipated = (LinearLayout) findViewById(R.id.layout_inbox_sort_participated);
		mlayoutInvitations = (LinearLayout) findViewById(R.id.layout_inbox_sort_invitations);
		mlayoutAttachments = (LinearLayout) findViewById(R.id.layout_inbox_sort_attachments);
		
		mlayoutDate.setOnClickListener(this);
		mlayoutGroups.setOnClickListener(this);
		mlayoutMissed.setOnClickListener(this);
		mlayoutParticipated.setOnClickListener(this);
		mlayoutInvitations.setOnClickListener(this);
		mlayoutAttachments.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_inbox_sort_date:
		
			Collections.sort(inboxAdadpter.getInboxs(),Comparators.dateComparator());
			inboxAdadpter.notifyDataSetChanged();
			dismiss();
			break;
		case R.id.layout_inbox_sort_groups:
			//ToastCustom.underDevelopment(context);
			//Collections.sort(inboxAdadpter.getInboxs(),Comparators.getGroupComparator());
			inboxAdadpter.setInboxs(SortUtils.sortByGroups(inboxAdadpter.getInboxs(), SortEnum.BYGROUP, Comparators.nameComparator(), Comparators.dateComparator()));
			inboxAdadpter.notifyDataSetChanged();
			dismiss();
			break;
		case R.id.layout_inbox_sort_missed:
			//Collections.sort(inboxAdadpter.getInboxs(),Comparators.getMissedComparator());
			inboxAdadpter.setInboxs(SortUtils.sortByGroups(inboxAdadpter.getInboxs(), SortEnum.BYMISED, Comparators.dateComparator(), Comparators.dateComparator()));
			inboxAdadpter.notifyDataSetChanged();
		
			dismiss();
		
			break;
		case R.id.layout_inbox_sort_participated:
			//Collections.sort(inboxAdadpter.getInboxs(),Comparators.getParticipatedComparator());
			inboxAdadpter.setInboxs(SortUtils.sortByGroups(inboxAdadpter.getInboxs(), SortEnum.BYPARTICIPATED, Comparators.dateComparator(), Comparators.dateComparator()));
			
			inboxAdadpter.notifyDataSetChanged();
			dismiss();
			break;
		case R.id.layout_inbox_sort_invitations:
			//inboxAdadpter.getFilter().filter(MessageType.INVITATION.getDesc());	
			//Collections.sort(inboxAdadpter.getInboxs(),Comparators.getInvitationComparator());
			inboxAdadpter.setInboxs(SortUtils.sortByGroups(inboxAdadpter.getInboxs(), SortEnum.BYINVITATION, Comparators.dateComparator(), Comparators.dateComparator()));
			inboxAdadpter.notifyDataSetChanged();
			dismiss();
			break;
		case R.id.layout_inbox_sort_attachments:
			
			//inboxAdadpter.getFilter().filter(MessageType.COLLAGE.getDesc());	
			//Collections.sort(inboxAdadpter.getInboxs(),Comparators.getAttachmentComparator());
			//inboxAdadpter.getFilter().
			inboxAdadpter.setInboxs(SortUtils.sortByGroups(inboxAdadpter.getInboxs(), SortEnum.BYATTACHMENT, Comparators.dateComparator(), Comparators.dateComparator()));
			inboxAdadpter.notifyDataSetChanged();
			dismiss();
			break;

		default:
			break;
		}
	}
	
	
	
}
