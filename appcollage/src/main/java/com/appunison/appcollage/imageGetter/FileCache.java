package com.appunison.appcollage.imageGetter;

import java.io.File;
import java.util.List;

import android.content.Context;
 
public class FileCache {
 
    private File cacheDir;
 
    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"TempImages");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
 
    public File getFile(String url){
    	if(url !=null)
    	{
        String filename=String.valueOf(url.hashCode()+".png");
        File f = new File(cacheDir, filename);
        return f;
    	}
    	return null;
 
    }
 
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }
    /**
     * 
     * @param urls
     * @return
     */
    public static boolean filesForUrlExists(List<String> urls)
    {
    	for(String url:urls)
    	{
    		File f=new File(android.os.Environment.getExternalStorageDirectory(),"TempImages");
    		String filename=String.valueOf(url.hashCode())+".png";
            File f1 = new File(f, filename);
            if(!f1.exists())
            {
            	return false;
            }
    		
    	}
    	
    	return true;
    }
    
}