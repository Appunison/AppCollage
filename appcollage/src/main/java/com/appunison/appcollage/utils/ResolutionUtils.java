package com.appunison.appcollage.utils;

import com.appunison.appcollage.log.AppCollageLogger;

public class ResolutionUtils {
	private int height, width;
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void desireSize(int imageHeight, int imageWidth, int screenHeight, int screenWidth)
	{
		height= (imageHeight * screenWidth) / imageWidth;
		
		if(screenHeight > height)
		{
			width = screenWidth;
		}
		else
		{
			height = screenHeight;
			width= (imageWidth * screenHeight) / imageHeight;
			
		}
	}
}
