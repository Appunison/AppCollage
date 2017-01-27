package com.appunison.observe;

import org.springframework.util.MultiValueMap;
import com.appunison.pojo.TaskResponseWrapper;
import android.os.AsyncTask;


/**
 * Custom Observable Class
 * @author appunison
 *
 */
public interface CustomObservable {
	/**
	 * Call get Service
	 * @param url
	 * @param responseType
	 * @param actiontype
	 * @return
	 */
	public AsyncTask<Object, Double, TaskResponseWrapper> callGetService(String url,Class<? extends Object> responseType,int actiontype);
	
	
	
	/**
	 * Call post Service
	 * @param url
	 * @param request
	 * @param responseType
	 * @param actiontype
	 * @return
	 */
	public AsyncTask<Object, Double, TaskResponseWrapper> callpostService(String url,Object request,Class<? extends Object> responseType,int actiontype);
	
	/**
	 * Call post Service
	 * @param url
	 * @param attachments
	 * @param map
	 * @param request
	 * @param responseType
	 * @param actiontype
	 * @return
	 */
	public AsyncTask<Object, Double, TaskResponseWrapper> callpostService(String url,
			MultiValueMap<String, Object> map, Class<? extends Object> responseType, int actiontype);
}
