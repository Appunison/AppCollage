package com.appunison.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.Source;

import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.SyndFeedHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * This Class is to get RestTemplate instance for different type of message
 * formats(xml/json/text).
 * 
 * @author appunison
 * 
 */
public class RestTemplateUtil {
 
	/**
	 * This method return resttemplate which can convert json Strings to
	 * respective POJOS.
	 * 
	 * @return RestTemplate
	 */

	public static RestTemplate getRestTemplateForJSON() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());  
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections
				.singletonList(MediaType.APPLICATION_JSON));

		restTemplate.getMessageConverters().add(converter);
		return restTemplate;
	}

	public static RestTemplate getRestTemplateFor() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());  
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		//MediaType mediaType = new MediaType("text/html");
		converter.setSupportedMediaTypes(Collections
				.singletonList(MediaType.TEXT_HTML));
		//converter.setSupportedMediaTypes(Collections.singletonList(mediaType));
		restTemplate.getMessageConverters().add(converter);
		return restTemplate;
	}

	/**
	 * This method return resttemplate which can convert json Strings to
	 * respective POJOS and have have FileSupport as well
	 * 
	 * @return
	 */
	public static RestTemplate getRestTemplateToUploadFile() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());  
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new SourceHttpMessageConverter<Source>());
		messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(new MappingJacksonHttpMessageConverter());

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		/*converter.setSupportedMediaTypes(Collections
				.singletonList(MediaType.APPLICATION_JSON));*/
		
		converter.setSupportedMediaTypes(Collections
				.singletonList(MediaType.TEXT_HTML));
		messageConverters.add(converter);
		restTemplate.setMessageConverters(messageConverters);
		return restTemplate;
	}
	public static RestTemplate getRestTemplateForHeaders() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());  
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new SourceHttpMessageConverter<Source>());
		messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(new MappingJacksonHttpMessageConverter());
		
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		/*converter.setSupportedMediaTypes(Collections
				.singletonList(MediaType.APPLICATION_JSON));*/
		
		converter.setSupportedMediaTypes(Collections
				.singletonList(MediaType.TEXT_HTML));
		messageConverters.add(converter);
		restTemplate.setMessageConverters(messageConverters);
		return restTemplate;
	}
	
	 /**
		 * 
		 * @return
		 */
		public static MultiValueMap<String, Object> getTaskMultivaluedMap()
		{
			MultiValueMap<String, Object> map=new LinkedMultiValueMap<String, Object>();
			map.add("Content Type", MediaType.TEXT_HTML);
			map.add("Connection", "Close");
			return map;
		}

	
}
