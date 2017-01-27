package com.appunison.appcollage.views.adapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.appunison.appcollage.constants.MessageType;
import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.dialog.ShowInboxDetailDialog;
import com.appunison.appcollage.listener.ReadMessage;
import com.appunison.appcollage.log.AppCollageLogger;
import com.appunison.appcollage.model.pojo.response.Inbox;
import com.appunison.appcollage.utils.DateTimeConverter;
import com.appunison.appcollage.views.activities.DeleteMessage;
import com.appunison.appcollage.views.activities.GalleryActivity;
import com.appunison.appcollage.views.activities.InitiateAppCollageActivity;
import com.appunison.appcollage.R;
import com.appunison.persistence.CustomSharedPreference;
public class InboxAdapter extends  ArrayAdapter<Inbox>{
   private Activity activity;
   private List<Inbox> inboxs, inboxsOriginal;
   private int height, width;
   
   public List<Inbox> getInboxsOriginal() {
       return inboxsOriginal;
   }
   public void setInboxsOriginal(List<Inbox> inboxsOriginal) {
       this.inboxsOriginal = inboxsOriginal;
   }
   public InboxAdapter(Context context, int resource,
           List<Inbox> inboxs) {
       super(context, resource, inboxs);
       // TODO Auto-generated constructor stub
       this.activity = (Activity)context;
       this.inboxs = new ArrayList<Inbox>();
       this.inboxs.addAll(inboxs);
       this.inboxsOriginal = new ArrayList<Inbox>();
       this.inboxsOriginal.addAll(inboxs);
   }
   public void setDimentions(int height, int width)
   {
       this.height = height;
       this.width = width;
   }
   
   /*    public void AddItems(List<Inbox> inboxs)
   {
       this.inboxs.addAll(inboxs);
       notifyDataSetChanged();
   }*/
   public List<Inbox> getInboxs() {
       return inboxs;
   }
   
   public int getCount() {
       // TODO Auto-generated method stub
       if(inboxs != null && inboxs.size() > 0)
           return inboxs.size();
       return 0;
   }
   public Inbox getItem(int position) {
       // TODO Auto-generated method stub
       if(inboxs != null && inboxs.size() > 0)
           return inboxs.get(position);
       return null;
   }
   public void setItem(int position, Inbox inbox) {
       // TODO Auto-generated method stub
       if(inboxs != null && inboxs.size() > 0)
       {
           inboxs.remove(position);
           inboxs.add(position, inbox);
       }
   }
   public long getItemId(int position) {
       // TODO Auto-generated method stub
       return 0;
   }
   public View getView(final int position, View convertView, ViewGroup parent) {
       // TODO Auto-generated method stub
       final ViewHolder holder;
       if(convertView == null)
       {
           holder = new ViewHolder();
           LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = inflater.inflate(R.layout.layout_list_inbox, parent, false);
           holder.mlayout_inbox = (FrameLayout) convertView.findViewById(R.id.layout_inbox);
           holder.mimg_inbox_icon = (ImageView) convertView.findViewById(R.id.img_inbox_icon);
           holder.mimg_inbox_attchment = (ImageView) convertView.findViewById(R.id.img_inbox_attchment);
           holder.mtxt_inbox_to = (TextView) convertView.findViewById(R.id.txt_inbox_to);
           holder.mtxt_inbox_Date = (TextView) convertView.findViewById(R.id.txt_inbox_date);
           holder.mtxt_inbox_Time = (TextView) convertView.findViewById(R.id.txt_inbox_time);
           holder.mtxt_inbox_from_lable = (TextView) convertView.findViewById(R.id.txt_inbox_from_lable);
           holder.mtxt_inbox_to_lable = (TextView) convertView.findViewById(R.id.txt_inbox_to_lable);
           holder.mtxt_inbox_from = (TextView) convertView.findViewById(R.id.txt_inbox_from);
           holder.mtxt_inbox_desc = (TextView) convertView.findViewById(R.id.txt_inbox_desc);
           holder.mtxt_inbox_missed_status = (TextView) convertView.findViewById(R.id.txt_inbox_missed_status);
           convertView.setTag(holder);
       }
       else
       {
           holder = (ViewHolder) convertView.getTag();
       }
       Log.i("position", ": "+position);
       final Inbox inbox = getItem(position);
       holder.mtxt_inbox_to.setText(inbox.getTo().toUpperCase());
       DateTimeConverter converter = new DateTimeConverter();
       String date = converter.getDateFromTimeStamp(inbox.getTime());
       String time = converter.getTimeFromTimeStamp(inbox.getTime());
       //String time = converter.convertToCalendarTime(inbox.getTime());
       //holder.mtxt_inbox_time.setText(converter.Convert24to12(time));
       date = converter.convertDateFormat(date, DateTimeConverter.dd_MM_yyyy,
				DateTimeConverter.yyyy_MM_dd);
       holder.mtxt_inbox_Date.setText(date);
       holder.mtxt_inbox_Time.setText(time);
       holder.mtxt_inbox_Time.setTag(inbox.getTime_zone());
       holder.mtxt_inbox_from.setText(inbox.getFrom());
       holder.mtxt_inbox_desc.setText(inbox.getMessage());
       holder.mtxt_inbox_desc.setTag(inbox.getMessage_id());
       holder.mtxt_inbox_to.setTag(inbox.getRead_status());
       setReadStatus(holder, Integer.parseInt(inbox.getRead_status()));
       // Message type rounded  icon color
       final MessageType messageType = MessageType.getMessageType(inbox.getMessage_type());
       holder.mimg_inbox_attchment.setVisibility(View.GONE);
       switch (messageType) {
       case PROMOTIONAL:
           holder.mimg_inbox_icon.setImageResource(R.drawable.promotion); // white
           holder.mtxt_inbox_missed_status.setVisibility(View.GONE);
           break;
           
       case COLLAGE:
           holder.mimg_inbox_icon.setImageResource(R.drawable.collage_photo); // Gray
           holder.mtxt_inbox_from_lable.setText(activity.getResources().getString(R.string.initiated_by));
           holder.mtxt_inbox_missed_status.setVisibility(View.GONE);
           holder.mimg_inbox_attchment.setVisibility(View.VISIBLE);
           break;
       case INVITATION:
           holder.mimg_inbox_icon.setImageResource(R.drawable.invitiation); // black
           holder.mtxt_inbox_missed_status.setVisibility(View.GONE);
           break;
       case AppCollage_REQUEST:
           holder.mimg_inbox_icon.setImageResource(R.drawable.appcollage_request);//yellow
           holder.mtxt_inbox_from_lable.setText(activity.getResources().getString(R.string.initiated_by));
           long serverTime = Long.parseLong(inbox.getTime())*1000;
           long systemTime = new Date().getTime();
           long endTime =0;
           /*if(serverTime > systemTime)
           {
               endTime = systemTime + 5*60*1000;
           }
           else
           {*/
               endTime = serverTime + 5*60*1000;                
           //}
           Log.d("AppCollageREQUEST",inbox.getRead_status());
           if((endTime - systemTime)<0 && inbox.getRead_status().equals("0"))
           {
               holder.mtxt_inbox_missed_status.setVisibility(View.VISIBLE);
               setReadStatus(holder, 1);
           }
           else
           {
               holder.mtxt_inbox_missed_status.setVisibility(View.GONE);                
           }
       default:
           break;
       }
       /**
        * perform long click listner
        */
       convertView.setOnLongClickListener(new OnLongClickListener() {
           
           public boolean onLongClick(View v) {
               //ToastCustom.underDevelopment(activity);
               Intent intent = new Intent(activity, DeleteMessage.class);
               intent.putExtra("msgId", inbox.getMessage_id());
               intent.putExtra("msgType", inbox.getMessage_type());
               activity.startActivityForResult(intent, AppCollageConstants.DELETE_MSG);
               return true;
           }
       });
       
       convertView.setOnClickListener(new OnClickListener() {
           public void onClick(View v) {
               // TODO Auto-generated method stub
               
               ReadMessage readMessageInvitation = new ReadMessage() {
                   public void onClick() {
                       // TODO Auto-generated method stub
                       inbox.setRead_status("1");
                       notifyDataSetChanged();
                       
                   }
               };
               ReadMessage readMessagePromotional = new ReadMessage() {
                   public void onClick() {
                       // TODO Auto-generated method stub
                       inbox.setRead_status("1");
                       setReadStatus(holder, Integer.parseInt(inbox.getRead_status()));
                       setItem(position, inbox);
                   }
               };
               
               switch (messageType) {
               case AppCollage_REQUEST:
                   
                   if("1".equals(inbox.getRead_status()))
                   {
                       new ShowInboxDetailDialog(activity, inbox, readMessageInvitation, false).show();
                       return;
                   }
                   
                   long serverTime = Long.parseLong(inbox.getTime())*1000;
                   long systemTime = new Date().getTime();
                   long endTime =0;
                   /*if(serverTime > systemTime)
                   {
                       endTime = systemTime + 5*60*1000;
                   }
                   else
                   {*/
                       endTime = serverTime + 5*60*1000;                
                   //s}
                   AppCollageLogger.d("serverTime", ""+serverTime);
                   AppCollageLogger.d("systemTime", ""+systemTime);
                   AppCollageLogger.d("endTime", ""+endTime);
                   CustomSharedPreference mPref = new CustomSharedPreference(AppCollageConstants.PREF_NAME, activity);
                   if((endTime - systemTime)>0)
                   {
                       Intent intent = new Intent(activity, InitiateAppCollageActivity.class);
                       intent.putExtra("GroupName", inbox.getTo().toUpperCase());
                       intent.putExtra("RequestID", inbox.getRequestID());
                       intent.putExtra("endTime", endTime);
                       intent.putExtra("InitiatedBy", inbox.getFrom());
                       intent.putExtra("msgid",inbox.getMessage_id() );
                       intent.putExtra("message",inbox.getMessage() );
                       intent.putExtra("appcollageRequest", true);
                       intent.putExtra("fromInbox", true);
                       activity.startActivityForResult(intent, AppCollageConstants.COLLAGE_REQUEST);
                       mPref.putBoolean(AppCollageConstants.SHOWING_DIALOG, true);
                   
                   }
                   else {
                       new ShowInboxDetailDialog(activity, inbox, readMessageInvitation, false).show();

				}
                   
                   break;
               case COLLAGE:
                   inbox.setRead_status("1");
                   setReadStatus(holder, Integer.parseInt(inbox.getRead_status()));
                   Intent intent = new Intent(activity, GalleryActivity.class);
                   intent.putExtra("groupId", inbox.getGroup_id());
                   intent.putExtra("groupName", inbox.getTo().toUpperCase());
                   intent.putExtra("messageid", inbox.getMessage_id());
                   intent.putExtra("ManageGroup", true);
                   intent.putExtra("hasmassageid", true);
                  // activity.startActivity(intent); 
                   activity.startActivityForResult(intent, AppCollageConstants.COLLAGE_REQUEST);
                   break;
               case INVITATION:
                   if("0".equals(inbox.getRead_status()))
                   {    
                       new ShowInboxDetailDialog(activity, inbox, readMessageInvitation, true).show();
                   }
                   else if ("1".equals(inbox.getRead_status())) {
                       new ShowInboxDetailDialog(activity, inbox, readMessageInvitation, false).show();
				}
                   break;
                   
               case PROMOTIONAL:
                   new ShowInboxDetailDialog(activity, inbox, readMessagePromotional, true).show();
                   break;
               default:
                   break;
               }
           }
       });
       return convertView;
   }
   public void setInboxs(List<Inbox> inboxs) {
       this.inboxs = inboxs;
   }
   private class ViewHolder
   {
       FrameLayout mlayout_inbox;
       ImageView mimg_inbox_icon, mimg_inbox_attchment;
       TextView mtxt_inbox_to,    mtxt_inbox_Date, mtxt_inbox_Time, mtxt_inbox_from_lable, mtxt_inbox_from,
       mtxt_inbox_desc, mtxt_inbox_missed_status,mtxt_inbox_to_lable;
   }
   private CountryFilter filter;
   @Override
   public Filter getFilter() {
       if (filter == null){
           filter  = new CountryFilter();
       }
       return filter;
   }
   private class CountryFilter extends Filter
   {
       @Override
       protected FilterResults performFiltering(CharSequence constraint) {
           constraint = constraint.toString().toLowerCase();
           FilterResults result = new FilterResults();
           if(constraint != null && constraint.toString().length() > 0)
           {
               ArrayList<Inbox> filteredItems = new ArrayList<Inbox>();
               for(int i = 0, l = inboxsOriginal.size(); i < l; i++)
               {
                   Inbox inbox = inboxsOriginal.get(i);
                   //if(inbox.toString().toLowerCase().contains(constraint))
                   if(inbox.getMessage_type().toLowerCase().contains(constraint))
                       filteredItems.add(inbox);
               }
               result.count = filteredItems.size();
               result.values = filteredItems;
           }
           else
           {
               synchronized(this)
               {
                   result.values = inboxsOriginal;
                   result.count = inboxsOriginal.size();
               }
           }
           return result;
       }
       @SuppressWarnings("unchecked")
       @Override
       protected void publishResults(CharSequence constraint,
               FilterResults results) {
           inboxs = (ArrayList<Inbox>)results.values;
           notifyDataSetChanged();
           clear();
           for(int i = 0, l = inboxs.size(); i < l; i++)
               add(inboxs.get(i));
           notifyDataSetInvalidated();
       }
   }
private void setReadStatus(ViewHolder holder, int read)
{
   // Read unread status
           if(read==0)
           {
               holder.mlayout_inbox.setBackgroundColor(Color.WHITE);
               
               holder.mtxt_inbox_to.setTypeface(null, Typeface.BOLD);
               holder.mtxt_inbox_Date.setTypeface(null, Typeface.BOLD);
               holder.mtxt_inbox_desc.setTypeface(null, Typeface.BOLD);
               holder.mtxt_inbox_from.setTypeface(null, Typeface.BOLD);
               holder.mtxt_inbox_from_lable.setTypeface(null, Typeface.BOLD);
               holder.mtxt_inbox_Time.setTypeface(null, Typeface.BOLD);
               holder.mtxt_inbox_to_lable.setTypeface(null, Typeface.BOLD);
           }
           else
           {
               holder.mlayout_inbox.setBackgroundColor(Color.parseColor("#E4E4E4"));
               
               holder.mtxt_inbox_to.setTypeface(null, Typeface.NORMAL);
               holder.mtxt_inbox_Date.setTypeface(null, Typeface.NORMAL);
               holder.mtxt_inbox_desc.setTypeface(null, Typeface.NORMAL);
               holder.mtxt_inbox_from.setTypeface(null, Typeface.NORMAL);
               holder.mtxt_inbox_from_lable.setTypeface(null, Typeface.NORMAL);
               holder.mtxt_inbox_Time.setTypeface(null, Typeface.NORMAL);
               holder.mtxt_inbox_to_lable.setTypeface(null, Typeface.NORMAL);
           }
   }
  
}