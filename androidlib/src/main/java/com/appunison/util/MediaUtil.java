package com.appunison.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * This class Used to perform operation on Media Files  
 * @author appunison
 *
 */
public class MediaUtil {


	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	public static Uri getOutputMediaFileUri(int type){
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "MyCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"IMG_"+ timeStamp + ".jpg");
		} else if(type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"VID_"+ timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}


	/**
	 * Load Bitmap from Uri
	 * @param url
	 * @return
	 */
	public static Bitmap loadBitmap(String url)
	{
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try 
		{
			URLConnection conn = new URL(url).openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is, 8192);
			bm = BitmapFactory.decodeStream(bis);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally {
			if (bis != null) 
			{
				try 
				{
					bis.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			if (is != null) 
			{
				try 
				{
					is.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		return bm;
	}

	public static Uri getPath(Activity context)
	{
		Log.i("TAG", "Inside PICK_FROM_CAMERA");
		Uri mImageCaptureUri=null;
		try {
			Log.i("TAG", "inside Samsung Phones");
			String[] projection = {
					MediaStore.Images.Thumbnails._ID, // The columns we want
					MediaStore.Images.Thumbnails.IMAGE_ID,
					MediaStore.Images.Thumbnails.KIND,
					MediaStore.Images.Thumbnails.DATA };
			String selection = MediaStore.Images.Thumbnails.KIND + "=" + // Select
					// only
					// mini's
					MediaStore.Images.Thumbnails.MINI_KIND;

			String sort = MediaStore.Images.Thumbnails._ID + " DESC";

			// At the moment, this is a bit of a hack, as I'm returning ALL
			// images, and just taking the latest one. There is a better way
			// to
			// narrow this down I think with a WHERE clause which is
			// currently
			// the selection variable
			Cursor myCursor = context.managedQuery(
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
					projection, selection, null, sort);

			long imageId = 0l;
			long thumbnailImageId = 0l;
			String thumbnailPath = "";

			try {
				myCursor.moveToFirst();
				imageId = myCursor
						.getLong(myCursor
								.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
				thumbnailImageId = myCursor.getLong(myCursor
						.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID));
				thumbnailPath = myCursor
						.getString(myCursor
								.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
			} finally {
				// myCursor.close();
			}

			// Create new Cursor to obtain the file Path for the large image

			String[] largeFileProjection = {
					MediaStore.Images.ImageColumns._ID,
					MediaStore.Images.ImageColumns.DATA };

			String largeFileSort = MediaStore.Images.ImageColumns._ID
					+ " DESC";
			myCursor = context.managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					largeFileProjection, null, null, largeFileSort);
			String largeImagePath = "";

			try {
				myCursor.moveToFirst();

				// This will actually give yo uthe file path location of the
				// image.
				String extr = Environment.getExternalStorageDirectory().toString();
				largeImagePath = myCursor
						.getString(myCursor
								.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
				mImageCaptureUri = Uri.fromFile(new File(
						extr+largeImagePath));} finally {
							// myCursor.close();
						}

		} catch (Exception e) {

		}
		return mImageCaptureUri;

	}




	public static Bitmap getBitmapFromSDP(Context context,String path)
	{
		ContentResolver cr = context.getContentResolver();
		String[] projection = {MediaStore.MediaColumns.DATA};
		Cursor cur = cr.query(Uri.parse(path), projection, null, null, null);
		if(cur != null) {
		    cur.moveToFirst();
		    String filePath = cur.getString(0);
		    return BitmapFactory.decodeFile(filePath);
		} 
		else {
		    return null;
		}
	}



}
