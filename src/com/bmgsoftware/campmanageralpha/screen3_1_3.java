package com.bmgsoftware.campmanageralpha;

import com.bmgsoftware.campmanageralpha.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
 
public class screen3_1_3 extends Activity {
	EditText notification;
	Button button2;
 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen3_1_3);
 
		addListenerOnButton();
 
	}
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	public void addListenerOnButton() {
		 
		final Context context = this;
		button2 = (Button) findViewById(R.id.buttonlog);
		button2.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				notification = (EditText) findViewById(R.id.editText1);
				//////
				boolean nullchecker = false;
				if(notification.getText().toString().equals(null)){nullchecker = true;}
				//////
				if(nullchecker==false)
				{
					new Thread() {
						@Override
						public void run() {
					String path = "http://www.bmgsoftware.com/send_notification.php"; 
				    HttpClient client = new DefaultHttpClient();
				    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);                                                         // Limit
				    HttpResponse response;
				    JSONObject json = new JSONObject();
				    try {
				        HttpPost post = new HttpPost(path);
				        json.put("notification", notification.getText().toString());
				        Log.i("json Object", json.toString());
				        post.setHeader("json", json.toString());
				        StringEntity se = new StringEntity(json.toString());
				        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
				        post.setEntity(se);
				        response = client.execute(post);
				        /* Checking response */
				        if (response != null) { 
				            InputStream in = response.getEntity().getContent();
				            String a = convertStreamToString(in);
				        }
				    } catch (Exception e) {
				        e.printStackTrace();
				    }
						}
					}.start();
				}
				else
				{
					notification.setText("Check all fields. Fields cannot be null");
				}
			}
		});
	}
 
}