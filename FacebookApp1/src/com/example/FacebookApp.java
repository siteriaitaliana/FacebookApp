package com.example;

import java.io.IOException;
import java.net.MalformedURLException;

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
	
	Facebook facebook = new Facebook("218266308198647");
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        tv = new TextView(this);
        setContentView(tv);

        facebook.authorize(this, new String[] { "user_location", "friends_location" }, new DialogListener() {
           
        	public void onComplete(Bundle values) {
        		try {
					retrieveInfo();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}

			public void onCancel() {}

			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub
				
			}

			public void onError(DialogError e) {
				// TODO Auto-generated method stub
				
			}
        });
    }
	

    private void retrieveInfo() throws MalformedURLException, IOException {
    	try{
    	String response = facebook.request("500663236");
    	
    	JSONObject json = new JSONObject(response);
    	JSONObject jsonsub = (JSONObject) json.getJSONObject("location");
    	
    	String name = (String) jsonsub.get("name");
    	
    	tv.setText(name);
    	
    	}catch(Exception e){}
		
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}
