package com.appunison.persistence;


import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

/**
 * This Class will be used to store/fetch
 * data to/from  shared preferences
 * @author appunison
 *
 */
public class CustomSharedPreference {
	
	private static final int PRIVATE_MODE=0; 
	private SharedPreferences sharedPrefernces; 
	private Editor editor;
	public CustomSharedPreference(String name,Context context)
	{
		sharedPrefernces=context.getSharedPreferences(name, PRIVATE_MODE);
		editor=sharedPrefernces.edit();
	}
	
	
	/**
	 * save a map
	 * @param map
	 */
	public void save(Map<String, Object> map)
	{
		Set<Entry<String, Object>> entries=map.entrySet();
		for(Entry<String, Object> entry:entries)
		{
			save(entry.getKey(),entry.getValue());
		}
		editor.commit();
	}
	
	
	
	
	/**
	 * remove data
	 * @param key
	 * @param t
	 * @return
	 */
	public void removeValue(String key)
	{
		editor.remove(key);
		editor.commit();
	}
	/**
	 * clear all data
	 * from preference
	 */
	public void clear()
	{
		editor.clear();
		editor.commit();
	}
	
	/**
	 * get value 
	 * @param key
	 * @param t
	 * @return
	 */
	public <T> Object getValue( String key,Class<T> t)
	{
		if(t.equals(Boolean.class))
		{
			return sharedPrefernces.getBoolean(key, false);
		}
		else if(t.equals(String.class))
		{
			return sharedPrefernces.getString(key, null);
		}
		else if(t.equals(Long.class))
		{
			return sharedPrefernces.getLong(key, (Long) null);
		}
		else if(t.equals(Float.class))
		{
			return sharedPrefernces.getFloat(key, (Float) null);
		}
		else if(t.equals(Integer.class))
		{
			return sharedPrefernces.getInt(key, (Integer) null);
		}
		else if(t.equals(HashSet.class))
		{
			if(VERSION.SDK_INT>VERSION_CODES.HONEYCOMB)
			{
				return getStringSet(key);
			}
		}
		//sharedPrefernces.getStringSet(key, null);
		
		return null;
	}
	
	/**
	 * fetch data one by one
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean getBoolean(String key,boolean defaultValue)
	{
		return sharedPrefernces.getBoolean(key, defaultValue);
	}
	
	public String getString(String key,String defaultValue)
	{
		return sharedPrefernces.getString(key, defaultValue);
	}
	public Long getLong(String key,Long defaultValue)
	{
		return sharedPrefernces.getLong(key, defaultValue);
	}
	public Float getFloat(String key,Float defaultValue)
	{
		return sharedPrefernces.getFloat(key, defaultValue);
	}
	/**
	 * save data one by one
	 * @param key
	 * @param value
	 */

	public void putBoolean(String key,boolean value)
	{
		editor.putBoolean(key, value);
		editor.commit();
	}
	public void putString(String key,String value)
	{
		editor.putString(key, value);
		editor.commit();
	}
	public void putLong(String key,Long value)
	{
		editor.putLong(key, value);
		editor.commit();
	}
	public void putFloat(String key,Float value)
	{
		editor.putFloat(key, value);
		editor.commit();
	}

	
	//
	
	
	/**
	 * private to save  
	 * @param editor
	 * @param key
	 * @param val
	 */
	private void save(String key,Object val)
	{
		if(val instanceof Integer)
		{
			editor.putInt(key, ((Integer) val).intValue());
		}
		else if(val instanceof Boolean)
		{
			editor.putBoolean(key, ((Boolean) val).booleanValue());
		}
		else if(val instanceof Float)
		{
			editor.putFloat(key, ((Float) val).floatValue());
		}
		else if(val instanceof String)
		{
			editor.putString(key, val.toString());
		}
		else if (val instanceof Long) {
			editor.putLong(key, ((Long) val).longValue());
		}
		else if(val instanceof Set<?>)
		{
			if(VERSION.SDK_INT>VERSION_CODES.HONEYCOMB)
			putStringSet(editor, key, (Set<String>)val);	
		}
		
	}
	
	/**
	 * called above honey comb
	 * @param editor
	 * @param key
	 * @param values
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void putStringSet(Editor editor,String key,Set<String> values)
	{
		editor.putStringSet(key, (Set<String>)values);
	}
	
	/**
	 * get value 
	 * above honey comb
	 * @param key
	 */

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private Set<String> getStringSet(String key)
	{
		return sharedPrefernces.getStringSet(key, null);
	}

	/**
	 * add list
	 * @param key
	 * @param list
	 */
	public void saveList(String key,HashSet<String> list)
	{
		editor.putStringSet(key,list);
		editor.commit();
	}
	/**
	 * get list
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Set<String> getList(String key,Set<String> defaultValue)
	{
		return sharedPrefernces.getStringSet(key, defaultValue);
	}
}
