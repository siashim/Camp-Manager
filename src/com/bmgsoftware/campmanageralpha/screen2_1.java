package com.bmgsoftware.campmanageralpha;

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
import com.bmgsoftware.campmanageralpha.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
 
/**
 * This class creates the screen where the user can
 * create a new account.
 */
public class screen2_1 extends Activity {

	Button button2;
	EditText usernamefieldd;
	EditText passwordfield;
	EditText firstfield;
	EditText lastfield;
	EditText emailfield;
	EditText prodcodefield;
	final Context context = this;
	
	/**
	 * Sets up the the layout of the screen.	
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen2_1);
		addListenerOnButton();
	}
	
	/**
	 * Takes the info that is given as stream and converts them to String.
	 * @param is the stream required to be converted to string
	 * @return a string version of the stream given to the method.
	 */
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

	/**
	 * Determines what happens when any of the text fields or
	 * the button is pressed by the user.
	 */
	public void addListenerOnButton() {
		 
		button2 = (Button) findViewById(R.id.buttonlog);
		button2.setOnClickListener(new OnClickListener() {
 
			/*
			 * Determines what happens when the text fields are
			 * pressed. Also checks if the text fields are filled 
			 * by the user.
			 */
			@Override
			public void onClick(View arg0) {
				usernamefieldd = (EditText) findViewById(R.id.editText1);
				passwordfield = (EditText) findViewById(R.id.editText2);
				firstfield = (EditText) findViewById(R.id.editText3);
				lastfield = (EditText) findViewById(R.id.editText4);
				emailfield = (EditText) findViewById(R.id.editText5);
				prodcodefield = (EditText) findViewById(R.id.editText6);
				//////
				boolean nullchecker = false;
				if(usernamefieldd.getText().toString().equals(null))
					{nullchecker = true;}
				if(passwordfield.getText().toString().equals(null))
					{nullchecker = true;}
				if(firstfield.getText().toString().equals(null))
					{nullchecker = true;}
				if(lastfield.getText().toString().equals(null))
					{nullchecker = true;}
				if(emailfield.getText().toString().equals(null))
					{nullchecker = true;}
				if(prodcodefield.getText().toString().equals(null))
					{nullchecker = true;}
				if(usernamefieldd.getText().toString().equals(""))
					{nullchecker = true;}
				if(passwordfield.getText().toString().equals(""))
					{nullchecker = true;}
				if(firstfield.getText().toString().equals(""))
					{nullchecker = true;}
				if(lastfield.getText().toString().equals(""))
					{nullchecker = true;}
				if(emailfield.getText().toString().equals(""))
					{nullchecker = true;}
				if(prodcodefield.getText().toString().equals(""))
					{nullchecker = true;}
				
				/*
				 * This thread takes the information on the text fields
				 * and sends them to the database after converting them to 
				 * String.			
				 */
				if(nullchecker==false)
				{
					new Thread() {
						@Override
						public void run() {
					String path = "http://www.bmgsoftware.com/create_account.php"; 
				    HttpClient client = new DefaultHttpClient();
				    HttpConnectionParams.setConnectionTimeout(client.getParams()
				     , 10000);                                                    
				    HttpResponse response;
				    JSONObject json = new JSONObject();
				    try {
				        HttpPost post = new HttpPost(path);
				        json.put("username", usernamefieldd.getText().toString());
				        json.put("password", passwordfield.getText().toString());
				        json.put("firstname", firstfield.getText().toString());
				        json.put("lastname", lastfield.getText().toString());
				        json.put("email", emailfield.getText().toString());
				        json.put("prodcode", prodcodefield.getText().toString());
				        json.put("userid", "3");
				        post.setHeader("json", json.toString());
				        StringEntity se = new StringEntity(json.toString());
				        se.setContentEncoding(new BasicHeader
				      	(HTTP.CONTENT_TYPE,"application/json"));
				        post.setEntity(se);
				        response = client.execute(post);
				        /* Checking response */
				        if (response != null) { 
				            InputStream in = response.getEntity().getContent();
				            String a = convertStreamToString(in);
				            if(a.contains("success"))
				            {
				            	Intent intent = new Intent(context,
				            	 screen2_1_2.class);
				            	startActivity(intent);
				            }
				            if(a.contains("email"))
				            {
				            	Intent intent = new Intent(context,
				            	 screen2_1_1.class);
				            	startActivity(intent);
				            }
				            if(a.contains("username"))
				            {
				            	Intent intent = new Intent(context,
				            	 screen2_1_3.class);
				            	startActivity(intent);
				            }
				        }
				    } catch (Exception e) {
				        e.printStackTrace();
				    }
						}
					}.start();
				}
				else
				{
					usernamefieldd.setText("Check all fields. Fields " +
					 "cannot be null");
				}
			}
		});
	}
}
