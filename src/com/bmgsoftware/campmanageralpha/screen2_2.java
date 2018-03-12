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
 * This class creates the screen where the user can request the recovery of
 * password or user name by inputing his or her email address.
 */
public class screen2_2 extends Activity {

	Button button2;
	EditText emailfield;

	/**
	 * Sets up the the layout of the screen.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen2_2);
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

	/**
	 * Determines what happens when the text field or the button is pressed by
	 * the user.
	 */
	public void addListenerOnButton() {
		final Context context = this;

		button2 = (Button) findViewById(R.id.buttonlog);
		button2.setOnClickListener(new OnClickListener() {

			/*
			 * Determines what happens when the text field is pressed. Also checks
			 * if the text fields are filled by the user.
			 */
			@Override
			public void onClick(View arg0) {
				emailfield = (EditText) findViewById(R.id.editText1);
				boolean nullchecker = false;
				if (emailfield.getText().toString().equals(null)) {
					nullchecker = true;
				}
				if (emailfield.getText().toString().equals("")) {
					nullchecker = true;
				}
				if (nullchecker == false) {
					/*
					 * Takes the information on the text fields and sends them
					 * to the database after converting them to String.
					 */
					Thread t = new Thread() {
						@Override
						public void run() {
							String path =
							 "http://www.bmgsoftware.com/emailrecovery.php";
							HttpClient client = new DefaultHttpClient();
							HttpConnectionParams.setConnectionTimeout(
							 client.getParams(), 10000); // Limit
							HttpResponse response;
							JSONObject json = new JSONObject();
							try {
								HttpPost post = new HttpPost(path);
								json.put("email", emailfield.getText().toString());
								post.setHeader("json", json.toString());
								StringEntity se = new StringEntity(json.toString());
								se.setContentEncoding(new BasicHeader(
								 HTTP.CONTENT_TYPE, "application/json"));
								post.setEntity(se);
								response = client.execute(post);
								if (response != null) {
									InputStream in = response.getEntity().getContent();
									String a = convertStreamToString(in);
									Intent intent = new Intent(context,
											screen2_2_1.class);
									startActivity(intent);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					t.start();
					try {
						t.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
}