package com.example;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FacebookApp extends Activity {
	
	protected final String APP_ID = "218266308198647";
	
	protected TextView tv;
	protected Long[] listaids;
	protected ArrayList<String> listanames;
	
	Facebook facebook = new Facebook(APP_ID);
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        styleUI();
        
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

	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);
    }

	protected void retrieveUsersIdAndName() throws Exception
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
	
	
    protected void retrieveUsersLocations() throws Exception 
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


	
	private void styleUI() {
		View layout = findViewById(R.id.back);
        layout.setBackgroundColor(0xFFFFFFFF);
        View titleView = getWindow().findViewById(android.R.id.title);
        if (titleView != null) {
          ViewParent parent = titleView.getParent();
          if (parent != null && (parent instanceof View)) {
            View parentView = (View)parent;
            parentView.setBackgroundColor(0xFF6D84B4);
          }
        }
        tv = (TextView) findViewById(R.id.location);
        tv.setTextColor(0xFF6D84B4);
	}
    
}
