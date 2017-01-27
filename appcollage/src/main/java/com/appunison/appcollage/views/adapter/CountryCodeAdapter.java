package com.appunison.appcollage.views.adapter;

import java.io.IOException;
import java.io.InputStream;

import com.appunison.appcollage.R;
import com.appunison.appcollage.utils.CountryUtils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CountryCodeAdapter extends ArrayAdapter<String>{
	
	private Context context;
	private Resources res;
	
	public CountryCodeAdapter(Context context, int resource) {
		super(context, resource);
		this.context = context;
		res = (Resources) context.getResources();
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return CountryUtils.countryNameValues.length;
	}

	public String getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder1 holder;

		if(convertView == null)
		{
			holder = new ViewHolder1();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.layout_countrycode, parent, false);
			
			holder.mtxt_country_name = (TextView) convertView.findViewById(R.id.txt_country_name);
			holder.mimg_country_flag = (ImageView) convertView.findViewById(R.id.img_country_flag);

			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder1) convertView.getTag();
		}
		
		Log.i("position", ": "+position);
		
		holder.mtxt_country_name.setText(CountryUtils.countryNameValues[position]+" "+
				CountryUtils.countryIsdCode[position]);
		setImageBitmapFromAsset(holder.mimg_country_flag ,CountryUtils.countryFlagImage[position]);
		return convertView;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder2 holder;

		if(convertView == null)
		{
			holder = new ViewHolder2();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.layout_countryflag, parent, false);
			
			holder.mimg_country_flag = (ImageView) convertView.findViewById(R.id.img_country_flag);

			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder2) convertView.getTag();
		}
		
		Log.i("position", ": "+position);
		
		setImageBitmapFromAsset(holder.mimg_country_flag ,CountryUtils.countryFlagImage[position]);
		return convertView;
	}
	
	private static class ViewHolder1
	{
		TextView mtxt_country_name;
		ImageView mimg_country_flag;
	}
	private static class ViewHolder2
	{
		ImageView mimg_country_flag;
	}
	private void setImageBitmapFromAsset(ImageView imageView ,String strName)
    {
        AssetManager assetManager = res.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open("country_flags/"+strName+".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        imageView.setImageBitmap(bitmap);
    }
}
