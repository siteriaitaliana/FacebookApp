package com.example;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FacebookApp extends Activity {
	
	protected TextView tv;
	protected Long[] listaids;
	protected ArrayList<String> listanames;
	
	Facebook facebook = new Facebook("218266308198647");
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        //tv.isVerticalScrollBarEnabled();
        
        setContentView(R.layout.main);
        tv = (TextView) findViewById(R.id.location);
       
        //tv = new TextView(this);
        //tv.setVerticalScrollBarEnabled(true);

        facebook.authorize(this, new String[] { "user_location", "friends_location" }, new DialogListener() {
           
        	public void onComplete(Bundle values) {
        		try 
        		{
					retrieveUsersIdAndName();
					retrieveUsersLocations();
					
        		}catch(Exception e){e.printStackTrace();}
        	}

			public void onCancel() {}

			public void onFacebookError(FacebookError e) {}

			public void onError(DialogError e) {}
			
        });
    }
	


	private void retrieveUsersIdAndName() throws Exception
	{
		try{
	    	String response = facebook.request("me/friends");
	    	JSONObject json = new JSONObject(response);
	    	JSONArray json_data = json.getJSONArray("data");
	    	int totlength = json_data.length();
	    	listaids = new Long[totlength];
	    	for(int i=0;i<totlength;i++)
	    	{
	    		JSONObject ids = json_data.getJSONObject(i);
	    		listaids[i] = ids.getLong("id");
	    		//listanames.add(ids.getString("name"));
	    	}	
	    }catch(Exception e){}
	}
	
	
    private void retrieveUsersLocations() throws Exception 
    {
    	
		for(int i=0; i<listaids.length; i++)
		{
	    	try
	    	{
	    		String torequire = listaids[i].toString();
		    	String response2 = facebook.request(torequire);
		    	JSONObject json = new JSONObject(response2);
		    	JSONObject jsonsub = (JSONObject) json.getJSONObject("location");
		    	String location = (String) jsonsub.get("name");
		    	
		    	tv.append(location+"\n");	 
		    
	    	}catch(Exception e){}

		}
    	
	} 

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}
