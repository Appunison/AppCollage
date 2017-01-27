package com.appunison;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import roboguice.activity.RoboActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
public class HelloAndroidActivity extends RoboActivity {

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getResources().getDimension(R.dimen.dp_1);
        Log.d("TaG", "kjkhgkg");
        Log.d("TaG", "djskjskdjfkdjf");
        
     }

    
    public void search(){
    	
    	String url = "https://ajax.googleapis.com/ajax/" + 
    	    "services/search/web?v=1.0&q={query}";

    	// Create a new RestTemplate instance
    	RestTemplate restTemplate = new RestTemplate();

    	// Add the String message converter
    	restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    	
    	// Make the HTTP GET request, marshaling the response to a String
    	String result = restTemplate.getForObject(url, String.class, "Android");
    	Toast.makeText(HelloAndroidActivity.this, result.toString()+":", Toast.LENGTH_LONG).show();
    	
    	// Create a new RestTemplate instance
        }

}

