package com.appunison.appcollage.views.adapter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.appunison.appcollage.constants.AppCollageConstants;
import com.appunison.appcollage.dialog.DownloadDialog;
import com.appunison.appcollage.imageGetter.FileCache;
import com.appunison.appcollage.model.pojo.response.Gallery;
import com.appunison.appcollage.model.pojo.response.GalleryUser;
import com.appunison.appcollage.utils.DateTimeConverter;
import com.appunison.appcollage.views.activities.ShowCollageActivity;
import com.appunison.appcollage.views.activities.ShowPhotosActivity;
import com.appunison.appcollage.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GalleryAdapter extends BaseAdapter{

	private Activity activity;
	private List<Gallery> listGallery;
	private List<Gallery> searchedGalleryList;
	private boolean manage;
	
	public GalleryAdapter(Activity activity,List<Gallery> listGallery, boolean manage)
	{
		this.manage = manage;
		this.activity = activity;
		this.listGallery = listGallery;
		searchedGalleryList=new ArrayList<Gallery>();
		if(listGallery!=null)
		searchedGalleryList.addAll(listGallery);
		
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		if(searchedGalleryList != null && searchedGalleryList.size() > 0)
		{
			return searchedGalleryList.size();
		}
		return 0;
	}

	public Gallery getItem(int position) {
		// TODO Auto-generated method stub
		if(searchedGalleryList != null && searchedGalleryList.size() > 0)
		{
			return searchedGalleryList.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.layout_gallery_list_details, parent, false);
			holder.mtxt_gallery_group_name = (TextView) convertView.findViewById(R.id.txt_gallery_group_name);
			holder.mtxt_gallery_date = (TextView) convertView.findViewById(R.id.txt_gallery_date);
			holder.mtxt_gallery_time = (TextView) convertView.findViewById(R.id.txt_gallery_time);
			
			holder.mlayout_gallery_list_collage = (LinearLayout) convertView.findViewById(R.id.layout_gallery_list_collage);
			holder.mlayout_gallery_list_photos = (LinearLayout) convertView.findViewById(R.id.layout_gallery_list_photos);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		final Gallery gallery = getItem(position);
		
		DateTimeConverter converter = new DateTimeConverter();
		String date = converter.getDateFromTimeStamp(gallery.getTime());
		//date = date.substring(0, 5);
		String time = converter.getTimeFromTimeStamp(gallery.getTime());
		/*String date = converter.convertToCalendarDate(gallery.getTime());
		date = converter.convertToScreenFormat(date);
		date = date.substring(0, 5);
		String time = converter.convertToCalendarTime(gallery.getTime());
		time = converter.Convert24to12(time);*/
		date = converter.convertDateFormat(date, DateTimeConverter.dd_MM_yyyy,
				DateTimeConverter.yyyy_MM_dd);
		//holder.mtxt_gallery_group_name.setText(StringUtils.capitalize(gallery.getGroup_name()));
		holder.mtxt_gallery_group_name.setText(gallery.getGroup_name().toUpperCase());
		holder.mtxt_gallery_time.setText(time);
		holder.mtxt_gallery_date.setText(date);
		
		holder.mlayout_gallery_list_collage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//ToastCustom.underDevelopment(context);
				Intent intentCollage = new Intent(activity, ShowCollageActivity.class);
				intentCollage.putExtra("groupName", gallery.getGroup_name());
				intentCollage.putExtra("time", gallery.getTime());
				intentCollage.putExtra("collage", gallery.getCollage_image());
				intentCollage.putExtra("arr_key", (ArrayList<GalleryUser>)gallery.getImages());
				(activity).startActivityForResult(intentCollage,AppCollageConstants.INTENT_HOME);
			}
		});
		holder.mlayout_gallery_list_photos.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//ToastCustom.underDevelopment(context);
				if(manage)
				{
					List<String> urls = new ArrayList<String>();
					ArrayList<GalleryUser> user_Images= (ArrayList<GalleryUser>) gallery.getImages();
					
					for(int i=0;i<user_Images.size();i++)
					{
						urls.add(user_Images.get(i).getImage());
					}
					if(FileCache.filesForUrlExists(urls))
					{
						Intent intentphotos = new Intent(activity, ShowPhotosActivity.class);
						intentphotos.putExtra("groupName", gallery.getGroup_name());
						intentphotos.putExtra("time", gallery.getTime());
						intentphotos.putExtra("collage", gallery.getCollage_image());
						intentphotos.putExtra("arr_key", (ArrayList<GalleryUser>)gallery.getImages());
						(activity).startActivityForResult(intentphotos,AppCollageConstants.INTENT_HOME);
					}
					else{
						new DownloadDialog(activity,user_Images, gallery.getGroup_name(), gallery.getTime(),gallery.getCollage_image()).show();
					}
				}
				else{
					Intent intentphotos = new Intent(activity, ShowPhotosActivity.class);
					intentphotos.putExtra("groupName", gallery.getGroup_name());
					intentphotos.putExtra("time", gallery.getTime());
					intentphotos.putExtra("collage", gallery.getCollage_image());
					intentphotos.putExtra("arr_key", (ArrayList<GalleryUser>)gallery.getImages());
					(activity).startActivityForResult(intentphotos,AppCollageConstants.INTENT_HOME);
				}
			}
		});
		return convertView;
	}
	/**
	 * 
	 * @author appunison
	 *
	 */
	private class ViewHolder
	{
		TextView mtxt_gallery_group_name, mtxt_gallery_date, mtxt_gallery_time;
		LinearLayout mlayout_gallery_list_collage,mlayout_gallery_list_photos;
	}
	
	/**
	 * Filter With Name
	 * @param text
	 */
	public void filterWithName(String text)
	{
		searchedGalleryList=new ArrayList<Gallery>();
		 for(Gallery gallery:listGallery)
		 {
			 if(gallery.getGroup_name().toLowerCase().contains(text.toLowerCase().trim()))
			 {
				 searchedGalleryList.add(gallery);
			 }
		 }
		
		 notifyDataSetChanged();
	}
	/**
	 * Filter with date
	 * @param date
	 */
	public void filterWithDate(String date)
	{
		searchedGalleryList=new ArrayList<Gallery>();
		 for(Gallery gallery:listGallery)
		 {
			 	DateTimeConverter converter = new DateTimeConverter();
				//String converterDate = converter.convertToCalendarDate(gallery.getTime());
				//String galleryDate = converter.convertToScreenFormat(converterDate);
			 	String galleryDate = converter.getDateFromTimeStamp(gallery.getTime());
				/*String galleryDate = converter.convertDateFormat(converterDate, DateTimeConverter.yyyy_MM_dd,
						DateTimeConverter.dd_MM_yyyy);*/
			 	galleryDate = converter.convertDateFormat(galleryDate, DateTimeConverter.dd_MM_yyyy,
						DateTimeConverter.yyyy_MM_dd);
				Log.d("galleryDate", galleryDate);
				if(galleryDate.contains(date.trim()))
				{
					searchedGalleryList.add(gallery);
				}
		 }
		
		 notifyDataSetChanged();
	}
	
	
	
}
