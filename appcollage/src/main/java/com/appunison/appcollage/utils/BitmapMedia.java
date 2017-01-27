package com.appunison.appcollage.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.widget.Toast;

public class BitmapMedia {
	/**
	 * this method is used for
	 * out of memory exception
	 * @param file f
	 * @param WIDTH
	 * @param HIGHT
	 * @return
	 */
	 public static Bitmap decodeFile(File f,int WIDTH,int HIGHT){
	     try {
	         //Decode image size
	         BitmapFactory.Options o = new BitmapFactory.Options();
	         o.inJustDecodeBounds = true;
	         BitmapFactory.decodeStream(new FileInputStream(f),null,o);

	         //The new size we want to scale to
	         final int REQUIRED_WIDTH=WIDTH;
	         final int REQUIRED_HIGHT=HIGHT;
	         //Find the correct scale value. It should be the power of 2.
	         int scale=1;
	         while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
	             scale*=2;

	         //Decode with inSampleSize
	         BitmapFactory.Options o2 = new BitmapFactory.Options();
	         o2.inSampleSize=scale;
	         return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	     } catch (FileNotFoundException e) {}
	     return null;
	 }
	 /**
	    * save Image to Sd Card
	    */
	   public static void saveImagetoSdcard(Bitmap bitmap,Context contex)
	    {
	    	File sdCardDirectory = Environment.getExternalStorageDirectory();
	    	 File image = new File(sdCardDirectory, "test.png");
	    	 boolean success = false;

	    	    // Encode the file as a PNG image.
	    	    FileOutputStream outStream;
	    	    try {

	    	        outStream = new FileOutputStream(image);
	    	        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream); 
	    	        /* 100 to keep full quality of the image */

	    	        outStream.flush();
	    	        outStream.close();
	    	        success = true;
	    	    } catch (FileNotFoundException e) {
	    	        e.printStackTrace();
	    	    } catch (IOException e) {
	    	        e.printStackTrace();
	    	    }
	    	    
	    	    if (success) {
	    	        //Toast.makeText(contex, "Image saved successfully",Toast.LENGTH_LONG).show();
	    	    } else {
	    	        Toast.makeText(contex,
	    	                "Error during image saving", Toast.LENGTH_LONG).show();
	    	    }


	    }
	   /**
	    * get image in original form
	    * @param image
	    * @param newHeight
	    * @param newWidth
	    * @param path
	    * @return
	    */
	   public static Bitmap getResizedBitmap(Bitmap image, int screenHeight, int screenWidth, String path) {
			int width = image.getWidth();
			int height = image.getHeight();
			
			ResolutionUtils utils = new ResolutionUtils();
			utils.desireSize(height, width, screenHeight, screenWidth);

			screenWidth = utils.getWidth();
			screenHeight = utils.getHeight();
			
			float scaleWidth = ((float) screenWidth) / width;
			float scaleHeight = ((float) screenHeight) / height;
			// create a matrix for the manipulation
			Matrix matrix = new Matrix();
			try {
				ExifInterface exif = new ExifInterface(path);
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

				switch (orientation) {
				case ExifInterface.ORIENTATION_NORMAL:
					break;
				case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
					matrix.setScale(-1, 1);
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					matrix.setRotate(180);
					break;
				case ExifInterface.ORIENTATION_FLIP_VERTICAL:
					matrix.setRotate(180);
					matrix.postScale(-1, 1);
					break;
				case ExifInterface.ORIENTATION_TRANSPOSE:
					matrix.setRotate(90);
					matrix.postScale(-1, 1);
					break;
				case ExifInterface.ORIENTATION_ROTATE_90:
					matrix.setRotate(90);
					break;
				case ExifInterface.ORIENTATION_TRANSVERSE:
					matrix.setRotate(-90);
					matrix.postScale(-1, 1);
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					matrix.setRotate(-90);
					break;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			// resize the bit map
			matrix.postScale(scaleWidth, scaleHeight);
			// recreate the new Bitmap
			Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
					matrix, false);


			return resizedBitmap;
		}
	   
}
