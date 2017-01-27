package com.appunison.task;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.os.AsyncTask;

import com.appunison.constants.NetworkEventsEnum;
import com.appunison.log.AndroidLibLogger;
import com.appunison.observe.CustomObservable;
import com.appunison.observe.CustomObserver;
import com.appunison.pojo.TaskResponseWrapper;
import com.appunison.util.NetworkUtil;
import com.appunison.util.RestTemplateUtil;
import com.appunison.util.TaskExecutor;

/**
 * This Class is to execute a task and update UI thread
 * 
 * @author appunison
 * 
 */
public class ForeGroundTask implements CustomObservable {
 
	private final String TAG = ForeGroundTask.class.getName();
	private CustomObserver observer;
	private Context context;

	public ForeGroundTask(CustomObserver observer, Context context) {
		this.observer = observer;
		this.context = context;
	}

	public AsyncTask<Object, Double, TaskResponseWrapper> callGetService(
			final String url, final Class<? extends Object> responseType,
			final int actiontype) {
		return call(url, null, null, responseType, actiontype);
	}
	public AsyncTask<Object, Double, TaskResponseWrapper> callGetService(RestTemplate template,
			final String url, final Class<? extends Object> responseType,
			final int actiontype) {
		return call(template,url, null, null, responseType, actiontype);
	}
	
	
	

	public AsyncTask<Object, Double, TaskResponseWrapper> callpostService(
			String url, Object request, Class<? extends Object> responseType,
			int actiontype) {
		return call(url, null, request, responseType, actiontype);
	}

	public AsyncTask<Object, Double, TaskResponseWrapper> callpostService(RestTemplate template,
			String url, Object request, Class<? extends Object> responseType,
			int actiontype) {
		return call(template,url, null, request, responseType, actiontype);
	}
	
	
	
	public AsyncTask<Object, Double, TaskResponseWrapper> callpostService(
			String url, MultiValueMap<String, Object> map,
			Class<? extends Object> responseType, int actiontype) {
		return call(url, map, null, responseType, actiontype);
	}
	
	public AsyncTask<Object, Double, TaskResponseWrapper> callpostService(
			RestTemplate template,String url, MultiValueMap<String, Object> map,
			Class<? extends Object> responseType, int actiontype) {
		return call(template,url, map, null, responseType, actiontype);
	}
	
	
	
	
	
	public AsyncTask<Object, Double, TaskResponseWrapper> callpostService(
			String url, MultiValueMap<String, Object> map,Object request,
			Class<? extends Object> responseType, int actiontype) {
		return call(url, map, request, responseType, actiontype);
	}
	public AsyncTask<Object, Double, TaskResponseWrapper> callpostService(RestTemplate template,
			String url, MultiValueMap<String, Object> map,Object request,
			Class<? extends Object> responseType, int actiontype) {
		return call(template,url, map, request, responseType, actiontype);
	}
	
	
	
	
	
	private AsyncTask<Object, Double, TaskResponseWrapper> call(
			final String url, final MultiValueMap<String, Object> map,
			final Object request, final Class<? extends Object> responseType,
			final int actiontype) {
				return call(null,url, map, request, responseType, actiontype);
	}
	
	
	
	private AsyncTask<Object, Double, TaskResponseWrapper> call(final RestTemplate restTemplate,
			final String url, final MultiValueMap<String, Object> map,
			final Object request, final Class<? extends Object> responseType,
			final int actiontype) {
		AsyncTask<Object, Double, TaskResponseWrapper> task = new AsyncTask<Object, Double, TaskResponseWrapper>() {

			@Override
			protected TaskResponseWrapper doInBackground(Object... params) {
				TaskResponseWrapper task = new TaskResponseWrapper();
				Object result = null;
				try {
					AndroidLibLogger.d(TAG, "Sending Request to Url=" + url
							+ " for action type " + actiontype + "Request="
							+ new ObjectMapper().writeValueAsString(request));
					
					if (map == null) {
						if (request == null) {
							
							if(restTemplate!=null)
									restTemplate.getForObject(url, responseType,RestTemplateUtil.getTaskMultivaluedMap());	
							
									else
										result = RestTemplateUtil.getRestTemplateFor()
										.getForObject(url, responseType,RestTemplateUtil.getTaskMultivaluedMap());
						
						}
						else{
								if(restTemplate!=null)
							     result = restTemplate
									.postForObject(url, request, responseType,RestTemplateUtil.getTaskMultivaluedMap());
							else
								result = RestTemplateUtil.getRestTemplateFor()
								.postForObject(url, request, responseType,RestTemplateUtil.getTaskMultivaluedMap());
						}
					
					} else {
						if (request == null) {
							if(restTemplate != null){
								
								result =restTemplate
										.getForObject(url, responseType, map);
							}else{
								
								
								result = RestTemplateUtil.getRestTemplateToUploadFile()
								.postForObject(url, map, responseType);
							}
						} else {
							if(restTemplate != null)
								result = restTemplate
								.postForObject(url, request, responseType,
										map);
	
								else
								result = RestTemplateUtil
								.getRestTemplateForHeaders()
								.postForObject(url, request, responseType,
										map);
	
							
						}
					}
					task.setObject(result);
					AndroidLibLogger.d(
							TAG,
							"Getting Response for Url="
									+ url
									+ " for action type "
									+ actiontype
									+ "and Response="
									+ "Sending Request to Url="
									+ url
									+ " for action type "
									+ actiontype
									+ " and Response="
									+ new ObjectMapper()
											.writeValueAsString(result));
				} catch (RestClientException ex) {
					AndroidLibLogger.e(TAG, ex.getMessage());
					task.setException(ex);

				} catch (HttpMessageNotReadableException ex) {

					AndroidLibLogger.e(TAG, ex.getMessage());
					task.setException(ex);
				} catch (Exception ex) {
					AndroidLibLogger.e(TAG, ex.getMessage());
					task.setException(ex);
				} 
				return task;
			}

			@Override
			protected void onPostExecute(TaskResponseWrapper task) {

				if (task.getObject() != null) {
					observer.onUpdate(actiontype, NetworkEventsEnum.SUCCESS,
							request, task.getObject(), null);
				} else {
					if (task.getException() instanceof RestClientException) {
						observer.onUpdate(actiontype,
								NetworkEventsEnum.EXCEPTION, request, null,
								task.getException());
					} else if (task.getException() instanceof HttpMessageNotReadableException) {
						observer.onUpdate(actiontype,
								NetworkEventsEnum.MSG_NOT_READABLE, request,
								null, task.getException());
					} else {
						observer.onUpdate(actiontype,
								NetworkEventsEnum.TIME_OUT, request, null,
								task.getException());
					}
				}
			}

			@Override
			protected void onProgressUpdate(Double... values) {
				observer.onProgress(actiontype, values[0],
						NetworkEventsEnum.SUCCESS, null);
			}
		};
		if (!NetworkUtil.isNetworkAvailable(context)) {
			observer.onpreExecute(actiontype,
					NetworkEventsEnum.NETWORK_NOT_AVAILABLE, null);

		} else {
			observer.onpreExecute(actiontype, NetworkEventsEnum.STARTED, null);
			TaskExecutor.executeonCachedThreadPool(task, null);
		}
		return task;

	}

}
