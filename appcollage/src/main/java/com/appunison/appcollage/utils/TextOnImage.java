package com.appunison.appcollage.utils;

import com.appunison.appcollage.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;


public class TextOnImage 
{

	public static Bitmap drawTextToBitmap(Context mContext,  Bitmap bitmap1, int imageViewWidth, int imageViewHeight, String mText) {
	    try {
	         Resources resources = mContext.getResources();
	            
	            Bitmap bitmap = Bitmap.createScaledBitmap(bitmap1, imageViewWidth, imageViewHeight, false);

	            android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
	            // set default bitmap config if none
	            if(bitmapConfig == null) {
	              bitmapConfig = android.graphics.Bitmap.Config.RGB_565;
	            }
	            // resource bitmaps are imutable,
	            // so we need to convert it to mutable one
	            bitmap = bitmap.copy(bitmapConfig, true);

	            Canvas canvas = new Canvas(bitmap);
	            // new antialised Paint
	          //Text Style
	            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	            // text color - #3D3D3D
	            paint.setColor(resources.getColor(R.color.black));
	            //paint.setColor(Color.parseColor("#ff0000"));
	            // text size in pixels
	            paint.setTextSize(21.12f);
////paint.setTextSize(11);
	           paint.setTextAlign(Align.CENTER);
	            // text shadow
	           //paint.setShadowLayer(1f, 0f, 1f, resources.getColor(R.color.yellow));
	            
	            //clear shadow
	           //paint.clearShadowLayer();
	            
	           // paint.ascent();
	            
	            // draw text to the Canvas center
	            Rect bounds = new Rect();
	            
	            paint.getTextBounds(mText, 0, mText.length(), bounds);
	            if(bounds.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
	                paint.setTextSize(convertToPixels(mContext, 7)); 
	            int xpos = (int) ((canvas.getWidth() / 2) - 3.84f);
////int xpos = (canvas.getWidth() / 2) - 2;    

	           // int ypos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ; 
////int ypos = bitmap.getHeight() - 35;
	            int ypos = (int) (bitmap.getHeight() - 67.2f);
	           int x = (bitmap.getWidth() - bounds.width())/2;
	            //int y =(bitmap.getHeight()*8/5 - bounds.height())/2;
	            int y =bitmap.getHeight() - bounds.height()-10;

	          //Box BG
	            Paint paint1 = new Paint();
	            paint1.setColor(Color.parseColor("#FFFFFF"));
	            //paint1.setColor(Color.parseColor("#00FF00"));
	            //canvas.drawRect(x-10, y-bounds.height()-10, x+bounds.width()+10, y+20, paint1); 
	            //canvas.drawRect(5, y-bounds.height()-4, imageViewWidth-5, y+10, paint1);  
	            canvas.drawRect(23f, imageViewHeight-115.5f, imageViewWidth-23f, imageViewHeight-39f, paint1);
////canvas.drawRect(10, imageViewHeight-60, imageViewWidth-10, imageViewHeight-18, paint1);

	            //inner border
	          //Box Border
	            Paint paint2 = new Paint();
	            paint2.setStyle(Paint.Style.STROKE);
	            paint2.setColor(Color.BLACK);
	            //paint2.setColor(Color.parseColor("#0000FF"));
	            paint2.setStrokeWidth(3.84f);
	            canvas.drawRect(23f, imageViewHeight-115.5f, imageViewWidth-23f, imageViewHeight-39f, paint2);
////canvas.drawRect(10, imageViewHeight-60, imageViewWidth-10, imageViewHeight-18, paint2);
	            //canvas.drawRect(5, y-bounds.height()-4, imageViewWidth-5, y+10, paint2);
	            
	            //Image Main Border
	            Paint paint3 = new Paint();
	            
	            //outer border
	            paint3.setStyle(Paint.Style.STROKE);
	            //paint3.setColor(Color.parseColor("#FFFF00"));
	            paint3.setColor(Color.WHITE);
	            //paint3.setStrokeWidth(resources.getDimension(R.dimen.dp_1));
	            paint3.setStrokeWidth(4);
	            //canvas.drawRect(0, imageViewHeight, imageViewWidth, 0, paint3);
	            
	          canvas.drawText(mText, xpos , ypos , paint);

	            return bitmap;
	    } catch (Exception e) {
	        // TODO: handle exception
	        return null;
	    }

	  }
	public static int convertToPixels(Context context, int nDP)
	{
	    final float conversionScale = context.getResources().getDisplayMetrics().density;

	    return (int) ((nDP * conversionScale) + 0.5f) ;

	}
}
